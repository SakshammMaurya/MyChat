package com.example.chatapp.presentation.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatapp.DestinationScreens
import com.example.chatapp.R
import com.example.chatapp.Util.CommonProgressBar
import com.example.chatapp.Util.checkSignIn
import com.example.chatapp.Util.navigateTo
import com.example.chatapp.domain.LCViewModel


@Composable
fun LoginScreen(navController: NavController, vm: LCViewModel) {

    checkSignIn(vm,navController)
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
//            val nameState = remember { mutableStateOf(TextFieldValue()) }
//            val numberState = remember { mutableStateOf(TextFieldValue()) }
            val emailState = remember { mutableStateOf(TextFieldValue()) }
            val passwordState = remember { mutableStateOf(TextFieldValue()) }

            val focus = LocalFocusManager.current

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .padding(8.dp)
            )
            Text(
                text = "Login",
                fontSize = 30.sp,
                modifier = Modifier.padding(8.dp),
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Medium,
            )
//            OutlinedTextField(
//                value = nameState.value,
//                onValueChange = {
//                    nameState.value = it
//                },
//                label = { Text(text = "Name") },
//                modifier = Modifier.padding(8.dp)
//
//            )
            OutlinedTextField(
                value = emailState.value,
                onValueChange = {
                    emailState.value = it
                },
                label = { Text(text = "Email") },
                modifier = Modifier.padding(8.dp)

            )
//            OutlinedTextField(
//                value = numberState.value,
//                onValueChange = {
//                    numberState.value = it
//                },
//                label = { Text(text = "Phone Number") },
//                modifier = Modifier.padding(8.dp)
//
//            )
            OutlinedTextField(
                value = passwordState.value,
                onValueChange = {
                    passwordState.value = it
                },
                label = { Text(text = "Password") },
                modifier = Modifier.padding(8.dp)

            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    vm.login(emailState.value.text,passwordState.value.text)
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "SIGN IN !")
            }
            Text(
                text = "New user, SignUp !",
                color = Color(0xFFFF7E7D),
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        navigateTo(navController, DestinationScreens.Signup.route)
                    }
            )

        }
        if(vm.inProcess.value){
            CommonProgressBar()
        }
    }
}