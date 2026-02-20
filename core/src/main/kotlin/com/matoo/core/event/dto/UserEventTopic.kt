package com.matoo.core.event.dto

enum class UserEventTopic(override val value: String) : EventTopic {
    COMMENT_CREATED("comment.created")
}
