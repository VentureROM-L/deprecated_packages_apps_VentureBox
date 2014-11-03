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

public class ConfigHaloCard extends Card{
	
	final Context mContext;
	LinearLayout llHalo;
	
	public ConfigHaloCard(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super(context, attrs, savedInstanceState, false);
        
        mContext = context;

        setTitle(R.string.config_halo);
        setLayoutId(R.layout.card_config_halo);
        
        llHalo = (LinearLayout) findLayoutViewById(R.id.llHalo);

        Resources res = context.getResources();
        
        Switch switchEnable = (Switch) findLayoutViewById(R.id.halo_enable);
        switchEnable.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            	Settings.System.putInt(getContext().getContentResolver(), "halo_active", isChecked ? 1 : 0);
            }
        });
        if(Settings.System.getString(getContext().getContentResolver(), "halo_active") != null){
        	if(Integer.valueOf(Settings.System.getString(getContext().getContentResolver(), "halo_active")) == 1){
        		switchEnable.setChecked(true);
        	}
        }
        TextView tvHaloLabel = (TextView) findLayoutViewById(R.id.tvHaloLabel);
        tvHaloLabel.setText(res.getString(R.string.halolabel));
    }
	
	@Override
    public void expand() {
        super.expand();
        if (llHalo != null) {
        	llHalo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void collapse() {
        super.collapse();
        llHalo.setVisibility(View.GONE);
    }

    @Override
    public boolean canExpand() {
        return true;
    }

}
