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
import kotlinx.coroutines.launch
import org.example.voidchat.model.User
import org.example.voidchat.repository.UserRepository
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Text

@Page("/register")
@Composable
fun RegisterPage() {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()
    val pageContext = rememberPageContext()
    val isVisible = remember { mutableStateOf(false) }
    val errorText = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color("#0f0f1a"))
            .padding(32.px)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                src = "/images/mechanical-love.png",
                alt = "Register Image",
                modifier = Modifier
                    .maxWidth(420.px)
                    .borderRadius(16.px)
                    .boxShadow(blurRadius = 24.px, color = Color("#89b4fa"))
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(24.px)
                    .width(600.px)
            ) {
                SpanText(
                    "Create an Account",
                    modifier = Modifier
                        .color(Color("#cdd6f4"))
                        .fontSize(60.px)
                        .fontWeight(FontWeight.ExtraBold)
                        .textAlign(TextAlign.Center)
                        .letterSpacing(2.px)
                        .padding(bottom = 24.px)
                )

                InputField1("Name", name) { name = it }
                InputField1("Email", email) { email = it }
                InputField1("Password", password, isPassword = true) { password = it }

                StyledButton1("Register", "#89b4fa") {
                    coroutineScope.launch {
                        val res = UserRepository.registerUser(
                            User(name = name, email = email, password = password)
                        )
                        if (res.data == true) {
                            pageContext.router.navigateTo("/login")
                        } else {
                            errorText.value = res.message
                            isVisible.value = true
                        }
                    }
                }

                if (isVisible.value) {
                    SpanText(
                        errorText.value,
                        modifier = Modifier.color(Color("#f38ba8")).padding(top = 12.px)
                    )
                }
            }
        }
    }
}

@Composable
fun InputField1(label: String, value: String, isPassword: Boolean = false, onChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().margin(bottom = 16.px, leftRight = 16.px)) {
        Input(
            placeholder = label,
            type = if (isPassword) InputType.Password else InputType.Text,
            value = value,
            onValueChange = { onChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.px)
                .border(1.px, LineStyle.Solid, Color.gray)
                .backgroundColor(Color("#1e1e2e"))
                .color(Color.white)
                .borderRadius(8.px)
        )
    }
}

@Composable
fun StyledButton1(text: String, bgColor: String, onClick: () -> Unit) {
    var isHovered by remember { mutableStateOf(false) }

    Button(
        onClick = onClick as (SyntheticMouseEvent) -> Unit,
        modifier = Modifier
            .backgroundColor(Color(bgColor))
            .color(Color("#1e1e2e"))
            .padding(leftRight = 32.px, topBottom = 16.px)
            .borderRadius(12.px)
            .display(DisplayStyle.Flex)
            .justifyContent(JustifyContent.Center)
            .alignItems(AlignItems.Center)
            .fontSize(18.px)
            .fontWeight(FontWeight.Bold)
            .boxShadow(
                blurRadius = if (isHovered) 16.px else 0.px,
                color = Color(bgColor)
            )
            .onMouseEnter { isHovered = true }
            .onMouseLeave { isHovered = false }
    ) {
        Text(text)
    }
}
