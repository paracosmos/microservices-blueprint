package com.matoo.user.adapter.`in`.web.dto

import com.matoo.user.application.model.AgreeTermCommand
import jakarta.validation.constraints.NotEmpty

data class AgreeTermsRequest(
    @field:NotEmpty
    val agreeTerms: List<AgreeTerm>
) {
    data class AgreeTerm(
        val id: String,
        val agree: Boolean
    ) {
        fun toCommand(): AgreeTermCommand = AgreeTermCommand(id = id, agree = agree)
    }
}
