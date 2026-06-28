package com.matoo.board.application.port.out

import com.matoo.board.domain.model.Comment
import com.matoo.core.port.QueryPort

interface CommentQueryPort : QueryPort<Comment>
