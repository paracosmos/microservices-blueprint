package com.matoo.user.adapter.out.persistence.terms

import com.matoo.user.domain.model.terms.Terms

object TermsMapper {
    fun toDomain(e: TermsEntity): Terms = Terms(
        termsId = e.termsId,
        type = e.type,
        title = e.title,
        content = e.content,
        contentUrl = e.contentUrl,
        required = e.required,
        createdAt = e.createdAt
    )
}
