package com.synappticlabs.chuck.network

import io.ktor.client.call.TypeInfo
import io.ktor.client.features.json.JsonSerializer
import io.ktor.client.response.HttpResponse
import io.ktor.client.response.readText
import io.ktor.http.ContentType
import io.ktor.http.content.OutgoingContent
import io.ktor.http.content.TextContent
import kotlinx.io.charsets.Charsets
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JSON
import kotlin.reflect.KClass

/**
 * This serializer is a near 100% copy of KotlinxSerializer except for one crucial detail.
 * It forces the text read from the response to be in UTF-8.  This is only a problem on iOS.
 */
class ChuckSerializer : JsonSerializer {
    private val mappers = mutableMapOf<KClass<Any>, KSerializer<Any>>()

    /**
     * Set mapping from [type] to generated [KSerializer].
     */
    fun <T : Any> setMapper(type: KClass<T>, serializer: KSerializer<T>) {
        mappers[type as KClass<Any>] = serializer as KSerializer<Any>
    }

    override fun write(data: Any): OutgoingContent {
        @Suppress("UNCHECKED_CAST")
        val content = JSON.stringify(mappers[data::class]!!, data)
        return TextContent(content, ContentType.Application.Json)
    }

    override suspend fun read(type: TypeInfo, response: HttpResponse): Any {
        val mapper = mappers[type.type]!!
        val text = response.readText(charset = Charsets.UTF_8)
        return JSON.parse(mapper, text)
    }
}