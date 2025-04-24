package com.example.pages

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
import com.example.components.CustomInputField
import com.example.repository.AuthRepository
import kotlinx.coroutines.launch

val backgroundColor = Color(0xFF1E1E2E)
val primaryPastel = Color(0xFFFBE7A1)
val accentPastel = Color(0xFFBCAEFF)
val cardColor = Color(0xFF2B2B3C)

@Composable
fun LoginScreen() {
    val coroutine = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
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
                            coroutine.launch {
                                AuthRepository.userLogin(email, password)
                            }
                        },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(backgroundColor = primaryPastel),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("Login", color = Color.Black, fontWeight = FontWeight.SemiBold)
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



            Spacer(modifier = Modifier.height(24.dp))

            Row {
                Text("Don't have an account?", color = Color.LightGray)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "Sign up",
                    color = accentPastel,
                    modifier = Modifier.clickable { },
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

