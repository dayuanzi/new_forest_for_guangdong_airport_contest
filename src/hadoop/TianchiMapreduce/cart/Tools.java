package hadoop.TianchiMapreduce.cart;

/**
 * Created by chenwuji on 2016/11/13.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Tools {
    public static double mean(ArrayList<Double> list){
        double sum = 0;
        for(double i:list){
            sum += i;
        }
        return sum/list.size();
    }

    public static double meanOfMatrix(ArrayList<ArrayList<Double>> list){
        double sum = 0;
        int count = 0;
        for(ArrayList<Double> i:list){
            for(double j:i) {
                sum += j;
                count++;
            }
        }
        return sum/(double)count;
    }


    public static double sumOfList(ArrayList<Double> list){
        double sum = 0;
        for(double i:list){
            sum += i;
        }
        return sum;
    }

    public static double sumOfMatrix(ArrayList<ArrayList<Double>> list){
        double sum = 0;
        for(ArrayList<Double> i:list){
            for(double j:i) {
                sum += j;
            }
        }
        return sum;
    }

    public static void matrixDecreaseDouble(ArrayList<ArrayList<Double>> list, double certainValue){
        for(ArrayList<Double> i:list)
            for(int j = 0;j < i.size(); j++){
                i.set(j,i.get(j) - certainValue);
            }
    }

    public static void matrixAbs(ArrayList<ArrayList<Double>> list){
        for(ArrayList<Double> i:list)
            for(int j = 0;j < i.size(); j++){
                i.set(j,Math.pow(i.get(j),2));
            	//i.set(j,Math.abs(i.get(j)));
            }
    }

    public static ArrayList<ArrayList<Double>> values(HashMap<?,ArrayList<Double>> map){
        ArrayList<ArrayList<Double>> listAll = new ArrayList<>();
        for(ArrayList<Double> eachList:map.values()){
            listAll.add((ArrayList<Double>) eachList.clone());
        }
        return listAll;
    }

    public static ArrayList<Integer> keys(HashMap<Integer,ArrayList<Double>> map){
        ArrayList<Integer> listAll = new ArrayList<>();
        for(Integer key1:map.keySet()){
            listAll.add(key1);
        }
        return listAll;
    }

    public static void sort(ArrayList<Integer> list){
        Collections.sort(list);
    }
    
    public static HashMap<Integer,ArrayList<Double>> sampling(HashMap<Integer,ArrayList<Double>> data, int num){
    	HashMap<Integer,ArrayList<Double>> sampled = new HashMap<Integer,ArrayList<Double>>();
        Set<Integer> set = new HashSet<Integer>();
        if (data.size()<1) return null;
        if(num > data.get(0).size()) return null;
        while(set.size() < num){
            set.add((int)(Math.random() * data.get(0).size()));
        }
        for(Integer i:set)
        {
        	for (int j=0;j<144;j++){ 
        		ArrayList<Double> tempData = null;
        		if(sampled.containsKey(j)){
        			tempData = sampled.get(j);
        		}
        		else{
        			tempData = new ArrayList<Double>();
        			sampled.put(j, tempData);
        		}
        		tempData.add(data.get(j).get(i));
           
        		}
        }
        return sampled;
        
    }
		
    public static void removeNull(ArrayList<Double> list){
        double mean = 0.0;
        int count = 0;
        for(Double i:list){
            if(i == -1.0) {
                mean += i;
                count++;
            }
        }
        mean = mean/count;
        for(int i =0;i<list.size();i++){
            if(list.get(i) == -1.0) {
                list.set(i, mean);
            }
        }
    }

    
}
