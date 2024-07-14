package com.example.chatapp.presentation.Screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.chatapp.DestinationScreens
import com.example.chatapp.Util.CommonDivider
import com.example.chatapp.Util.CommonImage
import com.example.chatapp.Util.navigateTo
import com.example.chatapp.data.CHATS
import com.example.chatapp.data.Message
import com.example.chatapp.domain.LCViewModel
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton
import com.zegocloud.uikit.service.defines.ZegoUIKitUser

@Composable
fun SingleChatScreen(
    navController: NavController,
    vm: LCViewModel,
    chatId: String,
) {
    var reply by rememberSaveable {
        mutableStateOf("")
    }
    val onSendReply = {
        vm.onSendReply(chatId, reply)
        reply = ""
    }
    val myUser = vm.userData.value
    var currentChat = vm.chats.value.first { it.chatId == chatId }
    val chatUser = if (myUser?.userId == currentChat.user1.userId) currentChat.user2
    else currentChat.user1

    var chatMessage = vm.chatMessages

    val receiverId = vm.receiverData.value?.receiverId
      //------------------------------------------


    // -------------------------------------------

    LaunchedEffect(key1 = Unit) {
        vm.populateMessages(chatId)
    }
    BackHandler {
        vm.dePopulateMessage()
        navigateTo(navController,DestinationScreens.ChatList.route)
    }

    Column {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ){

            ChatHeader(name = chatUser.name ?: "", imageUrl = chatUser.imageUrl ?: "") {
                navController.popBackStack()
                vm.dePopulateMessage()
            }
            Spacer(modifier = Modifier.width(100.dp))
            AndroidView(
                modifier = Modifier.size(35.dp),
                factory = {
                    ZegoSendCallInvitationButton(it).apply {
                        setIsVideoCall(true)
                        resourceID = "zego_uikit_call"
                        setInvitees(
                            listOf(
                                ZegoUIKitUser(receiverId, receiverId)
                               // ZegoUIKitUser("6jyH4YOE4NTM9bpVCiDBrljhCKw2", "6jyH4YOE4NTM9bpVCiDBrljhCKw2")  //saksham
                               // ZegoUIKitUser("wb6UuZMOuTW1QTjK4zUP9159KSJ2", "wb6UuZMOuTW1QTjK4zUP9159KSJ2")  //pixel
                            )
                        )
                    }
                })
            Spacer(modifier = Modifier.width(8.dp))
            AndroidView(
                modifier = Modifier.size(35.dp),
                factory = {
                    ZegoSendCallInvitationButton(it).apply {
                        setIsVideoCall(false)
                        resourceID = "zego_uikit_call"
                        setInvitees(
                            listOf(
                                ZegoUIKitUser(receiverId, receiverId)
                               // ZegoUIKitUser("6jyH4YOE4NTM9bpVCiDBrljhCKw2", "6jyH4YOE4NTM9bpVCiDBrljhCKw2")  //saksham
                               //  ZegoUIKitUser("wb6UuZMOuTW1QTjK4zUP9159KSJ2", "wb6UuZMOuTW1QTjK4zUP9159KSJ2")  //pixel
                            )
                        )
                    }
                })
        }

        //  Text(text = vm.chatMessages.value.toString())
        MessageBox(
            modifier = Modifier.weight(1f),
            chatMessages = chatMessage.value,
            currentUserId = myUser?.userId ?: ""
        )
        ReplyBox(
            reply = reply,
            onReplyChange = { reply = it },
            onSendReply = onSendReply,
        )
    }
}

@Composable
fun ReplyBox(
    reply: String,
    onReplyChange: (String) -> Unit,
    onSendReply: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        CommonDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(value = reply, onValueChange = onReplyChange, maxLines = 3)
            Button(onClick = onSendReply) {
                Text(text = "Send")

            }
        }
    }

}

@Composable
fun ChatHeader(
    name: String,
    imageUrl: String,
    onBackClick: () -> Unit
) {

    Row(
        modifier = Modifier
           // .fillMaxWidth()
           // .wrapContentHeight()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .clickable {
                    onBackClick.invoke()
                }
                .padding(8.dp)
        )
        CommonImage(
            data = imageUrl,
            modifier = Modifier
                .padding(8.dp)
                .size(50.dp)
                .clip(CircleShape)
        )
        Text(
            text = name,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp)
        )

    }
}

@Composable
fun MessageBox(
    modifier: Modifier,
    chatMessages: List<Message>,
    currentUserId: String
) {
    LazyColumn(modifier = modifier) {
        items(chatMessages) { msg ->
            val alignment = if (msg.sendBy == currentUserId) Alignment.End else Alignment.Start
            val color = if (msg.sendBy == currentUserId) Color(0xFFFF7E7D) else Color(0xFFC0C0C0)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = alignment
            ) {
                Text(
                    text = msg.message ?: "",
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(color)
                        .padding(12.dp)
                    ,
                    color = Color.Black
                )
            }
        }
    }

}