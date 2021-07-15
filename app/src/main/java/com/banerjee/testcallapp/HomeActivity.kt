package com.banerjee.testcallapp

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.CallLog
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.banerjee.testcallapp.model.CallDataModel
import com.banerjee.testcallapp.model.CallReceiverService
import com.banerjee.testcallapp.model.Restarter
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*
import kotlin.collections.ArrayList


class HomeActivity : AppCompatActivity() {

    private val MY_PERMISSIONS_REQUEST_READ_CALL_LOG = 102
    private val MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 103
    private lateinit var callRVAdapter: CallRVAdapter
    private val callList: ArrayList<CallDataModel> = ArrayList()
    var mServiceIntent: Intent? = null
    private var callReceiverService: CallReceiverService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        callRVAdapter = CallRVAdapter(this@HomeActivity, callList)
        call_rv.adapter = callRVAdapter

        checkPermissions()

        callReceiverService = CallReceiverService()
        mServiceIntent = Intent(this, callReceiverService!!.javaClass)
        if (!isMyServiceRunning(callReceiverService!!.javaClass)) {
            startService(mServiceIntent)
        }
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                Log.d("Service status", "Running")
                return true
            }
        }
        Log.d("Service status", "Not running")
        return false
    }

    override fun onDestroy() {
//        stopService(mServiceIntent);
//        val broadcastIntent = Intent()
//        broadcastIntent.action = "android.intent.action.PHONE_STATE"
//        broadcastIntent.setClass(this, Restarter::class.java)
//        this.sendBroadcast(broadcastIntent)
        super.onDestroy()
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                this@HomeActivity,
                Manifest.permission.READ_CALL_LOG
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this@HomeActivity,
                arrayOf(Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_PHONE_STATE),
                MY_PERMISSIONS_REQUEST_READ_CALL_LOG
            );
        } else {
            getCallDetails()
        }

//        if (ContextCompat.checkSelfPermission(
//                this@HomeActivity,
//                Manifest.permission.READ_PHONE_STATE
//            ) !=
//            PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(this@HomeActivity,
//                arrayOf(Manifest.permission.READ_PHONE_STATE),
//                MY_PERMISSIONS_REQUEST_READ_PHONE_STATE
//            );
//        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        var canReadCallLog = false
        var canReadPhoneState = false

        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_CALL_LOG -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    canReadCallLog = true
                }
                if (!canReadCallLog) {
                    Toast.makeText(
                        this@HomeActivity,
                        "Cannot use feature without requested permission",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // user now provided permission
                    // perform function for what you want to achieve
                    getCallDetails()
                }
            }
        }
    }

    private fun getCallDetails() {
        val stringBuffer = StringBuffer()
        callList.clear()
        val cursor: Cursor = contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            null, null, null, CallLog.Calls.DATE + " DESC"
        )!!
        val number = cursor.getColumnIndex(CallLog.Calls.NUMBER)
        val type = cursor.getColumnIndex(CallLog.Calls.TYPE)
        val date = cursor.getColumnIndex(CallLog.Calls.DATE)
        val duration = cursor.getColumnIndex(CallLog.Calls.DURATION)
        while (cursor.moveToNext()) {
            val phNumber = cursor.getString(number)
            val callType = cursor.getString(type)
            val callDate = cursor.getString(date)
            val callDayTime = Date(java.lang.Long.valueOf(callDate))
            val callDuration = cursor.getString(duration)
            var dir: String? = null
            val dircode = callType.toInt()
            when (dircode) {
                CallLog.Calls.OUTGOING_TYPE -> dir = "OUTGOING"
                CallLog.Calls.INCOMING_TYPE -> dir = "INCOMING"
                CallLog.Calls.MISSED_TYPE -> dir = "MISSED"
            }

            if (dir == null || dir.length == 0){
                dir = "REJECTED"
            }

            val  callDataModel = CallDataModel(
                phone = phNumber,
                type = dir,
                date = callDayTime,
                duration = callDuration
            )

            callList.add(callDataModel)
        }
        cursor.close()
        callRVAdapter.notifyDataSetChanged()
    }
}