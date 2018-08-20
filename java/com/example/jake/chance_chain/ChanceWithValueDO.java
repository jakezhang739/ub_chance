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

@DynamoDBTable(tableName = "chance-mobilehub-653619147-ChanceWithValue")

public class ChanceWithValueDO {
    private String _id;
    private Double _bonus;
    private String _bonusType;
    private List<String> _commentIdList;
    private List<String> _completeList;
    private List<String> _confirmList;
    private List<String> _getList;
    private List<String> _liked;
    private List<String> _pictures;
    private String _profilePicture;
    private Double _reward;
    private String _rewardType;
    private Double _shared;
    private List<String> _sharedFrom;
    private Double _tag;
    private String _text;
    private Double _time;
    private String _title;
    private List<String> _unConfirmList;
    private String _username;

    @DynamoDBHashKey(attributeName = "Id")
    @DynamoDBAttribute(attributeName = "Id")
    public String getId() {
        return _id;
    }

    public void setId(final String _id) {
        this._id = _id;
    }
    @DynamoDBAttribute(attributeName = "bonus")
    public Double getBonus() {
        return _bonus;
    }

    public void setBonus(final Double _bonus) {
        this._bonus = _bonus;
    }
    @DynamoDBAttribute(attributeName = "bonus_type")
    public String getBonusType() {
        return _bonusType;
    }

    public void setBonusType(final String _bonusType) {
        this._bonusType = _bonusType;
    }
    @DynamoDBAttribute(attributeName = "commentIdList")
    public List<String> getCommentIdList() {
        return _commentIdList;
    }

    public void setCommentIdList(final List<String> _commentIdList) {
        this._commentIdList = _commentIdList;
    }
    @DynamoDBAttribute(attributeName = "completeList")
    public List<String> getCompleteList() {
        return _completeList;
    }

    public void setCompleteList(final List<String> _completeList) {
        this._completeList = _completeList;
    }
    @DynamoDBAttribute(attributeName = "confirmList")
    public List<String> getConfirmList() {
        return _confirmList;
    }

    public void setConfirmList(final List<String> _confirmList) {
        this._confirmList = _confirmList;
    }
    @DynamoDBAttribute(attributeName = "getList")
    public List<String> getGetList() {
        return _getList;
    }

    public void setGetList(final List<String> _getList) {
        this._getList = _getList;
    }
    @DynamoDBAttribute(attributeName = "liked")
    public List<String> getLiked() {
        return _liked;
    }

    public void setLiked(final List<String> _liked) {
        this._liked = _liked;
    }
    @DynamoDBAttribute(attributeName = "pictures")
    public List<String> getPictures() {
        return _pictures;
    }

    public void setPictures(final List<String> _pictures) {
        this._pictures = _pictures;
    }
    @DynamoDBAttribute(attributeName = "profile_picture")
    public String getProfilePicture() {
        return _profilePicture;
    }

    public void setProfilePicture(final String _profilePicture) {
        this._profilePicture = _profilePicture;
    }
    @DynamoDBAttribute(attributeName = "reward")
    public Double getReward() {
        return _reward;
    }

    public void setReward(final Double _reward) {
        this._reward = _reward;
    }
    @DynamoDBAttribute(attributeName = "reward_type")
    public String getRewardType() {
        return _rewardType;
    }

    public void setRewardType(final String _rewardType) {
        this._rewardType = _rewardType;
    }
    @DynamoDBAttribute(attributeName = "shared")
    public Double getShared() {
        return _shared;
    }

    public void setShared(final Double _shared) {
        this._shared = _shared;
    }
    @DynamoDBAttribute(attributeName = "sharedFrom")
    public List<String> getSharedFrom() {
        return _sharedFrom;
    }

    public void setSharedFrom(final List<String> _sharedFrom) {
        this._sharedFrom = _sharedFrom;
    }
    @DynamoDBAttribute(attributeName = "tag")
    public Double getTag() {
        return _tag;
    }

    public void setTag(final Double _tag) {
        this._tag = _tag;
    }
    @DynamoDBAttribute(attributeName = "text")
    public String getText() {
        return _text;
    }

    public void setText(final String _text) {
        this._text = _text;
    }
    @DynamoDBAttribute(attributeName = "time")
    public Double getTime() {
        return _time;
    }

    public void setTime(final Double _time) {
        this._time = _time;
    }
    @DynamoDBAttribute(attributeName = "title")
    public String getTitle() {
        return _title;
    }

    public void setTitle(final String _title) {
        this._title = _title;
    }
    @DynamoDBAttribute(attributeName = "unConfirmList")
    public List<String> getUnConfirmList() {
        return _unConfirmList;
    }

    public void setUnConfirmList(final List<String> _unConfirmList) {
        this._unConfirmList = _unConfirmList;
    }
    @DynamoDBAttribute(attributeName = "username")
    public String getUsername() {
        return _username;
    }

    public void setUsername(final String _username) {
        this._username = _username;
    }

}
