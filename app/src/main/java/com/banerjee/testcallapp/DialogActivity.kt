package com.banerjee.testcallapp

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog


class DialogActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to exit?").setCancelable(
            false
        ).setPositiveButton("Yes"
        ) { dialog, id ->
            dialog.cancel()
            finishAffinity()
        }
            .setNegativeButton("No"
            ) { dialog, id ->
                dialog.cancel()
                finish()
            }
        val alert: AlertDialog = builder.create()
        alert.show()

    }
}