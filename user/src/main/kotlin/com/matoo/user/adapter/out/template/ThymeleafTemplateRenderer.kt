package com.matoo.user.adapter.out.template

import com.matoo.user.application.port.out.TemplateRendererPort
import org.springframework.stereotype.Component
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.util.Locale

@Component
class ThymeleafTemplateRenderer(
    private val templateEngine: TemplateEngine
) : TemplateRendererPort {
    override fun render(template: String, variables: Map<String, Any?>): String {
        val ctx = Context(Locale.KOREA).apply { setVariables(variables) }
        return templateEngine.process(template, ctx)
    }
}
