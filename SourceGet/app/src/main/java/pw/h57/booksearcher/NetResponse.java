package pw.h57.booksearcher;
/**
 * 
 * @author chrishao
 * 这个类为服务器相应实体类，将信息从Utils传给MainActivity。如果相应正常，用于存放BookInfo实体
 * 如果响应异常，用于存放String类型的错误原因信息
 */
public class NetResponse {
	private int mCode; //响应码
	private Object mMessage; //响应详情
	
	public NetResponse(int code,Object message){
		mCode = code;
		mMessage = message;
	}
	
	public int getmCode() {
		return mCode;
	}

	public void setmCode(int mCode) {
		this.mCode = mCode;
	}

	public Object getmMessage() {
		return mMessage;
	}

	public void setmMessage(Object mMessage) {
		this.mMessage = mMessage;
	}
}
