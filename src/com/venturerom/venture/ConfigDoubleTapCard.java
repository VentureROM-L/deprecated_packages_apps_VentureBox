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
import android.provider.Settings;
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

public class ConfigDoubleTapCard extends Card{
	
	final Context mContext;
	LinearLayout llDoubleTap;
	
	public ConfigDoubleTapCard(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super(context, attrs, savedInstanceState, false);
        
        mContext = context;

        setTitle(R.string.config_doubletap);
        setLayoutId(R.layout.card_config_doubletap);
        
        llDoubleTap = (LinearLayout) findLayoutViewById(R.id.llDoubleTap);

        Resources res = context.getResources();
        
        Switch switchEnable = (Switch) findLayoutViewById(R.id.doubletapstatus_enable);
        switchEnable.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            	Settings.System.putInt(getContext().getContentResolver(), "double_tap_to_sleep", isChecked ? 1 : 0);
            }
        });
        if(Settings.System.getString(getContext().getContentResolver(), "double_tap_to_sleep") != null){
        	if(Integer.valueOf(Settings.System.getString(getContext().getContentResolver(), "double_tap_to_sleep")) == 1){
        		switchEnable.setChecked(true);
        	}
        }
        TextView tvHaloLabel = (TextView) findLayoutViewById(R.id.tvdoubletapstatusLabel);
        tvHaloLabel.setText(res.getString(R.string.doubletapstatuslabel));
    }
	
	@Override
    public void expand() {
        super.expand();
        if (llDoubleTap != null) {
        	llDoubleTap.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void collapse() {
        super.collapse();
        llDoubleTap.setVisibility(View.GONE);
    }

    @Override
    public boolean canExpand() {
        return true;
    }

}
