package pw.h57.booksearcher;


import android.os.Parcel;
import android.os.Parcelable;

public class BookPrice implements Parcelable{
	private String mTitle = ""; //商店名称
	private String mPrice = ""; //价格
	private String mSavePrice = "";//节省价格
	public static final Parcelable.Creator<BookPrice> CREATOR = new Creator<BookPrice>(){
		public BookPrice[] newArray(int size){
			return new BookPrice[size];
		}
		@Override
		public BookPrice createFromParcel(Parcel source) {
			BookPrice bookPrice = new BookPrice();
			bookPrice.mTitle = source.readString();
			bookPrice.mPrice = source.readString();
			bookPrice.mSavePrice = source.readString();
			return bookPrice;
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
		dest.writeString(mPrice);
		dest.writeString(mSavePrice);

	}
	public String getmTitle() {
		return mTitle;
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}


	public String getmPrice() {
		return mPrice;
	}

	public void setmPrice(String mPrice) {
		this.mPrice = mPrice;
	}

	public String getmSavePrice() {
		return mSavePrice;
	}

	public void setmSavePrice(String mSavePrice) {
		this.mSavePrice = mSavePrice;
	}

	
}
