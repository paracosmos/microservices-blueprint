package com.matoo.board.adapter.out.client

import com.matoo.board.adapter.out.client.dto.SignedUploadResponse
import org.springframework.stereotype.Component
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriUtils
import java.io.InputStream
import java.nio.charset.StandardCharsets

@Component
class SupabaseStorageClient(
    @Qualifier("supabaseWebClient")
    private val webClient: WebClient,
    @Value("\${cloud.supabase.storage.url}") private val supabaseUrl: String,
    @Value("\${cloud.supabase.storage.bucket}") private val bucketName: String
) {

    private val baseUrl: String = supabaseUrl.trimEnd('/')

    fun publicUrl(key: String): String {
        val encodedKey = UriUtils.encodePath(key, StandardCharsets.UTF_8)
        return "$baseUrl/storage/v1/object/public/$bucketName/$encodedKey"
    }

    suspend fun putObject(
        key: String,
        inputStream: InputStream,
        contentType: String
    ) {
        val bytes = withContext(Dispatchers.IO) { inputStream.readAllBytes() }
        val encodedKey = UriUtils.encodePath(key, StandardCharsets.UTF_8)
        webClient.post()
            .uri("/storage/v1/object/$bucketName/$encodedKey")
            .contentType(MediaType.parseMediaType(contentType))
            .bodyValue(bytes)
            .retrieve()
            .toBodilessEntity()
            .awaitSingle()
    }

    suspend fun getObjectBytes(key: String): ByteArray {
        val encodedKey = UriUtils.encodePath(key, StandardCharsets.UTF_8)
        return webClient.get()
            .uri("/storage/v1/object/$bucketName/$encodedKey")
            .retrieve()
            .bodyToMono(ByteArray::class.java)
            .awaitSingle()
    }

    suspend fun deleteObject(key: String) {
        val encodedKey = UriUtils.encodePath(key, StandardCharsets.UTF_8)
        webClient.delete()
            .uri("/storage/v1/object/$bucketName/$encodedKey")
            .retrieve()
            .toBodilessEntity()
            .awaitSingle()
    }

    suspend fun objectExists(key: String): Boolean {
        val encodedKey = UriUtils.encodePath(key, StandardCharsets.UTF_8)
        val exist = webClient.get()
            .uri("/storage/v1/object/info/$bucketName/$encodedKey")
            .exchangeToMono { resp ->
                resp.toBodilessEntity()
            }
            .awaitSingle()
        return when {
            exist.statusCode.is2xxSuccessful -> true
            exist.statusCode == HttpStatus.NOT_FOUND -> false
            else -> false
        }
    }

    suspend fun createSignedUploadUrl(
        key: String,
        upsert: Boolean = false
    ): SignedUploadResponse {
        val encodedKey = UriUtils.encodePath(key, StandardCharsets.UTF_8)
        val res = webClient.post()
            .uri("/storage/v1/object/upload/sign/$bucketName/$encodedKey")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(mapOf("upsert" to upsert))
            .retrieve()
            .bodyToMono(SignedUploadResponse::class.java)
            .awaitSingle()
        return res.copy(url = baseUrl + "/storage/v1" + res.url)
    }
}
