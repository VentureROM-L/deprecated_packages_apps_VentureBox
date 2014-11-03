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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.venturerom.venture.widget.Card;

public class ConfigWifiNotiCard extends Card{
	
	final Context mContext;
	RadioGroup radioWifiNoti;
	
	public ConfigWifiNotiCard(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super(context, attrs, savedInstanceState, false);
        
        mContext = context;

        setTitle(R.string.config_wifinoti);
        setLayoutId(R.layout.card_config_wifinoti);

        final Resources res = context.getResources();
        
        radioWifiNoti = (RadioGroup)findLayoutViewById(R.id.radioWifiNoti);
        radioWifiNoti.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				RadioButton rb = (RadioButton)findLayoutViewById(checkedId);
				if(rb.getText().equals(res.getString(R.string.wifinotidisabled))){
					Settings.System.putInt(getContext().getContentResolver(), "wifi_network_notifications", 0);
				} else if(rb.getText().equals(res.getString(R.string.wifinotitoast))){
					Settings.System.putInt(getContext().getContentResolver(), "wifi_network_notifications", 1);
				} else if(rb.getText().equals(res.getString(R.string.wifinotinotification))){
					Settings.System.putInt(getContext().getContentResolver(), "wifi_network_notifications", 2);
				} else if(rb.getText().equals(res.getString(R.string.wifinotinotificationsound))){
					Settings.System.putInt(getContext().getContentResolver(), "wifi_network_notifications", 3);
				}
			}
        	
        });
        if(Settings.System.getString(getContext().getContentResolver(), "wifi_network_notifications") != null){
        	RadioButton rb;
        	switch(Integer.valueOf(Settings.System.getString(getContext().getContentResolver(), "wifi_network_notifications"))){
        	case 0:
        		rb = (RadioButton) findLayoutViewById(R.id.radioDisabled);
        		rb.setChecked(true);
        		break;
        	case 1:
        		rb = (RadioButton) findLayoutViewById(R.id.radioToast);
        		rb.setChecked(true);
        		break;
        	case 2:
        		rb = (RadioButton) findLayoutViewById(R.id.radioNotification);
        		rb.setChecked(true);
        		break;
        	case 3:
        		rb = (RadioButton) findLayoutViewById(R.id.radioNotificationSound);
        		rb.setChecked(true);
        		break;
        	}
        }
        TextView tvHaloLabel = (TextView) findLayoutViewById(R.id.tvWifiNotiLabel);
        tvHaloLabel.setText(res.getString(R.string.wifinotilabel));
        
        if (isExpanded()) {
        	radioWifiNoti.setVisibility(View.VISIBLE);
        }
    }
	
	@Override
    public void expand() {
        super.expand();
        if (radioWifiNoti != null) {
        	radioWifiNoti.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void collapse() {
        super.collapse();
        radioWifiNoti.setVisibility(View.GONE);
    }

    @Override
    public boolean canExpand() {
        return true;
    }

}
