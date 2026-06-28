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

    override fun findLatestByTypes(types: List<TermType>): Map<TermType, Terms> =
        // TermType은 작은 enum이라 type별 최신 1행씩만 조회하는 게 가장 효율적이다.
        // (IN 쿼리로 전체 이력을 메모리에 적재한 뒤 버리는 방식은 버전이 쌓일수록 비효율.)
        types.distinct().mapNotNull { type ->
            termsJpaRepository.findFirstByTypeOrderByCreatedAtDesc(type)
                ?.let { type to TermsMapper.toDomain(it) }
        }.toMap()
}
