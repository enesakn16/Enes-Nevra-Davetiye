package com.enesakin.vkhesaplama.domain

import kotlin.math.pow
import kotlin.math.round

/**
 * Pure Kotlin BMI calculation logic.
 *
 * Keeping this logic outside Compose makes it deterministic, reusable and easy to unit test.
 */
object BmiCalculator {

    fun calculateMetric(weightKg: Double, heightCm: Double): BmiResult {
        require(weightKg in 20.0..500.0) { "Weight must be between 20 and 500 kg." }
        require(heightCm in 80.0..250.0) { "Height must be between 80 and 250 cm." }

        val heightMeters = heightCm / 100.0
        return resultFor(weightKg / heightMeters.pow(2))
    }

    fun calculateImperial(weightLb: Double, heightIn: Double): BmiResult {
        require(weightLb in 44.0..1102.0) { "Weight must be between 44 and 1102 lb." }
        require(heightIn in 31.5..98.5) { "Height must be between 31.5 and 98.5 in." }

        return resultFor(703.0 * weightLb / heightIn.pow(2))
    }

    private fun resultFor(rawBmi: Double): BmiResult {
        val roundedBmi = round(rawBmi * 10.0) / 10.0
        val category = when {
            roundedBmi < 18.5 -> BmiCategory.UNDERWEIGHT
            roundedBmi < 25.0 -> BmiCategory.NORMAL
            roundedBmi < 30.0 -> BmiCategory.OVERWEIGHT
            else -> BmiCategory.OBESE
        }
        return BmiResult(value = roundedBmi, category = category)
    }
}

data class BmiResult(
    val value: Double,
    val category: BmiCategory,
)

enum class BmiCategory {
    UNDERWEIGHT,
    NORMAL,
    OVERWEIGHT,
    OBESE,
}
