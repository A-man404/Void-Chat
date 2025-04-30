package com.example.pages.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.utils.Constants.BASE_URL_CHAT
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.util.Platform
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch


@Composable
fun ChatEntryScreen() {
    var sender by remember { mutableStateOf("") }
    var recipient by remember { mutableStateOf("") }
    var startChat by remember { mutableStateOf(false) }

    if (startChat) {
        ChatScreen(sender = sender, recipient = recipient)
    } else {
        Column(Modifier.padding(16.dp)) {
            Text("Enter Sender and Recipient Emails")
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = sender,
                onValueChange = { sender = it },
                label = { Text("Sender Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = recipient,
                onValueChange = { recipient = it },
                label = { Text("Recipient Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    if (sender.isNotBlank() && recipient.isNotBlank()) {
                        startChat = true
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Start Chat")
            }
        }
    }
}

@Composable
fun ChatScreen(sender: String, recipient: String) {
    val scope = rememberCoroutineScope()
    val chatClient = remember { ChatClient() }

    var messageText by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit) {
        chatClient.connectIn(scope, sender, recipient)
        scope.launch {
            chatClient.messages.collect {
                messages += it
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Chat: $sender → $recipient", style = MaterialTheme.typography.h6)
        Spacer(Modifier.height(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            messages.forEach {
                Text(it)
            }
        }

        Row {
            BasicTextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            )
            Button(onClick = {
                scope.launch {
                    chatClient.sendMessage(messageText)
                    messageText = ""
                }
            }) {
                Text("Send")
            }
        }
    }
}


class ChatClient {
    private val client = HttpClient {
        install(WebSockets)
    }

    private var session: DefaultClientWebSocketSession? = null
    private val _messages = MutableSharedFlow<String>()
    val messages = _messages.asSharedFlow()

    fun connectIn(scope: CoroutineScope, sender: String, recipient: String) {
        val url = "ws://${BASE_URL_CHAT}/chat?sender=$sender&recipient=$recipient"

        scope.launch {
            try {
                client.webSocket(urlString = url) {
                    session = this
                    try {
                        for (frame in incoming) {
                            if (frame is Frame.Text) {
                                val text = frame.readText()
                                _messages.emit(text)
                            }
                        }
                    } catch (e: Exception) {
                        println("Receive error: ${e.message}")
                    }
                }
            } catch (e: Exception) {
                println("WebSocket connection error: ${e.message}")
            } finally {
                println("WebSocket closed")
                session = null
            }
        }
    }


    suspend fun sendMessage(text: String) {
        try {
            session?.send(Frame.Text(text))
        } catch (e: Exception) {
            println("Send error: ${e.message}")

        }
    }

    fun disconnect() {
        session?.cancel()
        session = null
    }
}


