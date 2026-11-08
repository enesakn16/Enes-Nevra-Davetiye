package com.enesakin.vkhesaplama

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.enesakin.vkhesaplama.security.PasswordVerifier
import com.enesakin.vkhesaplama.ui.theme.BlueGray
import com.enesakin.vkhesaplama.ui.theme.VKİHesaplamaTheme

@Composable
fun Register(preferenceHelper: PreferenceHelper, navController: NavController) {
    var adSoyad by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var sifre by remember { mutableStateOf("") }
    val context = LocalContext.current

    Surface {
        Column(modifier = Modifier.fillMaxSize()) {
            TopSection1()
            Spacer(modifier = Modifier.height(22.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp)
            ) {
                LoginTextField(
                    label = "Ad Soyad",
                    trailing = "",
                    value = adSoyad,
                    onValueChange = { adSoyad = it },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(15.dp))
                LoginTextField1(
                    label = "E-Posta",
                    trailing = "",
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(15.dp))
                LoginTextField2(
                    label = "Şifre",
                    trailing = "En az 8 karakter",
                    value = sifre,
                    onValueChange = { sifre = it },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(15.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    onClick = {
                        val normalizedEmail = email.trim().lowercase()
                        when {
                            adSoyad.isBlank() || normalizedEmail.isBlank() || sifre.isBlank() -> {
                                Toast.makeText(context, "Tüm alanları doldur.", Toast.LENGTH_SHORT).show()
                            }

                            sifre.length < 8 -> {
                                Toast.makeText(context, "Şifre en az 8 karakter olmalı.", Toast.LENGTH_SHORT).show()
                            }

                            isSecureProfileRegistered(normalizedEmail, preferenceHelper) -> {
                                Toast.makeText(context, "Bu e-posta zaten kayıtlı.", Toast.LENGTH_SHORT).show()
                            }

                            else -> {
                                preferenceHelper.saveAdSoyad(adSoyad.trim())
                                preferenceHelper.saveEmail(normalizedEmail)
                                preferenceHelper.savePassword(PasswordVerifier.create(sifre))
                                sifre = ""
                                navController.navigate("registeredprofilcontent")
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSystemInDarkTheme()) BlueGray else BlueGray,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(size = 4.dp)
                ) {
                    Text(
                        text = "Kayıt Ol",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                ClickableLoginText(navController = navController)
            }
        }
    }
}

fun isSecureProfileRegistered(email: String, preferenceHelper: PreferenceHelper): Boolean {
    return preferenceHelper.getEmail().equals(email, ignoreCase = true) &&
        PasswordVerifier.isSecureEncoding(preferenceHelper.getPassword())
}

@Composable
fun ClickableLoginText(navController: NavController) {
    val text = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = Color.Black,
                fontSize = 14.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Medium
            )
        ) {
            append("Zaten bir hesabınız var mı?")
        }
        withStyle(
            style = SpanStyle(
                color = Color(0xFF007BA7),
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Medium,
            )
        ) {
            append(" Giriş Yap")
        }
    }

    Text(
        text = text,
        modifier = Modifier.clickable { navController.navigate("login") }
    )
}

@Composable
private fun TopSection1() {
    val backgroundColor = Color(0xFFF5F5F5)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFF32357A))
            .padding(top = 105.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    modifier = Modifier.size(42.dp),
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = stringResource(id = R.string.app),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(15.dp))
                Column {
                    Text(
                        text = stringResource(id = R.string.ideal_kilo),
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                    Text(
                        text = stringResource(id = R.string.find_house),
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.height(35.dp))
            Text(
                modifier = Modifier.padding(bottom = 55.dp),
                text = stringResource(id = R.string.login1),
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White
            )
        }
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            painter = painterResource(id = R.drawable.arc_3),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            colorFilter = ColorFilter.tint(backgroundColor)
        )
    }
}

@Preview
@Composable
fun RegisterScreenPreview() {
    val navController = rememberNavController()
    val sharedPreferences = PreferenceHelper(LocalContext.current)
    VKİHesaplamaTheme {
        Register(preferenceHelper = sharedPreferences, navController = navController)
    }
}
