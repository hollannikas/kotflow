package processbuilder

import java.util.Stack

class KotFlowExecutor {
    private val executionStack = Stack<FlowNode>()
    val executionHistory = mutableListOf<String>()

    fun execute(process: Process) {
        // Initialization: assuming your process has a 'root' property that is a FlowNode
        executionStack.push(process.flowNodes.first())

        while (executionStack.isNotEmpty()) {
            val currentNode = executionStack.pop()
            if (currentNode != null) {
                executionHistory.add(currentNode.name)
                processNode(currentNode)
                executionStack.push(findNextNode(currentNode, process))
            }
        }
    }

    private fun findNextNode(
        currentNode: FlowNode,
        process: Process,
    ): FlowNode? {
        return process.flowNodes.asSequence()
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
