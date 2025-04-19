package org.example.voidchat.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.text.SpanText
import org.example.voidchat.Settings.TokenSettings

@Page("/demo")
@Composable
fun demo() {

    val pageContext = rememberPageContext()

    LaunchedEffect(Unit) {
        if (TokenSettings.getToken() == null) {
            pageContext.router.navigateTo("/")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().backgroundColor(org.jetbrains.compose.web.css.Color.violet),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Button(onClick = {
            TokenSettings.removeToken()
            pageContext.router.navigateTo("/")

        }) {
            SpanText("Remove Token")
        }
    }
}
