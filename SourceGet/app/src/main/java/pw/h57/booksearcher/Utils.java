package pw.h57.booksearcher;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import hitamigos.sourceget.R;


/**
 * 
 * @author chrishao
 * 这个类是工具类，提供文件下载，json解析
 * json解析即包括正常响应解释，进而得到图书信息
 * 又包括一场相应解析，进而得到错误原因
 */
public class Utils {
	public static final String TAG = "BookSearcher";
	
	/**
	 * 从豆瓣下载数据，并得到图书详情
	 */
	public static NetResponse download(String url){
		Log.v(TAG, "download from:" + url);
		NetResponse ret = downloadFromDouban(url);
		JSONObject message = null;
		
		try {
			message = new JSONObject(String.valueOf(ret.getmMessage()));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		switch (ret.getmCode()) {
		case BookAPI.RESPONSE_CODE_SUCCEED:
			ret.setmMessage(parseBookInfo(message));
			break;

		default:
			int errorCode = parseErrorCode(message);
			ret.setmCode(errorCode);
			ret.setmMessage(getErrorMessage(errorCode));
			break;
		}
		
		return ret;	
	}
	/**
	 * 通过URL下载封面图片
	 */
	private static Bitmap downloadBitmap(String url){
		HttpURLConnection conn = null;
		InputStream is = null;
		BufferedInputStream bis = null;
		Bitmap bm = null;
		
		try {
			conn = (HttpURLConnection)(new URL(url)).openConnection();
			is = conn.getInputStream();
			bis = new BufferedInputStream(is);
			bm = BitmapFactory.decodeStream(bis);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try{
				if(bis != null){
					bis.close();
				}
				if(is != null){
					is.close();
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		return bm;
	}
	/**
	 * 将从豆瓣下载所得的书籍JSON文件解析成图书对象
	 */
	private static BookInfo parseBookInfo(JSONObject json){
		if(json == null){
			return null;
		}
		BookInfo bookInfo = null;
		try {
			bookInfo = new BookInfo();
			bookInfo.setmTitle(json.getString(BookAPI.TAG_TITLE));
			bookInfo.setmCover(downloadBitmap((json.getString(BookAPI.TAG_COVER))));
			bookInfo.setmAuthor(parseJSONArray2String(json.getJSONArray(BookAPI.TAG_AUTHOR)," "));
			bookInfo.setmPublisher(json.getString(BookAPI.TAG_PUBLISHER));
			bookInfo.setmPublishDate(json.getString(BookAPI.TAG_PUBLISH_DATE));
			bookInfo.setmISBN(json.getString(BookAPI.TAG_ISBN));
			bookInfo.setmSummary(json.getString(BookAPI.TAG_SUMMARY).replace("\n","\n\n"));
			bookInfo.setmId(json.getString(BookAPI.TAG_ID));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return bookInfo;
	}
	/**
	 * 将JSON对象解析成特定格式的字符串，例如
	 * ["String0","string1"] -> "string0 string1"
	 */
	private static String parseJSONArray2String(JSONArray json, String split){
		if((json == null) || (json.length() < 1)){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < json.length(); i++){
			try {
				sb = sb.append(json.getString(i));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			sb = sb.append(split);
		}
		sb.deleteCharAt(sb.length() - 1);
		Log.v(TAG, "parseJSONArray2String(" + json.toString() + "):" + sb.toString());
		return sb.toString();
	}
	/**
	 * 从豆瓣返回的错误消息中接续出错误码
	 * @param json
	 * @return
	 */
	private static int parseErrorCode(JSONObject json){
		int ret =  BookAPI.RESPONSE_CODE_ERROR_NET_EXCEPTION;
		
		if(json == null){
			return ret;
		}
		try {
			ret = json.getInt(BookAPI.TAG_ERROR_CODE);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ret;
	}
	/**
	 * 根据错误码找到对应错误详情的字符串编号
	 */
	private static int getErrorMessage(int errorCode){
		int ret = R.string.error_message_default;
		switch (errorCode) {
		case BookAPI.RESPONSE_CODE_ERROR_NET_EXCEPTION:
			ret = R.string.error_message_net_exception;
			break;
		case BookAPI.RESPONSE_CODE_ERROR_BOOK_NOT_FOUND:
			ret = R.string.error_message_book_not_found;
		default:
			break;
		}
		return ret;
	}
	private static NetResponse downloadFromDouban(String url) {
        NetResponse ret = new NetResponse(BookAPI.RESPONSE_CODE_ERROR_NET_EXCEPTION, null);
        
        BufferedReader reader = null;
        HttpClient client = null;
        HttpResponse response = null;
        
        BasicHttpParams httpParams = new BasicHttpParams();
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(httpParams, schemeRegistry);

        client = new DefaultHttpClient(cm, httpParams);

        try {
            response = client.execute(new HttpGet(url));
            ret.setmCode(response.getStatusLine().getStatusCode());
            
            StringBuffer sb = new StringBuffer();
            String line;
            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            
            while ((line = reader.readLine()) != null) {
            	sb.append(line);
            }
            
            Log.v(TAG, "The content of Book: " + sb.toString());
            ret.setmMessage(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.getConnectionManager().shutdown();
            }
            
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return ret;
    }
	public static Parcelable[] comPrice(String bookId){
		Document doc = null;
		try {
			doc = Jsoup.connect("http://book.douban.com/subject/" + bookId + "/buylinks").get();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String title = doc.title();
		Element element = doc.getElementById("buylink-table");
		Elements elements = element.getElementsByClass("pl2");
		BookPrice[] bookPrices = new BookPrice[elements.size()/3];
		for(int i = 0; i < elements.size()/3 ; i++){
			BookPrice bookPrice = new BookPrice();
			for(int j = 0; j < 3; j++){
				//Log.v("AAAAA", elements.get(i*3+j).childNode(0).toString());
//				elements.get(i*3+j).childNode(0).childNode(0).toString()
				
				switch (j) {
				case 0:
					//Log.v("AAAAA","0------" + elements.get(i*3+j).child(0).childNode(0).toString() + "----"+(i*3+j));
					bookPrice.setmTitle(elements.get(i*3+j).child(0).childNode(0).toString());
					break;
				case 1:
					//Log.v("AAAAA","1------" +  elements.get(i*3+j).child(0).childNode(0).toString() + "----"+(i*3+j));
					bookPrice.setmPrice(elements.get(i*3+j).child(0).childNode(0).toString());
					break;
				default:
					//Log.v("AAAAA","2------" +  elements.get(i*3+j).childNode(0).toString() + "----"+(i*3+j));
					bookPrice.setmSavePrice(elements.get(i*3+j).childNode(0).toString());
					break;
				}
			}
			bookPrices[i] = bookPrice;
			
		}
		return bookPrices;
	}
}
