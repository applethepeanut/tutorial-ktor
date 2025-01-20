package com.atpfury.example

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.statuspages.*

fun Application.configureRouting() {

    install(StatusPages) {
        exception<IllegalStateException> { call, cause ->
            call.respondText("App in illegal state as ${cause.message}")
        }
    }

    routing {

        staticResources("/content", "static")

        get("/") {
            call.respondText("Hello World!")
        }

        get("/hi/{name}") {
            val name = call.parameters["name"]
            if (name == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            call.respondText("Hello $name")

        }

        get("/test") {
            val text = "<h1>Hello From Ktor</h1>"
            val type = ContentType.parse("text/html")
            call.respondText(text, type)
        }

        get("/error") {
            throw IllegalStateException("Too Busy")
        }
    }
}
