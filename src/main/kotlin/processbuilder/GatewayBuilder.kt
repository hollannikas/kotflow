package processbuilder

class GatewayBuilder(private val gateway: ExclusiveGateway) {
    fun condition(evaluation: () -> Boolean) {
        gateway.conditionEvaluator = ConditionEvaluator { evaluation() } // Wrap the lambda in ConditionEvaluator
    }

    fun failure(onFail: SequenceBuilder.() -> Unit) {
        val tempBuilder = SequenceBuilder("temporary")
        onFail(tempBuilder)
        gateway.failurePath.addAll(tempBuilder.activities)
    }

    fun success(onSuccess: SequenceBuilder.() -> Unit) {
        val tempBuilder = SequenceBuilder("temporary")
        onSuccess(tempBuilder)
        gateway.successPath.addAll(tempBuilder.activities)
    }
}
