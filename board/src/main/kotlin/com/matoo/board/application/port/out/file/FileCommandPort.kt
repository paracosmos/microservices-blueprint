package com.matoo.board.application.port.out.file

import com.matoo.board.domain.model.File
import com.matoo.core.constant.IdType
import com.matoo.core.port.CommandPort

interface FileCommandPort : CommandPort<File>{
    fun deleteById(id: IdType)
    fun markUploaded(fileId: String, userId: String): Boolean
}
