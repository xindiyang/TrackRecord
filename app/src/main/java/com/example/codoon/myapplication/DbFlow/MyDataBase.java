package com.example.codoon.myapplication.DbFlow;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * 创建者：Sunday
 * 项目名：coddonGMap
 * 包名：com.example.codoon.myapplication.DbFlow
 * 文件名：MyDataBase
 * 创建时间：2018/10/14 下午6:27
 * 描述：运动记录📝 ID、time、distance、duration。
 */

@Table(database = DataBase.class)
public class MyDataBase extends BaseModel {
    @PrimaryKey(autoincrement = true)
    public int id=0;//ID

    public double distance;//距离
    @Column
    public String time;//时间

    @Column
    public long duration;//持续时间


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }


}
