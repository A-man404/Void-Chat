package com.example.pages.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.components.CustomInputField
import com.example.components.LoadingBar
import com.example.pages.main.MainScreen
import com.example.ui.theme.accentPastel
import com.example.ui.theme.backgroundColor
import com.example.ui.theme.cardColor
import com.example.ui.theme.errorColor
import com.example.ui.theme.primaryPastel
import com.example.ui.theme.successColor
import com.example.utils.Prefs
import com.example.viewmodel.AuthState
import com.example.viewmodel.AuthViewModel


object LoginScreen : Screen {
    @Composable
    override fun Content() {

        val viewModel: AuthViewModel = viewModel { AuthViewModel() }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        val navigator = LocalNavigator.currentOrThrow


        val authState = viewModel.authState.collectAsState()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(24.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Welcome back",
                    fontSize = 28.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(32.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = cardColor,
                    shape = RoundedCornerShape(24.dp),
                    elevation = 8.dp
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        email = CustomInputField("Email")
                        Spacer(modifier = Modifier.height(16.dp))
                        password = CustomInputField("Password", isPassword = true)
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = {
//                                coroutine.launch {
//                                    viewModel.loginUser(email, password)
//                                }
                                navigator.replaceAll(MainScreen)


                            },
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(backgroundColor = primaryPastel),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text("Login", color = Color.Black, fontWeight = FontWeight.SemiBold)
                        }

                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {

                            ResultData(authState, navigator)
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Forgot Password?",
                            color = accentPastel,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .clickable { /* TODO */ },
                            fontSize = 14.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                Row {
                    Text("Don't have an account?", color = Color.LightGray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Sign up",
                        color = accentPastel,
                        modifier = Modifier.clickable {
                            navigator.push(SignUpScreen)
                        },
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }

}


@Composable
fun ResultData(authState: State<AuthState>, navigator: Navigator) {


    if (authState.value.isLoading) {
        LoadingBar()
    }

    authState.value.errorMessage?.let {
        Text("Error: $it", color = errorColor)
        println(it)
    }

    authState.value.message?.let {
        Text("Success", color = successColor)
    }

    when (val data = authState.value.data) {
        is String -> {
            Prefs.saveToken(data)
            navigator.replaceAll(MainScreen)
        }

        is Boolean -> {
            if (data) {
//                navigator.replaceAll(MainScreen)
            } else {
                Text("Registration failed", color = errorColor)
            }
        }
    }
}

