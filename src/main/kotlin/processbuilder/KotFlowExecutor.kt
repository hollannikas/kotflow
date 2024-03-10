package com.hollannikas.processbuilder

const val STOP = "STOP"

class KotFlowExecutor {
    lateinit var currentState: String
    val executionHistory = mutableListOf<String>()

    fun execute(process: Process) {
        // ... find the initialState based on the process definition ...
        val initialState = process.flowElements.first().name
        currentState = initialState // Set the initial state

        while (currentState != STOP) {
            val task = findElementByName(currentState, process)
            if (task is Task) {
                task.action()
            }
            executionHistory.add(currentState)
            currentState = findNextState(currentState, process) // Update state for flow
        }
    }

    private fun findNextState(
        currentState: String,
        process: Process,
    ): String {
        val currentIndex = process.flowElements.indexOfFirst { it.name == currentState }
        return if (currentIndex != -1 && currentIndex < process.flowElements.size - 1) {
            process.flowElements[currentIndex + 1].name // Return the next task's name
        } else {
            STOP // Indicates the end of the process
        }
    }

    private fun findElementByName(
        currentState: String,
        process: Process,
    ): FlowElement {
        return process.flowElements.first { it.name == currentState }
    }
}
