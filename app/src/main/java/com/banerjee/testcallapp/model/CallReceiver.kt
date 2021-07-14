package com.banerjee.testcallapp.model

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.view.Gravity
import android.widget.Toast

class CallReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent!!.getStringExtra(TelephonyManager.EXTRA_STATE) ==
                TelephonyManager.EXTRA_STATE_OFFHOOK){
            showToastMessage(context!!, "Phone call has started...")
        }
        else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE) ==
            TelephonyManager.EXTRA_STATE_IDLE){
            showToastMessage(context!!, "Phone call has ended...")
        }
        else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE) ==
            TelephonyManager.EXTRA_STATE_RINGING){
            showToastMessage(context!!, "Phone call incoming...")
        }
    }

    fun showToastMessage(context: Context, message: String){
        val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}