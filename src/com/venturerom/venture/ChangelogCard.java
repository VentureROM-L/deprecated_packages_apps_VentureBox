package com.venturerom.venture;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.TextView;

import com.venturerom.venture.widget.Card;

public class ChangelogCard extends Card{

	public ChangelogCard(Context context, AttributeSet attrs, Bundle savedInstanceState, String Title, String Changes) {
        super(context, attrs, savedInstanceState);

        setTitle(Title);
        setLayoutId(R.layout.card_changelog);

        Resources res = context.getResources();

        //TextView romView = (TextView) findLayoutViewById(R.id.logTitle);
        //romView.setText("Thursday 2 October 2014");
        //romView.setText(res.getString(R.string.system_rom,romUpdater.getVersion().toString(false)));
        
        TextView maintainerView = (TextView) findLayoutViewById(R.id.log);
        maintainerView.setText(Changes);
    }

    @Override
    public boolean canExpand() {
        return false;
    }
}
