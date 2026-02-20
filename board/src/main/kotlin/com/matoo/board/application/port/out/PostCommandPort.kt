package com.matoo.board.application.port.out

import com.matoo.board.domain.model.Post
import com.matoo.core.constant.IdType
import com.matoo.core.port.CommandPort

interface PostCommandPort : CommandPort<Post>{
    fun deleteById(id: IdType)
}
