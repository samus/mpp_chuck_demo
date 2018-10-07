package com.synappticlabs.chuck.network

import com.synappticlabs.chuck.models.Joke
import io.ktor.client.HttpClient
import io.ktor.client.call.call
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.takeFrom
import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class JokeApiClient(val baseURL: String = "http://api.icndb.com") {

    private val client = HttpClient{
        install(JsonFeature) {
            serializer = KotlinxSerializer().apply {
                setMapper(JokesMessage::class, JokesMessage.serializer())
                setMapper(JokeMessage::class, JokeMessage.serializer())
            }
        }
    }

    suspend fun testRandom() = client.call {
        url{
            takeFrom(baseURL)
            encodedPath = "jokes/random/1"
            parameter("escape", "javascript")
        }
    }

    suspend fun random(count: Int = 1, exclusions: JokeExclusions = JokeExclusions.None): JokesMessage = client.get {
        url{
            takeFrom(baseURL)
            encodedPath = "jokes/random/${count}"
            parameter("escape", "javascript")
            exclusions.exclude?.let { exclusion -> parameter("exclude", exclusion) }
        }
    }
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
