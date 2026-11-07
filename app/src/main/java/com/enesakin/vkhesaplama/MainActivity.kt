package com.enesakin.vkhesaplama

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.ContextThemeWrapper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.enesakin.vkhesaplama.ui.theme.VKİHesaplamaTheme
import kotlinx.coroutines.launch

class PreferenceHelper(context: Context) {
    companion object {
        private const val IDEAL_WEIGHT_KEY = "IDEAL_WEIGHT"
        private const val BMI_KEY = "BMI"
        private const val HEIGHT_KEY = "HEIGHT"
        private const val WEIGHT_KEY = "WEIGHT"
        private const val DATETIME_KEY = "DATETIME"
        private const val AD_SOYAD_KEY = "AD_SOYAD"
        private const val EMAIL_KEY = "EMAIL"
        private const val PASSWORD_KEY = "PASSWORD"
    }

    private val sharedPreferences = context.getSharedPreferences("com.enesakin.myapplication", Context.MODE_PRIVATE)

    // Zaman kaydetme bölümü
    fun saveDateTime(dateTimeMillis: Long) {
        val editor = sharedPreferences.edit()
        editor.putLong(DATETIME_KEY, dateTimeMillis)
        editor.apply()
    }

    fun getDateTime(): Long {
        return sharedPreferences.getLong(DATETIME_KEY, 0L) // Varsayılan olarak 0L döndürülür.
    }

    // Kilo kaydetme bölümü
    fun saveWeight(weight: String) {
        val editor = sharedPreferences.edit()
        editor.putString(WEIGHT_KEY, weight)
        editor.apply()
    }

    fun getWeight(): String {
        return sharedPreferences.getString(WEIGHT_KEY, null) ?: ""
    }

    // Boy kaydetme bölümü
    fun saveHeight(height: String) {
        val editor = sharedPreferences.edit()
        editor.putString(HEIGHT_KEY, height)
        editor.apply()
    }

    fun getHeight(): String {
        return sharedPreferences.getString(HEIGHT_KEY, null) ?: ""
    }

    // Vücut kitle indeksi kaydetme bölümü
    fun saveBMI(bmi: String) {
        val editor = sharedPreferences.edit()
        editor.putString(BMI_KEY, bmi)
        editor.apply()
    }

    fun getBMI(): String {
        return sharedPreferences.getString(BMI_KEY, null) ?: ""
    }

    // İdeal kilo kaydetme bölümü
    fun saveIdealWeight(idealWeight: String) {
        val editor = sharedPreferences.edit()
        editor.putString(IDEAL_WEIGHT_KEY, idealWeight)
        editor.apply()
    }

    fun getIdealWeight(): String {
        return sharedPreferences.getString(IDEAL_WEIGHT_KEY, null) ?: ""
    }

    // AdSoyad kaydetme bölümü
    fun saveAdSoyad(adSoyad: String) {
        val editor = sharedPreferences.edit()
        editor.putString(AD_SOYAD_KEY, adSoyad)
        editor.apply()
    }

    fun getAdSoyad(): String {
        return sharedPreferences.getString(AD_SOYAD_KEY, null) ?: ""
    }

    // E-posta kaydetme bölümü
    fun saveEmail(email: String) {
        val editor = sharedPreferences.edit()
        editor.putString(EMAIL_KEY, email)
        editor.apply()
    }

    fun getEmail(): String {
        return sharedPreferences.getString(EMAIL_KEY, null) ?: ""
    }

    // Şifre kaydetme bölümü
    fun savePassword(password: String) {
        val editor = sharedPreferences.edit()
        editor.putString(PASSWORD_KEY, password)
        editor.apply()
    }

    fun getPassword(): String {
        return sharedPreferences.getString(PASSWORD_KEY, null) ?: ""
    }
    fun isRegistered(): Boolean {
        // Kullanıcı kaydı kontrolü yapılacak
        // Örnek bir kontrol, e-posta veya diğer kimlik bilgilerinin varlığını kontrol edebilir
        val email = getEmail()
        return !email.isNullOrEmpty()
    }
    fun setNotificationPermissionGranted(isGranted: Boolean) {
        sharedPreferences.edit().putBoolean("notification_permission_granted", isGranted).apply()
    }

