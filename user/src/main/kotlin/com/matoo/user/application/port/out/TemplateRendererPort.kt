package com.matoo.user.application.port.out

interface TemplateRendererPort {
    fun render(template: String, variables: Map<String, Any?>): String
}
