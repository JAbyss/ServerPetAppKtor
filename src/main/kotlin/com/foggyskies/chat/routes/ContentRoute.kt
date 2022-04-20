package com.foggyskies.chat.routes

import com.foggyskies.chat.data.model.CommentDC
import com.foggyskies.chat.data.model.ContentRequestDC
import com.foggyskies.chat.newroom.ContentRoutController
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject
import java.text.SimpleDateFormat
import java.util.*

fun Route.contentRoute() {
    val routController by inject<ContentRoutController>()

    route("/content") {

        post("/addPostImage") {
            val post = call.receive<ContentRequestDC>()

            val token = call.request.headers["Auth"] ?: call.respond(HttpStatusCode.BadRequest, "Токен не получен.")
            val isTokenExist = routController.checkOnExistToken(token.toString())
            if (isTokenExist) {
                routController.addNewContent(post)
                call.respond(HttpStatusCode.OK, "Новый контент добавлен")
            } else {
                call.respond(HttpStatusCode.BadRequest, "Токен не существует.")
            }
        }

        get("/getContentPreview{idPageProfile}") {
            val idPageProfile =
                call.parameters["idPageProfile"] ?: call.respond(HttpStatusCode.BadRequest, "Id не получен.")

            val token = call.request.headers["Auth"] ?: call.respond(HttpStatusCode.BadRequest, "Токен не получен.")
            val isTokenExist = routController.checkOnExistToken(token.toString())
            if (isTokenExist) {
                val content = routController.getFirstFiftyContent(idPageProfile.toString())
                call.respond(HttpStatusCode.OK, content)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Токен не существует.")
            }
        }

        post("/addCommentToPost{idPageProfile}{idPost}") {
            val comment = call.receive<CommentDC>() ?: call.respond(HttpStatusCode.BadRequest, "Comment не получен.")

            val idPageProfile =
                call.parameters["idPageProfile"] ?: call.respond(HttpStatusCode.BadRequest, "IdPageProfile не получен.")
            val idPost = call.parameters["idPost"] ?: call.respond(HttpStatusCode.BadRequest, "IdPost не получен.")

            val token = call.request.headers["Auth"] ?: call.respond(HttpStatusCode.BadRequest, "Токен не получен.")
            val isTokenExist = routController.checkOnExistToken(token.toString())
            if (isTokenExist) {
                val sdf = SimpleDateFormat("hh:mm")
                val currentDate = sdf.format(Date())
                routController.addNewComment(idPageProfile.toString(), idPost.toString(), (comment as CommentDC).copy(id = ObjectId().toString(), date = currentDate))
                call.respond(HttpStatusCode.OK)
            }
        }

        get("/getPosts") {

            val token = call.request.headers["Auth"] ?: call.respond(HttpStatusCode.BadRequest, "Токен не получен.")
            val isTokenExist = routController.checkOnExistToken(token.toString())
            if (isTokenExist) {
                val posts = routController.getPosts(token.toString())
                call.respond(posts)
            }
        }
        get("/getComments{idPageProfile}{idPost}") {
            val token = call.request.headers["Auth"] ?: call.respond(HttpStatusCode.BadRequest, "Токен не получен.")

            val idPageProfile =
                call.parameters["idPageProfile"] ?: call.respond(HttpStatusCode.BadRequest, "IdPageProfile не получен.")
            val idPost = call.parameters["idPost"] ?: call.respond(HttpStatusCode.BadRequest, "IdPost не получен.")

            val isTokenExist = routController.checkOnExistToken(token.toString())
            if (isTokenExist) {
                val comments = routController.getOnePostComments(idPageProfile.toString(), idPost.toString())
                call.respond(comments)
            }
        }
        get("/getLikedUsers{idPageProfile}{idPost}") {
            val token = call.request.headers["Auth"] ?: call.respond(HttpStatusCode.BadRequest, "Токен не получен.")

            val idPageProfile =
                call.parameters["idPageProfile"] ?: call.respond(HttpStatusCode.BadRequest, "IdPageProfile не получен.")
            val idPost = call.parameters["idPost"] ?: call.respond(HttpStatusCode.BadRequest, "IdPost не получен.")

            val isTokenExist = routController.checkOnExistToken(token.toString())
            if (isTokenExist) {
                val likedUsers = routController.getAllLikedUsers(idPageProfile.toString(), idPost.toString())
                call.respond(likedUsers)
            }
        }
        get("/addLikeToPost{idPageProfile}{idPost}") {
            val token = call.request.headers["Auth"] ?: call.respond(HttpStatusCode.BadRequest, "Токен не получен.")

            val idPageProfile =
                call.parameters["idPageProfile"] ?: call.respond(HttpStatusCode.BadRequest, "IdPageProfile не получен.")
            val idPost = call.parameters["idPost"] ?: call.respond(HttpStatusCode.BadRequest, "IdPost не получен.")

            val isTokenExist = routController.checkOnExistToken(token.toString())
            if (isTokenExist) {
                val isLiked = routController.addLikeToPost(idPageProfile.toString(), idPost.toString(), token.toString())
                call.respond(HttpStatusCode.OK, isLiked)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Token не существует.")
            }
        }
        get("/getInfoAboutOnePost{idPageProfile}{idPost}") {
            val token = call.request.headers["Auth"] ?: call.respond(HttpStatusCode.BadRequest, "Токен не получен.")

            val idPageProfile =
                call.parameters["idPageProfile"] ?: call.respond(HttpStatusCode.BadRequest, "IdPageProfile не получен.")
            val idPost = call.parameters["idPost"] ?: call.respond(HttpStatusCode.BadRequest, "IdPost не получен.")

            val isTokenExist = routController.checkOnExistToken(token.toString())
            if (isTokenExist) {
                val info = routController.getInfoAboutOnePost(idPageProfile.toString(), idPost.toString(), token.toString())
                call.respond(HttpStatusCode.OK, info!!)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Token не существует.")
            }
        }
    }
}