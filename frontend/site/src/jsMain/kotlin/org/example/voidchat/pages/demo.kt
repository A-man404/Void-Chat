package org.example.voidchat.pages

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.forms.TextInput
import com.varabyte.kobweb.silk.components.text.SpanText
import org.example.voidchat.Settings.TokenSettings
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.MessageEvent
import org.w3c.dom.WebSocket

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

@Page("/chat")
@Composable
fun ChatPage() {
    var senderEmail by remember { mutableStateOf("") }
    var targetEmail by remember { mutableStateOf("") }
    var isConnected by remember { mutableStateOf(false) }

    var message by remember { mutableStateOf("") }
    val chatMessages = remember { mutableStateListOf<String>() }
    var ws by remember { mutableStateOf<WebSocket?>(null) }

    // Connect only when emails are provided and user clicks "Connect"
    fun connectWebSocket() {
        if (senderEmail.isNotBlank() && targetEmail.isNotBlank()) {
            val socket = WebSocket("ws://localhost:8081/chat/$senderEmail/$targetEmail")

            socket.onopen = {
                chatMessages += "✅ Connected as $senderEmail"
                isConnected = true
            }

            socket.onmessage = { event ->
                val data = event.data.toString()
                chatMessages += data
            }

            socket.onclose = {
                chatMessages += "❌ Disconnected from server"
                isConnected = false
            }

            socket.onerror = {
                chatMessages += "⚠️ WebSocket error"
            }

            ws = socket
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.px),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Email Input Section
        if (!isConnected) {
            SpanText("👥 Enter emails to start chatting")

            Spacer()
            TextInput(text = senderEmail, onTextChange = { senderEmail = it }, placeholder = "Your Email")
            Spacer()
            TextInput(text = targetEmail, onTextChange = { targetEmail = it }, placeholder = "Target Email")
            Spacer()
            Button(onClick = { connectWebSocket() }) {
                Text("Connect")
            }
        } else {
            SpanText("💬 Chat with $targetEmail")

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.px)
                    .backgroundColor(org.jetbrains.compose.web.css.Color.lightgray)
                    .padding(10.px)
                    .overflow(Overflow.Auto)
            ) {
                chatMessages.forEach {
                    SpanText(it)
                    Spacer()
                }
            }

            Spacer()

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextInput(text = message, onTextChange = { message = it }, modifier = Modifier.width(300.px))
                Spacer()
                Button(onClick = {
                    if (message.isNotBlank()) {
                        ws?.send(message)
                        chatMessages += "You: $message"
                        message = ""
                    }
                }) {
                    Text("Send")
                }
            }

            Spacer()
            Button(onClick = {
                ws?.close()
                isConnected = false
                ws = null
                chatMessages.clear()
            }) {
                Text("Disconnect")
            }
        }
    }
}
