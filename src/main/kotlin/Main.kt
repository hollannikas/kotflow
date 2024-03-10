import processbuilder.KotFlowExecutor
import processbuilder.kotFlow

fun main() {
    val context = mutableMapOf<String, Any>()

    val simpleProcess =
        kotFlow("Simple Sequential Process") {
            start("Start")
            context["someKey"] = "someValue"
            task("Task A") { println("Task A") }
            task("Task B") {
                context["value"] = 200
                println("Task B")
            }
            exclusiveGateway("Decision Point") {
                condition { context["value"] == 200 }
                success {
                    task("Success Task") { println("Success Task") }
                }
                failure {
                    task("Failure Task") { println("Failure Task") }
                }
            }
            end("End")
        }

    val executor = KotFlowExecutor()
    executor.execute(simpleProcess)
}
