package com.dropboxapi;

/**
 * Created by Joel on 2016-01-29.
 */
public class XMLItem {
    String task="kimyunjong";
    String imgName="";
    String xInImage="100";
    String yInImage="100";
    String eventTye="Touch";
    String timeEntire="";
    String timeImg="";


    public String getTask(){        return task;}
    public String getImgName(){        return imgName;    }
    public String getxInImage(){        return xInImage;}
    public String getyInImage(){        return yInImage;}
    public  String getEventTye(){        return eventTye;    }
    public String getTimeEntire(){        return timeEntire;    }
    public  String getTimeImg(){        return timeImg;    }




    public void setTimeEntire(String TimeEntire){
        timeEntire=TimeEntire;
    }
    public void setTimeImg(String TimeImg)
    {
        timeImg=TimeImg;
    }

    public void setTask(String Task){
        task=Task;
    }
    public void setxInImage(String x)
    {
        xInImage=x;
    }
    public void setyInImage(String y)
    {
        yInImage=y;
    }
    public void setImgName(String imageName)    {imgName=imageName;}
    public void setEventType(String eventTyep)
    {
        eventTye=eventTyep;
    }

}
