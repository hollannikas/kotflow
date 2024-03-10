package processbuilder

import com.hollannikas.processbuilder.KotFlowExecutor
import com.hollannikas.processbuilder.STOP
import com.hollannikas.processbuilder.kotFlow
import kotlin.test.Test

class ProcessBuilderTest {
    @Test
    fun `executes tasks in sequential order`() {
        val process =
            kotFlow("Test Process") {
                startEvent("Start")
                task("Task A") { println("A") }
                task("Task B") { println("B") }
                endEvent("End")
            }

        // Need an Executor to test the KotFlow Process
        val executor = KotFlowExecutor()
        executor.execute(process)

        // Assertions about task execution order
        val expectedExecutionOrder = listOf("Start", "Task A", "Task B")
        assert(executor.executionHistory == expectedExecutionOrder)
        assert(executor.currentState == STOP) // After the whole process
    }
}
