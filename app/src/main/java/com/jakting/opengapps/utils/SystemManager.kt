package com.jakting.opengapps.utils

import android.app.Activity
import android.util.Log
import java.io.DataOutputStream


class SystemManager : Activity() {
    companion object {
        fun RootCommand(command: String): Boolean {
            var process: Process? = null
            var os: DataOutputStream? = null
            try {
                process = Runtime.getRuntime().exec("su")
                os = DataOutputStream(process.outputStream)
                os.writeBytes(command + "\n")
                os.writeBytes("exit\n")
                os.flush()
                process.waitFor()
            } catch (e: Exception) {
                Log.d("*** DEBUG ***", "ROOT REE" + e.message)
                return false
            } finally {
                try {
                    if (os != null) {
                        os.close()
                    }
                    process!!.destroy()
                } catch (e: Exception) {
                }
            }
            Log.d("*** DEBUG ***", "Root SUC ")
            return true
        }
    }
}