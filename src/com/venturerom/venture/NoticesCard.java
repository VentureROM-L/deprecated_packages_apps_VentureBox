package com.venturerom.venture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
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
        super(context, attrs, savedInstanceState, true);

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
			//Toast.makeText(getContext(), response.getString("priority"), Toast.LENGTH_SHORT).show();
			if(response.getString("priority").equals("low")){
				View bar = (View)findViewById(R.id.bar);
				bar.setBackgroundColor(Color.GREEN);
				bar = (View)findViewById(R.id.barTop);
				bar.setBackgroundColor(Color.GREEN);
			}
			else if(response.getString("priority").equals("normal")){
				View bar = (View)findViewById(R.id.bar);
				bar.setBackgroundColor(Color.BLUE);
				bar = (View)findViewById(R.id.barTop);
				bar.setBackgroundColor(Color.BLUE);
			}
			else if(response.getString("priority").equals("warning")){
				View bar = (View)findViewById(R.id.bar);
				bar.setBackgroundColor(Color.YELLOW);
				bar = (View)findViewById(R.id.barTop);
				bar.setBackgroundColor(Color.YELLOW);
			}else if (response.getString("priority").equals("urgent")){
				View bar = (View)findViewById(R.id.bar);
				bar.setBackgroundColor(Color.RED);
				bar = (View)findViewById(R.id.barTop);
				bar.setBackgroundColor(Color.RED);
			}
			updates = response.getJSONArray("data");
			for (int i = updates.length() - 1; i >= 0; i--) {
	            JSONObject file = updates.getJSONObject(i);
	            String date = file.optString("date");
	            String notice = file.optString("notice");
	            
	            //romView.setText(date + " " + notice);
	            //Toast.makeText(getContext(), date, Toast.LENGTH_LONG).show();
	            
	            TextView romView = (TextView) findLayoutViewById(R.id.updateitem);
	            romView.setText(date + " : " + notice);
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
