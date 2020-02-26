package com.jakting.opengapps.ui.misc

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.pm.PackageInfoCompat.getLongVersionCode
import androidx.fragment.app.Fragment
import com.jakting.opengapps.R
import de.psdev.licensesdialog.LicensesDialog
import kotlinx.android.synthetic.main.fragment_misc.*


class MiscFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_misc, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        button_donate.setOnClickListener(this)
        button_star.setOnClickListener(this)
        button_licenses.setOnClickListener(this)
        button_opensource.setOnClickListener(this)
        setVersionDev()
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.button_donate -> {
                val gp_link =
                    Uri.parse("market://details?id=com.jakting.opengapps.donation")
                activity?.startActivity(Intent(Intent.ACTION_VIEW, gp_link))
            }
            R.id.button_star -> {
                val gp_link =
                    Uri.parse("market://details?id=com.jakting.opengapps")
                activity?.startActivity(Intent(Intent.ACTION_VIEW, gp_link))
            }
            R.id.button_licenses ->
                LicensesDialog.Builder(activity)
                    .setNotices(R.raw.notices)
                    .setIncludeOwnLicense(true)
                    .build()
                    .show()
            R.id.button_opensource -> {
                val url = "https://github.com/hjthjthjt/RnOpenGApps/"
                val builder = CustomTabsIntent.Builder()
                builder.setShowTitle(true)
                builder.setToolbarColor(activity!!.resources.getColor(R.color.colorPrimary))
                val customTabsIntent = builder.build()
                customTabsIntent.launchUrl(activity!!, Uri.parse(url))
            }
        }
    }

    private fun setVersionDev() {
        val getV = arrayOf("", "")
        try {
            val packageInfo =
                activity!!.packageManager.getPackageInfo(activity!!.packageName, 0)
            getV[0] = packageInfo.versionName
            //getV[1] = packageInfo.versionCode.toString()
            getV[1] = getLongVersionCode(packageInfo).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        val version = getV[0] + "(" + getV[1] + ")"
        input_app_version.setText(version)
        input_app_version.keyListener = null
        input_app_dev.setText(getString(R.string.developer))
        input_app_dev.keyListener = null
    }
}