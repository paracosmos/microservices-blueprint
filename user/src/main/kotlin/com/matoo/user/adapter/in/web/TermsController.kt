package com.matoo.user.adapter.`in`.web

import com.matoo.core.constant.TraceConstants
import com.matoo.user.adapter.`in`.web.dto.AgreeTermsRequest
import com.matoo.user.adapter.`in`.web.dto.AgreeTermsResponse
import com.matoo.user.adapter.`in`.web.dto.TermsResponse
import com.matoo.user.adapter.`in`.web.dto.toResponse
import com.matoo.user.application.port.`in`.TermsUseCase
import com.matoo.user.application.port.`in`.UserTermsUseCase
import com.matoo.user.domain.model.terms.TermType
import jakarta.validation.Valid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user/terms")
class TermsController(
    private val termsUseCase: TermsUseCase,
    private val userTermsUseCase: UserTermsUseCase
) {
    /**
     * 최신 약관 조회 (여러 타입 한번에)
     * 예: /api/v1/terms/latest?types=TOS,PRIVACY
     */
    @GetMapping("/latest")
    suspend fun latest(
        @RequestParam types: List<TermType>
    ): ResponseEntity<Map<TermType, TermsResponse>> {
        val res = withContext(Dispatchers.IO) { termsUseCase.getLatestTerms(types) }
            .mapValues { (_, t) -> t.toResponse() }
        return ResponseEntity.ok(res)
    }

    /**
     * 타입별 약관 히스토리
     */
    @GetMapping("/{type}")
    suspend fun history(
        @PathVariable type: TermType
    ): ResponseEntity<List<TermsResponse>> {
        val res = withContext(Dispatchers.IO) { termsUseCase.getTermsHistory(type) }
            .map { it.toResponse() }
        return ResponseEntity.ok(res)
    }

    /**
     * 사용자 약관 동의
     */
    @PostMapping("/agreements")
    suspend fun agree(
        @RequestHeader(TraceConstants.HEADER_USER_ID) userId: String,
        @Valid @RequestBody req: AgreeTermsRequest
    ): ResponseEntity<Void> {
        val agreeTerms = req.agreeTerms.map { it.toCommand() }
        withContext(Dispatchers.IO) { userTermsUseCase.agree(userId, agreeTerms) }
        return ResponseEntity.noContent().build()
    }

    /**
     * 사용자 동의 목록 조회
     */
    @GetMapping("/agreements")
    suspend fun agreements(
        @RequestHeader(TraceConstants.HEADER_USER_ID) userId: String,
    ): ResponseEntity<List<AgreeTermsResponse>> {
        val body = withContext(Dispatchers.IO) { userTermsUseCase.getAgreements(userId) }
            .map { model -> AgreeTermsResponse.from(model) }
        return ResponseEntity.ok(body)
    }
}
