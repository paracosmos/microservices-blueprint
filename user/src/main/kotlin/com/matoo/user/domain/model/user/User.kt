package com.matoo.user.domain.model.user

import com.matoo.core.constant.IdType
import com.matoo.core.dto.OAuthProvider
import com.matoo.user.domain.model.provider.Provider
import com.matoo.user.domain.model.provider.ProviderKey
import java.time.Instant

class User private constructor(
    val userId: IdType,
    val email: String,
    private var password: String?,
    private var name: String?,
    private val providers: MutableSet<Provider> = mutableSetOf(),
    val createdAt: Instant = Instant.now()
) {
    private var updatedAt: Instant? = null

    init {
        require(email.isNotBlank()) { "email must not be blank" }
    }

    // 회원가입
    fun registerLocal(encodedPassword: String): Boolean {
        // requireStatus(!providers.any { it.providerKey == ProviderKey.LOCAL }, HttpStatus.CONFLICT)
        var isNew = false
        if (!hasProvider(ProviderKey.LOCAL)) {
            providers.add(Provider.local(this))
            isNew = true
        }
        password = encodedPassword
        touch()
        return isNew
    }

    fun registerOAuth(
        oauthProvider: OAuthProvider,
        providerUid: String,
        providerName: String? = null,
        providerPicture: String? = null
    ): Boolean {
        var touched = false

        val providerKey = ProviderKey.fromOAuthProvider(oauthProvider)
        val provider = providers.firstOrNull { it.providerKey == providerKey }

        if (provider == null) {
            providers.add(
                Provider.oauth(oauthProvider, providerUid, providerName, providerPicture)
            )
            touched = touch()
        } else {
            if (provider.hasNameTouched(providerName) || provider.hasPictureTouched(providerPicture)) {
                touched = touch()
            }
        }

        if (name == null && !providerName.isNullOrBlank()) {
            name = providerName
            touched = touch()
        }
        return touched
    }


    // 로그인
    fun verifyPassword(
        raw: String,
        matcher: (String, String) -> Boolean
    ) {
        requireNotNull(password) { "local account does not exist" }
        require(matcher(raw, password!!)) { "invalid password" }
    }

    private fun touch(): Boolean {
        updatedAt = Instant.now()
        return true
    }

    fun encodedPassword(): String? = password
    fun providers(): Set<Provider> = providers.toSet()
    fun name(): String? = name

    fun hasProvider(key: ProviderKey): Boolean {
        return providers.any { it.providerKey == key }
    }

    override fun toString(): String =
        "User(userId=$userId, email=$email)"

    companion object {
        fun create(
            userId: IdType,
            email: String,
            name: String?
        ): User =
            User(
                userId = userId,
                email = email,
                name = name,
                password = null,
                providers = mutableSetOf(),
                createdAt = Instant.now()
            )

        fun retrieve(
            userId: IdType,
            email: String,
            password: String?,
            name: String?,
            createdAt: Instant,
            providers: Set<Provider>
        ): User =
            User(
                userId = userId,
                email = email,
                name = name,
                password = password,
                providers = providers.toMutableSet(),
                createdAt = createdAt
            )
    }
}
