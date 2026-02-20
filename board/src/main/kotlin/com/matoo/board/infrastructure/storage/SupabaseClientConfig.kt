package com.matoo.board.infrastructure.storage

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class SupabaseClientConfig {

    @Bean
    @Qualifier("supabaseWebClient")
    fun supabaseWebClient(
        @Qualifier("externalWebClientBuilder") webClientBuilder: WebClient.Builder,
        @Value("\${cloud.supabase.storage.url}") supabaseUrl: String,
        @Value("\${cloud.supabase.storage.service-role-key}") serviceRoleKey: String
    ): WebClient {
        return webClientBuilder
            .baseUrl(supabaseUrl.trimEnd('/'))
            .defaultHeader("apikey", serviceRoleKey)
            .defaultHeader("Authorization", "Bearer $serviceRoleKey")
            .build()
    }
}
