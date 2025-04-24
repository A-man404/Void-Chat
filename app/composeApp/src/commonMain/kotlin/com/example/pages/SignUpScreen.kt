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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.components.CustomInputField

@Composable
fun SignUpScreen() {

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
                    CustomInputField("Name")
                    Spacer(modifier = Modifier.height(16.dp))
                    CustomInputField("Email")
                    Spacer(modifier = Modifier.height(16.dp))
                    CustomInputField("Password", isPassword = true)
                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { /* Sign up action */ },
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

            Spacer(modifier = Modifier.height(24.dp))

            Row {
                Text("Already have an account?", color = Color.LightGray)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "Login",
                    color = accentPastel,
                    modifier = Modifier.clickable { },
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}


