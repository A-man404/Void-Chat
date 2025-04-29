package service


import domain.repository.ChatRepository
import io.ktor.websocket.*
import model.ChatMessage

object ChatService {
    private val activeSessions = mutableMapOf<String, MutableSet<WebSocketSession>>()

    suspend fun fetchChatHistory(roomID: String): List<ChatMessage> {
        return ChatRepository.fetchAllMessages(roomID)
    }

    suspend fun saveMessage(message: ChatMessage) {
        ChatRepository.insertMessage(message)
    }

    suspend fun addSession(roomID: String, webSocketSession: WebSocketSession) {
        activeSessions.computeIfAbsent(roomID) { mutableSetOf() }.add(webSocketSession)
    }

    suspend fun removeSession(roomID: String, webSocketSession: WebSocketSession) {
        activeSessions[roomID]?.remove(webSocketSession)
    }

    suspend fun sendMessage(roomID: String, message: ChatMessage) {
        activeSessions[roomID]?.forEach { session ->
            session.send(message.message)

        }
    }

}