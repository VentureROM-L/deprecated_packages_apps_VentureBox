package com.venturerom.venture;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

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
        
        Switch switchdoubletapstatusEnable = (Switch) findLayoutViewById(R.id.doubletapstatus_enable);
        switchdoubletapstatusEnable.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            	Settings.System.putInt(getContext().getContentResolver(), "double_tap_status_bar_to_sleep", isChecked ? 1 : 0);
            }
        });
        if(Settings.System.getString(getContext().getContentResolver(), "double_tap_status_bar_to_sleep") != null){
        	if(Integer.valueOf(Settings.System.getString(getContext().getContentResolver(), "double_tap_status_bar_to_sleep")) == 1){
        		switchdoubletapstatusEnable.setChecked(true);
        	}
        }
        TextView tvDoubleTapStatusBarLabel = (TextView) findLayoutViewById(R.id.tvdoubletapstatusLabel);
        tvDoubleTapStatusBarLabel.setText(res.getString(R.string.doubletapstatuslabel));
        
        Switch switchdoubletapnavEnable = (Switch) findLayoutViewById(R.id.doubletapnav_enable);
        switchdoubletapnavEnable.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            	Settings.System.putInt(getContext().getContentResolver(), "double_tap_nav_bar_to_sleep", isChecked ? 1 : 0);
            }
        });
        if(Settings.System.getString(getContext().getContentResolver(), "double_tap_nav_bar_to_sleep") != null){
        	if(Integer.valueOf(Settings.System.getString(getContext().getContentResolver(), "double_tap_nav_bar_to_sleep")) == 1){
        		switchdoubletapnavEnable.setChecked(true);
        	}
        }
        TextView tvDoubleTapNavBarLabel = (TextView) findLayoutViewById(R.id.tvdoubletapnavLabel);
        tvDoubleTapNavBarLabel.setText(res.getString(R.string.doubletapnavlabel));
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
