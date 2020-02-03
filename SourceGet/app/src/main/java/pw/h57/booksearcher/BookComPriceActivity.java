package pw.h57.booksearcher;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import hitamigos.sourceget.R;


public class BookComPriceActivity extends Activity {
	
	private ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_com_price);
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		list = (ListView) findViewById(R.id.list);
		BookPrice bookPrice =  getIntent().getParcelableExtra("bookPrice");
		BookPrice[] bookPrices = new BookPrice[getIntent().getIntExtra("size", 1)];
		for(int i = 0 ; i< getIntent().getIntExtra("size", 1); i++){
			bookPrices[i] = getIntent().getParcelableExtra("bookPrice" + i);
		}
		list.setAdapter(new MyAdapter(bookPrices));
	}
	private class MyAdapter extends BaseAdapter {
		BookPrice[] bookPrice;
		public MyAdapter(BookPrice[] bookPrice){
			this.bookPrice = bookPrice;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return bookPrice.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return bookPrice[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView == null){
				LayoutInflater inflater = (LayoutInflater) BookComPriceActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View itemView = inflater.inflate(R.layout.items, null);
				TextView shop = (TextView) itemView.findViewById(R.id.shop);
				TextView price = (TextView) itemView.findViewById(R.id.price);
				TextView save = (TextView) itemView.findViewById(R.id.save);
				shop.setText(bookPrice[position].getmTitle());
				price.setText("价格 (元)"+bookPrice[position].getmPrice());
				save.setText("节省 (元)"+bookPrice[position].getmSavePrice());
				return itemView;
			}else{
				TextView shop = (TextView) convertView.findViewById(R.id.shop);
				TextView price = (TextView) convertView.findViewById(R.id.price);
				TextView save = (TextView) convertView.findViewById(R.id.save);
				shop.setText(bookPrice[position].getmTitle());
				price.setText("价格 (元)"+bookPrice[position].getmPrice());
				save.setText("节省 (元)"+bookPrice[position].getmSavePrice());
				return convertView;
			}
		}
		
	}
}
