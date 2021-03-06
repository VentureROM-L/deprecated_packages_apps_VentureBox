package com.venturerom.venture;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.venturerom.venture.widget.Card;

public class ConfigWifiNotiCard extends Card{
	
	final Context mContext;
	RadioGroup radioWifiNoti;
	LinearLayout llWifiNoti;
	
	public ConfigWifiNotiCard(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super(context, attrs, savedInstanceState, false);
        
        mContext = context;

        setTitle(R.string.config_wifinoti);
        setLayoutId(R.layout.card_config_wifinoti);
        
        llWifiNoti = (LinearLayout) findLayoutViewById(R.id.llWifiNoti);

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
    }
	
	@Override
    public void expand() {
        super.expand();
        if (llWifiNoti != null) {
        	llWifiNoti.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void collapse() {
        super.collapse();
        llWifiNoti.setVisibility(View.GONE);
    }

    @Override
    public boolean canExpand() {
        return true;
    }

}
