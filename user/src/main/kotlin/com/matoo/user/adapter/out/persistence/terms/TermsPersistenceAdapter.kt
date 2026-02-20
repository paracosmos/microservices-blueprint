package com.matoo.user.adapter.out.persistence.terms

import com.matoo.user.application.port.out.TermsQueryPort
import com.matoo.user.domain.model.terms.TermType
import com.matoo.user.domain.model.terms.Terms
import org.springframework.stereotype.Component

@Component
class TermsPersistenceAdapter(
    private val termsJpaRepository: TermsJpaRepository
) : TermsQueryPort {

    override fun findLatestByType(type: TermType): Terms? =
        termsJpaRepository.findFirstByTypeOrderByCreatedAtDesc(type)?.let(TermsMapper::toDomain)

    override fun findAllByType(type: TermType): List<Terms> =
        termsJpaRepository.findAllByTypeOrderByCreatedAtDesc(type).map(TermsMapper::toDomain)

    override fun findLatestByTypes(types: List<TermType>): Map<TermType, Terms> {
        // type별 최신 1개씩만 필요하면, DB마다 최적 쿼리가 다름.
        // 여기서는 표준적으로 "type별 최신을 루프"로 명확하게 처리(규모 작으면 충분).
        return types.associateWith { t ->
            termsJpaRepository.findFirstByTypeOrderByCreatedAtDesc(t)?.let(TermsMapper::toDomain)
        }.filterValues { it != null }
            .mapValues { it.value!! }
    }
}
