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
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import org.example.voidchat.Settings.TokenSettings
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Transition
import org.jetbrains.compose.web.css.Transitions
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Text

@Page
@Composable
fun HomePage() {
    val pageContext = rememberPageContext()

    LaunchedEffect(Unit) {
        if (TokenSettings.getToken() != null) {
            pageContext.router.navigateTo("/demo")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color("#0f0f1a")
            )
            .padding(32.px)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(420.px)
                        .border(width = 3.px, color = Color("#89b4fa"), style = LineStyle.Solid)
                        .borderRadius(24.px)
                        .boxShadow(blurRadius = 24.px, color = Color("#89b4fa")),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        src = "/images/astro.png",
                        alt = "Void Chat Bot",
                        modifier = Modifier.size(400.px)
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(24.px)
                    .width(600.px)
            ) {
                SpanText(
                    "Void Chat",
                    modifier = Modifier
                        .color(Color("#cdd6f4"))
                        .fontSize(90.px)
                        .fontWeight(FontWeight.ExtraBold)
                        .textAlign(TextAlign.Center)
                        .letterSpacing(2.px)
                        .padding(bottom = 24.px)
                )

                SpanText(
                    "A futuristic chat app built for the modern galaxy. Connect. Converse. Explore the void.",
                    modifier = Modifier
                        .color(Color("#a6adc8"))
                        .fontSize(20.px)
                        .textAlign(TextAlign.Center)
                        .lineHeight(32.px)
                        .padding(bottom = 40.px)
                )

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.gap(24.px)
                ) {
                    StyledButton("Login", "#89b4fa") {
                        pageContext.router.navigateTo("/login")
                    }

                    StyledButton("Register", "#f38ba8") {
                        // TODO: Navigate to register
                        pageContext.router.navigateTo("/register")

                    }
                }
            }
        }
    }
}


@Composable
fun StyledButton(text: String, bgColor: String, onClick: () -> Unit) {
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
