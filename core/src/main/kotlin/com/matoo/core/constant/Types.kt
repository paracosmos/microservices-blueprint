package com.matoo.core.constant

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

typealias ULID = String
typealias IdType = ULID

data class EmailType @JsonCreator(mode = JsonCreator.Mode.DELEGATING) constructor(
    @field:NotBlank(message = "이메일은 필수 입력 값입니다.")
    @field:Email(message = "올바른 이메일 형식을 입력해주세요.")
    val value: String
) {
    companion object {
        fun of(raw: String) = EmailType(raw)
    }
    @JsonValue fun toJson(): String = value
}

data class PasswordType @JsonCreator(mode = JsonCreator.Mode.DELEGATING) constructor(
    @field:Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    @field:Pattern(
        regexp = ".*[0-9!@#\$%^&*].*",
        message = "비밀번호에는 숫자 또는 특수문자가 최소 1개 포함되어야 합니다."
    )
    val value: String
) {
    companion object {
        fun of(raw: String) = PasswordType(raw)
    }
    @JsonValue fun toJson(): String = value
}
