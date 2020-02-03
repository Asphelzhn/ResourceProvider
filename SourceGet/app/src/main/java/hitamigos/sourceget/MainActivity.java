/*
 * Copyright (c) 2016.
 * 个人版权所有
 * kuangmeng.net
 */

package hitamigos.sourceget;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

import hitamigos.sourceget.speak.JsonParser;
import pw.h57.booksearcher.BookAPI;
import pw.h57.booksearcher.BookInfo;
import pw.h57.booksearcher.BookInfoDetailActivity;
import pw.h57.booksearcher.NetResponse;
import pw.h57.booksearcher.Utils;

public class MainActivity extends AppCompatActivity {
    private String speak;
    private ProgressDialog mProgressDialog;
    private DownloadHandler mHandler = new DownloadHandler(this);
    public String getSpeak(){
        return speak;
    }
    public void setSpeak(String speak){
        this.speak = speak;
    }
    private static final String TAG = MainActivity.class .getSimpleName();
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String , String>();
    public final static String EXTRA_MESSAGE = "uno.meng.download.MESSAGE";
    public void sendMessage(){
        EditText editText = (EditText)findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra(EXTRA_MESSAGE,message);
        startActivity(intent);
    }
    public void sendMessage(String speak){
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra(EXTRA_MESSAGE,speak);
        startActivity(intent);
    }

