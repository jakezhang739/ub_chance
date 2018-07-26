package com.example.jake.chance_chain;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class sharingActivity extends AppCompatActivity {
    Button shareBtn;
    TextView cancelText;
    chanceClass chC;
    TextView sUser,sNei;
    ImageView sUimg;
    EditText shuru;
    AppHelper helper = new AppHelper();
    DynamoDBMapper dynamoDBMapper;
    String userId,fengTxt;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing);
        chC = (chanceClass) getIntent().getParcelableExtra("link");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.sharingtopbar);
        cancelText = (TextView) actionBar.getCustomView().findViewById(R.id.Cancel);
        shareBtn = (Button) actionBar.getCustomView().findViewById(R.id.sharingFa);
        context=getApplication().getApplicationContext();
        dynamoDBMapper=helper.getMapper(context);
        userId = helper.getCurrentUserName(context);
        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(sharingActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
        sUser = (TextView) findViewById(R.id.UserNameAt);
        sNei = (TextView) findViewById(R.id.titleTxt);
        sUimg = (ImageView) findViewById(R.id.fenTou);
        shuru = (EditText) findViewById(R.id.fenXiangShare);
        RelativeLayout sharRel = (RelativeLayout) findViewById(R.id.shareLink);
        if(!chC.touUri.isEmpty()){
            Picasso.get().load(chC.touUri).into(sUimg);
        }
        sNei.setText(chC.txtTitle);
        sUser.setText("@"+chC.userid);

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fengTxt=shuru.getText().toString();
                new Thread(sharingRunnable).start();
                Intent intent = new Intent(sharingActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

    }

    Runnable sharingRunnable = new Runnable() {
        @Override
        public void run() {
            int cSize = helper.returnChanceeSize(dynamoDBMapper)+1;
            final ChanceWithValueDO chanceWithValueDO = new ChanceWithValueDO();
            UserPoolDO userPoolDO = dynamoDBMapper.load(UserPoolDO.class,userId);
            if(userPoolDO.getProfilePic()!=null){
                chanceWithValueDO.setProfilePicture(userPoolDO.getProfilePic());
            }
            chanceWithValueDO.setUsername(userId);
            chanceWithValueDO.setId(String.valueOf(cSize));
            List<String> shareLink = new ArrayList<>();
            shareLink.add(chC.cId);
            shareLink.add("@"+chC.userid);
            shareLink.add(chC.txtTitle);
            if(!chC.touUri.isEmpty()){
            shareLink.add(chC.touUri);}
            chanceWithValueDO.setSharedFrom(shareLink);
            chanceWithValueDO.setReward(0.0);
            chanceWithValueDO.setRewardType("cc");
            chanceWithValueDO.setBonus(0.0);
            chanceWithValueDO.setBonusType("cc");
            chanceWithValueDO.setTag(1.0);
            chanceWithValueDO.setText("s");
            Date currentTime = Calendar.getInstance().getTime();
            String dateString = DateFormat.format("yyyyMMddHHmmss", new Date(currentTime.getTime())).toString();
            chanceWithValueDO.setTime(Double.parseDouble(dateString));
            chanceWithValueDO.setTitle(fengTxt);
            dynamoDBMapper.save(chanceWithValueDO);
        }
    };
}
