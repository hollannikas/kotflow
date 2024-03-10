package processbuilder

fun kotFlow(
    name: String,
    init: ProcessBuilder.() -> Unit,
): Process {
    val builder = ProcessBuilder(name)
    builder.init()
    return builder.build()
}

open class ProcessBuilder(private val name: String = "Anonymous") {
    val flowNodes = mutableListOf<FlowNode>()

    fun initialState(name: String) {
        // Could add specific Initial State configuration here if needed
    }

    open fun startEvent(name: String) {
        flowNodes.add(StartEvent(name))
    }

    fun exclusiveGateway(
        name: String,
        init: GatewayBuilder.() -> Unit,
    ) {
        val gateway = ExclusiveGateway(name) // Here we don't pass a ConditionEvaluator yet
        val builder = GatewayBuilder(gateway) // Pass 'this' ProcessBuilder and the gateway
        builder.init()
        flowNodes.add(gateway)
    }

    open fun task(
        name: String,
        action: () -> Unit,
    ) {
        flowNodes.add(Task(name, action))
    }

    open fun endEvent(name: String) {
        flowNodes.add(EndEvent(name))
    }

    fun ProcessBuilder.next(block: ProcessBuilder.() -> Unit) {
        block() // Execute the next task or other DSL element in the flow
    }

    fun build(): Process {
        if (flowNodes.none { it::class == StartEvent::class }) {
            throw ProcessDefinitionException("Process must have a Start Event")
        }
        return Process(name, flowNodes.toList())
    }
}

interface FlowNode {
    val name: String
}

data class Process(val name: String, val flowNodes: List<FlowNode>)

data class Task(override val name: String, val action: () -> Unit) : FlowNode

abstract class Event(override val name: String) : FlowNode

data class StartEvent(override val name: String) : Event(name)

data class EndEvent(override val name: String) : Event(name)

abstract class Gateway(override val name: String) : FlowNode

class ExclusiveGateway(name: String) : Gateway(name) {
    lateinit var conditionEvaluator: ConditionEvaluator
    val successPath = mutableListOf<FlowNode>()
    val failurePath = mutableListOf<FlowNode>()
}

fun interface ConditionEvaluator {
    fun evaluate(): Boolean
}
