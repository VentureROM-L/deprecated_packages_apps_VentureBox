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
import android.util.Log;
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

public class ConfigHaloCard extends Card {

    private static final String KEY_HALO_STATE = "halo_state";
    private static final String KEY_HALO_HIDE = "halo_hide";
    private static final String KEY_HALO_SIZE = "halo_size";
    private static final String KEY_HALO_PAUSE = "halo_pause";
    private static final String KEY_HALO_MSGBOX = "halo_msgbox";
    private static final String KEY_HALO_NOTIFY_COUNT = "halo_notify_count";
    private static final String KEY_HALO_UNLOCK_PING = "halo_unlock_ping";

    private static final String TAG = "VentureBox";

	final Context mContext;
	LinearLayout llHalo;

    // Spinners
    Spinner stateSpinner;
    Spinner sizeSpinner;
    Spinner notifyCountSpinner;

    // Switches
    Switch hideSwitch;
    Switch pauseSwitch;
    Switch msgboxSwitch;
    Switch unlockPingSwitch;

	public ConfigHaloCard(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super(context, attrs, savedInstanceState, false);

        mContext = context;

        setTitle(R.string.config_halo);
        setLayoutId(R.layout.card_config_halo);

        llHalo = (LinearLayout) findLayoutViewById(R.id.llHalo);

        Resources res = context.getResources();

        // Find spinner views
        stateSpinner = (Spinner) findViewById(R.id.halo_state_spinner);
        sizeSpinner = (Spinner) findViewById(R.id.halo_size_spinner);
        notifyCountSpinner = (Spinner) findViewById(R.id.halo_notify_count_spinner);

        // Create spinner adapters
        ArrayAdapter<CharSequence> stateSpinnerAdapter = ArrayAdapter.createFromResource(
                mContext, R.array.halo_state_array, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> sizeSpinnerAdapter = ArrayAdapter.createFromResource(
                mContext, R.array.halo_size_array, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> notifyCountSpinnerAdapter = ArrayAdapter.createFromResource(
                mContext, R.array.halo_notify_count_array, android.R.layout.simple_spinner_item);

        // Select dropdown layout
        stateSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        notifyCountSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Finally, set the adapters! Woo!
        stateSpinner.setAdapter(stateSpinnerAdapter);
        sizeSpinner.setAdapter(sizeSpinnerAdapter);
        notifyCountSpinner.setAdapter(notifyCountSpinnerAdapter);

        // Grab current settings for spinners
        int stateSetting = Settings.System.getInt(getContext().getContentResolver(), KEY_HALO_STATE, 1);
        float sizeSetting = Settings.System.getFloat(getContext().getContentResolver(), KEY_HALO_SIZE, 1.0f);
        int notifyCountSetting = Settings.System.getInt(getContext().getContentResolver(), KEY_HALO_NOTIFY_COUNT, 3);


        // Set spinner selection based on current settings
        // We don't want to change the users settings when we load the screen
        if (stateSetting == 0) stateSpinner.setSelection(1);
        // if stateSetting != 0, then it'll default to 0 anyways

        if (sizeSetting == 0.6f) sizeSpinner.setSelection(0);
        else if (sizeSetting == 0.8f) sizeSpinner.setSelection(1);
        else if (sizeSetting == 1.0f) sizeSpinner.setSelection(2);
        else if (sizeSetting == 1.2f) sizeSpinner.setSelection(3);
        else if (sizeSetting == 1.4f) sizeSpinner.setSelection(4);
        else if (sizeSetting == 1.6f) sizeSpinner.setSelection(5);
        else sizeSpinner.setSelection(2);
        // ¯\_(ツ)_/¯
        // floats and switches don't work out, what are ya gonna do?

        notifyCountSpinner.setSelection(notifyCountSetting - 1);
        // clever, I know

        // Let's make and apply some spinner onItemSelected listeners
        stateSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected (AdapterView<?> parent, View view,
                int pos, long id) {
                switch (pos) {
                    case 0: Settings.System.putInt(getContext().getContentResolver(), KEY_HALO_STATE, 1);
                        break;
                    case 1: Settings.System.putInt(getContext().getContentResolver(), KEY_HALO_STATE, 0);
                        break;
                    default: Settings.System.putInt(getContext().getContentResolver(), KEY_HALO_STATE, 1);
                        break;
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "Nothing was selected from " + parent.toString());
            }
        });

        sizeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected (AdapterView<?> parent, View view,
                int pos, long id) {
                switch (pos) {
                    case 0: Settings.System.putFloat(getContext().getContentResolver(), KEY_HALO_SIZE, 0.6f);
                        break;
                    case 1: Settings.System.putFloat(getContext().getContentResolver(), KEY_HALO_SIZE, 0.8f);
                        break;
                    case 2: Settings.System.putFloat(getContext().getContentResolver(), KEY_HALO_SIZE, 1.0f);
                        break;
                    case 3: Settings.System.putFloat(getContext().getContentResolver(), KEY_HALO_SIZE, 1.2f);
                        break;
                    case 4: Settings.System.putFloat(getContext().getContentResolver(), KEY_HALO_SIZE, 1.4f);
                        break;
                    case 5: Settings.System.putFloat(getContext().getContentResolver(), KEY_HALO_SIZE, 1.6f);
                        break;
                    default: Settings.System.putFloat(getContext().getContentResolver(), KEY_HALO_SIZE, 1.0f);
                        break;
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "Nothing was selected from " + parent.toString());
            }
        });

        notifyCountSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected (AdapterView<?> parent, View view,
                int pos, long id) {
                switch (pos) {
                    case 0: Settings.System.putInt(getContext().getContentResolver(), KEY_HALO_NOTIFY_COUNT, 1);
                        break;
                    case 1: Settings.System.putInt(getContext().getContentResolver(), KEY_HALO_NOTIFY_COUNT, 2);
                        break;
                    case 2: Settings.System.putInt(getContext().getContentResolver(), KEY_HALO_NOTIFY_COUNT, 3);
                        break;
                    case 3: Settings.System.putInt(getContext().getContentResolver(), KEY_HALO_NOTIFY_COUNT, 4);
                        break;
                    default: Settings.System.putInt(getContext().getContentResolver(), KEY_HALO_NOTIFY_COUNT, 4);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "Nothing was selected from " + parent.toString());
            }
        });

        // We got the spinners - lets get the switches, bitches
        Switch switchEnable = (Switch) findLayoutViewById(R.id.halo_enable);
        hideSwitch = (Switch) findLayoutViewById(R.id.halo_hide_switch);
        pauseSwitch = (Switch) findLayoutViewById(R.id.halo_pause_switch);
        msgboxSwitch = (Switch) findLayoutViewById(R.id.halo_msgbox_switch);
        unlockPingSwitch = (Switch) findLayoutViewById(R.id.halo_unlock_ping_switch);

        // Get settings
        String hideSetting = Settings.System.getString(getContext().getContentResolver(), KEY_HALO_HIDE);
        String pauseSetting = Settings.System.getString(getContext().getContentResolver(), KEY_HALO_PAUSE);
        String msgboxSetting = Settings.System.getString(getContext().getContentResolver(), KEY_HALO_MSGBOX);
        String unlockPingSetting = Settings.System.getString(getContext().getContentResolver(), KEY_HALO_UNLOCK_PING);

        // Set switches appropriately
        if(Settings.System.getString(getContext().getContentResolver(), "halo_active") != null){
        	if(Integer.valueOf(Settings.System.getString(getContext().getContentResolver(), "halo_active")) == 1){
        		switchEnable.setChecked(true);
        	}
        }
        if (hideSetting != null && Integer.valueOf(hideSetting) == 1) hideSwitch.setChecked(true);
        if (pauseSetting != null && Integer.valueOf(pauseSetting) == 1) pauseSwitch.setChecked(true);
        if (msgboxSetting != null && Integer.valueOf(msgboxSetting) == 1) msgboxSwitch.setChecked(true);
        if (unlockPingSetting != null && Integer.valueOf(unlockPingSetting) == 0) unlockPingSwitch.setChecked(true);

        // All the cool kids set and create listeners at the same time
        switchEnable.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            	Settings.System.putInt(getContext().getContentResolver(), "halo_active", isChecked ? 1 : 0);
            }
        });
        hideSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.System.putInt(getContext().getContentResolver(), KEY_HALO_HIDE, isChecked ? 1 : 0);
            }
        });
        pauseSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.System.putInt(getContext().getContentResolver(), KEY_HALO_PAUSE, isChecked ? 1 : 0);
            }
        });
        msgboxSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.System.putInt(getContext().getContentResolver(), KEY_HALO_MSGBOX, isChecked ? 1 : 0);
            }
        });
        unlockPingSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonViefw, boolean isChecked) {
                Settings.System.putInt(getContext().getContentResolver(), KEY_HALO_UNLOCK_PING, isChecked ? 1 : 0);
            }
        });
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