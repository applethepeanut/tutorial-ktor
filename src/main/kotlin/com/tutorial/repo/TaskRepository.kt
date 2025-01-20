package com.tutorial.repo

import com.tutorial.models.Task
import com.tutorial.models.Priority

val tasks = mutableListOf(
    Task("cleaning", "Clean the house", Priority.Low),
    Task("gardening", "Mow the lawn", Priority.Medium),
    Task("shopping", "Buy the groceries", Priority.High),
    Task("painting", "Paint the fence", Priority.Medium)
)