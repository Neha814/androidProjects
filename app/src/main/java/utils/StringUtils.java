package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import functions.Constants;

import android.util.Log;

public class StringUtils {

	public static String replaceWords(String phoneNumber) {

		String added_phoneNo = phoneNumber.replace(" ", "").replace("+", "")
				.replace("-", "").replace("(", "").replace(")", "");
		// if(added_phoneNo.length() > 10) {
		// added_phoneNo = added_phoneNo.substring(added_phoneNo.length() - 10);
		//
		// }
		return added_phoneNo;

	}
	
	/**
	 * Email verification
	 * @param paramString
	 * @return
	 */

	public static boolean verify(String paramString) {
		return paramString
				.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$");
	}
	
	/**
	 * Date Format conversion
	 * @param dateToConvert
	 * @return
	 */

	public static String DateConverter(String dateToConvert) {

		String dateConvert = dateToConvert;
		String DateConverted = "";

		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		Date myDate = new Date();
		try {
			myDate = dateFormat.parse(dateConvert);
			Log.e("***** myDate *****", "" + myDate);
			SimpleDateFormat formatter = new SimpleDateFormat("MM / dd / yyyy");
			 DateConverted = formatter.format(myDate);
			Log.e("*conveted date*", "" + DateConverted);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return DateConverted;

	}

	// Convert date String from one format to another

	public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate){

		Date parsed = null;
		String outputDate = "";

		SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
		SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

		try {
			parsed = df_input.parse(inputDate);
			outputDate = df_output.format(parsed);

		} catch (ParseException e) {
			Log.e("TAG Date", "ParseException - dateFormat");
		}

		return outputDate;

	}

}
