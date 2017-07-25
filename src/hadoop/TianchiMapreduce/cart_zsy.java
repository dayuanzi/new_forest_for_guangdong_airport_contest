package hadoop.TianchiMapreduce;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import com.aliyun.odps.data.Record;
import com.aliyun.odps.data.TableInfo;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.MapperBase;
import com.aliyun.odps.mapred.ReducerBase;
import com.aliyun.odps.mapred.RunningJob;
import com.aliyun.odps.mapred.Reducer.TaskContext;
import com.aliyun.odps.mapred.conf.JobConf;
import com.aliyun.odps.mapred.utils.InputUtils;
import com.aliyun.odps.mapred.utils.OutputUtils;
import com.aliyun.odps.mapred.utils.SchemaUtils;

import hadoop.TianchiMapreduce.cart.Model;
import hadoop.TianchiMapreduce.cart.Tools;

public class cart_zsy {
	
	public static class MyMapper extends MapperBase {
	    Record id;
	    Record content;
	    
	    public void setup(TaskContext context) throws IOException {
	      id = context.createMapOutputKeyRecord();
	      content = context.createMapOutputValueRecord();
	    }

	    @Override
	    public void map(long recordNum, Record record, TaskContext context) throws IOException {
	    	
	    	String apid = record.getString(0);
    		Long time = record.getBigint(1);
    		double count = record.getDouble(2);
    		String apkey = apid.split("-")[0]+"-"+apid.split("-")[1];
    		id.set(new Object[] { apid });
    		content.set(new Object[] { time+"@"+count });
    		context.write(id, content);
    		
	    }
	  }
	
	 
	
	
	  /**
	   * A reducer class that just emits the sum of the input values.
	   */
	  public static class MyReducer extends ReducerBase {
	    private Record resultRecord;
	    private double[] personsNum;
	    private double[] personsNum_10min;
	    HashMap<Integer, ArrayList<Double>> currentTrainData;
	    @Override
	    public void setup(TaskContext context) throws IOException {
	      resultRecord = context.createOutputRecord();
	      
	      
	    }

	    @Override
	    public void reduce(Record key, Iterator<Record> values, TaskContext context) throws IOException {
	    	String apkey = key.getString(0);

	    	
	    	currentTrainData = new HashMap<Integer, ArrayList<Double>>();
	    	 while (values.hasNext()) {
	 	        Record val = values.next();
	 	        String value = val.getString(0);
	 	        String datestring = value.split("@")[0];
	 	        String countstring = value.split("@")[1];
	 	        Integer time = Integer.valueOf(datestring);
	 	        Double count = Double.valueOf(countstring);
	 	       ArrayList<Double> templist = null;
	 	        if (currentTrainData.containsKey(time)){
	 	        	templist = currentTrainData.get(time);
	 	        }
	 	        else{
	 	        	templist = new ArrayList<Double>();
	 	        	currentTrainData.put(time,templist);
	 	        }
	 	       templist.add(count);
	    }
	    	 double[] result = new double[144];
	    	 for (int frnum = 0;frnum<StaticValue.iternum;frnum++){
	    		  
	    		 HashMap<Integer, Double> result_mid = Model.train(Tools.sampling(currentTrainData, StaticValue.samplenum));
	    		 for (int timeid = 0;timeid<144;timeid++){
	    			 result[timeid] = result[timeid] + (double)result_mid.get(timeid)/(double)StaticValue.iternum;
	    		 }
	    	 }
	    	 for (int timeid = 0;timeid<144;timeid++){
	    		 resultRecord.set(0,apkey);
	    		 String timeString = UtilTools.getDateByNo(timeid);
	    		 resultRecord.set(2,"2016-11-10-"+timeString);
	    		 resultRecord.set(1, (double)result[timeid]);
	    		 context.write(resultRecord);
    		 }
	    	 
	    	 
	    	}
	    	 
	    	 
	  }
	  
	  
	  public static void main(String[] args) throws Exception {
		    JobConf job = new JobConf();
		    job.setMapperClass(MyMapper.class);
		    job.setReducerClass(MyReducer.class);

		    job.setMapOutputKeySchema(SchemaUtils.fromString("id:string"));
		    job.setMapOutputValueSchema(SchemaUtils.fromString("content:string"));
		    InputUtils.addTable(TableInfo.builder().tableName("mean_10min_zsy").build(), job);
		    OutputUtils.addTable(TableInfo.builder().tableName("airport_gz_passenger_predict").build(), job);
		    
		    RunningJob rj = JobClient.runJob(job);
		    rj.waitForCompletion();
		  }
	    
	  

}

