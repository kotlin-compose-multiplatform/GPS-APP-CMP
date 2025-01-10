package com.gs.wialonlocal.android

import App
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.common.IntentSenderForResultStarter
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.gs.wialonlocal.Greeting

class MainActivity : ComponentActivity() {
    private val TAG: String = "in-app-update"
    private val context: Context = this

    private lateinit var appUpdateManager: AppUpdateManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val c = LocalContext.current
            App(context = c)
        }

        appUpdateManager = AppUpdateManagerFactory.create(this)
        checkForAppUpdates(appUpdateManager)

    }



    private fun toast(message: () -> String) {
        Toast.makeText(context, message(), Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        setupAppUpdateListeners(appUpdateManager)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterAppUpdateListeners(appUpdateManager)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == APP_UPDATE_REQUEST_CODE && resultCode != RESULT_OK){
           toast { data?.dataString?:"Error while Check update" }
        }
    }



}