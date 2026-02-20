package com.matoo.user.adapter.out.persistence.terms

import com.matoo.user.domain.model.terms.TermType
import jakarta.persistence.Entity
import jakarta.persistence.Column
import jakarta.persistence.Table
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Lob
import org.springframework.data.annotation.CreatedDate
import java.time.Instant

@Entity
@Table(name = "terms")
class TermsEntity(
    @Id
    @Column(name = "terms_id", length = 26, nullable = false)
    var termsId: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 50, nullable = false)
    var type: TermType,

    @Column(name = "title", length = 200, nullable = false)
    var title: String,

    //    @Lob
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    var content: String,

    @Column(name = "content_url", length = 500)
    var contentUrl: String? = null,

    @Column(name = "required", nullable = false)
    var required: Boolean = false,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant? = null,
)
