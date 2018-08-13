package com.jakting.opengapps;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jakting.opengapps.utils.GetLatest;

public class GuideActivity extends SetupWizardBaseActivity implements View.OnClickListener {

    public final static int PAGE_WELCOME = 0;
    public final static int PAGE_ANDROID_VERSION = 1;
    public final static int PAGE_CPU = 2;
    public final static int PAGE_PACKAGE = 3;
    public final static int PAGE_FINAL = 4;
    public String EXTRA_SETUP_STEP = "com.jakting.opengapps.extra.SETUP_STEP";
    public final static int MODE_NONE = -1;
    public final static int MODE_ROOT = 0;
    public final static int MODE_ACCESSIBILITY_SERVICE = 1;

    private int setupStep;

    private static long lastClickTime = 0;
    SharedPreferences.Editor speditor;
    String osRelease = android.os.Build.VERSION.RELEASE; //Android 版本号
    String osRel = osRelease.substring(0, 3);//截取前三位
    String osCPU = Build.CPU_ABI; //CPU 平台
    String osCPUU;
    String osVar = "micro";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        setStatusBarColor(this, R.color.suw_status_bar);
        setupStep = getIntent().getIntExtra(EXTRA_SETUP_STEP, PAGE_WELCOME);
        speditor = getSharedPreferences("data", Context.MODE_PRIVATE).edit();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarColor(Window window, @ColorRes int statusBarColor) {
        window.setStatusBarColor(ContextCompat.getColor(window.getContext(), statusBarColor));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarColor(Activity activity, @ColorRes int statusBarColor) {
        setStatusBarColor(activity.getWindow(), statusBarColor);
    }


    public void getDeviceInfo() {
        if (osCPU.contains("armeabi")) {
            osCPUU = "arm";
        } else if (osCPU.contains("arm64")) {
            osCPUU = "arm64";
        } else if (osCPU.contains("x86_64")) {
            osCPUU = "x86_64";
        } else if (osCPU.equals("x86")) {
            osCPUU = "x86";
        }

    }

    public void removeAllActivity() {
        finish();
    }

    @Override
    public void onBackPressed() {
        if (setupStep == PAGE_WELCOME) {
            this.removeAllActivity();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onNavigateBack() {
        onBackPressed();
    }


    private void intentNextStep() {
        Intent intent = new Intent(this, GuideActivity.class);
        intent.putExtra(EXTRA_SETUP_STEP, setupStep + 1);
        startActivity(intent);
    }

    private void intentMainActivity() {
        speditor.putBoolean("firstRun", false);
        speditor.apply();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void initLayout(ViewGroup viewGroup) {
        switch (setupStep) {
            case PAGE_WELCOME:
                getNavigationBar().getBackButton().setVisibility(View.GONE);
                initLayout(viewGroup, R.layout.suw_introduction, R.string.suw_wellcome_title, true);
                break;
            case PAGE_ANDROID_VERSION:
                initLayout(viewGroup, R.layout.suw_android_verison, R.string.suw_android_verison_title, true);
                getDeviceInfo();
                TextView auto_andversion = (TextView) findViewById(R.id.auto_andversion);
                auto_andversion.setText(getString(R.string.suw_android_version) + " " + osRel);
                editAndroidRadio();
                RadioGroup radio_and = (RadioGroup) findViewById(R.id.radio_and);
                radio_and.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        RadioButton rad_and = (RadioButton) findViewById(i);
                        String save_and = rad_and.getText().toString();
                        osRel = save_and.substring(8, 11);
                        if (osRel.equals("4.4")) {
                            SharedPreferences.Editor sharedPreferences = getSharedPreferences("data", MODE_PRIVATE).edit();
                            sharedPreferences.putString("android_version", "4.4");
                        }
                    }
                });
                break;
            case PAGE_CPU:
                initLayout(viewGroup, R.layout.suw_cpu, R.string.suw_cpu_title, true);
                getDeviceInfo();
                TextView auto_cpu = (TextView) findViewById(R.id.auto_cpu);
                auto_cpu.setText(getString(R.string.suw_cpu) + " " + osCPUU);
                editCPURadio();
                RadioGroup radio_cpu = (RadioGroup) findViewById(R.id.radio_cpu);
                radio_cpu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        RadioButton rad_cpu = (RadioButton) findViewById(i);
                        osCPUU = rad_cpu.getText().toString();
                    }
                });
                break;
            case PAGE_PACKAGE:
                initLayout(viewGroup, R.layout.suw_var, R.string.suw_var_title, true);
                editPackageRadio();
                RadioGroup radio_var = (RadioGroup) findViewById(R.id.radio_var);
                radio_var.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        RadioButton rad_var = (RadioButton) findViewById(i);
                        osVar = rad_var.getText().toString();
                    }
                });
                break;
            case PAGE_FINAL:
                initLayout(viewGroup, R.layout.suw_final, R.string.suw_final_title, true);
