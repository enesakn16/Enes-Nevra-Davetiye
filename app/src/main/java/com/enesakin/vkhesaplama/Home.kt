package com.enesakin.vkhesaplama

import android.view.ContextThemeWrapper
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.enesakin.vkhesaplama.domain.BmiCategory
import com.enesakin.vkhesaplama.domain.BmiEvaluation
import com.enesakin.vkhesaplama.domain.BmiInputError
import com.enesakin.vkhesaplama.domain.BmiInputEvaluator
import com.enesakin.vkhesaplama.domain.BmiResult
import com.enesakin.vkhesaplama.ui.theme.VKİHesaplamaTheme

@Composable
fun Home(preferenceHelper: PreferenceHelper) {
    val backgroundColor = Color(0xFFF5F5F5)
    val context = LocalContext.current

    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var idealWeight by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var bmiResult by remember { mutableStateOf<BmiResult?>(null) }

    fun saveResult(result: BmiResult, calculatedIdealWeight: String) {
        preferenceHelper.saveBMI(result.value.toString())
        preferenceHelper.saveIdealWeight(calculatedIdealWeight)
        preferenceHelper.saveHeight(height.trim())
        preferenceHelper.saveWeight(weight.trim())
        preferenceHelper.saveDateTime(System.currentTimeMillis())
    }

    fun errorMessage(error: BmiInputError): String = when (error) {
        BmiInputError.INVALID_WEIGHT -> "Kilo alanına geçerli bir sayı gir."
        BmiInputError.INVALID_HEIGHT -> "Boy alanına geçerli bir sayı gir."
        BmiInputError.WEIGHT_OUT_OF_RANGE -> "Kilo 20 ile 500 kg arasında olmalı."
        BmiInputError.HEIGHT_OUT_OF_RANGE -> "Boy 80 ile 250 cm arasında olmalı."
    }

    Surface(color = backgroundColor, modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.back2),
            contentDescription = "Arka plan",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize(),
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .padding(top = 115.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    SpecialText(string = "Boy")
                    Spacer(modifier = Modifier.padding(5.dp))
                    SpecialTextField(string = height) { height = it }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    SpecialText(string = "Kilo")
                    Spacer(modifier = Modifier.padding(5.dp))
                    SpecialTextField1(string = weight) { weight = it }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                GenderButton(
                    gender = "Kadın",
                    iconId = R.drawable.kadin1,
                    isSelected = gender == "Kadın",
                    onGenderSelected = { gender = "Kadın" },
                    modifier = Modifier
                        .weight(1f)
                        .padding(5.dp)
                        .fillMaxWidth()
                        .height(75.dp),
                )
                GenderButton(
                    gender = "Erkek",
                    iconId = R.drawable.adam1,
                    isSelected = gender == "Erkek",
                    onGenderSelected = { gender = "Erkek" },
                    modifier = Modifier
                        .weight(1f)
                        .padding(5.dp)
                        .fillMaxWidth()
                        .height(75.dp),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SimpleButton(
                onClick = {
                    if (gender.isBlank()) {
                        Toast.makeText(context, "Cinsiyetini seç.", Toast.LENGTH_SHORT).show()
                        return@SimpleButton
                    }

                    when (val evaluation = BmiInputEvaluator.evaluateMetric(weight, height)) {
                        is BmiEvaluation.Invalid -> {
                            bmiResult = null
                            Toast.makeText(
                                context,
                                errorMessage(evaluation.error),
                                Toast.LENGTH_SHORT,
                            ).show()
                        }

                        is BmiEvaluation.Success -> {
                            val parsedHeight = height.trim().replace(',', '.').toFloat()
                            val parsedWeight = weight.trim().replace(',', '.').toFloat()
                            val calculatedIdealWeight = calculateIdealWeight(
                                height = parsedHeight,
                                weight = parsedWeight,
                                gender = gender,
                            )

                            bmiResult = evaluation.result
                            idealWeight = calculatedIdealWeight
                            saveResult(evaluation.result, calculatedIdealWeight)
                        }
                    }
                },
                modifier = Modifier.padding(top = 16.dp),
            )

            bmiResult?.let { result ->
                ResultContent(result = result, idealWeight = idealWeight)
            }
        }
    }
}

@Composable
private fun ResultContent(result: BmiResult, idealWeight: String) {
    val classification = when (result.category) {
        BmiCategory.UNDERWEIGHT -> "Zayıf"
        BmiCategory.NORMAL -> "Normal"
        BmiCategory.OVERWEIGHT -> "Fazla kilolu"
        BmiCategory.OBESE -> "Obez"
    }

    Column(
        modifier = Modifier.padding(vertical = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "İdeal Kilonuz: $idealWeight kg",
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Vücut Kitle İndeksiniz: ${result.value}",
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
        )
        Spacer(modifier = Modifier.height(16.dp))
        CircularProgressIndicator(
            progress = { calculateProgress(result.value.toFloat()) },
            modifier = Modifier.size(48.dp),
            color = calculateProgressColor(result.value.toFloat()),
            strokeWidth = 10.dp,
        )
        Text(
            text = "Sınıfınız: $classification",
            color = Color.Black,
            fontSize = 16.sp,
            modifier = Modifier.padding(vertical = 8.dp),
        )
        Text(
            text = "Sonuç bilgilendirme amaçlıdır; tıbbi değerlendirme yerine geçmez.",
            color = Color.Black,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
        )
    }
}

@Preview
@Composable
fun HomePreview() {
    VKİHesaplamaTheme {
        val preferenceHelper = PreferenceHelper(
            context = ContextThemeWrapper(LocalContext.current, R.style.Theme_VKİHesaplama),
        )
        Home(preferenceHelper = preferenceHelper)
    }
}
