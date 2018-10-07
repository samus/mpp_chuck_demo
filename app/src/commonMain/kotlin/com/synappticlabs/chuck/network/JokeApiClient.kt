package com.synappticlabs.chuck.network

//import io.ktor.client.HttpClient
//import io.ktor.client.features.json.JsonFeature
//import io.ktor.client.features.json.serializer.KotlinxSerializer
import com.synappticlabs.chuck.models.Joke
import kotlinx.serialization.*

//class JokeApiClient() {
//    val baseURL = "http://api.icndb.com/jokes/"
//
//    private val client = HttpClient{
//        install(JsonFeature) {
//            serializer = KotlinxSerializer().apply {
//
//            }
//        }
//    }
//}

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
