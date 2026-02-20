package com.matoo.user.infrastructure.config.email

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sesv2.SesV2Client

@Configuration
@EnableConfigurationProperties(SesProperties::class)
class AwsSesConfig(
    private val props: SesProperties
) {
    @Bean
    fun sesV2Client(): SesV2Client {
        val credentials = AwsBasicCredentials.create(props.accessKey, props.secretKey)
        return SesV2Client.builder()
            .credentialsProvider { credentials }    // .credentialsProvider(DefaultCredentialsProvider.create())
            .region(Region.of(props.region))  // .region(Region.AP_NORTHEAST_2)
            .build()
    }
}
