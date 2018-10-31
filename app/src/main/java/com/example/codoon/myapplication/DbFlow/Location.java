package com.example.codoon.myapplication.DbFlow;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * 创建者：Sunday
 * 项目名：coddonGMap
 * 包名：com.example.codoon.myapplication.DbFlow
 * 文件名：Location
 * 创建时间：2018/10/23 下午4:44
 * 描述：TODO
 */
@Table(database = DataBase.class)
public class Location extends BaseModel {
    @PrimaryKey(autoincrement = true)
    public int LocationId=0;//ID
    @Column
    public double longitutd;//经度
    @Column
    public double latitude;//纬度

    public int getLocationId() {
        return LocationId;
    }

    public void setLocationId(int locationId) {
        LocationId = locationId;
    }

    public double getLongitutd() {
        return longitutd;
    }

    public void setLongitutd(double longitutd) {
        this.longitutd = longitutd;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
