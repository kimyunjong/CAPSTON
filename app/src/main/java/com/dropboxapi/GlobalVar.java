package com.dropboxapi;

import java.util.ArrayList;

/**
 * Created by Joel on 2016-01-04.
 */
public class GlobalVar {

    static Boolean downloadDone=true;
    static int curFileIdx=0;
    private  static ArrayList<String> imgfileList =new ArrayList<>();
    private  static ArrayList<String> activityConversionTime =new ArrayList<>();
    private  static ArrayList<String> xmlDataList =new ArrayList<>();
    private  static ArrayList<LinkInfo> linkInfoList =new ArrayList<>();
    private  static ArrayList<XMLItem> logItemList=new ArrayList<>();
    static  String deviceInfo;
    static String curTask="NOTHING";
    static String curImg="";
    static float screen_width,screen_height,projectImgW,projectImgH ;
    static ArrayList<XMLItem> getLogItemList(){return  logItemList;}
    static ArrayList<String> getImgfileList()
    {
        return  imgfileList;
    }
    static ArrayList<LinkInfo> getLinkInfoList(){return linkInfoList;}
    static ArrayList<String> getActivityConversionTime()
    {
        return  activityConversionTime;
    }
    static ArrayList<String> getXmlDataList(){
        return  xmlDataList;
    }


}