    public void Jump(int num){
        Intent intent =new Intent();
        switch(num){
            case 1:
                intent = new Intent(this,hitamigos.sourceget.activity.MainActivity.class);
                break;
            case 2:
                intent = new Intent(this,cn.hufeifei.mediaplayer.activity.MainActivity.class);
                break;
            case 3:
                intent = new Intent(this,com.example.administrator.robot.MainActivity.class);
                break;
            default:
                break;
        }
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        initSpeech();
        EditText editText = (EditText)findViewById(R.id.edit_message);
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_ENTER) {
                    sendMessage();
                }
                return false;
            }
        });
        Button btn = (Button)findViewById(R.id.buttonsearch);
        btn .setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //你要执行的代码
                startSpeechDialog();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.chat:
                Toast.makeText(MainActivity.this, ""+"正在前往聊天页！", Toast.LENGTH_SHORT).show();
                Jump(3);
                break;
            case R.id.scan:
                startScanner();
                break;
            case R.id.news:
                Toast.makeText(MainActivity.this, ""+"正在前往新闻页！", Toast.LENGTH_SHORT).show();
                Jump(1);
                break;
            case R.id.download:
                Toast.makeText(MainActivity.this, ""+"正在前往下载页！", Toast.LENGTH_SHORT).show();
                Jump(2);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 启动第三方库ZXing，进行条码扫描
     */
    private void startScanner() {
        IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
        integrator.initiateScan();
    }

    /**
     * 获取结果 获取ISBN，并启动线程进行文件下载
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,
                resultCode, data);
        if ((result == null) || (result.getContents() == null)) {
            Log.v(Utils.TAG, "User cancel scan by pressing back hardkey.");
            return;
        }
        // 因为现在需要耗时，为了更好的用户体现，显示进度条，进行提示。
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.communicating));
        mProgressDialog.show();
        // 启动下载线程
        DownloadThread thread = new DownloadThread(BookAPI.URL_ISBN_BASE
                + result.getContents() + "?fields=id,title,image,author,publisher,pubdate,isbn13,summary");
        thread.start();
    }

    private class DownloadThread extends Thread {
        private String mURL;

        public DownloadThread(String url) {
            super();
            mURL = url;
        }

        @Override
        public void run() {
            Message msg = Message.obtain();
            msg.obj = Utils.download(mURL);
            mHandler.sendMessage(msg);
        }
    }

    private static class DownloadHandler extends Handler {
        private MainActivity mActivity;

        public DownloadHandler(MainActivity activity) {
            super();
            mActivity = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            if ((msg.obj == null) || (mActivity.mProgressDialog == null)
                    || (!mActivity.mProgressDialog.isShowing())) {
                return;
            }
            mActivity.mProgressDialog.dismiss();
            NetResponse response = (NetResponse) msg.obj;
            if (response.getmCode() != BookAPI.RESPONSE_CODE_SUCCEED){
                Toast.makeText(
                        mActivity,
                        "[" + response.getmCode() + "]:"
                                + "抱歉，没有找到相关图书！", Toast.LENGTH_LONG)
                        .show();
            } else {
                mActivity.startBookInfoDetailActivity((BookInfo) response
                        .getmMessage());
            }
        }
    }
    private void startBookInfoDetailActivity(BookInfo bookInfo){
        if(bookInfo == null){
            return;
        }
        Intent intent = new Intent(this,BookInfoDetailActivity.class);
        intent.putExtra(BookInfo.class.getName(), bookInfo);
        startActivity(intent);
    }
    /*
    语音搜索
     */
    private void initSpeech() {
        // 请勿在 “ =”与 appid 之间添加任务空字符或者转义符
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=584be381");
    }

    private void startSpeechDialog() {
        //1. 创建RecognizerDialog对象
        RecognizerDialog mDialog = new RecognizerDialog(this, new MyInitListener()) ;
        //2. 设置accent、 language等参数
        mDialog.setParameter(SpeechConstant. LANGUAGE, "zh_cn" );// 设置中文
        mDialog.setParameter(SpeechConstant. ACCENT, "mandarin" );
        // 若要将UI控件用于语义理解，必须添加以下参数设置，设置之后 onResult回调返回将是语义理解
        // 结果
        // mDialog.setParameter("asr_sch", "1");
        // mDialog.setParameter("nlp_version", "2.0");
        //3.设置回调接口
        mDialog.setListener( new MyRecognizerDialogListener()) ;
        //4. 显示dialog，接收语音输入
        mDialog.show() ;
    }
    class MyRecognizerDialogListener implements RecognizerDialogListener {

        /**
         * @param results
         * @param isLast  是否说完了
         */
        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            String result = results.getResultString(); //为解析的
          //  showTip(result) ;
            System. out.println(" 没有解析的 :" + result);

            String text = JsonParser.parseIatResult(result) ;//解析过后的
            System. out.println(" 解析后的 :" + text);

            String sn = null;
            // 读取json结果中的 sn字段
            try {
                JSONObject resultJson = new JSONObject(results.getResultString()) ;
                sn = resultJson.optString("sn" );
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mIatResults .put(sn, text) ;//没有得到一句，添加到

            StringBuffer resultBuffer = new StringBuffer();
            for (String key : mIatResults.keySet()) {
                resultBuffer.append(mIatResults .get(key));
            }

            setSpeak(resultBuffer.toString());
            sendMessage(getSpeak());
        }

        @Override
        public void onError(SpeechError speechError) {

        }
    }

    class MyInitListener implements InitListener {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败 ");
            }
        }
    }
    /**
     * 语音识别
     */
    private void startSpeech(){
        //1. 创建SpeechRecognizer对象，第二个参数： 本地识别时传 InitListener
        SpeechRecognizer mIat = SpeechRecognizer.createRecognizer( this, null); //语音识别器
        //2. 设置听写参数，详见《 MSC Reference Manual》 SpeechConstant类
        mIat.setParameter(SpeechConstant. DOMAIN, "iat" );// 短信和日常用语： iat (默认)
        mIat.setParameter(SpeechConstant. LANGUAGE, "zh_cn" );// 设置中文
        mIat.setParameter(SpeechConstant. ACCENT, "mandarin" );// 设置普通话
        //3. 开始听写
        mIat.startListening( mRecoListener);
    }


    // 听写监听器
    private RecognizerListener mRecoListener = new RecognizerListener() {
        // 听写结果回调接口 (返回Json 格式结果，用户可参见附录 13.1)；
//一般情况下会通过onResults接口多次返回结果，完整的识别内容是多次结果的累加；
//关于解析Json的代码可参见 Demo中JsonParser 类；
//isLast等于true 时会话结束。
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.e (TAG, results.getResultString());
            System.out.println(results.getResultString()) ;
            showTip(results.getResultString()) ;
        }

        // 会话发生错误回调接口
        public void onError(SpeechError error) {
            showTip(error.getPlainDescription(true)) ;
            // 获取错误码描述
            Log. e(TAG, "error.getPlainDescription(true)==" + error.getPlainDescription(true ));
        }

        // 开始录音
        public void onBeginOfSpeech() {
            showTip(" 开始录音 ");
        }

        //volume 音量值0~30， data音频数据
        public void onVolumeChanged(int volume, byte[] data) {
            showTip(" 声音改变了 ");
        }

        // 结束录音
        public void onEndOfSpeech() {
            showTip(" 结束录音 ");
        }

        // 扩展用接口
        public void onEvent(int eventType, int arg1 , int arg2, Bundle obj) {
        }
    };

    private void showTip (String data) {
        Toast.makeText( this, data, Toast.LENGTH_SHORT).show() ;
    }

}
