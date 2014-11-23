package com.venturerom.venture;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.venturerom.venture.widget.Card;

public class ContactCard extends Card{

	public ContactCard(final Context context, AttributeSet attrs, Bundle savedInstanceState, String Title, final String Link) {
        super(context, attrs, savedInstanceState, false);

        setTitle(Title);
        setLayoutId(R.layout.card_contact);

        Resources res = context.getResources();
        
        TextView tvEmail = (TextView) findLayoutViewById(R.id.link);
        tvEmail.setClickable(true);
        tvEmail.setText(Link);
        tvEmail.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				Uri data = Uri.parse("mailto:" + Link);
				intent.setData(data);
				context.startActivity(intent);
			}
        	
        });
    }

    @Override
    public boolean canExpand() {
        return false;
    }
}