package pw.h57.booksearcher;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * @author chrishao
 * 这个类为图书实体类，用于存放Utils解析所得的图书信息
 */
public class BookInfo implements Parcelable {
	private String mTitle = ""; //书名
	private Bitmap mCover; //封面
	private String mAuthor = ""; //作者
	private String mPublisher = "";//出版社
	private String mPublishDate = ""; //出版时间
	private String mISBN = ""; //ISBN
	private String mSummary = ""; //内容介绍
	private String mId = ""; //id
	public static final Parcelable.Creator<BookInfo> CREATOR = new Creator<BookInfo>(){
		public BookInfo[] newArray(int size){
			return new BookInfo[size];
		}
		@Override
		public BookInfo createFromParcel(Parcel source) {
			BookInfo bookInfo = new BookInfo();
			bookInfo.mTitle = source.readString();
			bookInfo.mCover = source.readParcelable(Bitmap.class.getClassLoader());
			bookInfo.mAuthor = source.readString();
			bookInfo.mPublisher = source.readString();
			bookInfo.mPublishDate = source.readString();
			bookInfo.mISBN = source.readString();
			bookInfo.mSummary = source.readString();
			bookInfo.mId = source.readString();
			return bookInfo;
		}
	};
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(mTitle);
		dest.writeParcelable(mCover, flags);
		dest.writeString(mAuthor);
		dest.writeString(mPublisher);
		dest.writeString(mPublishDate);
		dest.writeString(mISBN);
		dest.writeString(mSummary);
		dest.writeString(mId);
	}
	public String getmTitle() {
		return mTitle;
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public Bitmap getmCover() {
		return mCover;
	}

	public void setmCover(Bitmap mCover) {
		this.mCover = mCover;
	}

	public String getmAuthor() {
		return mAuthor;
	}

	public void setmAuthor(String mAuthor) {
		this.mAuthor = mAuthor;
	}

	public String getmPublisher() {
		return mPublisher;
	}

	public void setmPublisher(String mPublisher) {
		this.mPublisher = mPublisher;
	}

	public String getmPublishDate() {
		return mPublishDate;
	}

	public void setmPublishDate(String mPublishDate) {
		this.mPublishDate = mPublishDate;
	}

	public String getmISBN() {
		return mISBN;
	}

	public void setmISBN(String mISBN) {
		this.mISBN = "ISBN:" + mISBN;
	}

	public String getmSummary() {
		return mSummary;
	}

	public void setmSummary(String mSummary) {
		this.mSummary = mSummary;
	}

	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}
	
}
