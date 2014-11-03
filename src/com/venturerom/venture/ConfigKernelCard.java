package com.venturerom.venture;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.venturerom.venture.widget.Card;

public class ConfigKernelCard extends Card{
	
	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
	LinearLayout llKernel;
	
	public ConfigKernelCard(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super(context, attrs, savedInstanceState, false);

        setTitle(R.string.config_kernel);
        setLayoutId(R.layout.card_config_kernel);
        
        llKernel = (LinearLayout) findLayoutViewById(R.id.llKernel);

        Resources res = context.getResources();
        

        Switch switchEnable = (Switch) findLayoutViewById(R.id.performance_enable);
        switchEnable.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                	try{
                		Process process = Runtime.getRuntime().exec("su");
                		DataOutputStream os = new DataOutputStream(process.getOutputStream());
                		os.writeBytes("mount -o remount rw /system\n");
                		os.writeBytes("mv /system/etc/init.inactive/07venturekernel /system/etc/init.d/07venturekernel\n");
                		os.flush();
                	}catch(IOException e){
                		e.printStackTrace();
                	}
                	settings.edit().putBoolean("startupScriptEnabled", true).commit();
                }else{
                	try{
                		Process process = Runtime.getRuntime().exec("su");
                		DataOutputStream os = new DataOutputStream(process.getOutputStream());
                		os.writeBytes("mount -o remount rw /system\n");
                		os.writeBytes("mkdir /system/etc/init.inactive\n");
                		os.writeBytes("mv /system/etc/init.d/07venturekernel /system/etc/init.inactive/07venturekernel\n");
                		os.flush();
                	}catch(IOException e){
                		e.printStackTrace();
                	}
                	settings.edit().putBoolean("startupScriptEnabled", false).commit();
                }
            }
        });
        if(settings.getBoolean("startupScriptEnabled", true)){
        	switchEnable.setChecked(true);
        }
        
        TextView tvPerformanceLabel = (TextView) findLayoutViewById(R.id.tvPerformance);
        tvPerformanceLabel.setText(res.getString(R.string.config_performance));
    }
	
	@Override
    public void expand() {
        super.expand();
        if (llKernel != null) {
        	llKernel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void collapse() {
        super.collapse();
        llKernel.setVisibility(View.GONE);
    }

    @Override
    public boolean canExpand() {
        return true;
    }

}
