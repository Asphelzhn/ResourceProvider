/*
 * Copyright (c) 2016.
 * 个人版权所有
 * kuangmeng.net
 */

package hitamigos.sourceget.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

	public static String getNowDateTime() {
		SimpleDateFormat s_format = new SimpleDateFormat("HH:mm:ss");
		Date d_date = new Date();
		String s_date = "";
		s_date = s_format.format(d_date);
		return s_date;
	}

	public static String getDotOne(double src) {
		DecimalFormat df = new DecimalFormat(".0");
		String value = df.format(src);
		return value;
	}
	
}
