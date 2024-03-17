package processbuilder

open class SequenceBuilder(private val name: String = "Anonymous") {
    val activities = mutableListOf<Activity>()

    open fun receive(name: String) {
        activities.add(StartEvent(name))
    }

    fun exclusiveGateway(
        name: String,
        init: GatewayBuilder.() -> Unit,
    ) {
        val gateway = ExclusiveGateway(name) // Here we don't pass a ConditionEvaluator yet
        val builder = GatewayBuilder(gateway) // Pass 'this' ProcessBuilder and the gateway
        builder.init()
        activities.add(gateway)
    }

    open fun invoke(
        name: String,
        action: () -> Unit,
    ) {
        activities.add(Task(name, action))
    }

    open fun reply(name: String) {
        activities.add(EndEvent(name))
    }

    fun build(): Sequence {
        if (activities.none { it::class == StartEvent::class }) {
            throw ProcessDefinitionException("Process must have a Start Event")
        }
        return Sequence(name, activities.toList())
    }
}

interface Activity {
    val name: String
}

data class Sequence(val name: String, val activities: List<Activity>)

data class Task(override val name: String, val action: () -> Unit) : Activity

abstract class Event(override val name: String) : Activity

data class StartEvent(override val name: String) : Event(name)

data class EndEvent(override val name: String) : Event(name)

abstract class Gateway(override val name: String) : Activity

class ExclusiveGateway(name: String) : Gateway(name) {
    lateinit var conditionEvaluator: ConditionEvaluator
    val successPath = mutableListOf<Activity>()
    val failurePath = mutableListOf<Activity>()
}

fun interface ConditionEvaluator {
    fun evaluate(): Boolean
}
