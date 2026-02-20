package com.matoo.user.adapter.out.persistence.push

import com.matoo.core.util.CoreUtil
import com.matoo.user.application.model.PushSubscribeCommand
import com.matoo.user.application.port.out.PushSubscriptionCommandPort
import com.matoo.user.application.port.out.PushSubscriptionQueryPort
import com.matoo.user.domain.model.notification.PushSubscription
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class PushSubscriptionPersistenceAdapter(
    private val pushSubscriptionJpaRepository: PushSubscriptionJpaRepository,
) : PushSubscriptionCommandPort, PushSubscriptionQueryPort {

    /**
     * upsert 기준:
     * - endpoint는 브라우저 구독의 유니크 키로 보고 전역 unique 처리(권장)
     * - endpoint가 이미 있으면 p256dh/auth 갱신 + active=true + userId 갱신(공용PC 등 케이스 허용)
     * - 없으면 신규 생성
     */
    @Transactional
    override suspend fun upsert(userId: String, command: PushSubscribeCommand) {
        val existing = pushSubscriptionJpaRepository.findByEndpoint(command.endpoint)
        if (existing == null) {
            val entity = PushSubscriptionEntity(
                pushSubscriptionId = CoreUtil.generateId(), // ULID(26) 생성기 사용
                userId = userId,
                endpoint = command.endpoint,
                p256dh = command.p256dh,
                auth = command.auth,
                active = true,
            )
            pushSubscriptionJpaRepository.save(entity)
            return
        }
        // endpoint 재구독/키 로테이션/유저 변경 케이스 대응
        existing.userId = userId
        existing.p256dh = command.p256dh
        existing.auth = command.auth
        existing.active = true
        // save 호출 없어도 영속 컨텍스트면 flush 때 반영되지만,
        // 명시적으로 남겨도 무방
        pushSubscriptionJpaRepository.save(existing)
    }

    @Transactional
    override suspend fun deactivateByEndpoint(userId: String, endpoint: String) {
        val entity = pushSubscriptionJpaRepository.findByUserIdAndEndpoint(userId, endpoint) ?: return
        if (!entity.active) return
        entity.active = false
        pushSubscriptionJpaRepository.save(entity)
    }

    /**
     * 전송 실패(404/410 등)로 endpoint가 죽었을 때 전역 비활성화
     */
    @Transactional
    override suspend fun deactivateByEndpointGlobal(endpoint: String) {
        val entity = pushSubscriptionJpaRepository.findByEndpoint(endpoint) ?: return
        if (!entity.active) return
        entity.active = false
        pushSubscriptionJpaRepository.save(entity)
    }

    @Transactional(readOnly = true)
    override suspend fun findActiveByUserId(userId: String): List<PushSubscription> {
        return pushSubscriptionJpaRepository
            .findAllByUserIdAndActiveIsTrue(userId)
            .map(PushSubscriptionMapper::toDomain)
    }
}
