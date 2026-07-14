package com.enesakin.vkhesaplama.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class BmiCalculatorTest {

    @Test
    fun `metric calculation returns rounded normal BMI`() {
        val result = BmiCalculator.calculateMetric(weightKg = 75.0, heightCm = 180.0)

        assertEquals(23.1, result.value, 0.0)
        assertEquals(BmiCategory.NORMAL, result.category)
    }

    @Test
    fun `imperial calculation matches metric result category`() {
        val result = BmiCalculator.calculateImperial(weightLb = 165.35, heightIn = 70.87)

        assertEquals(23.1, result.value, 0.1)
        assertEquals(BmiCategory.NORMAL, result.category)
    }

    @Test
    fun `category boundaries are classified correctly`() {
        assertEquals(BmiCategory.UNDERWEIGHT, BmiCalculator.calculateMetric(59.6, 180.0).category)
        assertEquals(BmiCategory.NORMAL, BmiCalculator.calculateMetric(60.0, 180.0).category)
        assertEquals(BmiCategory.OVERWEIGHT, BmiCalculator.calculateMetric(81.0, 180.0).category)
        assertEquals(BmiCategory.OBESE, BmiCalculator.calculateMetric(97.2, 180.0).category)
    }

    @Test
    fun `invalid metric input fails fast`() {
        assertThrows(IllegalArgumentException::class.java) {
            BmiCalculator.calculateMetric(weightKg = 0.0, heightCm = 180.0)
        }
        assertThrows(IllegalArgumentException::class.java) {
            BmiCalculator.calculateMetric(weightKg = 75.0, heightCm = 0.0)
        }
    }
}
