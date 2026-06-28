package com.matoo.board.application.service

import com.matoo.board.application.model.PostSummary
import com.matoo.board.application.model.PostWithComments
import com.matoo.board.application.port.`in`.BoardQueryUseCase
import com.matoo.board.application.port.out.PostReadPort
import com.matoo.core.support.exception.orNotFound
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service

@Service
class BoardQueryService(
    private val postReadPort: PostReadPort,
) : BoardQueryUseCase {

    // 리스트: QueryDSL 집계 쿼리(게시글 + 댓글 수)
    override suspend fun getPostList(limit: Int, offset: Int): List<PostSummary> =
        withContext(Dispatchers.IO) { postReadPort.findSummaries(limit, offset) }

    // 상세: 게시글 + 댓글 projection (어댑터의 readOnly 트랜잭션 안에서 2개 쿼리 실행)
    override suspend fun getPostWithComments(postId: String): PostWithComments =
        withContext(Dispatchers.IO) { postReadPort.findDetail(postId) }.orNotFound()
}
