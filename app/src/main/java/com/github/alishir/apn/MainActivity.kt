package com.github.alishir.apn

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.DataOutputStream


class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "SINA-APN"
    }

    private lateinit var su: Process

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        volte_btn.setOnClickListener {
            val apn = apn_txt.text
            val mcc = mcc_txt.text
            val mnc = mnc_txt.text
            val type = type_txt.text
            val editable = if (editable_chk.isChecked) "1" else "0"

            var authtype = -1
            if ( auth_type_spinner.selectedItemId == 1L )
                authtype = 0;
            if ( auth_type_spinner.selectedItemId == 2L )
                authtype = 1;
            else if ( auth_type_spinner.selectedItemId == 3L )
                authtype = 2;
            else if ( auth_type_spinner.selectedItemId == 4L )
                authtype = 3;

            val cmd = "content insert --uri content://telephony/carriers --bind user_editable:i:${editable} --bind edited:i:${editable} --bind authtype:i:${authtype} --bind name:s:\"${apn}\" --bind numeric:s:\"${mcc}${mnc}\" --bind type:s:\"${type}\" --bind mcc:i:${mcc} --bind mnc:s:${mnc} --bind apn:s:${apn} --bind roaming_protocol:s:IP --bind protocol:s:IP"

            try {
                val su = Runtime.getRuntime().exec("su")
                val dos = DataOutputStream(su.getOutputStream())
                dos.writeBytes("${cmd}\n")
                dos.flush()
                dos.close()
                su.waitFor()
                Toast.makeText(applicationContext, "${cmd}", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(applicationContext, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                Log.e(TAG, "${e.message}")
            }
        }

        delete_btn.setOnClickListener {
            try {
                val su = Runtime.getRuntime().exec("su")
                val dos = DataOutputStream(su.getOutputStream())
                val mcc = mcc_txt.text
                val mnc = mnc_txt.text
                val delete_cmd = "content delete --uri content://telephony/carriers --where \"mcc='${mcc}' and mnc='${mnc}'\""
                dos.writeBytes("${delete_cmd}\n")
                dos.flush()
                dos.close()
                su.waitFor()
                Toast.makeText(applicationContext, "${delete_cmd}", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(applicationContext, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                Log.e(TAG, "${e.message}")
            }
        }

    }
}
