package com.banerjee.testcallapp.model

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import com.banerjee.testcallapp.DialogActivity


class Restarter: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context!!.startForegroundService(Intent(context, CallReceiverService::class.java))
        } else {
            context!!.startService(Intent(context, CallReceiverService::class.java))
        }

        Log.d("Broadcast Listened", "Service tried to stop")

        val teleMgr = context!!.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val psl: PhoneStateListener = object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, incomingNumber: String) {
                Log.i("CallReceiverBroadcast", "onCallStateChanged() is called. ")
                when (state) {
                    TelephonyManager.CALL_STATE_RINGING -> {
                        Log.i(
                            "CallReceiverBroadcast",
                            "Incoming call caught. Caller's number is $incomingNumber."
                        )
                        showToastMessage(context, "Incoming call caught. Caller's number is $incomingNumber.")
                        //start activity which has dialog
                        val i = Intent(context, DialogActivity::class.java)
                        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        context!!.startActivity(i)
                    }
                }
            }
        }
        teleMgr.listen(psl, PhoneStateListener.LISTEN_CALL_STATE)
        teleMgr.listen(psl, PhoneStateListener.LISTEN_NONE)

//        if (intent!!.getStringExtra(TelephonyManager.EXTRA_STATE) ==
//            TelephonyManager.EXTRA_STATE_OFFHOOK){
//            Log.d("TAG", "onReceive: Phone call has started...")
//        }
//        else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE) ==
//            TelephonyManager.EXTRA_STATE_IDLE){
//            showToastMessage(context!!, "Phone call has ended...")
//            val i1 = Intent(context!!.applicationContext, HomeActivity::class.java)
//            i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            context.startActivity(i1)
//        }
//        else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE) ==
//            TelephonyManager.EXTRA_STATE_RINGING){
//            Log.d("TAG", "onReceive: Incoming call...")
//        }

    }

    fun showToastMessage(context: Context, message: String){
        val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}