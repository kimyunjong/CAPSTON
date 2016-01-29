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
import android.view.MotionEvent;
import android.view.View;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Joel on 2016-01-05.
 */
public class LoadFiles extends Activity {

    GetStream getStream;
    String[] fileList;
    Resources res;
    StopwatchEntire stopwatchEntire;
    DeviceScreenIfo deviceScreenIfo;
    int countTest = 0;
    Bitmap map_bitmap;
    BitmapDrawable map_drawable;
    CanvasView canvasView;
    private Paint mPaint;
    float proportion;
    int touch_x = 10;
    int touch_y = 10;
    int count = 0;
    float x1 = 0, x2, y1 = 0, y2, dx, dy, oldx = 0, oldy = 0;
    boolean standardwidth = true;
    String eventType="";
    Boolean touchFlag= false;
    int nextImageIndex =0;
    public static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AAA_DB/";
    ArrayList<String> items = new ArrayList<String>();

    String[] XmlList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        canvasView = new CanvasView(this);
        setContentView(canvasView);
        Resources res = getResources();
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(0, R.drawable.main_pic);
        fileList = getTitleList(path, "png");
        System.out.println("number ???check" + fileList.length);
        XmlList = getTitleList(path, "xml");
        stopwatchEntire = new StopwatchEntire();
        getStream = new GetStream();
        deviceScreenIfo = new DeviceScreenIfo();

        InputStream fis = getStream.GetStreamFromXmlFileOnSDCard(path, XmlList[0]);
        getStream.XmlParsing(fis);

        //get window size
        proportion = GlobalVar.screen_width / GlobalVar.projectImgW;
        if (proportion * GlobalVar.projectImgH > GlobalVar.screen_height)
        {
            System.out.println("세로비율 더 길다. 예외처리");
            standardwidth = false;
            proportion = GlobalVar.screen_height / GlobalVar.projectImgH;
            System.out.println("비율 스크린 확인1"+proportion);
        }

