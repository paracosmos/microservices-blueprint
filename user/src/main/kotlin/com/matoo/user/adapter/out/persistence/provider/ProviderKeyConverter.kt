package com.matoo.user.adapter.out.persistence.provider

import com.matoo.user.domain.model.provider.ProviderKey
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class ProviderKeyConverter : AttributeConverter<ProviderKey, Char> {
    override fun convertToDatabaseColumn(attribute: ProviderKey): Char {
        return attribute.code
    }

    override fun convertToEntityAttribute(code: Char): ProviderKey {
        return ProviderKey.fromCode(code)
    }
}
