package com.example.chatapp.data

data class UserData(
    var userId : String?="",
    var name : String?="",
    var number : String?="",
    var imageUrl : String?=""
){
    fun toMap() = mapOf(
        "userId" to userId,
        "name" to name,
        "number" to number,
        "imageUrl" to imageUrl
    )
}
data class ReceiverData(
    var receiverId : String?="",
    var receiverName : String?="",
    var receiverNumber : String?="",
    var receiverImageUrl : String?=""
){
    fun toMap() = mapOf(
        "receiverId" to receiverId,
        "receiverName" to receiverName,
        "receiverNumber" to receiverNumber,
        "receiverImageUrl" to receiverImageUrl
    )
}

data class ChatData(
    val chatId: String?="",
    val user1: ChatUser = ChatUser(),
    val user2: ChatUser = ChatUser(),
)

data class ChatUser(
    var userId: String?="",
    var name: String?="",
    var imageUrl: String?="",
    var number: String?="",
)

data class Message(
    var sendBy : String?="",
    val message : String?="",
    val timeStamp : String?=""
)

data class Status(
    val user: ChatUser = ChatUser(),
    val imageUrl : String?="",
    val timeStamp: Long?=null
)