//                clickAlipay();
//                clickGP();
                break;
        }
    }

    public void editAndroidRadio() {
        RadioButton a44 = (RadioButton) findViewById(R.id.Android44);
        RadioButton a50 = (RadioButton) findViewById(R.id.Android50);
        RadioButton a51 = (RadioButton) findViewById(R.id.Android51);
        RadioButton a60 = (RadioButton) findViewById(R.id.Android60);
        RadioButton a70 = (RadioButton) findViewById(R.id.Android70);
        RadioButton a71 = (RadioButton) findViewById(R.id.Android71);
        RadioButton a80 = (RadioButton) findViewById(R.id.Android80);
        RadioButton a81 = (RadioButton) findViewById(R.id.Android81);
        if (osRel.equals("5.0")) {
            a50.setChecked(true);
        } else if (osRel.equals("5.1")) {
            a51.setChecked(true);
        } else if (osRel.equals("6.0")) {
            a60.setChecked(true);
        } else if (osRel.equals("7.0")) {
            a70.setChecked(true);
        } else if (osRel.equals("7.1")) {
            a71.setChecked(true);
        } else if (osRel.equals("8.0")) {
            a80.setChecked(true);
        } else if (osRel.equals("8.1")) {
            a81.setChecked(true);
        }

    }

    public void editCPURadio() {
        RadioButton arm = (RadioButton) findViewById(R.id.arm);
        RadioButton arm64 = (RadioButton) findViewById(R.id.arm64);
        RadioButton x86 = (RadioButton) findViewById(R.id.x86);
        RadioButton x86_64 = (RadioButton) findViewById(R.id.x86_64);
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        String osRR = sharedPreferences.getString("android_version", "");
        if (osRR.equals("4.4")) {
            arm64.setEnabled(false);
        }
        if (osCPUU.equals("arm")) {
            arm.setChecked(true);
        } else if (osCPUU.equals("arm64")) {
            arm64.setChecked(true);
        } else if (osCPUU.equals("x86")) {
            x86.setChecked(true);
        } else if (osCPUU.equals("x86_64")) {
            x86_64.setChecked(true);
        }

    }

    public void editPackageRadio() {
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        String osRR = sharedPreferences.getString("android_version", "");
        String osCC = sharedPreferences.getString("cpu", "");
        RadioButton superA = (RadioButton) findViewById(R.id.superV);
        RadioButton full = (RadioButton) findViewById(R.id.full);
        RadioButton mini = (RadioButton) findViewById(R.id.mini);
        RadioButton micro = (RadioButton) findViewById(R.id.micro);
        RadioButton aroma = (RadioButton) findViewById(R.id.aroma);
        RadioButton stock = (RadioButton) findViewById(R.id.stock);
        RadioButton nano = (RadioButton) findViewById(R.id.nano);
        RadioButton tvstock = (RadioButton) findViewById(R.id.tvstock);
        if (osRR.equals("7.0") || osRR.equals("6.0") || osRR.equals("5.1") || osRR.equals("5.0") || osRR.equals("4.4")) {
            superA.setEnabled(false);
            full.setEnabled(false);
            mini.setEnabled(false);
            micro.setEnabled(false);
            if (osRR.equals("5.1")) {
                aroma.setEnabled(false);
            }
            if (osRR.equals("5.0") || osRR.equals("4.4")) {
                aroma.setEnabled(false);
                stock.setEnabled(false);
                tvstock.setEnabled(false);
            }
            nano.setChecked(true);
        } else {
            micro.setChecked(true);
        }
    }

   /* private void clickAlipay(){
        LinearLayout alipay = (LinearLayout)findViewById(R.id.alipay_f);
        alipay.setOnClickListener(this);
    }
    private void clickGP(){
        LinearLayout gp = (LinearLayout)findViewById(R.id.google_play_f);
        gp.setOnClickListener(this);
    }*/

    @Override
    public void onClick(View view) {
        /*switch (view.getId()) {
            case R.id.alipay_f:
                Uri alipay_linkf = Uri.parse("https://qr.alipay.com/a6x09739oi8tn3iretpt0ef");
                startActivity(new Intent(Intent.ACTION_VIEW,alipay_linkf));
                break;
            case R.id.google_play_f:
                Uri gp_linkf = Uri.parse("market://details?id=com.jakting.opengapps.donation");
                startActivity(new Intent(Intent.ACTION_VIEW,gp_linkf));
                break;
            default:
                break;
        }*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onNavigateNext() {
        switch (setupStep) {
            case PAGE_WELCOME:
            case PAGE_ANDROID_VERSION:
                speditor.putString("android_version", osRel);
                speditor.apply();
                intentNextStep();
                break;
            case PAGE_CPU:
                speditor.putString("cpu", osCPUU);
                speditor.apply();
                intentNextStep();
                break;
            case PAGE_PACKAGE:
                speditor.putString("var", osVar);
                speditor.apply();
                intentNextStep();
                break;
            case PAGE_FINAL:
                intentMainActivity();
                break;
        }
    }


    private void initLayout(ViewGroup viewGroup, @LayoutRes int layout, @StringRes int title, boolean nextButtonEnable) {
        View inflate = LayoutInflater.from(this).inflate(layout, viewGroup, false);
        viewGroup.addView(inflate);
        getSetupWizardLayout().setHeaderText(title);
        getSetupWizardLayout().getHeaderTextView().setTextColor(ContextCompat.getColor(viewGroup.getContext(), android.R.color.white));
        setNavigationBarNextButtonEnable(nextButtonEnable);
    }

    private void setNavigationBarNextButtonEnable(boolean enable) {
        getNavigationBar().getNextButton().setEnabled(enable);
    }
}
