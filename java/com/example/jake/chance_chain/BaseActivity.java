package com.example.jake.chance_chain;

import android.Manifest;
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
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

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
    String TestChance;
    String shiit;
    public String trynum = "ui";
    public List<String> touUri;
    private HomeFragment fragment = new HomeFragment();
    private FragmentTransaction fragmentTransaction;





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






        Log.d("uid","f"+uId);

        Log.d("number","f"+number);


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
            Button picButton = (Button) findViewById(R.id.picBtn);
            Button pubButton = (Button) findViewById(R.id.publicBtn);
            EditText vText = (EditText) findViewById(R.id.neirong);

            picButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    getChanceID();
                    Log.d("uuiiu",""+ChanceId);
                    requstStoragePermission();
                    Intent imageIntent = new Intent();
                    imageIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                    //imageIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    // imageIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    imageIntent.setType("image/*");
                    startActivityForResult(imageIntent.createChooser(imageIntent,"选取图片"),GALLERY_REQUEST);
                }
            });

            pubButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    //number=1;
                    vStr = vText.getText().toString();
                    setStuff(vStr);

                    Log.d("number2","value"+ChanceId);

                }
            });

        }
        else if(getContentViewId() == R.layout.activity_home) {

            Log.d("loading screen ","check if loading screen");
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            myThread mThread = new myThread(this,dynamoDBMapper,mDatasText,mDatasImage,touUri,fragmentTransaction,fragment);
            mThread.start();

            //fragmentTransaction = getSupportFragmentManager().beginTransaction();
            //fragmentTransaction.add(R.id.fragmentHome,fragment);
            //fragmentTransaction.commit();
            //myThread mThread = new myThread(this,context,dynamoDBMapper,mRecyclerView,mAdapter,mDatasText,mDatasImage,touUri,fragmentTransaction,fragment);
            //mThread.start();



            //File file = loadimg();
            //Log.d("good","e"+bmp.toString());
            //myTextView=(TextView) findViewById(R.id.dummy);

            //getstuff();
            //initDatas();
            //得到控件
            //Intent intent = new Intent(BaseActivity.this,Load.class);
            //startActivity(intent);
            /*RefreshLayout refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
            refreshLayout.setRefreshHeader(new ClassicsHeader(this).setSpinnerStyle(SpinnerStyle.FixedBehind));
            refreshLayout.setRefreshFooter(new ClassicsFooter(this).setSpinnerStyle(SpinnerStyle.Scale));

            while(trynum!="oo"){
                Log.d("yoyouuu",""+trynum);


            }
            ft.commit();
/*
            Log.d("yoyouuu",""+trynum);
            Log.d("trynum","nummm"+mDatasImage.size());
            mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview_horizontal);
            //设置布局管理器
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            //设置适配器
            mAdapter = new GalleryAdapter(this, mDatasImage,mDatasText,touUri);
            mRecyclerView.setAdapter(mAdapter);
            refreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(RefreshLayout refreshlayout) {
                    refreshlayout.finishRefresh(2000);//传入false表示刷新失败
                }
            });

            refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    refreshLayout.finishLoadMore(2000);
                }
            });*/

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


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            String str;
            if(msg.obj!=null){
                str=msg.obj.toString();
            }
            else{
                str=null;
            }
            int index = msg.what;
            /*if(index == 1){
                //myTextView.setText(str);
                //mDatasText.add(str);
                shiit = str;
            }*/
            if(index == 2){
                Log.d("chance","value"+String.valueOf(str));
                String str1 = "https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/"+str+".png";
                TestChance = str1;
                //mDatasImage.add(TestChance);
                Log.d("str22","value"+str1);
                //Picasso.get().load(str1).into(myimageView);
            }
            else if(index == 3){
                //num=num-1;
                ChanceId=str;
                Log.d("str2332","value"+ChanceId);
            }
        }
    };

    private void initDatas()
    {
        getstuff();
    }

    private void getChanceID(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("uid","f"+uId);
                UserPoolDO userPoolDO = dynamoDBMapper.load(UserPoolDO.class,uId);
                TotalChanceDO totalChanceDO = dynamoDBMapper.load(TotalChanceDO.class,"totalID");


                if(userPoolDO != null) {
                    Log.d("wtf", "sdf3" + userPoolDO.toString());
                    Log.d("wtf", "career" + userPoolDO.getChanceId());
                    String str = totalChanceDO.getTotC();
                    Log.d("wtf2", "career" + str);
                    if(str!=null) {
                        number = Integer.parseInt(str);
                        number++;
                        Log.d("wtf3", "n" + number);
                        Message msg=Message.obtain();
                        msg.what=3;
                        msg.obj=number;
                        handler.sendMessage(msg);


                    }
                    totalChanceDO.setTotC(String.valueOf(number));
                    dynamoDBMapper.save(totalChanceDO);
                    //userPoolDO.setNumofChance(String.valueOf(number));
                }

            }
        }).start();

    }

    private void setStuff(String value){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("uid","f"+uId);
                UserPoolDO userPoolDO = dynamoDBMapper.load(UserPoolDO.class,uId);
                TotalChanceDO totalChanceDO = dynamoDBMapper.load(TotalChanceDO.class,totId);
                String totNum = totalChanceDO.getTotC();


                if(userPoolDO != null) {
                    Log.d("wtf", "sdf3" + userPoolDO.toString());
                    Log.d("wtf", "career" + userPoolDO.getChanceId());
                    String str = userPoolDO.getNumofChance();
                    Log.d("wtf2", "career" + str);
                    if(str!=null) {
                        number = Integer.parseInt(str);
                        //number++;
                        Log.d("wtf3", "n" + str);

                    }
                }
                //number++;
                String chanceID = uId + String.valueOf(number);
                setChance(value,chanceID,String.valueOf(number),totNum);
            }
        }).start();

    }

    private void getstuff(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("uid","f"+uId);
                UserPoolDO userPoolDO = dynamoDBMapper.load(UserPoolDO.class,uId);
                Message msgText = Message.obtain();
                Message msgChance = Message.obtain();
                msgText.what=1;
                msgChance.what=2;


                if(userPoolDO != null) {
                    Log.d("wtf", "sdf3" + userPoolDO.toString());
                    Log.d("wtf", "career" + userPoolDO.getChanceId());
                    Log.d("wtf55", "career" + userPoolDO.getNumofChance());
                    Log.d("wtf55", "career" + userPoolDO.getNumofChance());
                    if(userPoolDO.getNumofChance()!=null) {
                        number = Integer.parseInt(userPoolDO.getNumofChance());

                    }Log.d("wtf55", "career" + uId+String.valueOf(number));
                    try {
                        String chanceid=uId + String.valueOf(number);
                        UserChanceDO userChanceDO = dynamoDBMapper.load(UserChanceDO.class,chanceid,uId);
                        if (userChanceDO != null) {
                            Log.d("tutu", "sdf3" + userChanceDO.toString());
                            Log.d("ert", "career" + userChanceDO.getChanceid());
                            Log.d("ertwe", "career" + userChanceDO.getValue());
                            msgText.obj=userChanceDO.getValue();
                            msgChance.obj=userChanceDO.getChanceid();
                            handler.sendMessage(msgText);
                            handler.sendMessage(msgChance);
                        }
                    }
                    catch (Exception e){
                        Log.d("erre", "career" + e+uId+String.valueOf(number));
                    }
                }
                //number++;
            }
        }).start();
    }

    public void setChance(String value,String chance,String num,String totNum){
        final UserChanceDO userC = new UserChanceDO();
        final ChanceWithValueDO chanceWithValueDO = new ChanceWithValueDO();
        chanceWithValueDO.setId(totNum);
        chanceWithValueDO.setUser(helper.getCurrentUserName(context));
        chanceWithValueDO.setValue(value);

        userC.setNumid(num);
        userC.setUserid(uId);
        userC.setValue(value);
        userC.setChanceid(chance);
        Log.d("chanceid","cc"+chance);
        new Thread(new Runnable() {
            @Override
            public void run() {
                dynamoDBMapper.save(userC);
                dynamoDBMapper.save(chanceWithValueDO);
                // Item saved
            }
        }).start();
    }





    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("actrrr", "n" + ChanceId);
        if (ChanceId == "asd"){
            ChanceId="1";
        }
        Uri galUri;
        String path;;
        if(requestCode == GALLERY_REQUEST){
            galUri=data.getData();
            try {
                path = AppHelper.getPath(galUri,context);
                File file = new File(path);
                Log.d("uyu",""+ChanceId);
                observer =
                        sTransferUtility.upload(helper.BUCKET_NAME,ChanceId+".png",file);
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
                //beginUpload(path);
                Log.d("gooodshit", "upload"+file.getName());
            } catch (URISyntaxException e) {
                Toast.makeText(this,
                        "Unable to get the file from the given URI.  See error log for details",
                        Toast.LENGTH_LONG).show();
                Log.d("fck2", "Unable to upload file from the given uri", e);
            }

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

    public void setFragment( Bundle bundle, FragmentTransaction ft){

        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragmentHome,fragment);
        ft.commit();
    }

    abstract int getContentViewId();

    abstract int getNavigationMenuItemId();


}
