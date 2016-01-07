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
    static ArrayList<String> getImgfileList()
    {
        return  imgfileList;
    }
    static ArrayList<String> getActivityConversionTime()
    {
        return  activityConversionTime;
    }


}
