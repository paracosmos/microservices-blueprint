package com.matoo.core.event

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.matoo.core.event.dto.EventDto
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.matoo.core.event.dto.EventEnvelope
import com.matoo.core.event.dto.EventTopic

object EventSerde {
    private val mapper = jacksonObjectMapper()
        .registerModule(JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

    private val mapType = object : TypeReference<Map<String, String?>>() {}

    fun toStringMap(message: EventDto): Map<String, String?> =
        mapper.convertValue(message, mapType)

    fun serialize(value: Any): String =
        mapper.writeValueAsString(value)

    fun <T : Any> deserialize(json: String, type: Class<T>): T =
        mapper.readValue(json, type)

    fun <T : Any> deserialize(json: String, type: JavaType): T =
        mapper.readValue(json, type)

    fun <T : EventDto> toEnvelope(
        topic: EventTopic,
        data: T
    ): EventEnvelope<T> =
        EventEnvelope(
            type = topic.value,
            data = data
        )

    fun <T : EventDto> serializeEnvelope(topic: EventTopic, data: T): String =
        serialize(value = toEnvelope(topic, data))

    fun <T : EventDto> deserializeEnvelope(json: String, dataType: Class<T>): EventEnvelope<T> {
        val javaType = mapper.typeFactory
            .constructParametricType(EventEnvelope::class.java, dataType)
        return deserialize(json, javaType)
    }

}
