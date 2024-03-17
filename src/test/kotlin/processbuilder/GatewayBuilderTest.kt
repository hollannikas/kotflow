package processbuilder

import kotlin.test.Test

class GatewayBuilderTest {
    @Test
    fun `executes tasks in success path when condition is true`() {
        // 1. Define process with an exclusive gateway
        val process =
            kotFlow("Process with Gateway") {
                // ... setup ...
                receive("Start")
                exclusiveGateway("Decision Point") {
                    condition {
                        true
                    }
                    success {
                        invoke("Success Task") { /* ... */ }
                    }
                    failure {
                        invoke("Failure Task") { /* ... */ }
                    }
                }
                reply("End")
                // ... rest of the process ...
            }

        // 2. Setup Executor and force the condition to be true
        val executor = KotFlowExecutor()
        // ... potentially add mocking to control gateway condition ...

        // 3. Execute the process
        executor.execute(process)

        // 4. Assertions
        assert(executor.executionHistory.contains("Success Task"))
        assert(!executor.executionHistory.contains("Failure Task"))
    }

    @Test
    fun `executes tasks in failure path when condition is false`() {
        val process =
            kotFlow("Process with Gateway") {
                receive("Start")
                exclusiveGateway("Decision Point") {
                    condition {
                        false
                    }
                    success {
                        invoke("Success Task") { /* ... */ }
                    }
                    failure {
                        invoke("Failure Task") { /* ... */ }
                    }
                }
                reply("End")
            }

        val executor = KotFlowExecutor()
        executor.execute(process)

        assert(executor.executionHistory.contains("Failure Task"))
        assert(!executor.executionHistory.contains("Success Task"))
    }

    @Test
    fun `executes tasks in success path when condition is true and failure path is not defined`() {
        val process =
            kotFlow("Process with Gateway") {
                receive("Start")
                exclusiveGateway("Decision Point") {
                    condition {
                        true
                    }
                    success {
                        invoke("Success Task") { /* ... */ }
                    }
                }
                reply("End")
            }

        val executor = KotFlowExecutor()
        executor.execute(process)

        assert(executor.executionHistory.contains("Success Task"))
    }

    @Test
    fun `executes tasks in failure path when condition is false and success path is not defined`() {
        val process =
            kotFlow("Process with Gateway") {
                receive("Start")
                exclusiveGateway("Decision Point") {
                    condition {
                        false
                    }
                    failure {
                        invoke("Failure Task") { /* ... */ }
                    }
                }
                reply("End")
            }

        val executor = KotFlowExecutor()
        executor.execute(process)

        assert(executor.executionHistory.contains("Failure Task"))
    }
}
