package com.venturerom.venture;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.venturerom.venture.widget.Card;

public class ConfigNetworkTrafficCard extends Card{
	
	final Context mContext;
	LinearLayout llNetworkTraffic;
	
	private static final String KEY_NETWORK_TRAFFIC_STATE = "network_traffic_state";
    private static final String TAG = "VentureBox";
	
	//Spinner
	private Spinner stateSpinner;
	private Spinner typeSpinner;
	private Spinner refreshSpinner;
	
	private int MASK_UP = 0x00000001;
    private int MASK_DOWN = 0x00000002;
    private int MASK_UNIT = 0x00000004;
    private int MASK_PERIOD = 0xFFFF0000;
    
    private int value;
	
	public ConfigNetworkTrafficCard(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super(context, attrs, savedInstanceState, false);
        
        mContext = context;

        setTitle(R.string.config_networktraffic);
        setLayoutId(R.layout.card_config_networktraffic);
        
        llNetworkTraffic = (LinearLayout) findLayoutViewById(R.id.llNetworkTraffic);
        
        stateSpinner = (Spinner) findViewById(R.id.networktraffic_state_spinner);
        typeSpinner = (Spinner) findViewById(R.id.networktraffic_type_spinner);
        refreshSpinner = (Spinner) findViewById(R.id.networktraffic_refresh_spinner);
        
        // Create spinner adapters
        ArrayAdapter<CharSequence> stateSpinnerAdapter = ArrayAdapter.createFromResource(
                mContext, R.array.networktraffic_state_array, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> typeSpinnerAdapter = ArrayAdapter.createFromResource(
                mContext, R.array.networktraffic_type_array, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> refreshSpinnerAdapter = ArrayAdapter.createFromResource(
                mContext, R.array.networktraffic_refresh_array, android.R.layout.simple_spinner_item);
        
        // Select dropdown layout
        stateSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        refreshSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Finally, set the adapters!
        stateSpinner.setAdapter(stateSpinnerAdapter);
        typeSpinner.setAdapter(typeSpinnerAdapter);
        refreshSpinner.setAdapter(refreshSpinnerAdapter);

        //Get values
        value = Settings.System.getInt(mContext.getContentResolver(), KEY_NETWORK_TRAFFIC_STATE, 0);
        int state = value & (MASK_UP + MASK_DOWN);
        int type = getBit(value, MASK_UNIT) ? 1 : 0;
        int refresh = (value & MASK_PERIOD) >>> 16;
        switch(refresh){
        case 500:
        	refresh = 0;
        	break;
        case 1000:
        	refresh = 1;
        	break;
        case 1500:
        	refresh = 2;
        	break;
        case 2000:
        	refresh = 3;
        	break;
        }
        // ¯\_(ツ)_/¯
        //Look at this beautiful logic :P
        
        //Set spinner value
        stateSpinner.setSelection(state);
        typeSpinner.setSelection(type);
        refreshSpinner.setSelection(refresh);
        
        //Set listeners
        stateSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected (AdapterView<?> parent, View view,
                int pos, long id) {
            		value = setBit(value, MASK_UP, getBit(pos, MASK_UP));
            		value = setBit(value, MASK_DOWN, getBit(pos, MASK_DOWN));
            		Settings.System.putInt(mContext.getContentResolver(), KEY_NETWORK_TRAFFIC_STATE, value);
                }

            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "Nothing was selected from " + parent.toString());
            }
        });
        
        typeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected (AdapterView<?> parent, View view,
                int pos, long id) {
            		value = setBit(value, MASK_UNIT, String.valueOf(pos).equals("1"));
            		Settings.System.putInt(mContext.getContentResolver(), KEY_NETWORK_TRAFFIC_STATE, value);
                }

            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "Nothing was selected from " + parent.toString());
            }
        });
        refreshSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected (AdapterView<?> parent, View view,
                int pos, long id) {
            		value = setBit(value, MASK_PERIOD, false) + (pos << 16);
            		Settings.System.putInt(mContext.getContentResolver(), KEY_NETWORK_TRAFFIC_STATE, value);
                }

            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "Nothing was selected from " + parent.toString());
            }
        });
        
        Resources res = context.getResources();
        /*
        Switch switchEnable = (Switch) findLayoutViewById(R.id.cardview_enable);
        switchEnable.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            	Settings.System.putInt(getContext().getContentResolver(), "status_bar_recents_card_stack", isChecked ? 1 : 2);
            }
        });
        if(Settings.System.getString(getContext().getContentResolver(), "status_bar_recents_card_stack") != null){
        	if(Integer.valueOf(Settings.System.getString(getContext().getContentResolver(), "status_bar_recents_card_stack")) == 1){
            	switchEnable.setChecked(true);
            }
        }
                */
    }
	
	@Override
    public void expand() {
        super.expand();
        if (llNetworkTraffic != null) {
        	llNetworkTraffic.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void collapse() {
        super.collapse();
        llNetworkTraffic.setVisibility(View.GONE);
    }

    @Override
    public boolean canExpand() {
        return true;
    }
    
    private int setBit(int intNumber, int intMask, boolean blnState) {
        if (blnState) {
            return (intNumber | intMask);
        }
        return (intNumber & ~intMask);
    }
    
    private boolean getBit(int intNumber, int intMask) {
        return (intNumber & intMask) == intMask;
    }

}
