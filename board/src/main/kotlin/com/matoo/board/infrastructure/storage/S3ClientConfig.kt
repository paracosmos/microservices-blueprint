package com.matoo.board.infrastructure.storage

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.http.TlsTrustManagersProvider
import software.amazon.awssdk.http.apache.ApacheHttpClient
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import java.security.cert.X509Certificate
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Configuration
class S3ClientConfig(
    @Value("\${cloud.aws.credentials.access-key}")
    private val accessKey: String,
    @Value("\${cloud.aws.credentials.secret-key}")
    private val secretKey: String,
    @Value("\${cloud.aws.region.static}")
    val region: String,
    @Value("\${cloud.aws.s3.trust-all-certs:false}")
    private val trustAllCerts: Boolean
){
    fun createCredentials(): AwsBasicCredentials =
        AwsBasicCredentials.create(accessKey, secretKey)

    fun createTrustAllCerts(): Array<TrustManager> =
        arrayOf(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        })

    fun createTlsTrustManagersProvider(): TlsTrustManagersProvider {
        val trustAllCerts = createTrustAllCerts()
        return TlsTrustManagersProvider { trustAllCerts }
    }

    @Bean
    fun s3Client(): S3Client {
        val builder = S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(createCredentials()))

        if (trustAllCerts) {
            builder.httpClient(
                ApacheHttpClient.builder()
                    .tlsTrustManagersProvider(createTlsTrustManagersProvider())
                    .build()
            )
        }

        return builder.build()
    }

    @Bean
    fun s3AsyncClient(): S3AsyncClient {
        val builder = S3AsyncClient.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(createCredentials()))

        if (trustAllCerts) {
            builder.httpClient(
                NettyNioAsyncHttpClient.builder()
                    .tlsTrustManagersProvider(createTlsTrustManagersProvider())
                    .build()
            )
        }

        return builder.build()
    }

    @Bean
    fun s3Presigner(): S3Presigner {
        return S3Presigner.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(createCredentials()))
            .build()
    }
}
