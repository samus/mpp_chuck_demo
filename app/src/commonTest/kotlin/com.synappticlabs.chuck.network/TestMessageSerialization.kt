package com.synappticlabs.chuck.network

import com.synappticlabs.chuck.models.Joke
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.JSON
import kotlinx.serialization.parse
import kotlinx.serialization.stringify
import kotlin.test.*


class TestMessageSerialization {
    @UseExperimental(ImplicitReflectionSerializer::class)
    @Test
    fun `parses a joke`() {
        val joke = Joke(5, "chuck")
        val text = """{ "id": 5, "joke": "chuck", "categories": []}"""
        assertEquals("{id:5,joke:chuck,categories:[]}", JSON.unquoted.stringify(joke))
        assertEquals(joke, JSON.parse<Joke>(text))
    }

    @UseExperimental(ImplicitReflectionSerializer::class)
    @Test
    fun `parses a single joke message`() {
        val msg = JokeMessage("success", Joke(5, "chuck"))
        val text = """{ "type": "success", "value": { "id": 5, "joke": "chuck", "categories": []} }"""
        assertEquals("{type:success,value:{id:5,joke:chuck,categories:[]}}", JSON.unquoted.stringify(msg))
        assertEquals(msg, JSON.parse<JokeMessage>(text))
    }

    @UseExperimental(ImplicitReflectionSerializer::class)
    @Test
    fun `parses a multi-joke message`() {
        val msg = JokesMessage(type = "success", jokes = listOf(Joke(1, "chuck1"), Joke(2, "chuck2")))
        val text = """{ "type": "success", "value": [{ "id": 1, "joke": "chuck1", "categories": []},{ "id": 2, "joke": "chuck2", "categories": []}]}"""

        assertEquals("{type:success,value:[{id:1,joke:chuck1,categories:[]},{id:2,joke:chuck2,categories:[]}]}", JSON.unquoted.stringify(msg))
        assertEquals(msg, JSON.parse<JokesMessage>(text))
    }
}