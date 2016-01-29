package com.dropboxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;

import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.chooser.android.DbxChooser;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.ProgressListener;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class MainActivity extends Activity  implements AbsListView.OnScrollListener{
    private Handler mHandler;
    public  static Button button1;
    private Button mChooserButton;
    private DbxChooser mChooser;
    private DownloadThread downloadThread;
    public  static Button button2;
    public String dropBoxProjectFolder="dbtuorial/";
    ArrayList<String> mData=null;
    ListView mListView=null;
    BaseAdapterEx mAdapter =null;

    final Context context=this;
    private AbsListView view;
    private int firstVisibleItem, visibleItemCount, totalItemCount;
    public  static Button button3;
    public String downFileName;
    public  static Button button4;
    public static String path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/AAA_DB/";
    public static CompressionUtil cu = new CompressionUtil();
    public static File Dir=new File(path);
    LinearLayout parentView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1=(Button)findViewById(R.id.btn1);
        button2=(Button)findViewById(R.id.btn2);
        button3=(Button)findViewById(R.id.btn3);
        button4=(Button)findViewById(R.id.btn4);
        mChooser = new DbxChooser(APP_KEY);
        mHandler = new Handler();
        cu = new CompressionUtil();

        parentView=(LinearLayout)findViewById(R.id.parentLayout);
         parentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                GlobalVar.screen_width=parentView.getWidth();
                GlobalVar.screen_height=parentView.getHeight();
               // System.out.println("제발 알려줘 나에게"+widthIW+" "+heightIW);
            }
        });

        mChooserButton = (Button) findViewById(R.id.choose);
        mChooserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbxChooser.ResultType resultType = DbxChooser.ResultType.DIRECT_LINK;
                mChooser.forResultType(resultType).launch(MainActivity.this, DBX_CHOOSER_REQUEST);
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadToDropboxFromPath(path + "testfile.txt", "dbtuorial/UploadFileFromPath.txt");
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    cu.unzip( new File(path+"Pictures.zip"),new File(path+"NewFolder"),"EUC-KR" );
                }
                catch (IOException ie)
                {
                    Toast.makeText(getApplicationContext(), "There is no File to Unzip, Please check the FileName.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,LoadFiles.class);
                startActivity(intent);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadThread=new DownloadThread(true);
                downloadThread.start();
            }
        });

        AndroidAuthSession session = buildSession();
        dropboxAPI = new DropboxAPI<AndroidAuthSession>(session);
        Dir.mkdir();

        mAdapter=new BaseAdapterEx(this);
        mListView=(ListView)findViewById(R.id.list_view);
        mListView.setAdapter(mAdapter);
        mListView.setOnScrollListener(this);

    }
    static DropboxAPI<AndroidAuthSession> dropboxAPI;
    private static final String APP_KEY = "gsmxbwijc4exwf0";
    private static final String APP_SECRET ="jpcmufu2pzw55i8";
    private static final String ACCESSTOKEN = "4kSJuwoh2g8AAAAAAAAJGSU7wQC0DEh7Y6DEOWFK5oWtY8caNVKjdGrcMovMx-8w";
    static final int DBX_CHOOSER_REQUEST = 0;  // You can change this if needed
    private DropboxAPI.UploadRequest request;
    private AndroidAuthSession buildSession()
    {
        AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeyPair);
        session.setOAuth2AccessToken(ACCESSTOKEN);
        return session;
    }
    static final int UploadFromSelectApp = 9501;
    static final int UploadFromFilemanager = 9502;
    public static String DropboxUploadPathFrom = "";
    public static String DropboxUploadName = "";
    public static String DropboxDownloadPathFrom = "";
    public static String DropboxDownloadPathTo = "";

    private void UploadToDropboxFromPath (String uploadPathFrom, String uploadPathTo)
    {
        Toast.makeText(getApplicationContext(), "Upload file ...", Toast.LENGTH_SHORT).show();
        final String uploadPathF = uploadPathFrom;
        final String uploadPathT = uploadPathTo;
        Thread th = new Thread(new Runnable()
        {
            public void run()
            {
                File tmpFile = null;
                try
                {
                    tmpFile = new File(uploadPathF);
                }
                catch (Exception e) {e.printStackTrace();}
                FileInputStream fis = null;
                try
                {
                    fis = new FileInputStream(tmpFile);
                }
                catch (FileNotFoundException e) {e.printStackTrace();}
                try
                {
                    dropboxAPI.putFileOverwrite(uploadPathT, fis, tmpFile.length(), null);
                }
                catch (Exception e) {}
                getMain().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "File successfully uploaded.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        th.start();
    }
    private void UploadToDropboxFromSelectedApp (String uploadName)
    {
        DropboxUploadName = uploadName;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Upload from ..."), UploadFromSelectApp);
    }
    private void UploadToDropboxFromFilemanager (String uploadName)
    {
        DropboxUploadName = uploadName;
        Intent intent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
        intent.putExtra("CONTENT_TYPE", "*/*");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(intent, UploadFromFilemanager);
    }

    private void DownloadFromDropboxFromPath (final String downloadPathTo, String downloadPathFrom, final int index)
    {
        DropboxDownloadPathTo = downloadPathTo;
        DropboxDownloadPathFrom = downloadPathFrom;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (index == 0) {
                    Toast.makeText(getApplicationContext(), "Download file ...", Toast.LENGTH_SHORT).show();
                }
                Thread th = new Thread(new Runnable() {
                    public void run() {
                        File file = new File(DropboxDownloadPathTo);
                        if (file.exists()) file.delete();
                        try {
                            FileOutputStream outputStream = new FileOutputStream(file);
                            MainActivity.dropboxAPI.getFile(DropboxDownloadPathFrom, null, outputStream, null);
                            getMain().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (index == 1) {
                                        Toast.makeText(getApplicationContext(), "File successfully downloaded.", Toast.LENGTH_SHORT).show();

                                    }
                                    GlobalVar.downloadDone = true;
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                th.start();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if (requestCode == UploadFromFilemanager)
        {
            final Uri currFileURI = intent.getData();
            final String pathFrom = currFileURI.getPath();
            Toast.makeText(getApplicationContext(), "Upload file ...", Toast.LENGTH_SHORT).show();
            Thread th = new Thread(new Runnable()
            {
                public void run()
                {
                    getMain().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            UploadToDropboxFromPath(pathFrom, "/db-test/" + DropboxUploadName + pathFrom.substring(pathFrom.lastIndexOf('.')));
                            Toast.makeText(getApplicationContext(), "File successfully uploaded.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            th.start();
        }
        if (requestCode == DBX_CHOOSER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                DbxChooser.Result result = new DbxChooser.Result(intent);
                Log.d("main", "Link to selected file: " + result.getLink());


                ((TextView) findViewById(R.id.filename)).setText(result.getName().toString(), TextView.BufferType.NORMAL);
                ((TextView) findViewById(R.id.size)).setText(String.valueOf(result.getSize()), TextView.BufferType.NORMAL);
                downFileName=result.getName().toString();
                GlobalVar.getImgfileList().add(downFileName);
                mAdapter.notifyDataSetChanged();

            } else {
                // Failed or was cancelled by the user.
            }
        }
        if (requestCode == UploadFromSelectApp)
        {
            Toast.makeText(getApplicationContext(), "Upload file ...", Toast.LENGTH_SHORT).show();
            final Uri uri = intent.getData();

            DropboxUploadPathFrom = getPath(getApplicationContext(), uri);
            if(DropboxUploadPathFrom == null) {
                DropboxUploadPathFrom = uri.getPath();
            }
            Thread th = new Thread(new Runnable(){
                public void run() {
                    try
                    {
                        final File file = new File(DropboxUploadPathFrom);
                        InputStream inputStream = getContentResolver().openInputStream(uri);

                        dropboxAPI.putFile("/db-test/" + DropboxUploadName + file.getName().substring(file.getName().lastIndexOf("."),
                                file.getName().length()), inputStream, file.length(), null, new ProgressListener(){
                            @Override
                            public long progressInterval() {return 100;}
                            @Override
                            public void onProgress(long arg0, long arg1){}
                        });
                        getMain().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Toast.makeText(getApplicationContext(), "File successfully uploaded.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (Exception e) {e.printStackTrace();}
                }
            });
            th.start();
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    public String getPath(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA, MediaStore.Video.Media.DATA, MediaStore.Audio.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String s = cursor.getString(column_index);
            if(s!=null) {
                cursor.close();
                return s;
            }
        }
        catch(Exception e){}
        try {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            String s = cursor.getString(column_index);
            if(s!=null) {
                cursor.close();
                return s;
            }
        }
        catch(Exception e){}
        try {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            cursor.moveToFirst();
            String s = cursor.getString(column_index);
            cursor.close();
            return s;
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public MainActivity getMain()
    {
        return this;
    }
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // TODO Auto-generated method stub
        this.view = view;

        this.firstVisibleItem = firstVisibleItem;
        this.visibleItemCount = visibleItemCount;
        this.totalItemCount = totalItemCount;

    }

    private String[] getTitleList(String path, String _filetype)
    {
        final String filetype=_filetype;
        try
        {
            FilenameFilter fileFilter = new FilenameFilter()  //이부분은 특정 확장자만 가지고 오고 싶을 경우 사용하시면 됩니다.
            {
                public boolean accept(File dir, String name)
                {
                    return name.endsWith(filetype); //이 부분에 사용하고 싶은 확장자를 넣으시면 됩니다.
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
    public class DownloadThread extends Thread
    {
        private boolean startThread=false;
        public DownloadThread(boolean startThread)
        {
            this.startThread=startThread;
        }

        @Override
        public void run(){
            super.run();
            while (startThread){
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(GlobalVar.downloadDone)
                        {
                            System.out.println("download Try " + GlobalVar.curFileIdx+" "+GlobalVar.getImgfileList().size());
                            if(GlobalVar.curFileIdx==0&&GlobalVar.getImgfileList().size()!=1) {
                             int type = 0;//download 시작 알림
                             DownloadFromDropboxFromPath(path + GlobalVar.getImgfileList().get(GlobalVar.curFileIdx), dropBoxProjectFolder + GlobalVar.getImgfileList().get(GlobalVar.curFileIdx), type);

                            }
                            else if(GlobalVar.curFileIdx==GlobalVar.getImgfileList().size()-1) {
                                int type = 1;//download 끝 알림
                                DownloadFromDropboxFromPath(path + GlobalVar.getImgfileList().get(GlobalVar.curFileIdx), dropBoxProjectFolder + GlobalVar.getImgfileList().get(GlobalVar.curFileIdx), type);

                            }
                            else if(GlobalVar.curFileIdx==GlobalVar.getImgfileList().size()){
                                startThread=false;

                            }
                            else{
                                int type = 2;//download 아무것도 알리지 않음
                                DownloadFromDropboxFromPath(path + GlobalVar.getImgfileList().get(GlobalVar.curFileIdx), dropBoxProjectFolder + GlobalVar.getImgfileList().get(GlobalVar.curFileIdx), type);
                            }
                            if(GlobalVar.curFileIdx!=GlobalVar.getImgfileList().size()) {
                                GlobalVar.curFileIdx++;
                                GlobalVar.downloadDone = false;
                            }
                            else
                            {
                                GlobalVar.curFileIdx=0;


                            }

                        }
                        else
                        {

                        }

                    }

                });
            }
        }
    }

}
