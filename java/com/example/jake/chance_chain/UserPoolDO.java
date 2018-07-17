package com.example.jake.chance_chain;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "chance-mobilehub-653619147-UserPool")

public class UserPoolDO {
    private String _userId;
    private String _career;
    private String _chanceId;
    private String _gender;
    private String _name;
    private String _nickName;
    private String _resume;
    private String _walletAddress;
    private String _cryptoCurrency;
    private String _numofChance;
    private String _profilePic;

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBAttribute(attributeName = "Career")
    public String getCareer() {
        return _career;
    }

    public void setCareer(final String _career) {
        this._career = _career;
    }
    @DynamoDBAttribute(attributeName = "ChanceId")
    public String getChanceId() {
        return _chanceId;
    }

    public void setChanceId(final String _chanceId) {
        this._chanceId = _chanceId;
    }
    @DynamoDBAttribute(attributeName = "Gender")
    public String getGender() {
        return _gender;
    }

    public void setGender(final String _gender) {
        this._gender = _gender;
    }
    @DynamoDBAttribute(attributeName = "Name")
    public String getName() {
        return _name;
    }

    public void setName(final String _name) {
        this._name = _name;
    }
    @DynamoDBAttribute(attributeName = "NickName")
    public String getNickName() {
        return _nickName;
    }

    public void setNickName(final String _nickName) {
        this._nickName = _nickName;
    }
    @DynamoDBAttribute(attributeName = "Resume")
    public String getResume() {
        return _resume;
    }

    public void setResume(final String _resume) {
        this._resume = _resume;
    }
    @DynamoDBAttribute(attributeName = "WalletAddress")
    public String getWalletAddress() {
        return _walletAddress;
    }

    public void setWalletAddress(final String _walletAddress) {
        this._walletAddress = _walletAddress;
    }
    @DynamoDBAttribute(attributeName = "cryptoCurrency")
    public String getCryptoCurrency() {
        return _cryptoCurrency;
    }

    public void setCryptoCurrency(final String _cryptoCurrency) {
        this._cryptoCurrency = _cryptoCurrency;
    }
    @DynamoDBAttribute(attributeName = "numofChance")
    public String getNumofChance() {
        return _numofChance;
    }

    public void setNumofChance(final String _numofChance) {
        this._numofChance = _numofChance;
    }
    @DynamoDBAttribute(attributeName = "profilePic")
    public String getProfilePic() {
        return _profilePic;
    }

    public void setProfilePic(final String _profilePic) {
        this._profilePic = _profilePic;
    }

}