    fun getNotificationPermissionGranted(): Boolean {
        return sharedPreferences.getBoolean("notification_permission_granted", false)
    }
}

class MainActivity : ComponentActivity() {
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        PreferenceHelper(applicationContext).setNotificationPermissionGranted(granted)
        if (granted) allowNotifications()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val preferenceHelper = PreferenceHelper(context = applicationContext)
        // AlarmManager ile bildirim ayarı
        setupNotificationAlarm()

        setContent {
            VKİHesaplamaTheme {
                var showDialog by remember { mutableStateOf(true) }
                val permissionGranted = preferenceHelper.getNotificationPermissionGranted()
                var showDialog1 by remember { mutableStateOf(!permissionGranted) }

                if (showDialog) {
                    RateUsDialog(
                        onRateUs = { rateUs() },
                        onClose = { showDialog = false }
                    )
                } else if (showDialog1) {
                    NotificationPermissionDialog(
                        onPermissionGranted = {
                            showDialog1 = false
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                                ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
                            ) {
                                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                preferenceHelper.setNotificationPermissionGranted(true)
                                allowNotifications()
                            }
                        },
                        onClose = {
                            showDialog1 = false
                            // Kullanıcı kapat dediğinde de bu durumu sakla
                            preferenceHelper.setNotificationPermissionGranted(false)
                        }
                    )
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    NavBotSheet(preferenceHelper)
                }
            }
        }
    }
    private fun setupNotificationAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE)

        // Şu andan 10 saniye sonra alarmı ayarla
        val triggerTime = System.currentTimeMillis() + 10000 // 10 saniye

        // Alarmı kur
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        )
    }
    private fun allowNotifications() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Bildirim kanalı oluştur
        val channelId = "channel_id"
        val channelName = "Channel Name"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        // Notification channels exist only on Android 8.0 (API 26) and newer.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(channel)
        }

        // Bildirim oluştur
        val notificationId = 1
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle("VKİ Hesaplama")
            .setContentText("Hey! Günlük diyetini takip ediyorsun değil mi?")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Android 13+ requires explicit runtime permission before posting.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) return
        try {
            NotificationManagerCompat.from(this).notify(notificationId, builder.build())
        } catch (_: SecurityException) {
            // Permission can be revoked between checking and posting.
            return
        }

        // AlarmManager kullanarak bildirim gönderimini planla (24 saatte bir)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_MUTABLE)

        val intervalMillis = 24 * 60 * 60 * 1000L // 24 saat
        val triggerTime = System.currentTimeMillis() + intervalMillis

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            intervalMillis,
            pendingIntent
        )

    }
    private fun rateUs() {
        val uri = Uri.parse("market://details?id=$packageName")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001
    }
}

