package pw.h57.booksearcher;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;

import hitamigos.sourceget.R;

public class BookInfoDetailActivity extends Activity {
	private TextView mTitle;
	private ImageView mCover;
	private TextView mAuthor;
	private TextView mPublisher;
	private TextView mPublishDate;
	private TextView mISBN;
	private TextView mSummary;
	private Button mButton;
	private ProgressDialog mProgressDialog;
	public String messa;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_info_detail_activity);
		initViews();
		initData(getIntent().getParcelableExtra(BookInfo.class.getName()));
		initSpeech();
	}
	private void initViews(){
		mTitle = (TextView)findViewById(R.id.book_detail_title);
		mCover = (ImageView)findViewById(R.id.book_detail_cover);
		mAuthor = (TextView)findViewById(R.id.book_detail_author);
		mPublisher = (TextView)findViewById(R.id.book_detail_publisher);
		mPublishDate = (TextView)findViewById(R.id.book_detail_pubdate);
		mISBN = (TextView)findViewById(R.id.book_detail_isbn);
		mSummary = (TextView)findViewById(R.id.book_detail_summary);
		mButton = (Button) findViewById(R.id.book_search_price);
	}
	private class DownloadThread extends Thread {
		private String mId;
		private BookInfoDetailActivity mActivity;
		public DownloadThread(BookInfoDetailActivity activity,String id) {
			super();
			mId = id;
			mActivity = activity;
			
		}

		@Override
		public void run() {
			BookPrice[] bookPrices = (BookPrice[]) Utils.comPrice(mId);
			mActivity.mProgressDialog.dismiss();
			Intent intent = new Intent(BookInfoDetailActivity.this,BookComPriceActivity.class);
			for(int i = 0; i < bookPrices.length; i++){
				intent.putExtra("bookPrice" + i, bookPrices[i]);
				Log.v("AAAAA","2------" + bookPrices[i].getmTitle());
			}
			intent.putExtra("size", bookPrices.length);
			startActivity(intent);
		}
	}
	private void initData(Parcelable data){
		if(data == null){
			return;
		}
		final BookInfo bookInfo = (BookInfo)data;
		messa+="书籍名称："+bookInfo.getmTitle();
		mTitle.setText(bookInfo.getmTitle());
		mCover.setImageBitmap(bookInfo.getmCover());
		messa+="作者："+bookInfo.getmAuthor();
		mAuthor.setText(bookInfo.getmAuthor());
		messa+="出版社："+bookInfo.getmPublisher();
		mPublisher.setText(bookInfo.getmPublisher());
		messa+="出版日期："+bookInfo.getmPublishDate();
		mPublishDate.setText(bookInfo.getmPublishDate());
		mISBN.setText(bookInfo.getmISBN());
		messa+="概要："+bookInfo.getmSummary();
		mSummary.setText(bookInfo.getmSummary());
		mButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mProgressDialog = new ProgressDialog(BookInfoDetailActivity.this);
				mProgressDialog.setMessage(getString(R.string.communicating));
				mProgressDialog.show();
				DownloadThread thread = new DownloadThread(BookInfoDetailActivity.this,bookInfo.getmId());
				thread.start();
			}
		});
	}
	private void initSpeech() {
		// 请勿在 “ =”与 appid 之间添加任务空字符或者转义符
		SpeechUtility.createUtility(this, SpeechConstant.APPID + "=584be381");
		speekText(messa);
	}
	private void speekText(String mess) {
		//1. 创建 SpeechSynthesizer 对象 , 第二个参数： 本地合成时传 InitListener
		SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(this, null);
		//2.合成参数设置，详见《 MSC Reference Manual》 SpeechSynthesizer 类
		//设置发音人（更多在线发音人，用户可参见 附录 13.2
		mTts.setParameter(SpeechConstant.VOICE_NAME, "vixyun"); // 设置发音人
		mTts.setParameter(SpeechConstant.SPEED, "50");// 设置语速
		mTts.setParameter(SpeechConstant.VOLUME, "80");// 设置音量，范围 0~100
		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
		//设置合成音频保存位置（可自定义保存位置），保存在 “./sdcard/iflytek.pcm”
		//保存在 SD 卡需要在 AndroidManifest.xml 添加写 SD 卡权限
		//仅支持保存为 pcm 和 wav 格式， 如果不需要保存合成音频，注释该行代码
		mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");
		//3.开始合成
		mTts.startSpeaking(mess.toString(), new BookInfoDetailActivity.MySynthesizerListener());
	}

	class MySynthesizerListener implements com.iflytek.cloud.SynthesizerListener {

		@Override
		public void onSpeakBegin() {
			showTip(" 开始阅读 ");
		}

		@Override
		public void onSpeakPaused() {
			showTip(" 暂停 ");
		}

		@Override
		public void onSpeakResumed() {
			showTip(" 继续阅读 ");
		}

		@Override
		public void onBufferProgress(int percent, int beginPos, int endPos ,
									 String info) {
			// 合成进度
		}

		@Override
		public void onSpeakProgress(int percent, int beginPos, int endPos) {
			// 播放进度
		}

		@Override
		public void onCompleted(SpeechError error) {
			if (error == null) {
				showTip("阅读完成 ");
			} else if (error != null ) {
				showTip(error.getPlainDescription( true));
			}
		}

		@Override
		public void onEvent(int eventType, int arg1 , int arg2, Bundle obj) {
		}
	}
	private void showTip (String data) {
		Toast.makeText( this, data, Toast.LENGTH_SHORT).show() ;
	}
}