        //나중에 시나리오가 있으면 시나리오를 가져와서 첫번째 이미지를 띄워주자, 첫번쨰 이미지의 인덱스를 알아서 주기.
        int firstImgIndex = 0;
        loadLocalImage(fileList, firstImgIndex, true);
        stopwatchEntire.start();
    }


    class CanvasView extends View {

        public CanvasView(Context context) {
            super(context);
        }

        public void onDraw(Canvas canvas) {
            canvas.drawColor(Color.BLACK);
            mPaint.setColor(Color.RED);
            canvas.drawBitmap(map_bitmap, 0, 0, null);
            //canvas.drawCircle(0, 0, 40, mPaint);

            int count = 0;
            float xInList = 0;
            float yInList = 0;
            float widthInList = 0;
            float heightInList = 0;
            boolean findFlag = false;
            /*
            while (count < GlobalVar.getLinkInfoList().size()) {    // 나중에 비율을 곱해줘야함, 스크린 크기에따라.
                if ("10-06.png".equals(GlobalVar.getLinkInfoList().get(count).startImg)) {


                    xInList = GlobalVar.getLinkInfoList().get(count).x_col*proportion;
                    yInList = GlobalVar.getLinkInfoList().get(count).y_col*proportion;
                    widthInList = GlobalVar.getLinkInfoList().get(count).width*proportion ;
                    heightInList = GlobalVar.getLinkInfoList().get(count).height*proportion;

                    canvas.drawRect(xInList, yInList, xInList + widthInList, yInList + heightInList, mPaint);
                    count++;
                } else {
                    count++;
                }
            }
            */

        }

        public boolean onTouchEvent(MotionEvent event) {
            int action = event.getAction();

            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    //count++;
                    oldx = event.getX();
                    oldy = event.getY();
                    System.out.println("touch check down" + oldx + "  " + oldy);

                    break;
                case MotionEvent.ACTION_UP:
                    x2 = event.getX();
                    y2 = event.getY();
                    dx = x2 - oldx;
                    dy = y2 - oldy;
                    //이벤트 종류
                    touchFlag = true;
                    if (Math.abs(dx) < 100 && Math.abs(dy) < 100) {
                        eventType = "touch";
                    } else {
                        if (Math.abs(dx) > Math.abs(dy)) {
                            if (dx > 0) {
                                eventType = "rightSwipe";

                            } else {
                                eventType = "leftSwipe";

                            }
                        } else {
                            if (dy > 0) {
                                eventType = "downSwipe";


                            } else {
                                eventType = "upSwipe";

                            }
                        }
                    }
                    //해당 이벤트 종류를 리스트에서 가져오기&&이벤트 처리
                    if (eventType.equals("touch")) {
                        System.out.println("touch check5");

                        //touch 일때 curimg를 global 에서 가져옴.
                        int result = compareTouchXY(GlobalVar.curImg, (int) x2, (int) y2);
                        if (result == -1) {
                            //Do Not thing
                            System.out.println("touch check6");

                        } else {
                            loadLocalImage(fileList, result, false);
                            System.out.println("touch check7");

                            invalidate();

                        }
                    } else if (eventType.equals("rightSwipe") || eventType.equals("leftSwipe") || eventType.equals("downSwipe") || eventType.equals("upSwipe")) {
                        //current Img 어떤것인지 가져오기
                        compareSwipe(GlobalVar.curImg, eventType);
                        invalidate();
                    }
                    break;
            }
            if (touchFlag)
            {
                //사용자의 ipnput에대한 입력 정보가 추가
                XMLItem tempItem = new XMLItem();
                //전체 시간 기준 정보 담기
                stopwatchEntire.stop();
                System.out.println("전체시간" + stopwatchEntire.getRunTime());
                tempItem.setTimeEntire(stopwatchEntire.getRunTime());
                //현재 Task
                tempItem.setTask(GlobalVar.curTask);
                //이벤트타입
                tempItem.setEventType(eventType);
                //좌표
                if (eventType.equals("touch"))
                {
                    tempItem.setxInImage(String.valueOf(x2));
                    tempItem.setyInImage(String.valueOf(y2));
                }
                else
                {
                    tempItem.setxInImage("-1");
                    tempItem.setyInImage("-1");
                }
                System.out.println("어느 것이 먼저일까 A" + GlobalVar.curImg);

                //현재 이미지

                tempItem.setImgName(GlobalVar.curImg);
                //현재의 이미지 index를 바꾼다. 나중에 바꿔줘야하기 떄문에 어쩔수 없이 nextImage Index를 선언해 처리함.
                GlobalVar.curImg=GlobalVar.getLinkInfoList().get(nextImageIndex).destImg;
                //이미지 기준시간

                //넣기
                GlobalVar.getLogItemList().add(tempItem);
                touchFlag=false;
            }

            return true;
        }
    }
        public int compareSwipe(String curImg, String eventType) {
            int count = 0;
            boolean findFlag = false;
            System.out.println("확인0 ");
            while (count < GlobalVar.getLinkInfoList().size()) {
                if (curImg.equals(GlobalVar.getLinkInfoList().get(count).destImg)  && GlobalVar.getLinkInfoList().get(count).eventType.toString().equals( eventType)) {
                    System.out.println("확인1 ");
                    return count;
                }
                count++;
            }
            System.out.println("확인2 ");
            return -1;
        }

        public int compareTouchXY(String curImg, int touch_x, int touch_y) {
            int count = 0;
            float xInList = 0;
            float yInList = 0;
            float widthInList = 0;
            float heightInList = 0;
            boolean findFlag = false;
            while (count<GlobalVar.getLinkInfoList().size()) {
                System.out.println("check the link info: " + GlobalVar.getLinkInfoList().get(count).x_col + " " + GlobalVar.getLinkInfoList().get(count).startImg + " " + GlobalVar.getLinkInfoList().get(count).destImg + " " + GlobalVar.getLinkInfoList().get(count).eventType);
                count++;
            }
            count=0;
            while (count < GlobalVar.getLinkInfoList().size()) {    // 나중에 비율을 곱해줘야함, 스크린 크기에따라.
                if (curImg.equals(GlobalVar.getLinkInfoList().get(count).startImg)  && GlobalVar.getLinkInfoList().get(count).eventType.equals("Single_Touch")) {
                    xInList = GlobalVar.getLinkInfoList().get(count).x_col * proportion;
                    yInList = GlobalVar.getLinkInfoList().get(count).y_col * proportion;
                    widthInList = GlobalVar.getLinkInfoList().get(count).width * proportion;
                    heightInList = GlobalVar.getLinkInfoList().get(count).height * proportion;

                    System.out.println("check+check: " + xInList + " " + yInList + " " + widthInList + " " + heightInList);
                    System.out.println("touch info: " + x2 + " " +y2);
                    if (xInList < touch_x && xInList + widthInList > touch_x && yInList < touch_y && yInList + heightInList > touch_y) {
                        findFlag = true;
                        System.out.println("Box in touch");
                        break;
                    } else {
                        //정보가 터치도 맞으며 해당 이미지에 대한 것도 맞지만 영역에 들어오지 않음
                        count++;
                        System.out.println("카운트 체크/ 영역에 들어오지않음" + count);
                    }
                } else {
                    //다른 정보임
                    count++;
                    System.out.println("카운트 체크/ 다른 이미지" + count);
                }
            }
            if (findFlag) {
                System.out.println("카운트 체크/ 카운트를 찾음" + count);
                return count;
            } else {
                return -1;
            }
        }

        //스크린에 이미지를 띄어주기
        public void loadLocalImage(String[] fileList, int index, boolean isFirstImg) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            Bitmap src;
            if (isFirstImg) {   //현재는 명시적으로 주지만 나중에 첫번째 이미지를 받아와 화면에 출력해주자.
                src = BitmapFactory.decodeFile(path + "10-06.png", options);
                GlobalVar.curImg="10-06.png";


            } else {

                src = BitmapFactory.decodeFile(path + GlobalVar.getLinkInfoList().get(index).destImg, options);
                nextImageIndex=index;
            }
            if (standardwidth) {
                int heightSize = (int) (GlobalVar.screen_width * GlobalVar.projectImgH / GlobalVar.projectImgW);
                map_bitmap = Bitmap.createScaledBitmap(src, (int) GlobalVar.screen_width, heightSize, true);
            } else {
                int widthSize = (int) (GlobalVar.screen_height * GlobalVar.projectImgW / GlobalVar.projectImgH);

                map_bitmap = Bitmap.createScaledBitmap(src, (int) widthSize, (int) GlobalVar.screen_height, true);
            }

        }

        //로컬저장소에서 리스트 불러오기
        private String[] getTitleList(String path, String _filetype) {
            final String filetype = _filetype;
            try {
                FilenameFilter fileFilter = new FilenameFilter()  //이부분은 특정 확장자만 가지고 오고 싶을 경우 사용하시면 됩니다.
                {
                    public boolean accept(File dir, String name) {
                        return name.endsWith(filetype); //이 부분에 사용하고 싶은 확장자를 넣으시면 됩니다.
                    } //end accept
                };
                File file = new File(path);
                File[] files = file.listFiles(fileFilter);//위에 만들어 두신 필터를 넣으세요. 만약 필요치 않으시면 fileFilter를 지우세요.
                String[] titleList = new String[files.length]; //파일이 있는 만큼 어레이 생성했구요
                for (int i = 0; i < files.length; i++) {
                    titleList[i] = files[i].getName();    //루프로 돌면서 어레이에 하나씩 집어 넣습니다.
                }//end for
                return titleList;
            } catch (Exception e) {
                return null;
            }//end catch()
        }//end getTitleList

    }