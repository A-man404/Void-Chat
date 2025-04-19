package org.example.voidchat.pages

import androidx.compose.runtime.*
import androidx.compose.web.events.SyntheticMouseEvent
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.forms.Input
import com.varabyte.kobweb.silk.components.text.SpanText
import kotlinx.coroutines.launch
import org.example.voidchat.model.User1
import org.example.voidchat.repository.UserRepository
import org.jetbrains.compose.web.ExperimentalComposeWebApi
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.*
import org.w3c.dom.Text

@Page("/login")
@Composable
fun LoginPage() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val pageContext = rememberPageContext()
    val isVisible = remember { mutableStateOf(false) }
    val errorText = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .backgroundColor(Color("#1e1e2e"))
            .display(DisplayStyle.Flex)
            .flexDirection(FlexDirection.Column)
            .justifyContent(JustifyContent.Center)
            .alignItems(AlignItems.Center)
            .padding(24.px)
    ) {
        Column(
            modifier = Modifier
                .maxWidth(400.px)
                .padding(32.px)
                .backgroundColor(Color("#181825"))
                .borderRadius(16.px)
                .boxShadow(blurRadius = 20.px, color = Color("#b4befe"))
        ) {
            SpanText(
                "Welcome Back",
                modifier = Modifier
                    .fontSize(28.px)
                    .fontWeight(FontWeight.Bold)
                    .color(Color("#cdd6f4"))
                    .margin(bottom = 24.px)
            )

            InputField("Email", email) { email = it }
            InputField("Password", password, isPassword = true) { password = it }

            StyledButton2("Log In", "#b4befe") {
                coroutineScope.launch {
                    val res = UserRepository.loginUser(User1(email = email, password = password))
                    if (res.statusCode == 200) {
                        pageContext.router.navigateTo("/demo")
                    } else {
                        errorText.value = res.message
                        isVisible.value = true
                    }
                }
            }

            if (isVisible.value) {
                SpanText(
                    errorText.value,
                    modifier = Modifier.color(Color("#f38ba8")).margin(top = 12.px)
                )
            }
        }
    }
}

@Composable
fun InputField(label: String, value: String, isPassword: Boolean = false, onChange: (String) -> Unit) {
    var isFocused by remember { mutableStateOf(false) }

    Column(modifier = Modifier.margin(bottom = 20.px)) {
        Input(
            placeholder = label,
            type = if (isPassword) InputType.Password else InputType.Text,
            value = value,
            onValueChange = { onChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.px)
                .border(1.px, LineStyle.Solid, if (isFocused) Color("#b4befe") else Color("#585b70"))
                .backgroundColor(Color("#1e1e2e"))
                .color(Color("#cdd6f4"))
                .borderRadius(10.px)
                .onFocusIn { isFocused = true }
                .onFocusOut { isFocused = false }
        )
    }
}

@Composable
fun StyledButton2(text: String, bgColor: String, onClick: () -> Unit) {
    var isHovered by remember { mutableStateOf(false) }

    Button(
        onClick = onClick as (SyntheticMouseEvent) -> Unit,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.px)
            .display(DisplayStyle.Flex)
            .justifyContent(JustifyContent.Center)
            .backgroundColor(Color(bgColor))
            .color(Color("#1e1e2e"))
            .padding(topBottom = 16.px)
            .borderRadius(10.px)
            .fontSize(16.px)
            .fontWeight(FontWeight.Bold)
            .boxShadow(
                blurRadius = if (isHovered) 12.px else 0.px,
                spreadRadius = if (isHovered) 2.px else 0.px,
                color = Color(bgColor)
            )

            .onMouseEnter { isHovered = true }
            .onMouseLeave { isHovered = false },
    ) {
        org.jetbrains.compose.web.dom.Text(text)
    }
}

