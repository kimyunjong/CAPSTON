package com.dropboxapi;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Chronometer;
import android.widget.TextView;

import java.io.File;
import java.io.FilenameFilter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Joel on 2016-01-05.
 */
public class LoadFiles extends Activity {
    static final float projectImgW = 600;
    static final float projectImgH = 900;
    String[] fileList;
    Resources res;
    Stopwatch stopwatch;
    int countTest = 0;
    Bitmap map_bitmap;
    BitmapDrawable map_drawable;
    CanvasView canvasView;
    private Paint mPaint;
    float screen_width, screen_height;
    int touch_x=10;
    int touch_y=10;
    int count=0;
    public static String path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/AAA_DB/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        canvasView = new CanvasView(this);
        setContentView(canvasView);
        Resources res = getResources();
        ArrayList<Integer> arrayList=new ArrayList<>();
        arrayList.add(0, R.drawable.main_pic);
        fileList=getTitleList();
        stopwatch=new Stopwatch();

        DisplayMetrics metrics = new DisplayMetrics();
        //get actual window size
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screen_width = metrics.widthPixels;
        screen_height = metrics.heightPixels;
        loadLocalImage(fileList);
        /*
        map_drawable = (BitmapDrawable) res.getDrawable(R.drawable.back_img);
        int heightSize = (int) (screen_width * projectImgH / projectImgW);
        map_bitmap = Bitmap.createScaledBitmap(map_drawable.getBitmap(), (int) screen_width, heightSize, true);
        */
        stopwatch.start();
    }


    class CanvasView extends View {

        public CanvasView(Context context) {
            super(context);
        }

        public void onDraw(Canvas canvas) {
            canvas.drawColor(Color.BLACK);
            mPaint.setColor(Color.RED);
            canvas.drawBitmap(map_bitmap, 0, 0, null);
            canvas.drawCircle(touch_x, touch_y, 40, mPaint);

        }

        public boolean onTouchEvent(MotionEvent event) {
            int action = event.getAction();
            touch_x = (int) event.getX();
            touch_y = (int) event.getY();

            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    count++;
                    break;
                case MotionEvent.ACTION_UP:
                    invalidate();

                    Random rand=new Random();
                    countTest= rand.nextInt(1000)%4;
                    loadLocalImage(fileList);
                    stopwatch.stop();
                    System.out.println(stopwatch.getRunTime());
                    GlobalVar.getActivityConversionTime().add(stopwatch.getRunTime());
                    break;
            }
            return true;
        }
    }
    //스크린에 이미지를 띄어주기
    public void loadLocalImage(String[] fileList){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize=1;
        Bitmap src=BitmapFactory.decodeFile(path+fileList[countTest],options);
        int heightSize = (int) (screen_width * projectImgH / projectImgW);
        map_bitmap = Bitmap.createScaledBitmap(src, (int) screen_width, heightSize, true);

    }
    //로컬저장소에서 이미지 이름 리스트 불러오기
    private String[] getTitleList()
    {
        try
        {
            FilenameFilter fileFilter = new FilenameFilter()  //이부분은 특정 확장자만 가지고 오고 싶을 경우 사용하시면 됩니다.
            {
                public boolean accept(File dir, String name)
                {
                    return name.endsWith("png"); //이 부분에 사용하고 싶은 확장자를 넣으시면 됩니다.
                } //end accept
            };
            File file = new File(path);
            File[] files = file.listFiles(fileFilter);//위에 만들어 두신 필터를 넣으세요. 만약 필요치 않으시면 fileFilter를 지우세요.
            String [] titleList = new String [files.length]; //파일이 있는 만큼 어레이 생성했구요
            for(int i = 0;i < files.length;i++)
            {
                titleList[i] = files[i].getName();	//루프로 돌면서 어레이에 하나씩 집어 넣습니다.
            }//end for
            return titleList;
        } catch( Exception e )
        {
            return null;
        }//end catch()
    }//end getTitleList
}
