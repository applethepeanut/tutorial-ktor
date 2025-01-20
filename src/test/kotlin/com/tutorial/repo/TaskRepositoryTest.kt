package com.tutorial.repo

import com.tutorial.models.Priority
import com.tutorial.models.Task
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.assertFailsWith

class TaskRepositoryTest {

//    @Test
//    fun `addTask adds a new task`() {
//        val count = TaskRepository.allTasks().size
//        TaskRepository.addTask(Task("some new task", "some description", Priority.High))
//        assertEquals(count + 1, TaskRepository.allTasks().size)
//    }

    @Test
    fun `addTask fails when the name is empty`() {
        val exception = assertFailsWith<IllegalStateException> {
            TaskRepository.addTask(Task("", "some description", Priority.High))
        }
        assertEquals("Task name cannot be empty!", exception.message)
    }

    @Test
    fun `addTask fails when given a duplicate`() {
        val exception = assertFailsWith<IllegalStateException> {
            TaskRepository.addTask(Task("shopping", "", Priority.High))
        }
        assertEquals("Cannot duplicate task names!", exception.message)
    }

    @Test
    fun `taskByName finds the matching task`() {
        val expected = Task("cleaning", "Clean the house", Priority.Low)
        val task = TaskRepository.taskByName("cleaning")
        assertEquals(expected, task)
    }

    @Test
    fun `taskByName returns null when none are found`() {
        val task = TaskRepository.taskByName("nope")
        assertEquals(null, task)
    }

    @Test
    fun `tasksByPriority returns tasks with the given priority`() {
        val expectedTasks = mutableListOf(
            Task("shopping", "Buy the groceries", Priority.High)
        )
        val highPriorityTasks = TaskRepository.tasksByPriority(Priority.High)
        assertEquals(expectedTasks, highPriorityTasks)
    }

    @Test
    fun `tasksByPriority returns an empty list when none are found`() {
        assertEquals(emptyList<Task>(), TaskRepository.tasksByPriority(Priority.Vital))
    }

    @Test
    fun `allTasks returns all the repo tasks`() {
        val expectedTasks = mutableListOf(
            Task("cleaning", "Clean the house", Priority.Low),
            Task("gardening", "Mow the lawn", Priority.Medium),
            Task("shopping", "Buy the groceries", Priority.High),
            Task("painting", "Paint the fence", Priority.Medium)
        )
        val allTasks = TaskRepository.allTasks()
        assertEquals(expectedTasks, allTasks)
    }

}