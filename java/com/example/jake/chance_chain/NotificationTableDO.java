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

@DynamoDBTable(tableName = "chance-mobilehub-653619147-notificationTable")

public class NotificationTableDO {
    private String _receiver;
    private String _sender;
    private Double _unReadNum;

    @DynamoDBHashKey(attributeName = "Receiver")
    @DynamoDBAttribute(attributeName = "Receiver")
    public String getReceiver() {
        return _receiver;
    }

    public void setReceiver(final String _receiver) {
        this._receiver = _receiver;
    }
    @DynamoDBRangeKey(attributeName = "Sender")
    @DynamoDBAttribute(attributeName = "Sender")
    public String getSender() {
        return _sender;
    }

    public void setSender(final String _sender) {
        this._sender = _sender;
    }
    @DynamoDBAttribute(attributeName = "unReadNum")
    public Double getUnReadNum() {
        return _unReadNum;
    }

    public void setUnReadNum(final Double _unReadNum) {
        this._unReadNum = _unReadNum;
    }

}
