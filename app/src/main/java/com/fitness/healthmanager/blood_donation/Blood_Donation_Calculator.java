package com.fitness.healthmanager.blood_donation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdView;
import com.fitness.healthmanager.R;
import com.fitness.healthmanager.general.MyApplication;
import com.fitness.healthmanager.utils.DateUtil;
import com.fitness.healthmanager.utils.GlobalFunction;
import com.fitness.healthmanager.utils.SharedPreferenceManager;
import com.fitness.healthmanager.utils.TypefaceManager;
import com.fitness.healthmanager.shashikant.calendar.SNPCalendarView;
import com.fitness.healthmanager.shashikant.calendar.onSNPCalendarViewListener;
import com.zplesac.connectionbuddy.ConnectionBuddy;
import com.zplesac.connectionbuddy.interfaces.NetworkRequestCheckListener;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Blood_Donation_Calculator extends Activity {
    String TAG = getClass().getSimpleName();
    AdView adView;
    String eligieble_date;
    GlobalFunction globalFunction;
    ImageView iv_back;
    SNPCalendarView mFCalendarView;
    String prev_date;
    SharedPreferenceManager sharedPreferenceManager;
    String todays_date;
    TextView tv_blood_donation;
    TextView tv_date;
    TextView tv_search_date;
    TypefaceManager typefaceManager;


    public void attachBaseContext(Context context) {
        super.attachBaseContext(uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper.wrap(context));
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.blood_donation_calculator);
        this.globalFunction = new GlobalFunction(this);
        this.sharedPreferenceManager = new SharedPreferenceManager(this);
        this.typefaceManager = new TypefaceManager(getAssets(), this);
        this.globalFunction.set_locale_language();
        this.globalFunction.sendAnalyticsData(this.TAG, this.TAG);
        this.mFCalendarView = (SNPCalendarView) findViewById(R.id.mFCalendarView);
        this.tv_search_date = (TextView) findViewById(R.id.tv_search_date);
        this.tv_date = (TextView) findViewById(R.id.tv_date);
        this.tv_blood_donation = (TextView) findViewById(R.id.tv_blood_donation);
        this.iv_back = (ImageView) findViewById(R.id.iv_back);
        this.adView = (AdView) findViewById(R.id.adView);

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        this.tv_blood_donation.setTypeface(this.typefaceManager.getBold());
        this.tv_search_date.setTypeface(this.typefaceManager.getBold());
        if (VERSION.SDK_INT >= 21) {
            getWindow().addFlags(67108864);
        }
        this.tv_date.setFocusable(true);
        this.tv_date.setFocusableInTouchMode(true);
        this.tv_date.requestFocus();
        this.iv_back.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Blood_Donation_Calculator.this.onBackPressed();
            }
        });
        this.todays_date = getDateTime();
        get_eligieble_date(this.todays_date);
        this.tv_search_date.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                int random = ((int) (Math.random() * 2.0d)) + 1;
                PrintStream printStream = System.out;
                StringBuilder sb = new StringBuilder();
                sb.append("random_number==>");
                sb.append(random);
                printStream.println(sb.toString());
                if (random == 2) {
                    Blood_Donation_Calculator.this.showIntertitial();
                    return;
                }
                Intent intent = new Intent(Blood_Donation_Calculator.this, Blood_Donation_Result.class);
                intent.putExtra("prevdate", Blood_Donation_Calculator.this.prev_date);
                intent.putExtra("nextdate", Blood_Donation_Calculator.this.eligieble_date);
                intent.putExtra("flag", "0");
                Blood_Donation_Calculator.this.startActivity(intent);
            }
        });
        this.mFCalendarView.setOnCalendarViewListener(new onSNPCalendarViewListener() {
            public void onDisplayedMonthChanged(int i, int i2, String str) {
            }

            public void onDateChanged(String str) {
                String str2 = "date";
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("date->");
                    sb.append(str);
                    Log.d(str2, sb.toString());
                    Blood_Donation_Calculator.this.get_eligieble_date(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String getDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    public void get_eligieble_date(String str) {
        try {
            Date parse = new SimpleDateFormat("yyyy-MM-dd").parse(str);
            Date addDays = DateUtil.addDays(parse, 56);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd MMM yyyy");
            this.eligieble_date = simpleDateFormat.format(addDays);
            this.prev_date = simpleDateFormat2.format(parse);
            StringBuilder sb = new StringBuilder();
            sb.append("eligieble_date->");
            sb.append(this.eligieble_date);
            Log.d("eligieble_date", sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("prev_date->");
            sb2.append(this.prev_date);
            Log.d("prev_date", sb2.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        super.onBackPressed();
        this.adView.setVisibility(8);
        ActivityCompat.finishAfterTransition(this);
    }


    public void onResume() {
        super.onResume();
        if (!this.sharedPreferenceManager.get_Remove_Ad().booleanValue() && MyApplication.interstitial != null && !MyApplication.interstitial.isLoaded() && !MyApplication.interstitial.isLoading()) {
            ConnectionBuddy.getInstance().hasNetworkConnection(new NetworkRequestCheckListener() {
                public void onNoResponse() {
                }

                public void onResponseObtained() {
                    MyApplication.interstitial.loadAd(new Builder().build());
                }
            });
        }
        if (!this.sharedPreferenceManager.get_Remove_Ad().booleanValue()) {
            MyApplication.interstitial.setAdListener(new AdListener() {
                public void onAdClosed() {
                    super.onAdClosed();
                    MyApplication.interstitial.loadAd(new Builder().build());
                    Intent intent = new Intent(Blood_Donation_Calculator.this, Blood_Donation_Result.class);
                    intent.putExtra("prevdate", Blood_Donation_Calculator.this.prev_date);
                    intent.putExtra("nextdate", Blood_Donation_Calculator.this.eligieble_date);
                    intent.putExtra("flag", "0");
                    Blood_Donation_Calculator.this.startActivity(intent);
                }

                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    if (MyApplication.interstitial != null && !MyApplication.interstitial.isLoading()) {
                        ConnectionBuddy.getInstance().hasNetworkConnection(new NetworkRequestCheckListener() {
                            public void onNoResponse() {
                            }

                            public void onResponseObtained() {
                                MyApplication.interstitial.loadAd(new Builder().build());
                            }
                        });
                    }
                }
            });
        }
    }

    public void showIntertitial() {
        if (this.sharedPreferenceManager.get_Remove_Ad().booleanValue()) {
            Intent intent = new Intent(this, Blood_Donation_Result.class);
            intent.putExtra("prevdate", this.prev_date);
            intent.putExtra("nextdate", this.eligieble_date);
            intent.putExtra("flag", "0");
            startActivity(intent);
        } else if (MyApplication.interstitial == null || !MyApplication.interstitial.isLoaded()) {
            if (!MyApplication.interstitial.isLoading()) {
                ConnectionBuddy.getInstance().hasNetworkConnection(new NetworkRequestCheckListener() {
                    public void onNoResponse() {
                    }

                    public void onResponseObtained() {
                        MyApplication.interstitial.loadAd(new Builder().build());
                    }
                });
            }
            Intent intent2 = new Intent(this, Blood_Donation_Result.class);
            intent2.putExtra("prevdate", this.prev_date);
            intent2.putExtra("nextdate", this.eligieble_date);
            intent2.putExtra("flag", "0");
            startActivity(intent2);
        } else {
            MyApplication.interstitial.show();
        }
    }
}
