package com.venturerom.venture;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.venturerom.venture.widget.Card;

public class FAQCard extends Card{
	
	LinearLayout llFAQ;

	public FAQCard(Context context, AttributeSet attrs, Bundle savedInstanceState, String Title) {
        super(context, attrs, savedInstanceState, false);

        setTitle(Title);
        setLayoutId(R.layout.card_faq);
        
        llFAQ = (LinearLayout) findLayoutViewById(R.id.llFAQ);
        
        Resources res = context.getResources();
        if(Title.equals(res.getString(R.string.faq_q_1))){
        	TextView tvAnswer = (TextView)findLayoutViewById(R.id.faq);
        	tvAnswer.setText(res.getString(R.string.faq_a_1));
        }
        
    }

	@Override
    public void expand() {
        super.expand();
        if (llFAQ != null) {
        	llFAQ.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void collapse() {
        super.collapse();
        llFAQ.setVisibility(View.GONE);
    }

    @Override
    public boolean canExpand() {
        return true;
    }
}
