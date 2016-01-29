package com.dropboxapi;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.transform.stream.StreamSource;

/**
 * Created by Joel on 2016-01-07.
 */
public class GetStream {

    public GetStream() {

    }

    public InputStream GetStreamFromXmlFileOnSDCard(String xml_path, String fileName) {
        File file = new File(xml_path + fileName);
        InputStream istr = null;

        try {
            istr = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return istr;
    }

    public void XmlParsing(InputStream fis) {
        DeviceScreenIfo deviceScreenInfo = new DeviceScreenIfo();
        ArrayList<String> items = new ArrayList<String>();
        try {

            XmlPullParserFactory xppf = XmlPullParserFactory.newInstance();
            xppf.setNamespaceAware(true);
            XmlPullParser xpp = xppf.newPullParser();
            xpp.setInput(fis, null);

            LinkInfo pLinkinfo = new LinkInfo();
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {


                if (xpp.next() == XmlPullParser.START_TAG) {
                    System.out.println("pLEASEPLEAS HELP ME   : ");
                    System.out.println(xpp.getName());
                    if(xpp.getName().contains("DeviceInfo")){
                        if(xpp.next() == XmlPullParser.TEXT) {
                            System.out.println("디바이스인포 : " + xpp.getText()); //여기 xpp.getText()가 "iPhone5" 불러옴
                            deviceScreenInfo.getDeviceInfo(xpp.getText());
                        }
                    }
                    //End Deviceinfo

                    else if(xpp.getName().compareToIgnoreCase("Link") == 0){
                        System.out.println("LinkAttribute : " + xpp.getAttributeValue(null, "fileName"));
                        GlobalVar.getImgfileList().add(xpp.getAttributeValue(null, "fileName"));
                    }

                    else if(xpp.getName().compareToIgnoreCase("InputType") == 0){
                        if(xpp.next() == XmlPullParser.TEXT) {
                            System.out.println("eventType : " + xpp.getText());
                            pLinkinfo.eventType = xpp.getText();
                        }
                    }
                    else if(xpp.getName().compareToIgnoreCase("StartFile") == 0){
                        if(xpp.next() == XmlPullParser.TEXT) {
                            System.out.println("StartFile : " + xpp.getText());
                            pLinkinfo.startImg = xpp.getText();
                        }
                    }

                    else if(xpp.getName().compareToIgnoreCase("LinkX") == 0){
                        if(xpp.next() == XmlPullParser.TEXT) {
                            System.out.println("LinkX : " + xpp.getText());
                            pLinkinfo.x_col = Integer.valueOf(xpp.getText());
                        }
                    }

                    else if(xpp.getName().compareToIgnoreCase("LinkY") == 0){
                        if(xpp.next() == XmlPullParser.TEXT) {
                            System.out.println("LinkY : " + xpp.getText());
                            pLinkinfo.y_col = Integer.valueOf(xpp.getText());
                        }
                    }

                    else if(xpp.getName().compareToIgnoreCase("LinkWidth") == 0){
                        if(xpp.next() == XmlPullParser.TEXT) {
                            System.out.println("LinkWidth : " + xpp.getText());
                            pLinkinfo.width = Integer.valueOf(xpp.getText());
                        }
                    }

                    else if(xpp.getName().compareToIgnoreCase("LinkHeight") == 0){
                        if(xpp.next() == XmlPullParser.TEXT) {
                            System.out.println("LinkHeight : " + xpp.getText());
                            pLinkinfo.height = Integer.valueOf(xpp.getText());
                        }
                    }
                    else if(xpp.getName().compareToIgnoreCase("StartFile") == 0){
                        if(xpp.next() == XmlPullParser.TEXT) {
                            System.out.println("LinkHeight : " + xpp.getText());
                            pLinkinfo.startImg = xpp.getText();
                        }
                    }
                    //DstFile 정보가 입력될 때 마지막으로 간주하고 어레이리스트에 add하기
                    else if(xpp.getName().compareToIgnoreCase("DstFile") == 0){
                        if(xpp.next() == XmlPullParser.TEXT) {
                            System.out.println("DstFile : " + xpp.getText());
                            pLinkinfo.destImg = xpp.getText();


                            //touch 정보가 아니면 x,y,width,height 모두 -1로 초기화 하고 add
                            if(!pLinkinfo.eventType.contains("Touch")){
                                pLinkinfo.x_col = -1;
                                pLinkinfo.y_col = -1;
                                pLinkinfo.width = -1;
                                pLinkinfo.height = -1;
                            }
                            GlobalVar.getLinkInfoList().add(pLinkinfo);
                            System.out.println("입력된 정보 : " + pLinkinfo.eventType + ", "+ pLinkinfo.startImg + ", "+ pLinkinfo.destImg + ", "+ pLinkinfo.x_col + ", "+ pLinkinfo.y_col);
                            pLinkinfo = new LinkInfo();
                        }
                    }
                }
            }
        }
        catch (Throwable t) {

        }

        for(int i = 0; i < GlobalVar.getImgfileList().size(); i++){
            System.out.println("image : " + GlobalVar.getImgfileList().get(i));
        }

        for(int i = 0; i < GlobalVar.getLinkInfoList().size(); i++){
            System.out.println("InputType : " + GlobalVar.getLinkInfoList().get(i).eventType);
            System.out.println("StartFile : " + GlobalVar.getLinkInfoList().get(i).startImg);
            System.out.println("DstFile : " + GlobalVar.getLinkInfoList().get(i).destImg);
            System.out.println("x : " + GlobalVar.getLinkInfoList().get(i).x_col);
            System.out.println("y : " + GlobalVar.getLinkInfoList().get(i).y_col);
            System.out.println("width : " + GlobalVar.getLinkInfoList().get(i).width);
            System.out.println("height : " + GlobalVar.getLinkInfoList().get(i).height);
        }


    }
}


//        for (int i = 0; i < items.size(); i++)                            //이후에 시나리오까지 받아올 경우 사용하기로.
//            System.out.println("check xml result" + items.get(i));




