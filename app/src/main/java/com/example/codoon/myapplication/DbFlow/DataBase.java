package com.example.codoon.myapplication.DbFlow;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * 创建者：Sunday
 * 项目名：coddonGMap
 * 包名：com.example.codoon.myapplication.DBflow
 * 文件名：DataBase
 * 创建时间：2018/10/11 下午5:37
 * 描述：采用DBflow数据库
 */
@Database(name=DataBase.NAME,version = DataBase.VERSION)
public class DataBase {
    //数据库名称
    public static final String NAME = "MyDataBase";
    //数据库版本号
    public static final int VERSION = 1;

}
