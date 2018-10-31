package com.example.codoon.myapplication.Algorithm;

import android.graphics.Point;
import android.support.annotation.NonNull;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 创建者：Sunday
 * 项目名：coddonGMap
 * 包名：com.example.codoon.myapplication.Algorithm
 * 文件名：LatLngPoint
 * 创建时间：2018/10/23 上午11:35
 * 描述：存储得到的坐标点，并进行抽稀算法，对坐标进行压缩
 */
public class LatLngPoint extends Point implements Comparable<LatLngPoint> {
    /**
     * 用于记录每一个点的序号
     */
    public int id;
    /**
     * 每一个点的经纬度
     */
    public LatLng latLng;

    public LatLngPoint(int id, LatLng latLng) {
        this.id = id;
        this.latLng = latLng;
    }

    @Override
    public int compareTo(@NonNull LatLngPoint o) {
        if (this.id < o.id) {
            return -1;
        } else if (this.id > o.id)
            return 1;
        return 0;
    }

    /**
     * 使用三角形面积（使用海伦公式求得）相等方法计算点pX到点pA和pB所确定的直线的距离
     * @param start  起始经纬度
     * @param end    结束经纬度
     * @param center 前两个点之间的中心点
     * @return 中心点到 start和end所在直线的距离
     */
    private double distToSegment(LatLngPoint start, LatLngPoint end, LatLngPoint center) {
        double a = Math.abs(AMapUtils.calculateLineDistance(start.latLng, end.latLng));
        double b = Math.abs(AMapUtils.calculateLineDistance(start.latLng, center.latLng));
        double c = Math.abs(AMapUtils.calculateLineDistance(end.latLng, center.latLng));
        double p = (a + b + c) / 2.0;
        double s = Math.sqrt(Math.abs(p * (p - a) * (p - b) * (p - c)));
        double d = s * 2.0 / a;
        return d;
    }

    /**
     * 道格拉斯算法
     */
    public class Douglas {
        private double dMax;
        private int start;
        private int end;
        private List<LatLngPoint> mLineInit = new ArrayList<LatLngPoint>();

        public Douglas(ArrayList<LatLng> mLineInit, double dmax) {
            if (mLineInit == null) {
                throw new IllegalArgumentException("传入的经纬度坐标list == null");
            }
            this.dMax = dmax;
            this.start = 0;
            this.end = mLineInit.size() - 1;
            for (int i = 0; i < mLineInit.size(); i++) {
                this.mLineInit.add(new LatLngPoint(i, mLineInit.get(i)));
            }
        }


        /**
         * 压缩经纬度点
         *
         * @return
         */
        public ArrayList<LatLng> compress() {
            int size = mLineInit.size();
            ArrayList<LatLngPoint> latLngPoints = compressLine(mLineInit.toArray(new LatLngPoint[size]), (ArrayList<LatLngPoint>) mLineInit, start, end, dMax);
            latLngPoints.add(mLineInit.get(0));
            latLngPoints.add(mLineInit.get(size - 1));
            //对抽稀之后的点进行排序
            Collections.sort(latLngPoints, new Comparator<LatLngPoint>() {
                @Override
                public int compare(LatLngPoint o1, LatLngPoint o2) {
                    return o1.compareTo(o2);
                }
            });
            ArrayList<LatLng> latLngs = new ArrayList<>();
            for (LatLngPoint point : latLngPoints) {
                latLngs.add(point.latLng);
            }
            return latLngs;

        }

        /**
         * 根据最大距离限制，采用DP方法递归的对原始轨迹进行采样，得到压缩后的轨迹
         * x
         *
         * @param originalLatLngs 原始经纬度坐标点数组
         * @param endLatLngs      保持过滤后的点坐标数组
         * @param start           起始下标
         * @param end             结束下标
         * @param dMax            预先指定好的最大距离误差
         */
        private ArrayList<LatLngPoint> compressLine(LatLngPoint[]
                                                            originalLatLngs, ArrayList<LatLngPoint> endLatLngs, int start, int end, double dMax) {
            if (start < end) {
                //递归进行调教筛选
                double maxDist = 0;
                int currentIndex = 0;
                for (int i = start + 1; i < end; i++) {
                    double currentDist = distToSegment(originalLatLngs[start], originalLatLngs[end], originalLatLngs[i]);
                    if (currentDist > maxDist) {
                        maxDist = currentDist;
                        currentIndex = i;
                    }
                }
                //若当前最大距离大于最大距离误差
                if (maxDist >= dMax) {
                    //将当前点加入到过滤数组中
                    endLatLngs.add(originalLatLngs[currentIndex]);
                    //将原来的线段以当前点为中心拆成两段，分别进行递归处理
                    compressLine(originalLatLngs, endLatLngs, start, currentIndex, dMax);
                    compressLine(originalLatLngs, endLatLngs, currentIndex, end, dMax);
                }
            }
            return endLatLngs;
        }
    }

}
