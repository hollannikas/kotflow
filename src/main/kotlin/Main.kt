package com.hollannikas

import com.hollannikas.processbuilder.Task
import com.hollannikas.processbuilder.kotFlow

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    fun main() {
        val simpleProcess = kotFlow("Simple Sequential Process") {
            initialState("Start") // Mark the starting point
            startEvent("Start")
            task("Task A") { /* ... */ }
            next {
                task("Task B") { /* ... */ }
            }
            endEvent("End")
        }

        // Placeholder for execution logic
        println("Process Name: ${simpleProcess.name}")
        for (task in simpleProcess.tasks) {
            println("Executing Task: ${task.name}")
            if ( task is Task) {
                task.action()
            }
        }
    }
}