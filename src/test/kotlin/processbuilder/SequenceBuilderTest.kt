package processbuilder

import kotlin.test.Test

class SequenceBuilderTest {
    @Test
    fun `executes tasks in sequential order`() {
        val process =
            kotFlow("Test Process") {
                receive("Start")
                invoke("Task A") { println("A") }
                invoke("Task B") { println("B") }
                reply("End")
            }

        // Need an Executor to test the KotFlow Process
        val executor = KotFlowExecutor()
        executor.execute(process)

        // Assertions about task execution order
        val expectedExecutionOrder = listOf("Start", "Task A", "Task B", "End")
        assert(executor.executionHistory == expectedExecutionOrder)
        // assert(executor.currentState == STOP) // After the whole process
    }
}
