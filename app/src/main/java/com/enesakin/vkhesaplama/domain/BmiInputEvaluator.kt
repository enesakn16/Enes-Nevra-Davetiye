package com.enesakin.vkhesaplama.domain

/**
 * Converts text-field values into a validated BMI result without depending on Android APIs.
 *
 * The evaluator accepts both dot and comma decimal separators so Turkish keyboards and
 * imported values behave consistently.
 */
object BmiInputEvaluator {

    fun evaluateMetric(weightInput: String, heightInput: String): BmiEvaluation {
        val weight = parseNumber(weightInput)
            ?: return BmiEvaluation.Invalid(BmiInputError.INVALID_WEIGHT)
        val height = parseNumber(heightInput)
            ?: return BmiEvaluation.Invalid(BmiInputError.INVALID_HEIGHT)

        if (weight !in 20.0..500.0) {
            return BmiEvaluation.Invalid(BmiInputError.WEIGHT_OUT_OF_RANGE)
        }
        if (height !in 80.0..250.0) {
            return BmiEvaluation.Invalid(BmiInputError.HEIGHT_OUT_OF_RANGE)
        }

        return BmiEvaluation.Success(
            BmiCalculator.calculateMetric(weightKg = weight, heightCm = height),
        )
    }

    private fun parseNumber(value: String): Double? {
        return value
            .trim()
            .replace(',', '.')
            .takeIf(String::isNotEmpty)
            ?.toDoubleOrNull()
            ?.takeIf(Double::isFinite)
    }
}

sealed interface BmiEvaluation {
    data class Success(val result: BmiResult) : BmiEvaluation
    data class Invalid(val error: BmiInputError) : BmiEvaluation
}

enum class BmiInputError {
    INVALID_WEIGHT,
    INVALID_HEIGHT,
    WEIGHT_OUT_OF_RANGE,
    HEIGHT_OUT_OF_RANGE,
}
