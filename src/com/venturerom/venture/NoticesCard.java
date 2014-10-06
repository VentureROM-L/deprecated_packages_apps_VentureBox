package com.venturerom.venture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.venturerom.venture.ota.Version;
import com.venturerom.venture.ota.updater.UpdatePackage;
import com.venturerom.venture.widget.Card;

public class NoticesCard extends Card implements Response.Listener<JSONObject>, Response.ErrorListener{
	
	private RequestQueue mQueue;

	public NoticesCard(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super(context, attrs, savedInstanceState);

        setTitle(R.string.updates_title);
        setLayoutId(R.layout.card_updates_notices);

        Resources res = context.getResources();
        mQueue = Volley.newRequestQueue(context);
        
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, "http://api.venturerom.com/notices/", null, this, this);
        mQueue.add(jsObjRequest);

        //TextView romView = (TextView) findLayoutViewById(R.id.updateitem);
        //romView.setText("- Do not flash latest update!");
    }
	
	@Override
    public void onResponse(JSONObject response) {
		JSONArray updates;
		try {
			Toast.makeText(getContext(), response.getString("priority"), Toast.LENGTH_SHORT).show();
			updates = response.getJSONArray("data");
			for (int i = updates.length() - 1; i >= 0; i--) {
	            JSONObject file = updates.getJSONObject(i);
	            String date = file.optString("date");
	            String notice = file.optString("notice");
	            
	            //romView.setText(date + " " + notice);
	            Toast.makeText(getContext(), date, Toast.LENGTH_LONG).show();
	            
	            String text = "<font color='red'>" + date + "</font> : <font color='blue'>" + notice + "</font>.";
	            TextView romView = (TextView) findLayoutViewById(R.id.updateitem);
	            romView.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
	        }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}

    @Override
    public boolean canExpand() {
        return false;
    }

	@Override
	public void onErrorResponse(VolleyError error) {
		Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
		
	}

}
