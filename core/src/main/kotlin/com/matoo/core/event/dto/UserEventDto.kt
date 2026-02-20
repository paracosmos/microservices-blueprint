package com.matoo.core.event.dto

interface UserEventDto : EventDto

data class CommentCreatedEvent(
    val commentId: String,
    val postId: String,
    val userId: String
) : UserEventDto
