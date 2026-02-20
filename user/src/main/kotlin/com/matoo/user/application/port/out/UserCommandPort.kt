package com.matoo.user.application.port.out

import com.matoo.core.constant.IdType
import com.matoo.user.domain.model.user.User
import com.matoo.core.port.CommandPort

interface UserCommandPort : CommandPort<User>{
    fun deleteById(id: IdType)
}
