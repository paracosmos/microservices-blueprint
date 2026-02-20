package com.matoo.board.application.port.out

import com.matoo.board.domain.model.Post
import com.matoo.core.constant.IdType
import com.matoo.core.port.QueryPort

interface PostQueryPort : QueryPort<Post>{
    fun findAll(): List<Post>
}
