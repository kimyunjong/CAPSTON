package com.dropboxapi;

/**
 * Created by Joel on 2016-01-06.
 */
public class Stopwatch {
    long start;
    long end;

    void start(){
        start=System.currentTimeMillis();
    }

    void stop(){
        end=System.currentTimeMillis();
    }
    String getRunTime(){
        return (end-start)/1000.000+"";
    }
}
