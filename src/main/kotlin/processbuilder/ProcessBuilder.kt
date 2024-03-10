package com.hollannikas.processbuilder

fun kotFlow(
    name: String,
    init: ProcessBuilder.() -> Unit,
): Process {
    val builder = ProcessBuilder(name)
    builder.init()
    return builder.build()
}

class ProcessBuilder(private val name: String) {
    private val flowElements = mutableListOf<FlowElement>()

    fun initialState(name: String) {
        // Could add specific Initial State configuration here if needed
    }

    fun startEvent(name: String) {
        flowElements.add(StartEvent(name))
    }

    fun task(
        name: String,
        action: () -> Unit,
    ) {
        flowElements.add(Task(name, action))
    }

    fun endEvent(name: String) {
        flowElements.add(EndEvent(name))
    }

    fun ProcessBuilder.next(block: ProcessBuilder.() -> Unit) {
        block() // Execute the next task or other DSL element in the flow
    }

    fun build(): Process {
        if (flowElements.none { it::class == StartEvent::class }) {
            throw ProcessDefinitionException("Process must have a Start Event")
        }
        return Process(name, flowElements.toList())
    }
}

interface FlowElement {
    val name: String
}

data class Process(val name: String, val tasks: List<FlowElement>)

data class Task(override val name: String, val action: () -> Unit) : FlowElement

abstract class Event(override val name: String) : FlowElement

data class StartEvent(override val name: String) : Event(name)

data class EndEvent(override val name: String) : Event(name)
