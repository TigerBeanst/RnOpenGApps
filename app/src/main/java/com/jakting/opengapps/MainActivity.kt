package com.jakting.opengapps

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jakting.opengapps.ui.device.DeviceFragment
import com.jakting.opengapps.ui.download.DownloadFragment
import com.jakting.opengapps.ui.misc.MiscFragment
import com.jakting.opengapps.utils.logd
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

    private val deviceFragment: DeviceFragment = DeviceFragment()
    private val downloadFragment: DownloadFragment = DownloadFragment()
    private val miscFragment: MiscFragment = MiscFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!getDarkModeStatus(this)){
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setContentView(R.layout.activity_main)
        viewPaper.addOnPageChangeListener(this)
        nav_view.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        viewPaper.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                when (position) {
                    0 -> {
                        logd("deviceFragment")
                        return deviceFragment
                    }
                    1 -> {
                        logd("downloadFragment")
                        return downloadFragment
                    }
                    2 -> {
                        logd("miscFragment")
                        return miscFragment
                    }
                    else -> {
                        return deviceFragment
                    }
                }

            }

            override fun getCount(): Int {
                return 3
            }
        }

    }

    override fun onBackPressed() {
        finish()
    }

    private val mOnNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener =
        object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                //点击BottomNavigationView的Item项，切换ViewPager页面
//menu/navigation.xml里加的android:orderInCategory属性就是下面item.getOrder()取的值
                viewPaper.currentItem = item.order
                return true
            }
        }

    override fun onPageScrolled(
        position: Int,
        positionOffset: Float,
        positionOffsetPixels: Int
    ) {
    }

    override fun onPageSelected(position: Int) { //页面滑动的时候，改变BottomNavigationView的Item高亮
        nav_view.getMenu().getItem(position).isChecked = true
    }

    override fun onPageScrollStateChanged(state: Int) {}

    fun getDarkModeStatus(context: Context): Boolean {
        val mode: Int =
            context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return mode == Configuration.UI_MODE_NIGHT_YES
    }
}
