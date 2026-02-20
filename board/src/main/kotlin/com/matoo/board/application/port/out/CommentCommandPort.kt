package com.matoo.board.application.port.out

import com.matoo.board.domain.model.Comment
import com.matoo.core.constant.IdType
import com.matoo.core.port.CommandPort

interface CommentCommandPort: CommandPort<Comment>{
    fun deleteById(id: IdType)
}
