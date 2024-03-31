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
    private val sequences = mutableListOf<Sequence>()

    fun sequence(
        name: String,
        init: SequenceBuilder.() -> Unit,
    ) {
        val builder = SequenceBuilder(name)
        builder.init()
        sequences.add(builder.build())
    }

    fun build(): Process {
        return Process(name, sequences.toList())
    }
}

data class Process(val name: String, val sequences: List<Sequence> = emptyList())
