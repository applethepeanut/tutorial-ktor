package com.tutorial.app

import com.tutorial.models.Priority
import com.tutorial.models.Task
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.statuspages.*
import com.tutorial.repo.TaskRepository.allTasks
import com.tutorial.models.tasksAsTable
import com.tutorial.repo.TaskRepository
import io.ktor.server.request.*

fun Application.configureRouting() {

    install(StatusPages) {
        exception<IllegalStateException> { call, cause ->
            call.respondText("App in illegal state as ${cause.message}")
        }
    }

    routing {

        staticResources("/content", "static")
        staticResources("/task-ui", "task-ui")

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

        route("/tasks") {

            get("/static") {
                call.respondText(
                    contentType = ContentType.parse("text/html"),
                    text = """
                <h3>TODO:</h3>
                <ol>
                    <li>A table of all the tasks</li>
                    <li>A form to submit new tasks</li>
                </ol>
                """.trimIndent()
                )
            }

            get {
                call.respondText(
                    contentType = ContentType.parse("text/html"),
                    text = allTasks().tasksAsTable()
                )
            }

            get("/byPriority/{priority}") {
                val priorityAsText = call.parameters["priority"]
                if (priorityAsText == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }

                try {
                    val priority = Priority.valueOf(priorityAsText)
                    val tasks = TaskRepository.tasksByPriority(priority)

                    if (tasks.isEmpty()) {
                        call.respond(HttpStatusCode.NotFound)
                        return@get
                    }

                    call.respondText(
                        contentType = ContentType.parse("text/html"),
                        text = tasks.tasksAsTable()
                    )
                } catch (ex: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            post {
                val formContent = call.receiveParameters()
                val params = Triple(
                    formContent["name"] ?: "",
                    formContent["description"] ?: "",
                    formContent["priority"] ?: ""
                )

                if (params.toList().any { it.isEmpty() }) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }

                try {
                    val priority = Priority.valueOf(params.third)
                    TaskRepository.addTask(
                        Task(
                            params.first,
                            params.second,
                            priority
                        )
                    )
                    call.respond(HttpStatusCode.NoContent)
                } catch (ex: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }
    }
}
