package com.jakting.opengapps;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends Activity {
    //Set text to show in Toast
    String CPU = "arm", VE = "5.1", PA = "pico";
    //Set text to use
    RadioGroup androidcpu, androidversion, androidzip, date;
    //Set CPU
    RadioButton arm, arm64, x86, x86_64;
    //Set Version
    RadioButton kk, ll0, ll1, mm, no, no1, oo;
    //Set Package
    RadioButton aroma, super0, stock, full, mini, micro, nano, pico, tvstock;
    //Set for Download
    RadioButton today, yesterday, tdbyesterday;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDownloadLink();
        startDownLoad();
        dialogWhenStart();
    }

    public void dialogWhenStart() {
        AlertDialog.Builder builder0 = new AlertDialog.Builder(MainActivity.this);
        builder0.setTitle(R.string.start_title);
        builder0.setMessage(R.string.start);
        builder0.setPositiveButton(getString(R.string.start_ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface p1, int p2) {
                Toast.makeText(MainActivity.this, getString(R.string.use), Toast.LENGTH_LONG).show();
            }
        });
        builder0.setNegativeButton(getString(R.string.gapps_links), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface p1, int p2) {
                Uri gappslinks = Uri.parse("https://jakting.com/archives/gapps-links-what.html");
                startActivity(new Intent(Intent.ACTION_VIEW, gappslinks));
            }
        });
        builder0.create();
        builder0.show();
    }


    private String getTodayDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar calto = Calendar.getInstance();
        calto.add(Calendar.DATE, 0);
        return dateFormat.format(calto.getTime());
    }

    private String getYesterdayDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar calye = Calendar.getInstance();
        calye.add(Calendar.DATE, -1);
        return dateFormat.format(calye.getTime());
    }

    private String getTheDayBeforeYesterdayDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar caltd = Calendar.getInstance();
        caltd.add(Calendar.DATE, -2);
        return dateFormat.format(caltd.getTime());
    }

    String dateFinish = getTodayDateString();

    public void setDownloadLink() {
        androidzip = (RadioGroup) findViewById(R.id.androidzip);
        date = (RadioGroup) findViewById(R.id.date);
        //Set Package
        aroma = (RadioButton) findViewById(R.id.aroma);
        super0 = (RadioButton) findViewById(R.id.super0);
        stock = (RadioButton) findViewById(R.id.stock);
        full = (RadioButton) findViewById(R.id.full);
        mini = (RadioButton) findViewById(R.id.mini);
        micro = (RadioButton) findViewById(R.id.micro);
        nano = (RadioButton) findViewById(R.id.nano);
        pico = (RadioButton) findViewById(R.id.pico);
        tvstock = (RadioButton) findViewById(R.id.tvstock);
        //Set for Download
        today = (RadioButton) findViewById(R.id.today);
        yesterday = (RadioButton) findViewById(R.id.yesterday);
        tdbyesterday = (RadioButton) findViewById(R.id.tdbyesterday);

        //Setting CPU
        {
            Spinner spinner = (Spinner) findViewById(R.id.platform_set);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.platform_version, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String[] mItems = getResources().getStringArray(R.array.platform_version);
                    String cpuCPU = mItems[i].toString().toLowerCase();;
                    CPU = cpuCPU;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        //Setting Android Version
        {
            {
                Spinner spinner1 = (Spinner) findViewById(R.id.android_set);
                ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                        R.array.android_version, android.R.layout.simple_spinner_item);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner1.setAdapter(adapter1);
                spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String[] mItems1 = getResources().getStringArray(R.array.android_version);
                        String veVE = mItems1[i].toString().toLowerCase();;
                        VE = veVE;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }
        androidzip.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup p1, int p2) {
                if (aroma.isChecked()) {
                    PA = "aroma";
                }
                if (super0.isChecked()) {
                    PA = "super";
                }
                if (stock.isChecked()) {
                    PA = "stock";
                }
                if (full.isChecked()) {
                    PA = "full";
                }
                if (mini.isChecked()) {
                    PA = "mini";
                }
                if (micro.isChecked()) {
                    PA = "micro";
                }
                if (nano.isChecked()) {
                    PA = "nano";
                }
                if (pico.isChecked()) {
                    PA = "pico";
                }
                if (tvstock.isChecked()) {
                    PA = "tvstock";
                }
            }

        });
        date.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup p1, int p2) {
                if (today.isChecked()) {
                    dateFinish = getTodayDateString();
                }
                if (yesterday.isChecked()) {
                    dateFinish = getYesterdayDateString();
                }
                if (tdbyesterday.isChecked()) {
                    dateFinish = getTheDayBeforeYesterdayDateString();
                }

            }

        });


    }

    private void urlDownloadGet() {
        Uri uri = Uri.parse("https://github.com/opengapps/" + CPU + "/releases/download/" + dateFinish + "/open_gapps-" + CPU + "-" + VE + "-" + PA + "-" + dateFinish + ".zip");
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    public void startDownLoad() {
        findViewById(R.id.download).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {
                urlDownloadGet();
            }
        });
    }

    private void urlMD5Get() {
        Uri urimd5 = Uri.parse("https://github.com/opengapps/" + CPU + "/releases/download/" + dateFinish + "/open_gapps-" + CPU + "-" + VE + "-" + PA + "-" + dateFinish + ".zip.md5");
        startActivity(new Intent(Intent.ACTION_VIEW, urimd5));
    }

    public void startMD5DownLoad() {
        urlMD5Get();
    }

    private void urlReGet() {
        Uri urire = Uri.parse("https://github.com/opengapps/" + CPU + "/releases/download/" + dateFinish + "/sources_report-" + CPU + "-" + VE + "-" + dateFinish + ".txt");
        startActivity(new Intent(Intent.ACTION_VIEW, urire));
    }

    public void startReDownLoad() {
        urlReGet();
    }

    private void urlInfoGet() {
        Uri uriin = Uri.parse("https://github.com/opengapps/" + CPU + "/releases/download/" + dateFinish + "/open_gapps-" + CPU + "-" + VE + "-" + PA + "-" + dateFinish + ".versionlog.txt");
        startActivity(new Intent(Intent.ACTION_VIEW, uriin));
    }

    public void startInfoDownLoad() {
        urlInfoGet();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info_menu:
                startInfoDownLoad();
                return true;
            case R.id.md5_menu:
                startMD5DownLoad();
                return true;
            case R.id.report_menu:
                startReDownLoad();
                return true;
            case R.id.about_menu:
                openAbout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openAbout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.about_title);
        builder.setMessage(R.string.about);
        builder.setPositiveButton(getString(R.string.about_ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface p1, int p2) {
                Toast.makeText(MainActivity.this, getString(R.string.enjoy), Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton(getString(R.string.gapps_links), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface p1, int p2) {
                Uri gappslinks0 = Uri.parse("https://jakting.com/archives/gapps-links-what.html");
                startActivity(new Intent(Intent.ACTION_VIEW, gappslinks0));
            }
        });
        builder.create();
        builder.show();
    }
}
