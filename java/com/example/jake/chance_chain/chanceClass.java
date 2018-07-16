package com.example.jake.chance_chain;

import android.util.ArraySet;

import java.util.List;
import java.util.Set;

public class chanceClass {

    public Set<String> imageSet = new ArraySet<String>();
    public Set<String> commentSet = new ArraySet<String>();
    public double bonus;
    public double reward,uploadTime,tag;
    public String touUri, bType,rType,userid,txtTitle,txtNeirong,cId,liked,shared;




    public chanceClass(String touUri, String bType,String rType,String userid,String txtTitle,String txtNeirong,String cId, double bonus, double reward,  double tag, double uploadTime){
        this.touUri = touUri;
        this.bType = bType;
        this.rType=rType;
        this.userid = userid;
        this.txtTitle = txtTitle;
        this.txtNeirong = txtNeirong;
        this.cId = cId;
        this.bonus = bonus;
        this.reward = reward;
        this.uploadTime = uploadTime;
        this.tag = tag;
    }

    public void setPicture(Set<String> imgSet){
        this.imageSet=imgSet;
    }
}
