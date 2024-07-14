package com.example.chatapp.presentation.Screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chatapp.DestinationScreens
import com.example.chatapp.Util.CommonDivider
import com.example.chatapp.Util.CommonProgressBar
import com.example.chatapp.Util.CommonRow
import com.example.chatapp.Util.TitleText
import com.example.chatapp.Util.navigateTo
import com.example.chatapp.domain.LCViewModel

@SuppressLint("SuspiciousIndentation", "LogNotTimber")
@Composable
fun StatusScreen(navController: NavController, vm: LCViewModel) {
    val inProcess = vm.inProcess
    if (inProcess.value) {
        CommonProgressBar()
    } else {
        val statuses = vm.status.value
        val userData = vm.userData.value

        val myStatus = statuses.filter {
            it.user.userId == userData?.userId
        }
        val otherStatus = statuses.filter {
            it.user.userId != userData?.userId
        }
        Log.d("Statuses", statuses.size.toString())
        Log.d("myStatuses", myStatus.size.toString())
        Log.d("otherStatuses", otherStatus.size.toString())

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            uri?.let {
                vm.uploadStatus(uri)
            }
        }
        Scaffold(
            floatingActionButton = {
                FAB {
                    launcher.launch("image/*")
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                TitleText(txt = "Status")
                if (statuses.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "No Status Available")
                    }
                } else {

                    if (myStatus.isNotEmpty()) {
                        CommonRow(
                            imageUrl = myStatus[0].user.imageUrl,
                            name = myStatus[0].user.name,
                        ) {
                            navigateTo(
                                navController,
                                DestinationScreens.SingleStatus.createRoute(myStatus[0].user.userId!!)
                            )
                        }
                    }
                    // CommonDivider()
                    val uniqueUsers = otherStatus.map { it.user }.toSet().toList()
                    Log.d("uniqueUser", uniqueUsers.size.toString())
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(uniqueUsers) { user ->
                            CommonRow(
                                imageUrl = user.imageUrl, name = user.name
                            ) {
                                navigateTo(
                                    navController,
                                    DestinationScreens.SingleStatus.createRoute(user.userId!!)
                                )
                            }
                        }
                    }

                }
                BottomNavigationMenu(
                    selectedItem = BottomNavigationItem.STATUSLIST,
                    navController = navController
                )
            }
        }

    }
}


@Composable
fun FAB(
    onFabClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onFabClick,
        containerColor = MaterialTheme.colorScheme.secondary,
        shape = CircleShape,
        modifier = Modifier.padding(bottom = 40.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Status",
            tint = Color.Black
        )
    }
}
