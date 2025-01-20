package com.tutorial.app

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.assertContains

class TasksTest {

    @Test
    fun `getting tasks by priority` () = testApplication {
        application {
            module()
        }
        val response = client.get("/tasks/byPriority/Medium")
        val body = response.bodyAsText()

        assertContains(body, "Mow the lawn")
        assertContains(body, "Paint the fence")
    }

    @Test
    fun `getting tasks by priority, where none are found` () = testApplication {
        application {
            module()
        }
        val response = client.get("/tasks/byPriority/Vital")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `getting tasks by invalid priority` () = testApplication {
        application {
            module()
        }

        val response = client.get("/tasks/byPriority/bad")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `getting tasks by absent priority` () = testApplication {
        application {
            module()
        }

        val response = client.get("/tasks/byPriority/")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `get all tasks` () = testApplication {
        application {
            module()
        }

        val response = client.get("/tasks")
        val body = response.bodyAsText()

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("html", response.contentType()?.contentSubtype)
        assertContains(body, "Clean the house")
        assertContains(body, "Mow the lawn")
        assertContains(body, "Buy the groceries")
        assertContains(body, "Paint the fence")
    }
}