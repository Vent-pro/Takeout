package com.ventapp.takeout.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.ventapp.takeout.R;
import com.ventapp.takeout.adapter.SearchLocationResultAdapter;
import com.ventapp.takeout.gson.LiteAddress;
import com.ventapp.takeout.util.HttpUtility;
import com.ventapp.takeout.util.Utility;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class SearchLocationActivity extends AppCompatActivity {

    private static String TAG="SearchLocationTAG";

    private LiteAddress liteAddress;

    @Bind(R.id.text_search)EditText searchText;
    @Bind(R.id.text_curr_city)TextView currCityText;
    @Bind(R.id.search_location_result)RecyclerView resultRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);
        ButterKnife.bind(this);
        Intent intent=getIntent();
        liteAddress=intent.getParcelableExtra("address_data");
        initLayout();
    }

    private void initLayout(){
        currCityText.setText(liteAddress.getCity());
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i==KeyEvent.KEYCODE_ENTER){
                    return true;
                }else if(i== EditorInfo.IME_ACTION_SEARCH){
                    searchStart();
                }
                return false;
            }
        });
        showSoftInput();
    }
    private void showSoftInput(){
        searchText.setFocusable(true);
        searchText.setFocusableInTouchMode(true);
        searchText.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
    private void searchStart(){
        String query=searchText.getText().toString();
        String httpAddres="http://api.map.baidu.com/place/v2/search" +
                "?query=" + query +
                "&location=" + liteAddress.getLocation().getLat() + "," + liteAddress.getLocation().getLng() +
                "&radius=20000" +
                "&radius_limit=true" +
                "&output=json" +
                "&scope=2" +
                "&filter=sort_name:distance|sort_rule:1" +
                "&page_size=20" +
                "&ak=" + Utility.WEB_AK;
        Log.d(TAG, "searchStart: httpAddress: "+httpAddres);
        HttpUtility.sendOkHttpRequest(httpAddres, new HttpUtility.CallBack() {
            @Override
            public void onResponse(Call call,final Response response) throws IOException {
                runOnUiThread(new Thread(){
                    @Override
                    public void run() {
                        try{
                            String responseString=response.body().string();
                            List<LiteAddress> liteAddressList=Utility.handleSearchLocationReponse(responseString);
                            resultRecyclerView.setLayoutManager(new LinearLayoutManager(SearchLocationActivity.this){
                                @Override
                                public boolean canScrollVertically() {
                                    return false;
                                }
                            });
                            SearchLocationResultAdapter resultAdapter=new SearchLocationResultAdapter(liteAddressList);
                            resultRecyclerView.setAdapter(resultAdapter);
                        }catch(IOException e){
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
        InputMethodManager manager = ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE));
        if (manager != null)
            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void actionStart(Activity activity, LiteAddress liteAddress){
        Intent intent=new Intent(activity,SearchLocationActivity.class);
        intent.putExtra("address_data",liteAddress);
        activity.startActivity(intent);
    }

    @OnClick({R.id.button_back,R.id.button_cancel})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.button_back:
            case R.id.button_cancel:
                finish();
                break;
            default:
                break;
        }
    }
}
