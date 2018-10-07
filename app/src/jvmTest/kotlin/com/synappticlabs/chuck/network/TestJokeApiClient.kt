package com.synappticlabs.chuck.network

import io.ktor.client.response.readText
import kotlin.test.Test
import kotlinx.coroutines.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestJokeApiClient {
    @Test
    fun `Can retrieve a random joke`() = runBlocking<Unit> {
        val client = JokeApiClient(GlobalScope.coroutineContext)
        val result = client.random()
        assertEquals("success", result.type)
        assertTrue(result.jokes.isNotEmpty(), "Should have returned a joke")
    }
}