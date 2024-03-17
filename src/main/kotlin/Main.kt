import processbuilder.KotFlowExecutor
import processbuilder.kotFlow

fun main() {
    val context = mutableMapOf<String, Any>()

    val simpleProcess =
        kotFlow("Simple Sequential Process") {
            receive("Start")
            context["someKey"] = "someValue"
            invoke("Task A") { println("Task A") }
            invoke("Task B") {
                context["value"] = 200
                println("Task B")
            }
            exclusiveGateway("Decision Point") {
                condition { context["value"] == 200 }
                success {
                    invoke("Success Task") { println("Success Task") }
                }
                failure {
                    invoke("Failure Task") { println("Failure Task") }
                }
            }
            reply("End")
        }

    val executor = KotFlowExecutor()
    executor.execute(simpleProcess)
}
