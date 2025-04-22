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
import org.jetbrains.compose.web.ExperimentalComposeWebApi
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Text

@OptIn(ExperimentalComposeWebApi::class)
@Page
@Composable
fun HomePage() {
    val pageContext = rememberPageContext()
    var isHovered by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (TokenSettings.getToken() != null) {
            pageContext.router.navigateTo("/demo")
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(
            Color("#000000")
        ).padding(32.px)
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
                        src = "/images/astro.png", alt = "Void Chat Bot", modifier = Modifier.size(480.px)
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(24.px).width(600.px)
            ) {
                SpanText(
                    "Void Chat",
                    modifier = Modifier.color(Color("#cdd6f4")).fontSize(90.px).fontWeight(FontWeight.ExtraBold)
                        .textAlign(TextAlign.Center).letterSpacing(2.px).padding(bottom = 24.px)
                )

                SpanText(
                    "A futuristic chat app built for the modern galaxy. Connect. Converse. Explore the void.",
                    modifier = Modifier.color(Color("#a6adc8")).fontSize(20.px).textAlign(TextAlign.Center)
                        .lineHeight(32.px).padding(bottom = 40.px)
                )

                Row(
                    horizontalArrangement = Arrangement.Center, modifier = Modifier.gap(24.px)
                ) {
                    StyledButton("Login", "#6b91fe") {
                        pageContext.router.navigateTo("/login")
                    }

                    StyledButton("Register", "#ff44b6") {
                        pageContext.router.navigateTo("/register")

                    }
                }
            }
        }
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
            ).onMouseEnter { isHovered = true }.onMouseLeave { isHovered = false }) {
        Text(text)
    }
}
