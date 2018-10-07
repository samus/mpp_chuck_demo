package com.synappticlabs.chuck.models

import kotlinx.serialization.*

@Serializable
data class Joke(val id: Int,
                @SerialName("joke") val text: String,
                val categories: List<String> = emptyList())