package com.example.chatapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatapp.domain.LCViewModel
import com.example.chatapp.presentation.Screens.ChatListScreen
import com.example.chatapp.presentation.Screens.LoginScreen
import com.example.chatapp.presentation.Screens.ProfileScreen
import com.example.chatapp.presentation.Screens.SignupScreen
import com.example.chatapp.presentation.Screens.SingleChatScreen
import com.example.chatapp.presentation.Screens.SingleStatusScreen
import com.example.chatapp.presentation.Screens.StatusScreen
import com.example.chatapp.ui.theme.ChatAppTheme
import dagger.hilt.android.AndroidEntryPoint

//creating routes for navigation
sealed class DestinationScreens(var route: String){
    object Signup : DestinationScreens("signup")
    object Login : DestinationScreens("login")
    object Profile : DestinationScreens("profile")
    object ChatList : DestinationScreens("chatList")

    object SingleChat : DestinationScreens("singleChat/{chatId}"){
        // for navigating to chat of a particular person
        fun createRoute(id: String) ="singleChat/$id"
    }

    object StatusList : DestinationScreens("statusList")
    object SingleStatus : DestinationScreens("singleStatus/{userId}"){
        fun createRoute(userId: String) ="singleStatus/$userId"
    }
}
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatAppNavigation()
                }
            }
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun ChatAppNavigation() {
    val navController = rememberNavController()
    var vm = hiltViewModel<LCViewModel>()
    NavHost(navController = navController, startDestination = DestinationScreens.Signup.route) {
        composable(DestinationScreens.Signup.route) {
            SignupScreen(navController, vm)

        }
        composable(DestinationScreens.Login.route) {
            LoginScreen(navController, vm)
        }
        composable(DestinationScreens.ChatList.route) {
            ChatListScreen(navController, vm)
        }
        composable(DestinationScreens.SingleChat.route) {
            val chatId = it.arguments?.getString("chatId")
            chatId?.let {
                SingleChatScreen(navController, vm, chatId)
            }
        }
        composable(DestinationScreens.StatusList.route) {
            StatusScreen(navController, vm)
        }
        composable(DestinationScreens.Profile.route) {
            ProfileScreen(navController, vm)
        }
        composable(DestinationScreens.SingleStatus.route) {
            val userId = it.arguments?.getString("userId")
            userId?.let {
                SingleStatusScreen(navController, vm, userId)
            }

        }
    }
}

