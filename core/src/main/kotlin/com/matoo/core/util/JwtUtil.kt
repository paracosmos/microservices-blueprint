package com.matoo.core.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import java.util.Date

object JwtUtil {

    private val signatureAlgorithm = SignatureAlgorithm.ES256

    private const val KEY_PRIVATE_BEGIN = "-----BEGIN PRIVATE KEY-----"
    private const val KEY_PRIVATE_END = "-----END PRIVATE KEY-----"
    private const val KEY_PUBLIC_BEGIN = "-----BEGIN PUBLIC KEY-----"
    private const val KEY_PUBLIC_END = "-----END PUBLIC KEY-----"
    private const val EC_ALGORITHM = "EC"
    private const val EMPTY = ""

    private fun getPrivateKeyFromString(privateKeyString: String): PrivateKey {
        try {
            val cleanKey = privateKeyString
                .replace(KEY_PRIVATE_BEGIN, EMPTY)
                .replace(KEY_PRIVATE_END, EMPTY)
                .replace("\\s".toRegex(), EMPTY)
                .trim()

            val decodedKey = Base64.getDecoder().decode(cleanKey)
            val keySpec = PKCS8EncodedKeySpec(decodedKey)
            val keyFactory = KeyFactory.getInstance(EC_ALGORITHM)
            return keyFactory.generatePrivate(keySpec)
        } catch (e: Exception) {
            throw RuntimeException(e.message, e)
        }
    }

    fun getPublicKeyFromString(publicKeyString: String, algorithm: String? = EC_ALGORITHM): PublicKey {
        val cleanKey = publicKeyString
            .replace(KEY_PUBLIC_BEGIN, EMPTY)
            .replace(KEY_PUBLIC_END, EMPTY)
            .replace("\\s".toRegex(), EMPTY)
            .trim()

        val decodedPublicKey = Base64.getDecoder().decode(cleanKey)
        val keySpec = X509EncodedKeySpec(decodedPublicKey)
        val keyFactory = KeyFactory.getInstance(algorithm)
        return keyFactory.generatePublic(keySpec)
    }

    fun generateToken(
        subject: String,
        privateKeyString: String,
        claims: Map<String, Any>? = null,
        ttl: Long
    ): String {
        val sanitizedSubject = subject.trim()
        val now = Date()
        val expiryDate = Date(now.time + ttl)
        val privateKey = getPrivateKeyFromString(privateKeyString)

        return Jwts.builder()
            .setSubject(sanitizedSubject)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .apply {
                claims?.forEach { (key, value) ->
                    claim(key, value)
                }
            }
            .signWith(privateKey, signatureAlgorithm)
            .compact()
    }

    fun validateToken(token: String, publicKeyString: String): Boolean {
        return try {
            val publicKey = getPublicKeyFromString(publicKeyString)
            Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getClaims(token: String, publicKeyString: String): Claims {
        val publicKey = getPublicKeyFromString(publicKeyString)
        return Jwts.parserBuilder()
            .setSigningKey(publicKey)
            .build()
            .parseClaimsJws(token)
            .body
    }
}
