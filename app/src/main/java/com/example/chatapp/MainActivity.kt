package com.example.chatapp

import android.Manifest
import android.Manifest.permission
import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatapp.data.ReceiverData
import com.example.chatapp.data.UserData
import com.example.chatapp.domain.LCViewModel
import com.example.chatapp.presentation.Screens.ChatListScreen
import com.example.chatapp.presentation.Screens.LoginScreen
import com.example.chatapp.presentation.Screens.ProfileScreen
import com.example.chatapp.presentation.Screens.SignupScreen
import com.example.chatapp.presentation.Screens.SingleChatScreen
import com.example.chatapp.presentation.Screens.SingleStatusScreen
import com.example.chatapp.presentation.Screens.StatusScreen
import com.example.chatapp.ui.theme.ChatAppTheme
import com.google.firebase.firestore.FirebaseFirestore
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.callback.ExplainReasonCallback
import com.permissionx.guolindev.callback.RequestCallback
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallConfig
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallFragment
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


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
class MainActivity : FragmentActivity() {


    @SuppressLint("LogNotTimber")
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
        //----------------------------------------------------
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            permissions {
//                initialize()
//            }
//        } else {
//            initialize()
//        }

     //   addCallFragment()

    }
    var userData = mutableStateOf<UserData?>(null)
    val userId = userData.value?.userId.toString()
    //--------------------------------------------------------
//    private fun addCallFragment() {
//        val appID: Long = 2030862703L
//        val appSign: String = "3cb4272ea31a531f410749acc71fca86567032d345bcdd178e3dde9d82003a43"
////        val callID: String = userId
////        val userID: String = userId
////        val userName: String = userData.value?.name.toString()
//        val callID: String = "6jyH4YOE4NTM9bpVCiDBrljhCKw2"
//        val userID: String = "6jyH4YOE4NTM9bpVCiDBrljhCKw2"
//        val userName: String = "6jyH4YOE4NTM9bpVCiDBrljhCKw2"
//
//
//        // You can also use GroupVideo/GroupVoice/OneOnOneVoice to make more types of calls.
//        val config = ZegoUIKitPrebuiltCallConfig.oneOnOneVideoCall()
//        val fragment = ZegoUIKitPrebuiltCallFragment.newInstance(
//            appID, appSign, userID, userName, callID, config
//        )
////        supportFragmentManager.beginTransaction()
////            .replace(R.id.fragment_container, fragment)
////            .commitNow()
//    }
     //------------------------------------------------------------
   private fun permissions(
        onGranted:()->Unit
    ){ //ZegoCloud permissions
        PermissionX.init(this).permissions(Manifest.permission.SYSTEM_ALERT_WINDOW)
            .onExplainRequestReason(ExplainReasonCallback { scope, deniedList ->
                val message =
                    "We need your consent for the following permissions in order to use the offline call function properly"
                scope.showRequestReasonDialog(deniedList, message, "Allow", "Deny")
            }).request(RequestCallback { allGranted, grantedList, deniedList ->

                if(allGranted){
                    onGranted()
                }

            })
    }

    fun initialize(
    ){
        val appID = 2030862703L ;   // yourAppID
        val appSign = "3cb4272ea31a531f410749acc71fca86567032d345bcdd178e3dde9d82003a43";  // yourAppSign
        val userID = userId // yourUserID, userID should only contain numbers, English characters, and '_'.
        val userName = userId  // yourUserName
        val callInvitationConfig =  ZegoUIKitPrebuiltCallInvitationConfig();
        ZegoUIKitPrebuiltCallService.init(Application(), appID, appSign, userID, userName,callInvitationConfig);
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

