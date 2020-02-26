package com.jakting.opengapps.ui.download

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.jakting.opengapps.R
import com.jakting.opengapps.utils.*
import com.jaredrummler.materialspinner.MaterialSpinner
import com.tonyodev.fetch2.*
import com.tonyodev.fetch2core.DownloadBlock
import com.tonyodev.fetch2core.Func
import kotlinx.android.synthetic.main.fragment_download.*
import org.jetbrains.annotations.NotNull


class DownloadFragment : Fragment(), View.OnClickListener {

    var json: String = ""
    var pls: String = ""
    lateinit var cpuObject: JsonObject
    private var download_status = 0
    private var cpushortS = ""
    private var versionS = ""
    private var varS = ""
    private var date = ""
    var downloadurl: String = ""
    lateinit var roott: View
    lateinit var apisObject: JsonObject
    lateinit var apisList: ArrayList<String?>
    var sp: SharedPreferences? = null
    lateinit var getAPI: GetAPI
    lateinit var getMD5: GetAPI

    lateinit var fetch: Fetch
    lateinit var request: Request
    //var getLatestOpenGApps: GetLatestOpenGApps? = null
    var getDeviceInfo: GetDeviceInfo = GetDeviceInfo()
    private var uri: Uri? = null
    lateinit var urii: Uri
    val WRITE_REQUEST_CODE: Int = 43

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_download, container, false)
        sp = activity!!.getSharedPreferences("data", Context.MODE_PRIVATE)
        roott = root
        cpushortS = getDeviceInfo.getCPUshort()
        versionS = getDeviceInfo.getOS()
        varS = getString(R.string.text_download_pls)
        Thread(Runnable {
            getAPI = GetAPI(myHandler)
            getAPI.run(OPENGAPPS_API)
        }).start()
        pls = getString(R.string.text_download_pls)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val fetchConfiguration =
            FetchConfiguration.Builder(activity as Context)
                .setDownloadConcurrentLimit(10)
                .build()

        fetch = Fetch.getInstance(fetchConfiguration)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (versionS.contains("R") || versionS.contains("11")) {
            //OpenGApps 尚未支持的版本，不能自动获取：Android R/11
            text_download_unsupported.visibility = View.VISIBLE
            text_download_unsupported.text =
                String.format(getString(R.string.text_download_alert_unsupport), versionS)
        }

        button_fab.setOnClickListener(this)
        button_gapps_blog.setOnClickListener(this)
        button_md5.setOnClickListener(this)
        spinner_download_variant.setOnItemSelectedListener(MaterialSpinner.OnItemSelectedListener<String> { view, position, id, item ->
            varS = item
        })

        spinner_download_cpu.setItems(
            getString(R.string.text_download_pls),
            "arm",
            "arm64",
            "x86",
            "x86_64"
        )
        spinner_download_cpu.setOnItemSelectedListener(MaterialSpinner.OnItemSelectedListener<String> { view, position, id, item ->
            if (item != pls) {
                cpushortS = item
                logd("cpushortS=$cpushortS")
                parseData()
            }

        })
        spinner_download_version.setOnItemSelectedListener(MaterialSpinner.OnItemSelectedListener<String> { view, position, id, item ->
            if (item != pls) {
                versionS = item
                spinner_download_variant.setItems(getVariantsList(activity!!, apisObject, versionS))
                text_download_for.text =
                    String.format(getString(R.string.text_download_alert), cpushortS, versionS)
            }
        })
    }

    val myHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                RETURN_JSON -> {
                    parseData()
                }
                RETURN_MD5 -> {
                    echoMD5()
                }
            }
            super.handleMessage(msg)
        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.button_fab -> {
                Log.d("debug", "onClick: clickDownload")
                when (download_status) {
                    0 -> {
                        logd("未下载，点击开始弹出提示框")
                        //还没开始下载
                        clickDown()
                    }
                    1 -> {
                        logd("已经开始下载，要求暂停")
                        val play_icon = ContextCompat.getDrawable(
                            context!!,
                            R.drawable.ic_play_arrow_black_24dp
                        )
                        button_fab.icon = play_icon
                        button_fab.text = getString(R.string.text_button_continue)
                        fetch.pause(request.id)
                        download_status = 2 //处于下载暂停状态
                    }
                    2 -> {
                        logd("已经暂停，要求继续下载")
                        fetch.resume(request.id)
                        button_fab.text = getString(R.string.text_download_waiting)
                        val pause_icon =
                            ContextCompat.getDrawable(context!!, R.drawable.ic_pause_black_24dp)
                        button_fab.icon = pause_icon
                        download_status = 1 //处于下载状态
                    }
                    else -> {
                        logd("有问题")
                    }
                }
            }
            R.id.button_gapps_blog -> {
                val url = "https://jakting.com/archives/gapps-links-what.html"
                val builder = CustomTabsIntent.Builder()
                builder.setShowTitle(true)
                builder.setToolbarColor(activity!!.resources.getColor(R.color.colorPrimary))
                val customTabsIntent = builder.build()
                customTabsIntent.launchUrl(activity!!, Uri.parse(url))
            }

            R.id.button_md5 -> {
                if (varS == getString(R.string.text_download_pls)) {
                    Snackbar.make(roott, R.string.text_download_error, Snackbar.LENGTH_SHORT)
                        .show()
                } else {
                    text_md5.text = "MD5: " + getString(R.string.text_download_waiting)
                    Thread(Runnable {
                        val time = (System.currentTimeMillis() / 1000).toInt()
                        val download_md5_url =
                            "https://downloads.sourceforge.net/project/opengapps/$cpushortS/$date/open_gapps-$cpushortS-$versionS-$varS-$date.zip.md5?r=&ts=$time&use_mirror=autoselect"
                        getMD5 = GetAPI(myHandler)
                        getMD5.run(download_md5_url)
                    }).start()
                }

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == WRITE_REQUEST_CODE && data != null && data.data != null) {
            urii = data.data as Uri
            handleUri(urii)
        } else {
            Snackbar.make(roott, "data of activity result is not valid", Snackbar.LENGTH_LONG)
                .show()
        }
    }

    //https://downloads.sourceforge.net/project/opengapps/arm64/20190926/open_gapps-arm64-9.0-tvstock-20190926.zip?r=&ts=1569487433&use_mirror=autoselect
    private fun setDate() {
        logd("获取日期结果：$date")
        text_opengapps_version_get.text = date
        progress_circular.visibility = View.INVISIBLE
        img_update.visibility = View.VISIBLE
        spinner_download_variant.setItems(getVariantsList(activity!!, apisObject, versionS))
        if (versionS.contains("R") || versionS.contains("11")) {
            //OpenGApps 尚未支持的版本，不能自动获取：Android R/11
            versionS = "10.0"
            text_download_for.text = getString(R.string.text_download_alert_unsupport_nowait)
        } else {
            text_download_for.text =
                String.format(getString(R.string.text_download_alert), cpushortS, versionS)
        }
    }

    private fun parseData() {
        cpuObject = getCPUObject(getAPI.json, cpushortS)
        apisObject = getApisObject(cpuObject)
        apisList = getApisList(activity!!, apisObject)
        date = cpuObject.get("date").asString
        spinner_download_version.setItems(apisList)
        setDate()
    }

    private fun echoMD5() {
        val md5 = getMD5.json.substring(0, 32)
        text_md5.text = "MD5: $md5"
    }

    private fun clickDown() {
        /*val time = (System.currentTimeMillis() / 1000).toInt()
        downloadurl =
            "https://downloads.sourceforge.net/project/opengapps/$cpushortS/$date/open_gapps-$cpushortS-$versionS-$varS-$date.zip?r=&ts=$time&use_mirror=autoselect"*/
        Log.d("debug", "cpushortS: $cpushortS")
        Log.d("debug", "date: $date")
        Log.d("debug", "versionS: $versionS")
        Log.d("debug", "varS: $varS")
        Log.d("debug", "downloadurl: $downloadurl")
        if (varS == getString(R.string.text_download_pls)) {
            Snackbar.make(roott, R.string.text_download_error, Snackbar.LENGTH_SHORT).show()
        } else {
            MaterialAlertDialogBuilder(activity)
                .setTitle(R.string.text_dialog_title_choose)
                .setMessage(
                    String.format(
                        getString(R.string.text_dialog_msg_choose),
                        cpushortS,
                        versionS,
                        varS,
                        date
                    )
                )
                .setPositiveButton(
                    R.string.text_dialog_option_1,
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        if (uri == null) {
                            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
                            intent.addCategory(Intent.CATEGORY_OPENABLE)

                            intent.type = "application/zip"
                            intent.putExtra(
                                Intent.EXTRA_TITLE,
                                "open_gapps-$cpushortS-$versionS-$varS-$date.zip"
                            )
                            startActivityForResult(intent, WRITE_REQUEST_CODE)
                        } else {
                            urii = uri as Uri
                            handleUri(urii)
                        }
                    })
                .setNegativeButton(
                    R.string.text_dialog_option_2,
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        val time = (System.currentTimeMillis() / 1000).toInt()
                        downloadurl =
                            "https://downloads.sourceforge.net/project/opengapps/$cpushortS/$date/open_gapps-$cpushortS-$versionS-$varS-$date.zip?r=&ts=$time&use_mirror=autoselect"
                        val uri = Uri.parse(downloadurl)
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        activity?.startActivity(intent)
                    })
                .show()
        }
    }

    private fun handleUri(uri: Uri) {
        val time = (System.currentTimeMillis() / 1000).toInt()
        downloadurl =
            "https://downloads.sourceforge.net/project/opengapps/$cpushortS/$date/open_gapps-$cpushortS-$versionS-$varS-$date.zip?r=&ts=$time&use_mirror=autoselect"
        request = Request(downloadurl, uri)
        request.priority = Priority.HIGH
        request.networkType = NetworkType.ALL

        fetch.enqueue(request,
            Func { updatedRequest: Request? -> logd("查询成功，预备下载") },
            Func { error: Error? -> logd("查询失败") }
        )
        fetch.addListener(fetchListener)
        logd("执行下载，下载地址$downloadurl")
        button_fab.text = getString(R.string.text_download_waiting)
        val pause_icon = ContextCompat.getDrawable(context!!, R.drawable.ic_pause_black_24dp)
        button_fab.icon = pause_icon
        download_status = 1
    }

    var fetchListener: FetchListener = object : FetchListener {

        override fun onAdded(download: Download) {}

        override fun onQueued(@NotNull download: Download, waitingOnNetwork: Boolean) {}

        override fun onCompleted(@NotNull download: Download) {
            logd("完成了")
            val check_icon = ContextCompat.getDrawable(context!!, R.drawable.ic_check_black_24dp)
            button_fab.icon = check_icon
            button_fab.text = getString(R.string.text_button_finish)
        }


        override fun onError(download: Download, error: Error, throwable: Throwable?) {
            logd("错误日志：${download.error}")
        }

        override fun onProgress(
            @NotNull download: Download, etaInMilliSeconds: Long,
            downloadedBytesPerSecond: Long
        ) {
            //val progress = download.progress
            //val speed = humanReadableBytes(downloadedBytesPerSecond,false)
            val now = humanReadableBytes(download.downloaded, false)
            val total = humanReadableBytes(download.total, false)
            button_fab.text = "$now / $total"
            //logd(progress.toString())
        }

        override fun onPaused(@NotNull download: Download) {}
        override fun onResumed(@NotNull download: Download) {
            val pause_icon = ContextCompat.getDrawable(context!!, R.drawable.ic_pause_black_24dp)
            button_fab.icon = pause_icon
        }

        override fun onWaitingNetwork(download: Download) {}
        override fun onCancelled(@NotNull download: Download) {}
        override fun onRemoved(@NotNull download: Download) {}
        override fun onDeleted(@NotNull download: Download) {}
        override fun onDownloadBlockUpdated(
            download: Download,
            downloadBlock: DownloadBlock,
            totalBlocks: Int
        ) {
        }

        override fun onStarted(
            download: Download,
            downloadBlocks: List<DownloadBlock>,
            totalBlocks: Int
        ) {
            logd("开始嗷")
            logd("下载到${download.file}")
        }


    }


}