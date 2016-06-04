package com.jakting.opengapps;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.util.Calendar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class MainActivity extends Activity 
{
    //Set text to show in Toast
    String CPU="arm";
    String VE="4.4";
    String PA="pico";
    //Set text to use
    RadioGroup androidcpu;
    RadioGroup androidversion;
    RadioGroup androidzip;
    RadioGroup date;
    //Set CPU
    RadioButton arm;
    RadioButton arm64;
    RadioButton x86;
    RadioButton x86_64;
    //Set Version
    RadioButton kk;
    RadioButton ll0;
    RadioButton ll1;
    RadioButton mm;
    //Set Package
    RadioButton aroma;
    RadioButton super0;
    RadioButton stock;
    RadioButton full;
    RadioButton mini;
    RadioButton micro;
    RadioButton nano;
    RadioButton pico;
    RadioButton tvstock;
    //Set for Download
    RadioButton today;
    RadioButton yesterday;
    
    
    
    //Set Download Link
    Calendar t = Calendar.getInstance();
    int yi=t.get(Calendar.YEAR);
    int mi=t.get(Calendar.MONTH)+1;
    int di=t.get(Calendar.DAY_OF_MONTH);
    String y=String.valueOf(yi);
    String m=String.valueOf(mi);
    String d=String.valueOf(di);
    
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setDownloadLink();
        startDownLoad();
        monthAddOne();
        dialogWhenStart();
    }
    
    public void dialogWhenStart(){
        AlertDialog.Builder builder0 = new AlertDialog.Builder(MainActivity.this);
        builder0.setTitle(R.string.start_title);
        builder0.setMessage(R.string.start);
        builder0.setPositiveButton(getString(R.string.start_ok), new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface p1, int p2)
                {
                    Toast.makeText(MainActivity.this,getString(R.string.use),Toast.LENGTH_LONG).show();
                }
            });
        builder0.create();
        builder0.show();
    }
    public void monthAddOne(){
        if(mi<10){
            m="0"+m;
        }
        if(di<10){
            d="0"+d;
        }
    }
    
    public void setDownloadLink(){
         androidcpu=(RadioGroup)findViewById(R.id.androidcpu);
         androidversion=(RadioGroup)findViewById(R.id.androidversion);
         androidzip=(RadioGroup)findViewById(R.id.androidzip);
         date=(RadioGroup)findViewById(R.id.date);
        //Set CPU
         arm=(RadioButton)findViewById(R.id.arm);
         arm64=(RadioButton)findViewById(R.id.arm64);
         x86=(RadioButton)findViewById(R.id.x86);
         x86_64=(RadioButton)findViewById(R.id.x86_64);
        //Set Version
         kk=(RadioButton)findViewById(R.id.Kitkat);
         ll0=(RadioButton)findViewById(R.id.Lollipop0);
         ll1=(RadioButton)findViewById(R.id.Lollipop1);
         mm=(RadioButton)findViewById(R.id.MarshMallow);
        //Set Package
         aroma=(RadioButton)findViewById(R.id.aroma);
         super0=(RadioButton)findViewById(R.id.super0);
         stock=(RadioButton)findViewById(R.id.stock);
         full=(RadioButton)findViewById(R.id.full);
         mini=(RadioButton)findViewById(R.id.mini);
         micro=(RadioButton)findViewById(R.id.micro);
         nano=(RadioButton)findViewById(R.id.nano);
         pico=(RadioButton)findViewById(R.id.pico);
         tvstock=(RadioButton)findViewById(R.id.tvstock);
         //Set for Download
         today=(RadioButton)findViewById(R.id.today);
         yesterday=(RadioButton)findViewById(R.id.yesterday);
        
        androidcpu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

                @Override
                public void onCheckedChanged(RadioGroup p1, int p2)
                {
                    if(arm.isChecked()){
                        CPU="arm";
                    }
                    if(arm64.isChecked()){
                        CPU="arm64";
                    }
                    if(x86.isChecked()){
                        CPU="x86";
                    }
                    if(x86_64.isChecked()){
                        CPU="x86_64";
                    }
                    
                }
                
            });
        androidversion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

                @Override
                public void onCheckedChanged(RadioGroup p1, int p2)
                {
                    if(kk.isChecked()){
                        VE="4.4";
                    }
                    if(ll0.isChecked()){
                        VE="5.0";
                    }
                    if(ll1.isChecked()){
                        VE="5.1";
                    }
                    if(mm.isChecked()){
                        VE="6.0";
                    }

                }

            });
        androidzip.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

                @Override
                public void onCheckedChanged(RadioGroup p1, int p2)
                {
                    if(aroma.isChecked()){
                        PA="aroma";
                    }
                    if(super0.isChecked()){
                        PA="super";
                    }
                    if(stock.isChecked()){
                        PA="stock";
                    }
                    if(full.isChecked()){
                        PA="full";
                    }
                    if(mini.isChecked()){
                        PA="mini";
                    }
                    if(micro.isChecked()){
                        PA="micro";
                    }
                    if(nano.isChecked()){
                        PA="nano";
                    }
                    if(pico.isChecked()){
                        PA="pico";
                    }
                    if(tvstock.isChecked()){
                        PA="tvstock";
                    }
                }

            });
        date.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

                @Override
                public void onCheckedChanged(RadioGroup p1, int p2)
                {
                    if(today.isChecked()){
                        d=String.valueOf(di);
                    }
                    if(yesterday.isChecked()){
                        int dii=di;
                        int mii=mi;
                        int yii=yi;
                        if(mii==1){
                            if(dii==1){
                                mii--;
                                dii--;
                                yii--;
                            }else{
                                dii--;
                            }
                        }
                        if(dii==1){
                            mii--;
                            dii--;
                        }else{
                            dii--;
                        }
                        
                        if(dii<10){
                            d="0"+dii;
                        }
                            d=String.valueOf(dii);
                        
                        if(mii<10){
                            m="0"+mii;
                        }
                            m=String.valueOf(mii);
                        
                        
                    }

                }

            });
        
        
    }
    
    private void urlGet(){
        Uri uri = Uri.parse("https://github.com/opengapps/"+CPU+"/releases/download/"+y+m+d+"/open_gapps-"+CPU+"-"+VE+"-"+PA+"-"+y+m+d+".zip");
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }
    public void startDownLoad(){
        findViewById(R.id.download).setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View p1)
                {
                    urlGet();
                }
            });
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.about_menu:
                openAbout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void openAbout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.about_title);
        builder.setMessage(R.string.about);
        builder.setPositiveButton(getString(R.string.about_ok), new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface p1, int p2)
                {
                    Toast.makeText(MainActivity.this,getString(R.string.enjoy),Toast.LENGTH_LONG).show();
                }
            });
        builder.create();
        builder.show();
    }
}
