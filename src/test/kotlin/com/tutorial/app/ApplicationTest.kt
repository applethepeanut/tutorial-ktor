package com.tutorial.app

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.assertContains
import kotlin.test.assertTrue

class ApplicationTest {

    @Test
    fun testRoot() = testApplication {
        application {
            module()
        }
        val response = client.get("/")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Hello World!", response.bodyAsText())
    }

    @Test
    fun testTest() = testApplication {
        application {
            module()
        }

        val response = client.get("/test")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("html", response.contentType()?.contentSubtype)
        assertContains(response.bodyAsText(), "Hello From Ktor")
    }

    @Test
    fun testHi() = testApplication {
        application {
            module()
        }

        val response = client.get("/hi/shane")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("plain", response.contentType()?.contentSubtype)
        assertEquals("Hello shane", response.bodyAsText())
    }

    @Test
    fun testError() = testApplication {
        application {
            module()
        }

        val response = client.get("/error")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("plain", response.contentType()?.contentSubtype)
        assertEquals("App in illegal state as Too Busy", response.bodyAsText())
    }

    @Test
    fun testStaticContent() = testApplication {
        application {
            module()
        }

        val response = client.get("/content/sample.html")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("html", response.contentType()?.contentSubtype)
        assertTrue(response.bodyAsText().contains("This page is built with:"))
    }
}