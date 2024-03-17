package processbuilder

import java.util.Stack

class KotFlowExecutor {
    private val executionStack = Stack<FlowNode>()
    val executionHistory = mutableListOf<String>()

    fun execute(sequence: Sequence) {
        // Initialization: assuming your process has a 'root' property that is a FlowNode
        executionStack.push(sequence.flowNodes.first())

        while (executionStack.isNotEmpty()) {
            val currentNode = executionStack.pop()
            if (currentNode != null) {
                executionHistory.add(currentNode.name)
                processNode(currentNode)
                executionStack.push(findNextNode(currentNode, sequence))
            }
        }
    }

    private fun findNextNode(
        currentNode: FlowNode,
        sequence: Sequence,
    ): FlowNode? {
        return sequence.flowNodes.asSequence()
            .dropWhile { it != currentNode }
            .drop(1)
            .firstOrNull()
    }

    private fun processNode(node: FlowNode) {
        when (node) {
            is Task -> {
                node.action()
            }
            is ExclusiveGateway -> {
                processGateway(node)
            }
        }
    }

    private fun processGateway(gatewayNode: ExclusiveGateway) {
        val path = if (gatewayNode.conditionEvaluator.evaluate()) gatewayNode.successPath else gatewayNode.failurePath
        executionStack.push(path.first()) // Push the first element of the chosen path
    }
}
