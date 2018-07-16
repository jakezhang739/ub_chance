package com.example.jake.chance_chain;

import android.Manifest;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.ArraySet;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;



import com.amazonaws.mobile.auth.core.IdentityHandler;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.squareup.picasso.Picasso;

import junit.framework.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;



public abstract class BaseActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    DynamoDBMapper dynamoDBMapper;
    protected BottomNavigationView navigationView;
    String us;
    private int STORAGE_PERMISSION_CODE = 10;
    private static final int GALLERY_REQUEST= 5;
    private Context context;
    TransferObserver observer;
    private TransferUtility sTransferUtility;
    AppHelper helper;
    private String uId;
    int number=0;
    ImageView myimageView,tImage;
    TextView myTextView;
    String ChanceId="asd";
    String totId="totalID";
    String vStr;
    private RecyclerView mRecyclerView;
    private GalleryAdapter mAdapter;
    private List<String> mDatasText;
    private List<String> mDatasImage;
    private String username,textTilte,textValue,txtBonus,txtBonusType,txtReward,txtRewardType;
    private List<String> picList;
    String TestChance;
    String shiit;
    public String trynum = "ui";
    public List<String> touUri;
    private List<String> uid;
    private List<Uri> uriList;
    private HomeFragment fragment = new HomeFragment();
    private FragmentTransaction fragmentTransaction;
    private int clickFlag =0;
    private int rewardtypeInt,bonusTypeInt;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        context=getApplication().getApplicationContext();
        helper = new AppHelper();
        sTransferUtility = helper.getTransferUtility(context);
        uId = helper.getCurrentUserName(context);
        dynamoDBMapper=AppHelper.getMapper(context);
        mDatasImage = new ArrayList<String>(Arrays.asList());
        mDatasText = new ArrayList<String>(Arrays.asList());
        touUri = new ArrayList<String>(Arrays.asList());
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        //fragmentTransaction.add(R.id.fragmentHome,fragment);
        //fragmentTransaction.commit();
        uriList = new ArrayList<Uri>();
        username=helper.getCurrentUserName(context);






        Log.d("uid","f"+uId);



        if(getContentViewId()==R.layout.activity_my) {

            tImage = (ImageView) findViewById(R.id.touImage);
            Button clickButton = (Button) findViewById(R.id.logoutBtn);
            Button infButton = (Button) findViewById(R.id.informationBtn);
            clickButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IdentityManager.getDefaultIdentityManager().signOut();
                    Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            infButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intentInf = new Intent(BaseActivity.this, InformationActivity.class);
                    startActivity(intentInf);
                }
            });

            TextView userTxt = (TextView) findViewById(R.id.userName);
            userTxt.setText(AppHelper.getCurrentUserName(context));
            us = AppHelper.getCurrentUserName(context);
            Log.d("username","www"+us);
            try{
                Picasso.get().load("https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/"+us+".png").resize(60,60).centerCrop().into(tImage);

            }
            catch (Exception e){

            }




        }


        else if(getContentViewId()==R.layout.activity_notification){
            ImageView picView = (ImageView) findViewById(R.id.getPic);
            EditText titleText = (EditText) findViewById(R.id.titletext);
            EditText Neirong = (EditText) findViewById(R.id.neirong);
            EditText reWard = (EditText) findViewById(R.id.jiaovalue);
            EditText bonus = (EditText) findViewById(R.id.zhuivalue);

            TextView cic1 = (TextView) findViewById(R.id.circleText1);
            TextView cic2 = (TextView) findViewById(R.id.circleText2);
            TextView cic3 = (TextView) findViewById(R.id.circleText3);
            TextView cic4 = (TextView) findViewById(R.id.circleText4);

            Button fabuBtn = (Button) findViewById(R.id.fabubtn);


            cic1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                   cic1.setBackground(ContextCompat.getDrawable(context,R.drawable.yeallow_cic));
                   cic2.setBackground(ContextCompat.getDrawable(context,R.drawable.transparent_circle));
                   cic3.setBackground(ContextCompat.getDrawable(context,R.drawable.transparent_circle));
                   cic4.setBackground(ContextCompat.getDrawable(context,R.drawable.transparent_circle));
                   cic1.setTextColor(getColor(R.color.black));
                   cic2.setTextColor(getColor(R.color.white));
                   cic3.setTextColor(getColor(R.color.white));
                   cic4.setTextColor(getColor(R.color.white));
                   clickFlag=1;

                }
            });

            cic2.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    cic1.setBackground(ContextCompat.getDrawable(context,R.drawable.transparent_circle));
                    cic2.setBackground(ContextCompat.getDrawable(context,R.drawable.yeallow_cic));
                    cic3.setBackground(ContextCompat.getDrawable(context,R.drawable.transparent_circle));
                    cic4.setBackground(ContextCompat.getDrawable(context,R.drawable.transparent_circle));
                    cic1.setTextColor(getColor(R.color.white));
                    cic2.setTextColor(getColor(R.color.black));
                    cic3.setTextColor(getColor(R.color.white));
                    cic4.setTextColor(getColor(R.color.white));
                    clickFlag=2;

                }
            });

            cic3.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    cic1.setBackground(ContextCompat.getDrawable(context,R.drawable.transparent_circle));
                    cic2.setBackground(ContextCompat.getDrawable(context,R.drawable.transparent_circle));
                    cic3.setBackground(ContextCompat.getDrawable(context,R.drawable.yeallow_cic));
                    cic4.setBackground(ContextCompat.getDrawable(context,R.drawable.transparent_circle));
                    cic1.setTextColor(getColor(R.color.white));
                    cic2.setTextColor(getColor(R.color.white));
                    cic3.setTextColor(getColor(R.color.black));
                    cic4.setTextColor(getColor(R.color.white));
                    clickFlag=3;

                }
            });

            cic4.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    cic1.setBackground(ContextCompat.getDrawable(context,R.drawable.transparent_circle));
                    cic2.setBackground(ContextCompat.getDrawable(context,R.drawable.transparent_circle));
                    cic3.setBackground(ContextCompat.getDrawable(context,R.drawable.transparent_circle));
                    cic4.setBackground(ContextCompat.getDrawable(context,R.drawable.yeallow_cic));
                    cic1.setTextColor(getColor(R.color.white));
                    cic2.setTextColor(getColor(R.color.white));
                    cic3.setTextColor(getColor(R.color.white));
                    cic4.setTextColor(getColor(R.color.black));
                    clickFlag=4;

                }
            });

            Date currentTime = Calendar.getInstance().getTime();
            long yo = currentTime.getTime();
            String dateString = DateFormat.format("yyyyMMddHHmmss", new Date(yo)).toString();

            Log.d("time ", "tr " + currentTime.toString()+ " sd " + dateString+ " " + (double) currentTime.getTime());
            Spinner bi1 = (Spinner) findViewById(R.id.bizhong);
            Spinner bi2 = (Spinner) findViewById(R.id.bizhong2);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.currency_name, R.layout.item_select);

            adapter.setDropDownViewResource(R.layout.drop_down_item);

            bi1.setAdapter(adapter);
            bi2.setAdapter(adapter);
            bi1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //选择列表项的操作
                    parent.getItemAtPosition(position);
                    rewardtypeInt = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    //未选中时候的操作
                    rewardtypeInt = 0;
                }
            });

            bi2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //选择列表项的操作
                    parent.getItemAtPosition(position);
                    bonusTypeInt=position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    //未选中时候的操作
                    bonusTypeInt=0;

                }
            });


            picView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Log.d("typetry"," rew "+ rewardtypeInt+" bonus " + bonusTypeInt);

                    requstStoragePermission();
                    FishBun.with(BaseActivity.this).setImageAdapter(new GlideAdapter()).startAlbum();
                }
            });

            fabuBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(bonus.getText().length()!=0) {
                        txtBonus = bonus.getText().toString();
                    }
                    else{
                        txtBonus="0";
                    }
                    switch (bonusTypeInt){
                        case 0: txtBonusType="cc";break;
                        case 1: txtBonusType="eth";break;
                        case 2: txtBonusType="btc";break;
                    }
                    if(reWard.getText().length()!=0) {
                        txtReward = reWard.getText().toString();
                    }
                    else{
                        txtReward="0";
                    }
                    switch (rewardtypeInt){
                        case 0:txtRewardType = "cc";break;
                        case 1:txtRewardType="eth";break;
                        case 2:txtRewardType="btc";break;
                    }

                    if(titleText.length()==0){
                        Log.d("wtftt"," rew "+ reWard+" bonus " + bonus);
                        Toast.makeText(context,"请输入标题",Toast.LENGTH_LONG).show();
                    }
                    else if(Neirong.length()==0){
                        Toast.makeText(context,"请输入内容",Toast.LENGTH_LONG).show();
                    }
                    else if(clickFlag==0){
                        Toast.makeText(context,"请选择标签",Toast.LENGTH_LONG).show();
                    }
                    else {
                        textTilte = titleText.getText().toString();
                        textValue = Neirong.getText().toString();
                        new Thread(uploadRunnable).start();
                        Toast.makeText(context,"已上传发布",Toast.LENGTH_LONG).show();
                    }


                }
            });





        }
        else if(getContentViewId() == R.layout.activity_home) {
            


            Log.d("loading screen ","check if loading screen");
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(R.layout.actionbar);
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            myThread mThread = new myThread(this,dynamoDBMapper,fragmentTransaction,fragment);
            mThread.start();


        }



    }

    public int getNum(int num){
        number=num;
        return num;
    }



    @Override
    protected void onStart() {
        super.onStart();
        updateNavigationBarState();
    }



    public void setChance(String value,String chance,String num,String totNum){
//        final UserChanceDO userC = new UserChanceDO();
//        final PostsDO postsDO = new PostsDO();
//        postsDO.setPostId(totNum);
//        postsDO.setUsername(helper.getCurrentUserName(context));
//        postsDO.setText(value);
//
//        userC.setNumid(num);
//        userC.setUserid(uId);
//        userC.setValue(value);
//        userC.setChanceid(chance);
//        Log.d("chanceid","cc"+chance);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                dynamoDBMapper.save(userC);
//                dynamoDBMapper.save(postsDO);
//                // Item saved
//            }
//        }).start();
    }

    Runnable uploadRunnable = new Runnable() {
        @Override
        public void run() {
            int cSize = helper.returnChanceeSize(dynamoDBMapper)+1;
            final ChanceWithValueDO chanceWithValueDO = new ChanceWithValueDO();
            Set<String> pictureSet = new ArraySet<>();
            for(int i=0;i<uriList.size();i++){
                try {
                String path = AppHelper.getPath(uriList.get(i),context);
                File file = new File(path);
                Log.d("uyu",""+ChanceId);
                observer =
                        sTransferUtility.upload(helper.BUCKET_NAME,String.valueOf(cSize)+"_"+String.valueOf(i)+".png",file);
                observer.setTransferListener(new TransferListener() {
                    @Override
                    public void onError(int id, Exception e) {
                        Log.e("onError", "Error during upload: " + id, e);
                    }

                    @Override
                    public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                        Log.d("onProgress", String.format("onProgressChanged: %d, total: %d, current: %d",
                                id, bytesTotal, bytesCurrent));
                    }

                    @Override
                    public void onStateChanged(int id, TransferState newState) {
                        Log.d("onState", "onStateChanged: " + id + ", " + newState);
                    }
                });
                pictureSet.add("https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/"+String.valueOf(cSize)+"_"+String.valueOf(i)+".png");
                //beginUpload(path);
                Log.d("gooodshit", "upload "+String.valueOf(cSize)+"_"+String.valueOf(i)+".png");
            } catch (URISyntaxException e) {
                Log.d("fck2", "Unable to upload file from the given uri", e);
            }
            }
            Log.d("letsee ", " "+txtReward);
            if(pictureSet.size()!=0) {
                chanceWithValueDO.setPictures(pictureSet);
            }
            chanceWithValueDO.setUsername(username);
            chanceWithValueDO.setProfilePicture("https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/"+username+".png");
            chanceWithValueDO.setId(String.valueOf(cSize));
            chanceWithValueDO.setReward(Double.parseDouble(txtReward));
            chanceWithValueDO.setRewardType(txtRewardType);
            chanceWithValueDO.setBonus(Double.parseDouble(txtBonus));
            chanceWithValueDO.setBonusType(txtBonusType);
            chanceWithValueDO.setTag((double)clickFlag);
            chanceWithValueDO.setTitle(textTilte);
            chanceWithValueDO.setText(textValue);
            Date currentTime = Calendar.getInstance().getTime();
            String dateString = DateFormat.format("yyyyMMddHHmmss", new Date(currentTime.getTime())).toString();
            chanceWithValueDO.setTime(Double.parseDouble(dateString));
            dynamoDBMapper.save(chanceWithValueDO);

        }
    };





    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("actrrr", "n" + ChanceId);

        Log.d("uri","size "+uriList.size());
        Log.d("get code","reque" + requestCode + " resu " + resultCode);

        if(requestCode == Define.ALBUM_REQUEST_CODE){

            uriList=data.getParcelableArrayListExtra(Define.INTENT_PATH);


        }
    }

    // Remove inter-activity transition to avoid screen tossing on tapping bottom navigation items
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        navigationView.postDelayed(() -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, HomeActivity.class));
            } else if (itemId == R.id.navigation_dashboard) {
                startActivity(new Intent(this, MyActivity.class));
            } else if (itemId == R.id.navigation_notifications) {
                startActivity(new Intent(this, NotificationActivity.class));
            }
            finish();
        }, 300);
        return true;
    }

    private void updateNavigationBarState(){
        int actionId = getNavigationMenuItemId();
        selectBottomNavigationBarItem(actionId);
    }

    private File loadimg(){
        String key = "download.png";
        File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + key);
        observer =
                sTransferUtility.download(helper.BUCKET_NAME,
                        key,
                        file);
        Log.d("observer","ob "+observer.toString());

        // Attach a listener to the observer to get state update and progress notifications
        observer.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    // Handle a completed upload.
                    Log.d("motherfucker","yoyoyshit");
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float)bytesCurrent/(float)bytesTotal) * 100;
                int percentDone = (int)percentDonef;

                Log.d("MainActivity", "   ID:" + id + "   bytesCurrent: " + bytesCurrent + "   bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                // Handle errors
                Log.d("fucker2","yoyoyshit");
            }

        });

        // If you prefer to poll for the data, instead of attaching a
        // listener, check for the state and progress in the observer.
        if (TransferState.COMPLETED == observer.getState()) {
            // Handle a completed upload.
        }

        Log.d("YourActivity", "Bytes Transferrred: " + observer.getBytesTransferred());
        Log.d("YourActivity", "Bytes Total: " + observer.getBytesTotal());
        return file;
    }


    void selectBottomNavigationBarItem(int itemId) {
        MenuItem item = navigationView.getMenu().findItem(itemId);
        item.setChecked(true);
    }

    private void requstStoragePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(BaseActivity.this,
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    public void setTry(List<String> mDatasImage,List<String> mDatasText, List<String> tImg, String n){
        this.mDatasText=mDatasText;
        this.mDatasImage=mDatasImage;
        this.touUri = tImg;
        this.trynum=n;
    }

    public void setFragment( chanceClass cc, FragmentTransaction ft){

        fragment.setClass(cc);
        fragmentTransaction.replace(R.id.fragmentHome,fragment);
        ft.commit();
    }

    abstract int getContentViewId();

    abstract int getNavigationMenuItemId();


}
