package com.github.alishir.apn

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.DataOutputStream


class MainActivity : AppCompatActivity() {

    private lateinit var su: Process

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        su = Runtime.getRuntime().exec("su")
    }

    override fun onResume() {
        super.onResume()
        val apn = apn_txt.text
        val mcc = mcc_txt.text
        val mnc = mnc_txt.text
        val cmd = "content insert --uri content://telephony/carriers --bind name:s:\"${apn}\" --bind numeric:s:\"${mcc}${mnc}\" --bind type:s:\"ims\" --bind mcc:i:${mcc} --bind mnc:s:${mnc} --bind apn:s:${apn} --bind roaming_protocol:s:IP --bind protocol:s:IP"
        volte_btn.setOnClickListener {
            val dos = DataOutputStream(su.getOutputStream())
            dos.writeBytes("${cmd}\n")
            dos.writeBytes("exit\n")
            dos.flush()
            dos.close()
            su.waitFor()
            Toast.makeText(applicationContext, "${cmd}", Toast.LENGTH_LONG).show()
        }
    }
}
