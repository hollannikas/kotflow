package processbuilder

import kotlin.test.Test

class ProcessBuilderTest {
    @Test
    fun `executes tasks in sequential order`() {
        val process =
            kotFlow("Test Process") {
                start("Start")
                task("Task A") { println("A") }
                task("Task B") { println("B") }
                end("End")
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
