package com.example.chatapp.presentation.Screens

import android.app.AlertDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatapp.DestinationScreens
import com.example.chatapp.R
import com.example.chatapp.Util.CommonProgressBar
import com.example.chatapp.Util.CommonRow
import com.example.chatapp.Util.CommonRowDeletable
import com.example.chatapp.Util.SwipeToDelete
import com.example.chatapp.Util.TitleText
import com.example.chatapp.Util.navigateTo
import com.example.chatapp.domain.LCViewModel

@Composable
fun ChatListScreen(navController: NavController, vm: LCViewModel) {
    val inProgress = vm.inProcessChats
    if (inProgress.value) {
        CommonProgressBar()
    } else {
        var chats =  mutableStateOf(vm.chats.value)
        val userData = vm.userData.value
        val showDialog = remember {
            mutableStateOf(false)
        }
        val onFabClick: () -> Unit = { showDialog.value = true }
        val onDismiss: () -> Unit = { showDialog.value = false }
        val onAddChat: (String) -> Unit = {
            vm.onAddChat(it)
            showDialog.value = false
        }
        Scaffold(
            floatingActionButton = {
                FAB(
                    showDialog = showDialog.value,
                    onFabClick = onFabClick,
                    onDismiss = onDismiss,
                    onAddChat = onAddChat
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                TitleText(txt = "Chats")
                if (chats.value.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.start),
                            contentDescription = null,
                        )
                        Text(
                            text = "Start Chatting",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Message privately with your Contacts",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Gray
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {
                        items(
                            items = chats.value,
                        ) { chat ->

                            val chatUser = if (chat.user1.userId == userData?.userId) {
                                chat.user2
                            } else {
                                chat.user1
                            }

                            CommonRowDeletable(
                                imageUrl = chatUser.imageUrl,
                                name = chatUser.name,
                                item = chats,
                                onDelete = {
                                           // chats -= chat
                                            vm.DeleteChat(chatId = chat.chatId.toString())
                                            chats.value -= chat

                                           },
                                onItemClick = {
                                    chat.chatId?.let {
                                        navigateTo(
                                            navController,
                                            DestinationScreens.SingleChat.createRoute(id = it)
                                        )
                                    }
                                }
                            )
//                            CommonRow(imageUrl = chatUser.imageUrl, name = chatUser.name) {
//
//                                chat.chatId?.let {
//                                    navigateTo(
//                                        navController,
//                                        DestinationScreens.SingleChat.createRoute(id = it)
//                                    )
//                                }
//
//                            }

                        }
                    }
                }
                BottomNavigationMenu(
                    selectedItem = BottomNavigationItem.CHATLIST,
                    navController = navController
                )
            }
        }

    }

}

@Composable
fun FAB(
    showDialog: Boolean,
    onFabClick: () -> Unit,
    onDismiss: () -> Unit,
    onAddChat: (String) -> Unit
) {
    val addChatNumber = remember {
        mutableStateOf("")
    }
    if (showDialog) {
        AlertDialog(onDismissRequest = {
            onDismiss.invoke()
            addChatNumber.value = ""
        },
            confirmButton = {
                Button(onClick = {
                    onAddChat(addChatNumber.value)
                }) {
                    Text(text = "Add Chat")
                }
            },
            title = { Text(text = "Add Chat") },
            text = {
                OutlinedTextField(

                    value = addChatNumber.value,
                    onValueChange = {
                        addChatNumber.value = it
                    },
                    label = {
                        Text(text = "Number")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        )
    }
    FloatingActionButton(
        onClick = onFabClick,
        containerColor = Color(0xFFFF7E7D),
        shape = CircleShape,
        modifier = Modifier.padding(bottom = 60.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add, contentDescription = null,
            tint = Color.Black
        )
    }


}