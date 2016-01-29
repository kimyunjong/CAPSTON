package com.dropboxapi;

/**
 * Created by Joel on 2016-01-23.
 */
public class DeviceScreenIfo {




    public void getDeviceInfo (String deviceinfo)
    {
        switch (deviceinfo)
        {
            case "galaxyS4":case  "gPro": case "g2": case "iphone6p":case "galaxyS5": case "iphone6sp":case "note3":
            GlobalVar.projectImgH=1920;
            GlobalVar.projectImgW=1080;
            break;
            case "iphone6": case "iphone6s":
            GlobalVar.projectImgH=1334;
            GlobalVar.projectImgW=750;
            break;
            case "galaxyS6":case  "note4": case "note5": case "g3":case "g4":
            GlobalVar.projectImgH=2560;
            GlobalVar.projectImgW=1440;
            break;
            case "galaxyS3":case "galaxyS2HD":case "note2":
            GlobalVar.projectImgH=1280;
            GlobalVar.projectImgW=720;
            break;
            case  "TEST":
                GlobalVar.projectImgH=316;
                GlobalVar.projectImgW=357;
                break;

            case "iphone5":
            GlobalVar.projectImgH=1136;
            GlobalVar.projectImgW=640;
            break;
            default:
                GlobalVar.projectImgH=1920;
                GlobalVar.projectImgW=1080;
            break;
        }
    }
}
