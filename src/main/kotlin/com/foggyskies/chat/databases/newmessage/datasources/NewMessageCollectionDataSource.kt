package com.foggyskies.chat.databases.newmessage.datasources

import com.foggyskies.chat.databases.newmessage.NewMessagesCollection
import com.jetbrains.handson.chat.server.chat.data.model.ChatMessage
import io.ktor.websocket.*

interface NewMessageCollectionDataSource {

    suspend fun checkOnExistDocument(idChat: String, idUser: String): Boolean

    suspend fun getNewMessagesByIdChat(idChat: String, idUser: String): List<ChatMessage>

    suspend fun insertOneMessage(idChat: String, idUser: String, message: ChatMessage)

    suspend fun clearOneChat(idChat: String, idUser: String)

    suspend fun createCollection(idUser: String)

    suspend fun createDocument(idChat: String, idUser: String)

    suspend fun watchForNewMessages(idUser: String, socket: DefaultWebSocketServerSession)

    suspend fun getAllNewMessages(idUser: String): List<NewMessagesCollection>
}