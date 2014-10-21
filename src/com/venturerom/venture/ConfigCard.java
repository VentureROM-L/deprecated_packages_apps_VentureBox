package com.venturerom.venture;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.venturerom.venture.widget.Card;

public class ConfigCard extends Card{
	
	public ConfigCard(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super(context, attrs, savedInstanceState, false);

        setTitle(R.string.config_kernel);
        setLayoutId(R.layout.card_config);

        Resources res = context.getResources();

        Spinner spinner = (Spinner) findLayoutViewById(R.id.performance_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
             R.array.performance_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            	switch(position){
            	//Performance
				case 0:
					
					break;
				//Standard
				case 1:
					
					break;
				//Battery Smart
				case 2:
					
					break;
				}
            }

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}

        });
        
        TextView tvPerformanceLabel = (TextView) findLayoutViewById(R.id.tvPerformance);
        tvPerformanceLabel.setText(res.getString(R.string.config_performance));
    }

    @Override
    public boolean canExpand() {
        return false;
    }

}
