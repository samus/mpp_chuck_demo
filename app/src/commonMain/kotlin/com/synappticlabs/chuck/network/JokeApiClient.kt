package com.synappticlabs.chuck.network

import com.synappticlabs.chuck.models.Joke
import io.ktor.client.HttpClient
import io.ktor.client.call.HttpClientCall
import io.ktor.client.call.ReceivePipelineFail
import io.ktor.client.call.call
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.*
import io.ktor.client.response.readText
import io.ktor.http.*
import kotlinx.coroutines.CompletionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.io.charsets.Charset
import kotlinx.io.charsets.Charsets
import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.coroutines.CoroutineContext

interface Client {
    suspend fun random(count: Int = 1, exclusions: JokeExclusions = JokeExclusions.None): JokesMessage
}

class JokeApiClient(private val uiContext: CoroutineContext,
                    val baseURL: String = "http://api.icndb.com") {

    val client = HttpClient{
        install(JsonFeature) {
            serializer = ChuckSerializer().apply {
                setMapper(JokesMessage::class, JokesMessage.serializer())
                setMapper(JokeMessage::class, JokeMessage.serializer())
            }
        }
    }

    fun random(count: Int = 1,
               exclusions: JokeExclusions = JokeExclusions.None,
               completion: (message: JokesMessage?, error: Throwable?) -> Unit) {
        launchAndCatch(context = uiContext, onError = { error ->
            completion(null, error)
        }) {
            val message = random(count=count, exclusions = exclusions)
            completion(message, null)
        }
    }

    suspend fun random(count: Int = 1, exclusions: JokeExclusions = JokeExclusions.None): JokesMessage = client.get {
        url {
            takeFrom(baseURL)
            encodedPath = "jokes/random/${count}"
        }
        parameter("escape", "javascript")
        exclusions.exclude?.let { exclusion -> parameter("exclude", exclusion) }
        accept(ContentType.Application.Json.withCharset(Charsets.UTF_8))
    }
}

fun HttpClientCall.debugInfo(): String {
    return this.request.url.toString()
}


sealed class JokeExclusions(val exclude: String?) {
    object All: JokeExclusions("nerdy,explicit")
    object Nerdy: JokeExclusions("nerdy")
    object Explicit: JokeExclusions("explicit")
    object None: JokeExclusions(null)
}

@Serializable
data class JokeMessage(val type: String, @SerialName("value") @Optional val joke: Joke? = null) {
    fun isSuccess(): Boolean {
        return type == "success"
    }
}

@Serializable
data class JokesMessage(val type: String,
                       @SerialName("value") @Optional val jokes: List<Joke> = emptyList()){
    fun isSuccess(): Boolean {
        return type == "success"
    }
}

fun launchAndCatch(
    context: CoroutineContext,
    onError: (Throwable) -> Unit,
    function: suspend () -> Unit): Finalizable {
    val ret = Finalizable()
    GlobalScope.launch(context) {
        try {
            function()
        } catch (e: Throwable) {
            onError(e)
        } finally {
            ret.onFinally?.invoke()
        }
    }
    return ret
}

class Finalizable {
    var onFinally: (() -> Unit)? = null

    infix fun finally(f: () -> Unit) {
        onFinally = f
    }
}
