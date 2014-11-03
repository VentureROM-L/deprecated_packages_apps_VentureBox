package com.venturerom.venture;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.venturerom.venture.ota.IOUtils;
import com.venturerom.venture.ota.Utils.NotificationInfo;
import com.venturerom.venture.ota.activities.SettingsActivity;
import com.venturerom.venture.ota.cards.DownloadCard;
import com.venturerom.venture.ota.cards.InstallCard;
import com.venturerom.venture.ota.cards.SystemCard;
import com.venturerom.venture.ota.cards.UpdatesCard;
import com.venturerom.venture.ota.helpers.DownloadHelper;
import com.venturerom.venture.ota.helpers.DownloadHelper.DownloadCallback;
import com.venturerom.venture.ota.helpers.RebootHelper;
import com.venturerom.venture.ota.helpers.RecoveryHelper;
import com.venturerom.venture.ota.updater.GappsUpdater;
import com.venturerom.venture.ota.updater.RomUpdater;
import com.venturerom.venture.ota.updater.Updater;
import com.venturerom.venture.ota.updater.Updater.UpdaterListener;
import com.venturerom.venture.widget.Card;
import com.venturerom.venture.ota.Utils;

public class MainActivity extends Activity implements UpdaterListener, DownloadCallback,
OnItemClickListener, Response.Listener<JSONObject>, Response.ErrorListener {
	
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    
    private RecoveryHelper mRecoveryHelper;
    private RebootHelper mRebootHelper;
    private DownloadCallback mDownloadCallback;

    private SystemCard mSystemCard;
    private UpdatesCard mUpdatesCard;
    private DownloadCard mDownloadCard;
    private InstallCard mInstallCard;
    private ChangelogCard mChangelogCard;
    private ConfigKernelCard mConfigKernelCard;
    private ConfigHaloCard mConfigHaloCard;
    private ConfigCardViewCard mConfigCardViewCard;
    private ConfigWifiNotiCard mConfigWifiNotiCard;
    
    private static final String STATE = "STATE";
    
    public static final int STATE_HOME = 0;
    public static final int STATE_CHANGELOG = 1;
    public static final int STATE_CONFIG = 2;
    public static final int STATE_UPDATES = 3;
    public static final int STATE_DOWNLOAD = 4;
    public static final int STATE_INSTALL = 5;
    
    private RomUpdater mRomUpdater;
    private GappsUpdater mGappsUpdater;
    private NotificationInfo mNotificationInfo;

    private LinearLayout mCardsLayout;
    private TextView mTitle;

    private Context mContext;
    private Bundle mSavedInstanceState;
    private RequestQueue mQueue;

    private int mState = STATE_HOME;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mContext = this;
        mSavedInstanceState = savedInstanceState;
        mQueue = Volley.newRequestQueue(this);

        setContentView(R.layout.activity_main);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        Resources res = getResources();
        List<String> itemText = new ArrayList<String>();
        itemText.add(res.getString(R.string.home));
        itemText.add(res.getString(R.string.config));
        itemText.add(res.getString(R.string.updates));
        itemText.add(res.getString(R.string.install));
        itemText.add(res.getString(R.string.settings));

        final Drawable[] icons = new Drawable[] {
                null, null, null, null, res.getDrawable(R.drawable.ic_settings)
        };

        mCardsLayout = (LinearLayout) findViewById(R.id.cards_layout);
        mTitle = (TextView) findViewById(R.id.header);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, itemText) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LinearLayout itemView;
                String item = getItem(position);

                if (convertView == null) {
                    itemView = new LinearLayout(getContext());
                    LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
                    vi.inflate(R.layout.drawer_list_item, itemView, true);
                } else {
                    itemView = (LinearLayout) convertView;
                }

                View itemSmall = itemView.findViewById(R.id.item_small);
                TextView text = (TextView) itemView.findViewById(R.id.text);
                TextView textSmall = (TextView) itemView.findViewById(R.id.text_small);
                ImageView icon = (ImageView) itemView.findViewById(R.id.icon);
                if ((position == 0 && mState == STATE_HOME)
                        || (position == 1 && mState == STATE_CHANGELOG)) {
                    SpannableString spanString = new SpannableString(item);
                    spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
                    text.setText(spanString);
                    textSmall.setText(spanString);
                } else {
                    text.setText(item);
                    textSmall.setText(item);
                }
                if (icons[position] != null) {
                    icon.setImageDrawable(icons[position]);
                    text.setVisibility(View.GONE);
                    itemSmall.setVisibility(View.VISIBLE);
                } else {
                    text.setVisibility(View.VISIBLE);
                    itemSmall.setVisibility(View.GONE);
                }
                return itemView;
            }
        });
        mDrawerList.setOnItemClickListener(this);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {

            @Override
            public void onDrawerStateChanged(int newState) {
                Utils.setRobotoThin(mContext, mDrawerLayout);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        Utils.setRobotoThin(mContext, findViewById(R.id.mainlayout));
        
        mRecoveryHelper = new RecoveryHelper(this);
        mRebootHelper = new RebootHelper(mRecoveryHelper);

        mRomUpdater = new RomUpdater(this, false);
        mRomUpdater.addUpdaterListener(this);
        mGappsUpdater = new GappsUpdater(this, false);
        mGappsUpdater.addUpdaterListener(this);

        DownloadHelper.init(this, this);

        Intent intent = getIntent();
        onNewIntent(intent);

        if (mSavedInstanceState == null) {

            IOUtils.init(this);

            mCardsLayout.setAnimation(AnimationUtils.loadAnimation(this, R.anim.up_from_bottom));

            if (mNotificationInfo != null) {
                if (mNotificationInfo.mNotificationId != Updater.NOTIFICATION_ID) {
                    checkUpdates();
                } else {
                    mRomUpdater.setLastUpdates(mNotificationInfo.mPackageInfosRom);
                    mGappsUpdater.setLastUpdates(mNotificationInfo.mPackageInfosGapps);
                }
            } else {
                checkUpdates();
            }
            if (DownloadHelper.isDownloading(true) || DownloadHelper.isDownloading(false)) {
                setState(STATE_DOWNLOAD, true, false);
            } else {
                if (mState != STATE_INSTALL) {
                    setState(STATE_HOME, true, false);
                }
            }
        } else {
            setState(mSavedInstanceState.getInt(STATE), false, true);
        }

        if (!Utils.alarmExists(this, true)) {
            Utils.setAlarm(this, true, true);
        }

        if (!Utils.alarmExists(this, false)) {
            Utils.setAlarm(this, true, false);
        }
        
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if(extras.getBoolean("openUpdates")){
            	setState(STATE_UPDATES);
            }
        }
        
        if(isPackageInstalled("de.robv.android.xposed.installer", MainActivity.this)){
        	SharedPreferences prefs = getPreferences(MODE_PRIVATE); 
        	Boolean restoredText = prefs.getBoolean("xposedWarned", false);
        	if (!restoredText) 
        	{
        		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        		alertDialogBuilder.setTitle(R.string.xposedwarningTitle);
        	    alertDialogBuilder.setMessage(R.string.xposedwarningMessage);
        	    alertDialogBuilder.setPositiveButton(R.string.positive_button, 
        	      new DialogInterface.OnClickListener() {
        			
        	         @Override
        	         public void onClick(DialogInterface arg0, int arg1) {
        	        	 SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        	        	 editor.putBoolean("xposedWarned", true);
        	        	 editor.apply();
        	         }
        	      });
        	      AlertDialog alertDialog = alertDialogBuilder.create();
        	      alertDialog.show();
        	}
        }
	}
	
	public void setDownloadCallback(DownloadCallback downloadCallback) {
        mDownloadCallback = downloadCallback;
    }
	
	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE, mState);
        switch (mState) {
            case STATE_HOME:
                //mNoticesCard.saveState(outState);
                break;
            case STATE_CHANGELOG:
                //mChangelogCard.saveState(outState);
                break;
            case STATE_CONFIG:
                mConfigKernelCard.saveState(outState);
                mConfigHaloCard.saveState(outState);
                mConfigCardViewCard.saveState(outState);
                mConfigWifiNotiCard.saveState(outState);
                break;
            case STATE_UPDATES:
                mSystemCard.saveState(outState);
                mUpdatesCard.saveState(outState);
                break;
            case STATE_DOWNLOAD:
                mDownloadCard.saveState(outState);
                break;
            case STATE_INSTALL:
                mInstallCard.saveState(outState);
                break;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
    public void checkUpdates() {
        mRomUpdater.check();
        mGappsUpdater.check();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                if (mState == STATE_HOME) {
                    break;
                }
                setState(STATE_HOME, true, false);
                invalidateOptionsMenu();
                break;
            case 1:
                if (mState == STATE_CONFIG) {
                    break;
                }
                setState(STATE_CONFIG, true, false);
                invalidateOptionsMenu();
                break;
            case 2:
                if (mState == STATE_UPDATES || mState == STATE_DOWNLOAD) {
                    break;
                }
                setState(STATE_UPDATES, true, false);
                invalidateOptionsMenu();
                break;
            case 3:
                if (mState == STATE_INSTALL) {
                    break;
                }
                setState(STATE_INSTALL, true, false);
                invalidateOptionsMenu();
                break;
            case 4:
            	invalidateOptionsMenu();
                Intent intent = new Intent(this, SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        mNotificationInfo = null;
        if (intent != null && intent.getExtras() != null) {
            mNotificationInfo = (NotificationInfo) intent.getSerializableExtra(Utils.FILES_INFO);
            if (intent.getBooleanExtra(Utils.CHECK_DOWNLOADS_FINISHED, false)) {
                DownloadHelper.checkDownloadFinished(this,
                        intent.getLongExtra(Utils.CHECK_DOWNLOADS_ID, -1L));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        DownloadHelper.registerCallback(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DownloadHelper.unregisterCallback();
    }

    @Override
    public void startChecking(boolean isRom) {
        setProgressBarIndeterminate(true);
        setProgressBarVisibility(true);
    }

    @Override
    public void checkError(String cause, boolean isRom) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
        case R.id.action_refresh:
        	JsonObjectRequest jsObjRequest;
        	jsObjRequest = new JsonObjectRequest(Request.Method.GET, "http://api.venturerom.com/notices/", null, this, this);
            mQueue.add(jsObjRequest);
            return true;
    	}
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setState(int state) {
        setState(state, false, false);
    }

    public void setState(int state, boolean animate, boolean fromRotation) {
        setState(state, animate, null, null, null, false, fromRotation);
    }

    public void setState(int state, boolean animate, Updater.PackageInfo[] infos,
            Uri uri, String md5, boolean isRom, boolean fromRotation) {
        mState = state;
        JsonObjectRequest jsObjRequest;
        switch (state) {
            case STATE_HOME:
            	//Add card now so it does not look broken
            	addCards(new Card[] {
						new NoticesCard(mContext, null, mSavedInstanceState, "low", "No Date", "Empty", true)
                }, true, true);
            	jsObjRequest = new JsonObjectRequest(Request.Method.GET, "http://api.venturerom.com/notices/", null, this, this);
                mQueue.add(jsObjRequest);
                //The cards are actually add in onResponse()
                break;
            case STATE_CHANGELOG:
            	jsObjRequest = new JsonObjectRequest(Request.Method.GET, "http://api.venturerom.com/changelog/", null, this, this);
                mQueue.add(jsObjRequest);
            	/*
            	if (mChangelogCard == null) {
                	mChangelogCard = new ChangelogCard(mContext, null,mSavedInstanceState);
                }
                */
                addCards(new Card[] {
                		new ChangelogCard(mContext, null,mSavedInstanceState, "October 2 2014", "- Change 1"),
                		new ChangelogCard(mContext, null,mSavedInstanceState, "October 1 2014", "- Change 2"),
                		new ChangelogCard(mContext, null,mSavedInstanceState, "October 0 2014", "- Change 3")
                }, animate, true);
                break;
            case STATE_CONFIG:
            	if (mConfigKernelCard == null) {
                    mConfigKernelCard = new ConfigKernelCard(mContext, null,mSavedInstanceState);
            	}
            	if (mConfigHaloCard == null) {
                    mConfigHaloCard = new ConfigHaloCard(mContext, null,mSavedInstanceState);
            	}
            	if (mConfigCardViewCard == null) {
                    mConfigCardViewCard = new ConfigCardViewCard(mContext, null,mSavedInstanceState);
            	}
            	if (mConfigWifiNotiCard == null) {
                    mConfigWifiNotiCard = new ConfigWifiNotiCard(mContext, null,mSavedInstanceState);
            	}
            	addCards(new Card[] {
                        mConfigKernelCard, mConfigHaloCard, mConfigCardViewCard, mConfigWifiNotiCard
                }, animate, true);
            	break;
            case STATE_UPDATES:
                if (mSystemCard == null) {
                    mSystemCard = new SystemCard(mContext, null, mRomUpdater, mGappsUpdater,
                            mSavedInstanceState);
                }
                if (mUpdatesCard == null) {
                    mUpdatesCard = new UpdatesCard(mContext, null, mRomUpdater, mGappsUpdater,
                            mSavedInstanceState);
                }
                addCards(new Card[] {
                        mSystemCard, mUpdatesCard
                }, animate, true);
                break;
            case STATE_DOWNLOAD:
                if (mDownloadCard == null) {
                    mDownloadCard = new DownloadCard(mContext, null, infos, mSavedInstanceState);
                } else {
                    mDownloadCard.setInitialInfos(infos);
                }
                addCards(new Card[] {
                        mDownloadCard
                }, animate, true);
                break;
            case STATE_INSTALL:
                if (mInstallCard == null) {
                    mInstallCard = new InstallCard(mContext, null, mRebootHelper,
                            mSavedInstanceState);
                }
                if (!DownloadHelper.isDownloading(!isRom)) {
                    addCards(new Card[] {
                            mInstallCard
                    }, !fromRotation, true);
                } else {
                    addCards(new Card[] {
                            mInstallCard
                    }, true, false);
                }
                if (uri != null) {
                    mInstallCard.addFile(uri, md5);
                }
                break;
        }
        ((ArrayAdapter<String>) mDrawerList.getAdapter()).notifyDataSetChanged();
        updateTitle();
    }

    public void addCards(Card[] cards, boolean animate, boolean remove) {
        mCardsLayout.clearAnimation();
        if (remove) {
            mCardsLayout.removeAllViews();
        }
        if (animate) {
            mCardsLayout.setAnimation(AnimationUtils.loadAnimation(this, R.anim.up_from_bottom));
        }
        for (Card card : cards) {
            mCardsLayout.addView(card);
        }
    }
    
    @Override
    public void onDownloadStarted() {
        if (mDownloadCallback != null) {
            mDownloadCallback.onDownloadStarted();
        }
    }

    @Override
    public void onDownloadError(String reason) {
        if (mDownloadCallback != null) {
            mDownloadCallback.onDownloadError(reason);
        }
    }

    @Override
    public void onDownloadProgress(int progress) {
        if (mDownloadCallback != null) {
            mDownloadCallback.onDownloadProgress(progress);
        }
    }

    @Override
    public void onDownloadFinished(Uri uri, final String md5, boolean isRom) {
        if (mDownloadCallback != null) {
            mDownloadCallback.onDownloadFinished(uri, md5, isRom);
        }
        if (uri == null) {
            if (!DownloadHelper.isDownloading(!isRom)) {
                setState(STATE_UPDATES, true, false);
            }
        } else {
            setState(STATE_INSTALL, true, null, uri, md5, isRom, false);
        }
    }

    private void updateTitle() {
        switch (mState) {
            case STATE_HOME:
                mTitle.setText(R.string.home);
                break;
            case STATE_CHANGELOG:
                mTitle.setText(R.string.changelog);
                break;
	   case STATE_CONFIG:
                mTitle.setText(R.string.config);
                break;
            case STATE_UPDATES:
                mTitle.setText(R.string.updates);
                break;
            case STATE_INSTALL:
                mTitle.setText(R.string.install);
                break;
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if(mState == STATE_HOME){
			getMenuInflater().inflate(R.menu.notice, menu);
		}else{
			getMenuInflater().inflate(R.menu.main, menu);
		}
		return true;
	}

	@Override
	public void versionFound(com.venturerom.venture.ota.updater.Updater.PackageInfo[] info, boolean isRom) {
	}
	
	@Override
    public void onResponse(JSONObject response) {
		if(response.has("notices")){
			JSONArray updates;
			String aPriority[];
			String aDate[];
			String aNotice[];
			try {
				int notices = Integer.parseInt(response.getString("notices"));
				int count = 0;
				aPriority = new String[notices];
				aDate = new String[notices];
				aNotice = new String[notices];
				if(notices != 0){
					updates = response.getJSONArray("data");
					for (int i = updates.length() - 1; i >= 0; i--) {
			            JSONObject file = updates.getJSONObject(i);
			            String date = file.optString("date");
			            String notice = file.optString("notice");
			            String priority = file.optString("priority");
			            aPriority[count] = priority;
			            aDate[count] = date;
			            aNotice[count] = notice;
			            count++;
			        }
					if(mState == STATE_HOME){
						Card[] cards = new Card[notices];
						for(int i = 0; i < notices; i++){
							cards[i] = new NoticesCard(mContext, null, mSavedInstanceState, aPriority[i], aDate[i], aNotice[i], false);
						}
						addCards(cards, true, true);
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(NumberFormatException nfe){
				nfe.printStackTrace();
			}
		}
		else if(response.has("changelog")){
			//Add changelog code here
		}
		
        
	}

	@Override
	public void onErrorResponse(VolleyError error) {

	}
	
	private boolean isPackageInstalled(String packagename, Context context) {
	    PackageManager pm = context.getPackageManager();
	    try {
	        pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
	        return true;
	    } catch (NameNotFoundException e) {
	        return false;
	    }
	}
}

