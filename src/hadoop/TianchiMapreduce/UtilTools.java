package hadoop.TianchiMapreduce;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class UtilTools {
	public static Date string2Date_flight(String time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	public static int deltDay_flight(String time){
		Date dateLater = string2Date_flight(time);
		Date dateBase = string2Date("2016-09-10-10-55");
		//System.out.println((int)((dateLater.getTime()-dateBase.getTime())/60000));
		return (int)((dateLater.getTime()-dateBase.getTime())/60000);
	}
	
	public static Date string2Date(String time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static int deltDay(String time){
		Date dateLater = string2Date(time);
		Date dateBase = string2Date("2016-09-10-18-55");
		//System.out.println((int)((dateLater.getTime()-dateBase.getTime())/60000));
		return (int)((dateLater.getTime()-dateBase.getTime())/60000);
	}
	
	
	public static double[] parseTo10min(double[] personsNum){
		
		double[] result = new double[StaticValue.dataNum_10min];

		for (int i = 1;i<StaticValue.dataNum_10min;i=i+1){
			boolean lost = false;
			double meanValue = (double) 0;
			for (int j =0; j <10;j++){
				
				if (personsNum[StaticValue.dataNum-i*10+j] == (double)0.0){
					lost = true;
					break;
				}
				if (personsNum[StaticValue.dataNum-i*10+j] == (double)-1.0){
					meanValue = meanValue + 0;
					continue;
				}
				meanValue = meanValue + personsNum[StaticValue.dataNum-i*10+j];
			}
			if (lost==true){
				result[StaticValue.dataNum_10min-i] = (double)-1;	
			}
			else
			{
				result[StaticValue.dataNum_10min-i] = (double)meanValue/10;	
			}
			
		}
		
		return result;
	}
	
	
	
	public static String getDateByNo(int No){
		int h = No/6;
		int m = No%6;
		String  h_s;
		String  m_s;
		if (h<10){
			h_s =  "0"+Integer.toString(h);
		}
		else{
			h_s =  Integer.toString(h);
		}
		if (m<10){
			m_s =  Integer.toString(m);
		}
		else{
			m_s =  Integer.toString(m);
		}
		String result = h_s+"-"+m_s;
		return result;
	}
	

	public static double[] parseTo10min_flight(double[] personsNum){
		
		double[] result = new double[StaticValue.flightDataNum_10min];

		for (int i = 1;i<StaticValue.flightDataNum_10min;i=i+1){
			double meanValue = (double) 0;
			for (int j =0; j <10;j++){
				
				meanValue = meanValue + personsNum[StaticValue.flightDataNum-i*10+j];
			}
			
			result[StaticValue.flightDataNum_10min-i] = (double)meanValue;	
			
		}
		
		return result;
	}
	
	
	 public static void main(String[] args) {
	       
		 deltDay("2016-11-12-23-59-00");
		 deltDay("2016-10-15-23-59-00");
		 //deltDay_flight("2016/11/10 13:05:00");
		 //System.out.println(8796-8813+144);
		// System.out.println(getDateByNo(8796-8813+144));
		 

		 deltDay_flight("2016/11/12 01:00:00");

		 System.out.println(getDateByNo((9011-8813)%144));

	    }
}
