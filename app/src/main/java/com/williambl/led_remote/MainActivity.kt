package com.williambl.led_remote

import android.Manifest.permission.TRANSMIT_IR
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    var toRunAfterPermissionGranted: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun power(view: View) {
        this.runWithPermissions {
            transmit(view.context, (0xFFu.toByte()), Integer.parseInt(this.findViewById<EditText>(R.id.valueToSend).text.toString(), 16).toUByte().toByte())
        }
    }

    private fun runWithPermissions(toRun: () -> Unit) {
        if (this.checkSelfPermission(TRANSMIT_IR) == PERMISSION_GRANTED) {
            toRun.invoke()
        } else {
            this.toRunAfterPermissionGranted = toRun
            this.requestPermissions(arrayOf(TRANSMIT_IR), 0)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 0) {
            if (grantResults[0] == PERMISSION_GRANTED) {
                this.toRunAfterPermissionGranted?.invoke()
                this.toRunAfterPermissionGranted = null
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}