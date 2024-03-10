package processbuilder

class GatewayBuilder(private val gateway: ExclusiveGateway) {
    fun condition(evaluation: () -> Boolean) {
        gateway.conditionEvaluator = ConditionEvaluator { evaluation() } // Wrap the lambda in ConditionEvaluator
    }

    fun failure(onFail: ProcessBuilder.() -> Unit) {
        val tempElements = mutableListOf<String>()
        val tempBuilder = ProcessBuilder("temporary")
        onFail(tempBuilder)
        gateway.failurePath.addAll(tempBuilder.flowNodes)
    }

    fun success(onSuccess: ProcessBuilder.() -> Unit) {
        val tempElements = mutableListOf<String>()
        val tempBuilder = ProcessBuilder("temporary")
        onSuccess(tempBuilder)
        gateway.successPath.addAll(tempBuilder.flowNodes)
    }
}
