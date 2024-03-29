package com.foggyskies.chat.databases.content.models

import com.foggyskies.chat.newroom.SelectedPostWithIdPageProfile
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class ContentUsersDC(
    @BsonId
    var id: String,
    var type: String,
    var likes: List<String>,
    var comments: List<CommentDC>,
    var address: String,
    var description: String
){
    fun toSelectedPostWithIdPageProfile(idPageProfile: String, idUser: String, image: String, author: String): SelectedPostWithIdPageProfile {
        return SelectedPostWithIdPageProfile(
            idPageProfile = idPageProfile,
            item = ContentPreviewDC(
                id = id,
                address = address
            ),
            image = image,
            author = author,
            description = description,
            countComets = comments.size.toString(),
            countLikes = likes.size.toString(),
            isLiked = likes.contains(idUser)
        )
    }
    fun toContentPreview(): ContentPreviewDC {
        return ContentPreviewDC(
            id = id,
            address = address
        )
    }
}

@Serializable
data class CommentDC(
    var id: String,
    var idUser: String,
    var message: String,
    var date: String
)

@Serializable
data class ContentPreviewDC(
    @BsonId
    val id: String,
    val address: String,
)