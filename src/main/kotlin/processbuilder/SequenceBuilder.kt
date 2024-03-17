package processbuilder

fun kotFlow(
    name: String,
    init: SequenceBuilder.() -> Unit,
): Sequence {
    val builder = SequenceBuilder(name)
    builder.init()
    return builder.build()
}

open class SequenceBuilder(private val name: String = "Anonymous") {
    val flowNodes = mutableListOf<FlowNode>()

    open fun receive(name: String) {
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

    open fun invoke(
        name: String,
        action: () -> Unit,
    ) {
        flowNodes.add(Task(name, action))
    }

    open fun reply(name: String) {
        flowNodes.add(EndEvent(name))
    }

    fun build(): Sequence {
        if (flowNodes.none { it::class == StartEvent::class }) {
            throw ProcessDefinitionException("Process must have a Start Event")
        }
        return Sequence(name, flowNodes.toList())
    }
}

interface FlowNode {
    val name: String
}

data class Sequence(val name: String, val flowNodes: List<FlowNode>)

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
