import processbuilder.KotFlowExecutor
import processbuilder.kotFlow

fun main() {
    val simpleProcess =
        kotFlow("Simple Sequential Process") {
            startEvent("Start")
            task("Task A") { println("Task A") }
            task("Task B") { println("Task B") }
            exclusiveGateway("Decision Point") {
                condition { false }
                success {
                    task("Success Task") { println("Success Task") }
                }
                failure {
                    task("Failure Task") { println("Failure Task") }
                }
            }
            endEvent("End")
        }

    val executor = KotFlowExecutor()
    executor.execute(simpleProcess)
}
