package com.enesakin.vkhesaplama.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BmiInputEvaluatorTest {

    @Test
    fun `comma decimal separator is accepted`() {
        val evaluation = BmiInputEvaluator.evaluateMetric(
            weightInput = "75,5",
            heightInput = "180",
        )

        assertTrue(evaluation is BmiEvaluation.Success)
        val result = (evaluation as BmiEvaluation.Success).result
        assertEquals(23.3, result.value, 0.0)
        assertEquals(BmiCategory.NORMAL, result.category)
    }

    @Test
    fun `blank and non numeric values return field specific errors`() {
        assertEquals(
            BmiEvaluation.Invalid(BmiInputError.INVALID_WEIGHT),
            BmiInputEvaluator.evaluateMetric(" ", "180"),
        )
        assertEquals(
            BmiEvaluation.Invalid(BmiInputError.INVALID_HEIGHT),
            BmiInputEvaluator.evaluateMetric("75", "one-eighty"),
        )
    }

    @Test
    fun `values outside supported ranges return actionable errors`() {
        assertEquals(
            BmiEvaluation.Invalid(BmiInputError.WEIGHT_OUT_OF_RANGE),
            BmiInputEvaluator.evaluateMetric("19.9", "180"),
        )
        assertEquals(
            BmiEvaluation.Invalid(BmiInputError.HEIGHT_OUT_OF_RANGE),
            BmiInputEvaluator.evaluateMetric("75", "251"),
        )
    }

    @Test
    fun `whitespace is ignored`() {
        val evaluation = BmiInputEvaluator.evaluateMetric(" 80 ", " 200 ")

        assertEquals(
            BmiEvaluation.Success(BmiResult(20.0, BmiCategory.NORMAL)),
            evaluation,
        )
    }

    @Test
    fun `non finite values are rejected`() {
        assertEquals(
            BmiEvaluation.Invalid(BmiInputError.INVALID_WEIGHT),
            BmiInputEvaluator.evaluateMetric("NaN", "180"),
        )
        assertEquals(
            BmiEvaluation.Invalid(BmiInputError.INVALID_HEIGHT),
            BmiInputEvaluator.evaluateMetric("75", "Infinity"),
        )
    }
}
