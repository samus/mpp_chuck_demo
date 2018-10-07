package com.synappticlabs.chuck.network

import com.synappticlabs.chuck.models.Joke
import kotlinx.serialization.json.JSON
import kotlin.test.*


class TestMessageSerialization {
    @Test
    fun `parses a joke`() {
        val joke = Joke(5, "chuck")
        val text = """{ "id": 5, "joke": "chuck"}"""
        assertEquals("{id:5,joke:chuck}", JSON.unquoted.stringify(joke))
        assertEquals(joke, JSON.parse<Joke>(text))
    }

    @Test
    fun `parses a single joke message`() {
        val msg = JokeMessage("success", Joke(5, "chuck"))
        val text = """{ "type": "success", "value": { "id": 5, "joke": "chuck"} }"""
        assertEquals("{type:success,value:{id:5,joke:chuck}}", JSON.unquoted.stringify(msg))
        assertEquals(msg, JSON.parse<JokeMessage>(text))
    }

    @Test
    fun `parses a multi-joke message`() {
        val msg = JokesMessage(type = "success", jokes = listOf(Joke(1, "chuck1"), Joke(2, "chuck2")))
        val text = """{ "type": "success", "value": [{ "id": 1, "joke": "chuck1"}, { "id": 2, "joke": "chuck2"}]}"""

        assertEquals("{type:success,value:[{id:1,joke:chuck1},{id:2,joke:chuck2}]}", JSON.unquoted.stringify(msg))
        assertEquals(msg, JSON.parse<JokesMessage>(text))
    }
}