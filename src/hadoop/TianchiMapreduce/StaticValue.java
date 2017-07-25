package hadoop.TianchiMapreduce;

public class StaticValue {

	 public static int oneDay = 60*24;
     public static int dataNum = 88145;
     public static int flightDataNum = 91025;

	 //public static int dataNum = 50705;
     public static int trainTime = dataNum - oneDay*2;
     public static int preTime = dataNum;
     public static int iternum = 120;
     public static int samplenum = 5;
     
     public static int oneDay_10min = 6*24;
     public static int dataNum_10min = (int)dataNum/10;
     public static int preTime_10min = dataNum_10min;
     public static int dayTime_10min = 24*6;
     public static int trainTime_10min = dataNum_10min - oneDay_10min*2;
     
     public static int flightDataNum_10min = dataNum_10min+288;

}
