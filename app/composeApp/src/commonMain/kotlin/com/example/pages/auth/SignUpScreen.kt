package com.example.pages.auth

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.components.CustomInputField
import com.example.ui.theme.accentPastel
import com.example.ui.theme.backgroundColor
import com.example.ui.theme.cardColor
import com.example.ui.theme.primaryPastel
import com.example.viewmodel.AuthViewModel
import kotlinx.coroutines.launch


object SignUpScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel: AuthViewModel = viewModel()
        val coroutine = rememberCoroutineScope()
        var name by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        val navigator = LocalNavigator.currentOrThrow
        val authState = viewModel.authState.collectAsState()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(24.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Create Account",
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
                        name = CustomInputField("Name")
                        Spacer(modifier = Modifier.height(16.dp))
                        email = CustomInputField("Email")
                        Spacer(modifier = Modifier.height(16.dp))
                        password = CustomInputField("Password", isPassword = true)
                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {

                                coroutine.launch {
                                    viewModel.registerUser(name, email, password)
                                }
                            },
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(backgroundColor = primaryPastel),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text("Sign Up", color = Color.Black, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                ResultData(authState, navigator)

                Spacer(modifier = Modifier.height(24.dp))

                Row {
                    Text("Already have an account?", color = Color.LightGray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Login",
                        color = accentPastel,
                        modifier = Modifier.clickable {
                            navigator.pop()
                        },
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }


}


