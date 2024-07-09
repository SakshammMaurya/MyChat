//    @file:OptIn(ExperimentalMaterial3Api::class)

package com.example.chatapp.presentation.Screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatapp.DestinationScreens
import com.example.chatapp.Util.CommonDivider
import com.example.chatapp.Util.CommonImage
import com.example.chatapp.Util.CommonProgressBar
import com.example.chatapp.Util.navigateTo
import com.example.chatapp.domain.LCViewModel

@Composable
fun ProfileScreen(navController: NavController, vm: LCViewModel) {
    val inProgress = vm.inProcess.value
    if (inProgress) {
        CommonProgressBar()
    } else {
        val userData = vm.userData.value
        var name by rememberSaveable {
            mutableStateOf(userData?.name?:"") // can contain error
        }
        var number by rememberSaveable {
            mutableStateOf(userData?.number?:"") // can contain error
        }
        Column(
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            ProfileContent(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp),
                vm=vm,
                name=name,
                number=number,
                onNameChange = {
                    name = it
                },
                onNumberChange = {number = it},
                onSave = {
                         vm.createOrUpdateProfile(name = name,number = number)
                },
                onBack = {
                         navigateTo(navController = navController, route = DestinationScreens.ChatList.route)
                },
                onLogOut = {
                    vm.logout()
                      navigateTo(navController,DestinationScreens.Login.route)
                }

            )
            BottomNavigationMenu(
                selectedItem = BottomNavigationItem.PROFILE,
                navController = navController
            )
        }

    }

}

// @ExperimentalMaterial3Api
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    modifier: Modifier,
    vm: LCViewModel,
    name: String,
    number: String,
    onNameChange: ((String) -> Unit),
    onNumberChange: ((String) -> Unit),
    onBack: () -> Unit,
    onSave: () -> Unit,
    onLogOut: () -> Unit
) {
    val imageUrl = vm.userData.value?.imageUrl
    Column(
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Back",
                modifier = Modifier.clickable {
                    onBack.invoke()
                })
            Text(text = "Save",
                modifier = Modifier.clickable {
                    onSave.invoke()
                })
        }
            CommonDivider()
            ProfileImage(imageUrl = imageUrl, vm = vm)
            CommonDivider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = name,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                        // unfocusedLeadingIconColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp),
                    onValueChange = onNameChange,
                    label = {
                        Text(text = "Name")
                    }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                        // unfocusedLeadingIconColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp),
                    value = number,
                    onValueChange =
                        onNumberChange
                    ,
                    label = {
                        Text(text = "Number")
                    }
                )

            }
            CommonDivider()
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
                horizontalArrangement = Arrangement.Center){
                Text(text = "Logout",
                    modifier = Modifier.clickable {onLogOut()},
                    color = Color(0xFFFF7E7D),)
            }
        }
}

@Composable
fun ProfileImage(imageUrl: String?, vm: LCViewModel) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            vm.uploadProfileImage(uri)
        }
    }

    Box(modifier = Modifier.height(intrinsicSize = IntrinsicSize.Min)) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable {
                    launcher.launch("image/*")
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .size(100.dp)
                    .clip(CircleShape)
            ) {
                CommonImage(data = imageUrl)
            }
            Text(text = "Change Profile Picture")
        }

        if (vm.inProcess.value) {
            CommonProgressBar()
        }
    }
}