package com.jakting.opengapps.ui.device

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jakting.opengapps.R
import com.jakting.opengapps.utils.GetDeviceInfo
import com.jakting.opengapps.utils.IsInstall
import com.jakting.opengapps.utils.SystemManager
import com.jakting.opengapps.utils.longtoast
import com.scottyab.rootbeer.RootBeer
import kotlinx.android.synthetic.main.fragment_device.*
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.regex.Matcher
import java.util.regex.Pattern


class DeviceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_device, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getMyDevice()
        getGAppsInstalled()
    }

    private fun getMyDevice() {
        input_vendor.setText(GetDeviceInfo().getVendor())
        input_vendor.keyListener = null
        input_model.setText(GetDeviceInfo().getModel())
        input_model.keyListener = null
        input_cpu.setText(GetDeviceInfo().getCPU())
        input_cpu.keyListener = null
        var os: String = GetDeviceInfo().getOS()
        if (os.contains("R") || os.contains("11")) {
            //OpenGApps 尚未支持的版本，不能自动获取：Android R/11
            os += getString(R.string.text_device_os_unsupported)
        }
        input_os.setText(os)
        input_os.keyListener = null
        input_opengapps_gapps_version.keyListener = null

        val sp = activity!!.getSharedPreferences("data", Context.MODE_PRIVATE).edit()
        sp.putString("vendor", GetDeviceInfo().getVendor())
        sp.putString("model", GetDeviceInfo().getModel())
        sp.putString("cpu", GetDeviceInfo().getCPU())
        sp.putString("cpushort", GetDeviceInfo().getCPUshort())
        sp.putString("os", GetDeviceInfo().getOS())
        sp.apply()

        button_device_check.setOnClickListener {
            getOpenGAppsVersion()
        }
    }

    private fun getOpenGAppsVersion() {
        val apkRoot = "chmod 777 " + activity!!.packageCodePath
        SystemManager.RootCommand(apkRoot)
        val rootBeer = RootBeer(activity)
        if (rootBeer.isRooted) { //we found indication of root
            val g_prop = "/etc/g.prop"
            val file = File(g_prop)
            var getContext = ""
            if (file.exists()) { //如果文件存在
                //把文件内容读取进缓冲读取器
                val bufferedReader = BufferedReader(FileReader(file))
                var line: String

                while (true) {
                    //当有内容时读取一行数据，否则退出循环
                    line = bufferedReader.readLine() ?: break
                    getContext += line //打印一行数据
                }
                bufferedReader.close() //关闭缓冲读取器

                val regex = "ro\\.addon\\.open_version=(.*?)#"
                val pattern: Pattern = Pattern.compile(regex, Pattern.MULTILINE)
                val matcher: Matcher = pattern.matcher(getContext)
                while (matcher.find()) {
                    getContext = matcher.group(1)
                }
                Log.d("debug", getContext)
            } else { //不存在
                getContext = "null"
            }
            if (getContext != "null") { //已安装 OpenGApps
                input_opengapps_gapps_version.setText(getContext)
            } else { //未安装 OpenGApps
                input_opengapps_gapps_version.setText(R.string.text_device_unknown)
            }
        } else {
            activity.longtoast(getText(R.string.text_device_check_fail))

        }
    }

    private fun getGAppsInstalled() {
        val stateGApps = IsInstall.IsInstall(activity!!, "com.google.android.gms")
        if (!stateGApps) {
            card_gapps_status.strokeColor = resources.getColor(R.color.warning)
            text_device_gapps_status.setText(R.string.text_device_gms_no)
            text_device_gapps_status.setTextColor(resources.getColor(R.color.warning))
            layout_4.visibility = View.GONE
        }
    }
}