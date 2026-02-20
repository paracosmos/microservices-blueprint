package com.matoo.user.domain.model.provider

import com.matoo.core.constant.IdType
import com.matoo.core.dto.OAuthProvider
import com.matoo.core.util.CoreUtil
import com.matoo.user.domain.model.user.User
import java.time.Instant

class Provider(
    val providerId: IdType,
    val providerKey: ProviderKey,
    val providerUid: String,
    var providerName: String? = null,
    var providerPicture: String? = null,
    val createdAt: Instant = Instant.now()
) {
    companion object {
        fun local(user: User): Provider {
            return Provider(
                providerId = CoreUtil.generateId(),
                providerKey = ProviderKey.LOCAL,
                providerUid = user.userId
            )
        }

        fun oauth(
            oauthProvider: OAuthProvider,
            uid: String,
            name: String? = null,
            picture: String? = null
        ): Provider {
            val providerKey = ProviderKey.fromOAuthProvider(oauthProvider)
            return Provider(
                providerId = CoreUtil.generateId(),
                providerKey = providerKey,
                providerUid = uid,
                providerName = name,
                providerPicture = picture,
            )
        }

    }

    override fun toString(): String {
        return "Provider(id='$providerId', key=$providerKey, uid='$providerUid', providerName='$providerName', providerPicture='$providerPicture')"
    }

    fun hasNameTouched(
        name: String? = null,
    ): Boolean {
        var touched = false
        if (!name.isNullOrBlank() && name != providerName) {
            providerName = name
            touched = true
        }
        return touched
    }

    fun hasPictureTouched(
        picture: String? = null
    ): Boolean {
        var touched = false
        if (!picture.isNullOrBlank() && picture != providerPicture) {
            providerPicture = picture
            touched = true
        }
        return touched
    }
}
