import processbuilder.KotFlowExecutor
import processbuilder.kotFlow

fun main() {
    val simpleProcess =
        kotFlow("Simple Sequential Process") {
            initialState("Start") // Mark the starting point
            startEvent("Start")
            task("Task A") { println("Task A") }
            next {
                task("Task B") { println("Task B") }
            }
            endEvent("End")
        }

    val executor = KotFlowExecutor()
    executor.execute(simpleProcess)
}
