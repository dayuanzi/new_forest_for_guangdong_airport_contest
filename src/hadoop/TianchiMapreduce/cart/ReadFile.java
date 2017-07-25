package hadoop.TianchiMapreduce.cart;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * Created by chenwuji on 2016/11/12.
 */
public class ReadFile {


    public static HashMap<String, HashMap<Integer,ArrayList<Double>>> readPredictData(){
        HashMap<String, HashMap<Integer,ArrayList<Double>>> predict = readTrainAndPredictData("predict_1109.csv");
        return predict;
    }

    public static HashMap<String, HashMap<Integer,ArrayList<Double>>> readTrainData(){
        HashMap<String, HashMap<Integer,ArrayList<Double>>> train = readTrainAndPredictData("train_1109.csv");
        return train;
    }
    
    private static HashMap<String, HashMap<Integer,ArrayList<Double>>> readTrainAndPredictData(String filename){
        List<String> lineList = readFileByLines(filename);
        HashMap<String,HashMap<Integer,ArrayList<Double>>> predictData = new HashMap<>();
        for(String line:lineList){
            String col[] = line.split(",");
            String apid = col[0];
            int time = Integer.parseInt(col[1]);
            double value = Double.parseDouble(col[2]);
            HashMap<Integer,ArrayList<Double>> tempData = null;
            if(predictData.containsKey(apid)){
                tempData = predictData.get(apid);
            }
            else{
                tempData = new HashMap<>();
                predictData.put(apid, tempData);
            }
            ArrayList<Double> tempData2 = null;
            if(tempData.containsKey(time)){
                tempData2 = tempData.get(time);
            }
            else{
                tempData2 = new ArrayList<Double>();
                tempData.put(time,tempData2);
            }
            tempData2.add(value);
        }
        return predictData;
    }

    private static List<String> readFileByLines(String fileName) {
        File file = new File(fileName);
        List<String> data1 = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 涓�娆¤鍏ヤ竴琛岋紝鐩村埌璇诲叆null涓烘枃浠剁粨鏉�
            while ((tempString = reader.readLine()) != null) {
                // 鏄剧ず琛屽彿
                data1.add(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return data1;
    }

    public static void main(String args[]){
        System.out.println(System.getProperty("user.dir"));
        readFileByLines("predict_1109.csv");
    }

}