@Composable
fun RateUsDialog(onRateUs: () -> Unit, onClose: () -> Unit) {
    AlertDialog(
        onDismissRequest = onClose,
        title = { Text(text = "Bize Oy Ver!⭐") },
        text = { Text("Google Play'de bizi değerlendirin! ❤\uFE0F") },
        confirmButton = {
            TextButton(onClick = onRateUs) {
                Text("Rate Us")
            }
        },
        dismissButton = {
            TextButton(onClick = onClose) {
                Text("Close")
            }
        }
    )
}
@Composable
fun NotificationPermissionDialog(
    onPermissionGranted: () -> Unit,
    onClose: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onClose,
        title = { Text(text = "Bildirim İzinleri\uD83D\uDD14") },
        text = { Text("Diyetini takip etmen çok önemli!❤\uFE0F Bildirimlerini her zaman açık tut.") },
        confirmButton = {
            TextButton(onClick = onPermissionGranted) {
                Text("Allow")
            }
        },
        dismissButton = {
            TextButton(onClick = onClose) {
                Text("Close")
            }
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBotSheet(preferenceHelper: PreferenceHelper) {
    val username = preferenceHelper.getAdSoyad()
    val email = preferenceHelper.getEmail()
    val navigationController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val context = LocalContext.current.applicationContext
    val selected = remember {
        mutableStateOf(Icons.Default.Home)
    }
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember {
        mutableStateOf(false)
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet {
                val myColor = Color(0xFF32357A)
                Box(
                    modifier = Modifier
                        .background(color = myColor)
                        .fillMaxWidth()
                        .height(230.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painterResource(id = R.drawable.profilresmi),
                            contentDescription = ""
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxSize()
                            .padding(top = 100.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$username",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxSize()
                            .padding(top = 140.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$email",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                        )
                    }

                }
                HorizontalDivider(
                    modifier = Modifier,
                    thickness = 1.dp,
                    color = Color.Black
                )
                val myColor0 = Color(0xFF32357A)
                Spacer(modifier = Modifier.height(20.dp))
                NavigationDrawerItem(label = {
                    Text(
                        text = "Anasayfa",
                        color = myColor0,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                    selected = false,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "home",
                            tint = Color.Black,
                            modifier = Modifier.scale(1.5f)
                        )
                    },
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navigationController.navigate(Screens.Home.screen) {
                            popUpTo(0)
                        }
                    })
                Spacer(modifier = Modifier.height(20.dp))
                NavigationDrawerItem(label = {
                    Text(
                        text = "Profil",
                        color = myColor0,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                    selected = false,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "com.enesakin.vkhesaplama.RegisteredProfilContent",
                            tint = Color.Black,
                            modifier = Modifier.scale(1.5f)
                        )
                    },
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navigationController.navigate(Screens.RegisteredProfilContent.screen) {
                            popUpTo(0)
                        }
                    })
                Spacer(modifier = Modifier.height(20.dp))
            }
        },
    ) {
        val myColor = Color(0xFF32357A)
        Scaffold(
            topBar = {

                val sheetcoroutineScope = rememberCoroutineScope()
                TopAppBar(title = { Text(text = "İdeal Kilo Hesaplama") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = myColor,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    ),
                    navigationIcon = {
                        IconButton(onClick = {
                            sheetcoroutineScope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                Icons.Rounded.Menu, contentDescription = "MenuButton",
                                tint = Color.White
                            )
                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar(containerColor = myColor) {
                    //HOME
                    IconButton(onClick = {
                        selected.value = Icons.Default.Home
                        navigationController.navigate(Screens.Home.screen) {
                            popUpTo(0)
                        }
                    }, modifier = Modifier.weight(1f)) {
                        Icon(
                            Icons.Default.Home, contentDescription = null, modifier = Modifier.size(28.dp),
                            tint = if (selected.value == Icons.Default.Home) Color.White else Color.LightGray
                        )
                    }
                    //HOME

                    //SEARCH
                    val myImageVector: ImageVector = ImageVector.vectorResource(R.drawable.past)
                    val myImageVector1: ImageVector = ImageVector.vectorResource(R.drawable.food)
                    IconButton(onClick = {
                        selected.value = myImageVector
                        navigationController.navigate(Screens.Past.screen) {
                            popUpTo(0)
                        }
                    }, modifier = Modifier.weight(1f)) {
                        Icon(
                            imageVector = myImageVector,
                            contentDescription = null, modifier = Modifier.size(28.dp),
                            tint = if (selected.value == myImageVector) Color.White else Color.LightGray
                        )
                    }
                    //SEARCH
                    //DİYET LİSTELERİ
                    IconButton(onClick = {
                        selected.value = myImageVector1
                        navigationController.navigate(Screens.DiyetListeleri.screen) {
                            popUpTo(0)
                        }
                    }, modifier = Modifier.weight(1f)) {//burada weight var normalde
                        Icon(
                            imageVector = myImageVector1,
                            contentDescription = null, modifier = Modifier.size(28.dp),
                            tint = if (selected.value == myImageVector1) Color.White else Color.LightGray
                        )
                    }
                    //DİYET LİSTELERİ

                    //PROFİL
                    IconButton(onClick = {
                        selected.value = Icons.Default.Person
                        navigationController.navigate(Screens.RegisteredProfilContent.screen) {
                            popUpTo(0)
                        }
                    }, modifier = Modifier.weight(1f)) {
                        Icon(
                            Icons.Default.Person, contentDescription = null, modifier = Modifier.size(28.dp),
                            tint = if (selected.value == Icons.Default.Person) Color.White else Color.LightGray
                        )
                    }
                    //PROFİL
                }
            }
        ) {
            NavHost(
                navController = navigationController,
                startDestination = Screens.Home.screen
            ) {
                composable(Screens.Home.screen) { Home(preferenceHelper = PreferenceHelper(context)) }
                composable(Screens.RegisteredProfilContent.screen) {
                    RegisteredProfileContent(
                        navigationController,
                        preferenceHelper = PreferenceHelper(context)
                    )
                }
                composable(Screens.Past.screen) { Past(preferenceHelper = PreferenceHelper(context)) }
                composable(Screens.DiyetListeleri.screen) { DiyetListeleri(preferenceHelper, navigationController) }
                composable(Screens.Photo.screen) { Photo(navigationController) }
                composable(Screens.Login.screen) { Login(navigationController, preferenceHelper = PreferenceHelper(context)) }
                composable(Screens.Register.screen) {
                    Register(
                        preferenceHelper = PreferenceHelper(context),
                        navigationController
                    )
                }
                composable(Screens.ProfileLoginTrue.screen) {
                    ProfileLoginTrue(
                        preferenceHelper = PreferenceHelper(context),
                        navigationController
                    )
                }
                composable(Screens.Fitness.screen) { FitnessPage(navigationController) }
                composable(Screens.KiloAlma.screen) { KiloAlma(navigationController) }
                composable(Screens.KiloVerme.screen) { KiloVerme(navigationController) }
                composable(Screens.Gut.screen) { Gut(navigationController) }
                composable(Screens.AnneveBebek.screen) { AnneveBebek(navigationController) }
                composable(Screens.Meta.screen) { Meta(navigationController) }
                composable(Screens.Kardiyo.screen) { Kardiyo(navigationController) }
                composable(Screens.Splash.screen) { SplashScreen(navigationController) }
            }
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    BottomSheetItem(icon = Icons.Default.MailOutline, title = "İletişim") {
                        Toast.makeText(context, "İletişim", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

@Composable
fun SpecialText(string: String) {
    Text(
        text = string,
        fontSize = 20.sp,
        fontStyle = FontStyle.Normal,
        fontFamily = FontFamily.SansSerif,
        color = Color(0xFF32357A),
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun SpecialTextField1(
    string: String,
    function: (String) -> Unit
) {
    val maxLength = 3
    val value = if (string.length > maxLength) {
        string.substring(0, maxLength)
    } else {
        string
    }

    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.length <= maxLength) {
                function(newValue)
            }
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_pregnant_woman_24),
                contentDescription = "Kilo",
                tint = Color(0xFFF04DA1)
            )
        }, trailingIcon = {
            Text(
                text = "kg",
                modifier = Modifier.alpha(0.5f)
            )
        },
        label = {
            Text(
                text = "Kilo",
                color = Color.Black.copy(alpha = 0.3f),
                textAlign = TextAlign.Center
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        modifier = Modifier
            .fillMaxWidth(),
        maxLines = 1,
        shape = RoundedCornerShape(10.dp)
    )
}

@Composable
fun SpecialTextField(
    string: String,
    function: (String) -> Unit
) {
    var isClicked by remember { mutableStateOf(false) }
    var borderColor by remember { mutableStateOf(Color(0xFF5F9EA0)) }
    val maxLength = 3
    val value = if (string.length > maxLength) {
        string.substring(0, maxLength)
    } else {
        string
    }

    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.length <= maxLength) {
                function(newValue)
            }
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.boy_24),
                contentDescription = "Boy",
                tint = Color(0xFFF04DA1),
            )
        },
        trailingIcon = {
            Text(
                text = "cm",
                modifier = Modifier.alpha(0.5f)
            )
        },
        label = {
            Text(
                text = "Boy",
                color = Color.Black.copy(alpha = 0.3f),
                textAlign = TextAlign.Center
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                isClicked = !isClicked
                borderColor = if (isClicked) Color(0xFF4B0082) else Color(0xFF5F9EA0)
            },
        maxLines = 1,
        shape = RoundedCornerShape(10.dp),
    )
}

@Composable
fun GenderButton(
    modifier: Modifier,
    gender: String,
    isSelected: Boolean,
    iconId: Int,
    onGenderSelected: () -> Unit
) {
    if (gender == "Kadın") R.drawable.kadin1 else R.drawable.adam1
    val textColor = if (isSelected) Color.White else Color.Black
    val backgroundColor = if (isSelected && gender == "Kadın") {
        Color(0xFFF04DA1)
    } else if (isSelected && gender == "Erkek") {
        Color(0xFF007BA7)
    } else Color(0xFFF5F5F5)
    val iconColor = if (isSelected && gender == "Kadın"){
        Color(0xFFF5F5F5)
    } else if (isSelected && gender == "Erkek"){
        Color(0xFFF5F5F5)
    } else Color.Black
    Button(
        onClick = { onGenderSelected() },
        modifier = modifier
            .padding(horizontal = 8.dp)
            .height(40.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        ),
        border = BorderStroke(1.dp, color = Color.LightGray),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = gender,
                tint = iconColor, // İkon rengini beyaza ayarla
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp)) // İkon ile metin arasında bir boşluk bırak
            Text(
                text = gender,
                color = textColor // Buton içindeki yazı rengini beyaza ayarla
            )
        }
    }
}

fun calculateProgress(bmi: Float): Float {
    return when {
        bmi < 18.5 -> 0.2f
        bmi < 24.9 -> 0.5f
        bmi < 29.9 -> 0.8f
        else -> 1.0f
    }
}

fun calculateProgressColor(bmi: Float): Color {
    return when {
        bmi < 18.5 -> Color.Green // VKİ 18.5'ten küçükse yeşil
        bmi < 24.9 -> Color.Blue // VKİ 24.9'dan küçükse mavi
        bmi < 29.9 -> Color.Yellow // VKİ 29.9'dan küçükse sarı
        else -> Color.Red // Diğer durumlarda kırmızı
    }
}

fun calculateBMI(height: Float?, weight: Float?): Float? {
    // Boy veya kilo değerleri null ise null döndür
    if (height == null || weight == null) {
        return null
    }

    // Boyut birimi kontrolü ve dönüşümü
    val heightMeter = height / 100

    // Sıfıra bölme hatasını önlemek için kontrol ekle
    if (heightMeter == 0f) {
        return null
    }

    // BMI hesaplama
    return weight / (heightMeter * heightMeter)
}
// İdeal kilo hesaplama fonksiyonu
@SuppressLint("DefaultLocale")
fun calculateIdealWeight(height: Float, weight: Float, gender: String): String {
    val baseWeight = if (gender == "Kadın") 45.5 else 50.0
    val idealWeight = baseWeight + 2.3 * ((height / 2.54) - 60)
    return String.format("%.1f", idealWeight)
}
@Composable
fun SimpleButton(onClick: () -> Unit, modifier: Modifier) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF32357A),
            contentColor = Color.White
        )
    ) {
        Text(text = "Hesapla")
    }
}
@Composable
fun BottomSheetItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.clickable { onClick() }
    ) {
        val myColor = Color(0xFF007BA7)
        Icon(icon, contentDescription = null, tint = myColor)
        Text(text = title, color = Color.Black, fontSize = 22.sp)
    }
}
@Preview
@Composable
fun NavBotSheetPreview() {
    VKİHesaplamaTheme {
        val preferenceHelper = PreferenceHelper(context = ContextThemeWrapper(LocalContext.current, R.style.Theme_VKİHesaplama))
        NavBotSheet(preferenceHelper = preferenceHelper)
    }
}