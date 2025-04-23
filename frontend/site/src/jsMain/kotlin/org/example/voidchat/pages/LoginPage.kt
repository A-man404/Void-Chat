package org.example.voidchat.pages

import androidx.compose.runtime.*
import androidx.compose.web.events.SyntheticMouseEvent
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.forms.Input
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import org.example.voidchat.Settings.TokenSettings
import org.jetbrains.compose.web.ExperimentalComposeWebApi
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Text

@OptIn(ExperimentalComposeWebApi::class)
@Page(
    "/login"
)
@Composable
fun LoginPage() {
    val pageContext = rememberPageContext()
    var isHovered by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (TokenSettings.getToken() != null) {
            pageContext.router.navigateTo("/demo")
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color("#0d1117")).padding(32.px)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier.size(500.px).background(Color("#161c35"))
                        .border(width = 3.px, color = Color("#89b4fa"), style = LineStyle.Solid).borderRadius(24.px)
                        .boxShadow(blurRadius = 24.px, color = Color("#89b4fa")).onMouseEnter { isHovered = true }
                        .onMouseLeave { isHovered = false }.scale(if (isHovered) 1.2 else 1.0),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        src = "/images/login-astronaut.png", alt = "Login Image", modifier = Modifier.size(480.px)
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(24.px).width(600.px)
            ) {
                SpanText(
                    "Login to Void Chat",
                    modifier = Modifier.color(Color("#cdd6f4")).fontSize(60.px).fontWeight(FontWeight.ExtraBold)
                        .textAlign(TextAlign.Center).letterSpacing(2.px).padding(bottom = 24.px)
                )

                SpanText(
                    "Enter your credentials to connect with the galaxy.",
                    modifier = Modifier.color(Color("#a6adc8")).fontSize(18.px).textAlign(TextAlign.Center)
                        .lineHeight(32.px).padding(bottom = 40.px)
                )

                // Login form
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(24.px).width(500.px).background(Color("#1e2430"))
                        .borderRadius(16.px).border(width = 2.px, color = Color("#89b4fa")).padding(16.px)
                        .boxShadow(blurRadius = 8.px, color = Color("#00ffbf"))
                ) {
                    TextInputField(
                        label = "Username",
                        modifier = Modifier.padding(bottom = 16.px)
                    )
                    PasswordInputField(
                        label = "Password",
                        modifier = Modifier.padding(bottom = 24.px)
                    )

                    StyledButton("Login", "#6b91fe") {
                        pageContext.router.navigateTo("/dashboard")
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.Center, modifier = Modifier.gap(16.px).padding(top = 20.px)
                ) {
                    SpanText(
                        "Don't have an account? ",
                        modifier = Modifier.color(Color("#a6adc8")).fontSize(16.px)
                    )
                    StyledButton("Register", "#ff44b6") {
                        pageContext.router.navigateTo("/register")
                    }
                }
            }
        }
    }
}

@Composable
fun TextInputField(label: String, modifier: Modifier = Modifier) {
    var value by remember { mutableStateOf("") }

    Column(modifier = modifier) {
        SpanText(
            label, modifier = Modifier.color(Color("#a6adc8")).fontSize(16.px).padding(bottom = 8.px)
        )
        Input(
            value = value, type = InputType.Text, onValueChange = { value = it }, modifier = Modifier
                .background(Color("#2b2f3b"))
                .color(Color("#cdd6f4"))
                .border(width = 2.px, color = Color("#89b4fa"))
                .padding(12.px)
                .borderRadius(8.px)


        )
    }
}

@Composable
fun PasswordInputField(label: String, modifier: Modifier = Modifier) {
    var value by remember { mutableStateOf("") }

    Column(modifier = modifier) {
        SpanText(
            label, modifier = Modifier.color(Color("#a6adc8")).fontSize(16.px).padding(bottom = 8.px)
        )
        Input(
            type = InputType.Password, value = value, onValueChange = { value = it }, modifier = Modifier
                .background(Color("#2b2f3b"))
                .color(Color("#cdd6f4"))
                .border(width = 2.px, color = Color("#89b4fa"))
                .padding(12.px)
                .borderRadius(8.px)

        )
    }
}

@Composable
private fun StyledButton(text: String, bgColor: String, onClick: () -> Unit) {
    var isHovered by remember { mutableStateOf(false) }

    Button(
        onClick = onClick as (SyntheticMouseEvent) -> Unit,
        modifier = Modifier.backgroundColor(Color(bgColor)).color(Color("#1e1e2e"))
            .padding(leftRight = 32.px, topBottom = 16.px).borderRadius(24.px).display(DisplayStyle.Flex)
            .justifyContent(JustifyContent.Center).alignItems(AlignItems.Center).fontSize(18.px)
            .fontWeight(FontWeight.Bold).boxShadow(
                blurRadius = if (isHovered) 16.px else 0.px, color = Color(bgColor)
            ).onMouseEnter { isHovered = true }.onMouseLeave { isHovered = false }
    ) {
        Text(text)
    }
}
