package com.matoo.board.adapter.out.client

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.await
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import software.amazon.awssdk.core.async.AsyncRequestBody
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.*
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.io.InputStream
import java.time.Duration
import java.time.Instant

@Component
class S3StorageClient(
    private val s3Client: S3Client,
    private val s3AsyncClient: S3AsyncClient,
    private val s3Presigner: S3Presigner,
    @Value("\${cloud.aws.s3.bucket}") private val bucketName: String,
    @Value("\${cloud.aws.s3.cdn-url}") private val cdnUrl: String
) {

    fun publicUrl(key: String): String = "$cdnUrl/$key"

    suspend fun putObject(
        key: String,
        inputStream: InputStream,
        contentType: String
    ) {
        val bytes = withContext(Dispatchers.IO) { inputStream.readAllBytes() }
        val request = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .contentType(contentType)
            .build()
        s3Client.putObject(request, RequestBody.fromBytes(bytes))
    }

    suspend fun putObjectAsync(
        key: String,
        inputStream: InputStream,
        contentType: String
    ) {
        val bytes = withContext(Dispatchers.IO) { inputStream.readAllBytes() }
        val request = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .contentType(contentType)
            .build()
        s3AsyncClient.putObject(request, AsyncRequestBody.fromBytes(bytes)).await()
    }

    suspend fun getObjectBytes(key: String): ByteArray {
        val request = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build()

        return withContext(Dispatchers.IO) {
            s3Client.getObject(request).use { it.readAllBytes() }
        }
    }

    suspend fun deleteObject(key: String) {
        val request = DeleteObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build()
        s3Client.deleteObject(request)
    }

    suspend fun headObjectExists(key: String): Boolean {
        val request = HeadObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build()

        return try {
            s3Client.headObject(request)
            true
        } catch (e: S3Exception) {
            if (e.statusCode() == 404) false else throw e
        }
    }

    suspend fun presignPutObject(
        key: String,
        contentType: String,
        size: Long,
        expires: Duration = Duration.ofMinutes(5)
    ): PresignedPutInternal {
        val putReq = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .contentType(contentType)
            .contentLength(size)
            .build()

        val presignReq = PutObjectPresignRequest.builder()
            .signatureDuration(expires)
            .putObjectRequest(putReq)
            .build()

        val presigned = s3Presigner.presignPutObject(presignReq)
        val expiresInSec = presigned.expiration().epochSecond - Instant.now().epochSecond

        return PresignedPutInternal(
            uploadUrl = presigned.url().toString(),
            key = key,
            expiresInSec = expiresInSec,
            publicUrl = publicUrl(key)
        )
    }

    data class PresignedPutInternal(
        val uploadUrl: String,
        val key: String,
        val expiresInSec: Long,
        val publicUrl: String
    )
}
