package hadoop.TianchiMapreduce.cart;

import java.util.HashMap;
import java.util.ArrayList;

/**
 * Created by chenwuji on 2016/11/12.
 */

public class Model {
    private static HashMap<Integer, Double> result = new HashMap<>();

    public static void main(String args[]){
        HashMap<String, HashMap<Integer,ArrayList<Double>>> predict = ReadFile.readPredictData();
        HashMap<String, HashMap<Integer,ArrayList<Double>>> train = ReadFile.readTrainData();
        /**********Predict Data************/
        for(String apid:train.keySet())
        {
            HashMap<Integer,ArrayList<Double>> currentTrainData = train.get(apid);
            train(currentTrainData);
        }
        /**********Predict Result************/
        System.out.println("Final Result Of one Ap:" + judgeResult(result, predict.get("T1-1C<T1-1C-04>")));
        System.out.println("Final Result Of one Ap:" + judgeResult(result, predict.get("T1-1C<T1-1C-04>")));

    }

    public static HashMap<Integer, Double> train(HashMap<Integer,ArrayList<Double>> ap_data){
    	for (Integer i:ap_data.keySet()){
    		Tools.removeNull(ap_data.get(i));
    	}
        cart(ap_data);
        return result;
    }

    private static double judgeResult(HashMap<Integer, Double> result, HashMap<Integer, ArrayList<Double>> predict){
        double score = 0;
        for(Integer i:result.keySet()){
            score += Math.pow((result.get(i) - predict.get(i).get(0)),2);
        }
        return score;
    }


    private static void cart(HashMap<Integer,ArrayList<Double>> mid_data){
        if(mid_data.size() == 1){
            result.put(Tools.keys(mid_data).get(0), Tools.meanOfMatrix(Tools.values(mid_data)));
            return;
        }
        //double localMax = lossFun(mid_data);  //鍏ㄥ眬鏈�澶х殑鎹熷け鍑芥暟
        double localMax = lossFun(mid_data);  //鍏ㄥ眬鏈�澶х殑鎹熷け鍑芥暟
        short isCut = 1;
        ArrayList<Integer> keyset = Tools.keys(mid_data);
        Tools.sort(keyset);
        HashMap<Integer, ArrayList<Double>> localLeft = new HashMap<Integer,ArrayList<Double>>();
        HashMap<Integer, ArrayList<Double>> localRight = new HashMap<Integer,ArrayList<Double>>();
        for(int i = 0; i< keyset.size();i++){//i鏄乏杈规寚閽�
            for(int j = i+1 ;j < keyset.size(); j++){//j鏄彸杈圭殑鎸囬拡
                HashMap<Integer, ArrayList<Double>> inData = new HashMap<Integer,ArrayList<Double>>();
                HashMap<Integer, ArrayList<Double>> outData = new HashMap<Integer,ArrayList<Double>>();
                for(Integer k:keyset){//涓嶆槸璁℃暟鍊间簡  鐩存帴鏄泦鍚堥噷闈㈢殑鍏冪礌
                    if(keyset.get(i) <= k && k < keyset.get(j)){//鍦ㄤ袱涓寚閽堝唴 鍔犲叆鍒版柊鐨凪ap閲岄潰  鍚﹀垯鏀惧湪澶栭潰
                        inData.put(k, (ArrayList<Double>) mid_data.get(k).clone());
                    }
                    else{
                        outData.put(k, (ArrayList<Double>) mid_data.get(k).clone());
                    }
                }
                double localLoss = lossFun(inData, outData);  //褰撳墠鐨勬崯澶卞��
                if(localLoss > localMax) {
                    isCut = 0;//缁х画鍚戜笅鎼滅储 鍚庨潰闇�瑕佸仛浜ゅ弶妫�楠岀殑姝ラ
                    //System.out.println("current Loss:" + localLoss);
                    localMax = localLoss;
                    localLeft = inData;
                    localRight = outData;
                }
            }
        }
        
        if(isCut != 1){//杩橀渶瑕佺户缁潵鎵╁睍
            double errorMeanLeft = errorMean(localLeft);
            double errorMeanRight = errorMean(localRight);
            double errorMeanMid = errorMean(mid_data);
            if(errorMeanLeft + errorMeanRight > errorMeanMid){
                isCut = 1;
                //System.out.println("Jiaocha:" + errorMeanLeft + "," + errorMeanRight + "," + errorMeanMid);
            }
        }
        
        if(isCut == 1) {
            for (Integer keyResult : Tools.keys(mid_data)) {
                result.put(keyResult, Tools.meanOfMatrix(Tools.values(mid_data)));
            }
            return;
        }
        
        //System.out.println(localLeft);
        //System.out.println(localRight);
        cart(localLeft);
        cart(localRight);
    }


    private static double lossFun(HashMap<?,ArrayList<Double>> mid_data){
        ArrayList<ArrayList<Double>> matrix = Tools.values(mid_data);
        double matrixAvg = Tools.meanOfMatrix(matrix);
        Tools.matrixDecreaseDouble(matrix, matrixAvg);
        Tools.matrixAbs(matrix);
        return 1.0/Math.log(Tools.sumOfMatrix(matrix));
    }

    private static double lossFun(HashMap<?,ArrayList<Double>> in_data, HashMap<?,ArrayList<Double>> out_data){
        ArrayList<ArrayList<Double>> matrix = Tools.values(in_data);
        double matrixAvg = Tools.meanOfMatrix(matrix);
        Tools.matrixDecreaseDouble(matrix, matrixAvg);
        Tools.matrixAbs(matrix);

        ArrayList<ArrayList<Double>> matrix2 = Tools.values(out_data);
        double matrixAvg2 = Tools.meanOfMatrix(matrix2);
        Tools.matrixDecreaseDouble(matrix2, matrixAvg2);
        Tools.matrixAbs(matrix2);

        return 1.0/Math.log(Tools.sumOfMatrix(matrix) + Tools.sumOfMatrix(matrix2));
    }

    private static double errorMean(HashMap<Integer, ArrayList<Double>> localLeft){
        int dayCount = Tools.values(localLeft).get(0).size();
        ArrayList<Double> errorMeanLeft = new ArrayList<>();
        for(int i = 0; i < dayCount; i++){
            ArrayList<Double> jiaochaLabel = new ArrayList<>();
            double jiaocha = 0;
            int count = 0;
            for(ArrayList<Double> line:Tools.values(localLeft)){
                for(int j = 0; j < line.size(); j++)
                {
                    if(j!=i)
                    {
                        count += 1;
                        jiaocha = jiaocha + (double)line.get(j);
                    }
                    else{
                        jiaochaLabel.add(line.get(j));
                    }
                }
            }
            jiaocha = jiaocha/count;
            ArrayList<Double> errorlist = new ArrayList<>();
            for(double j:jiaochaLabel){
                errorlist.add(Math.abs(j - jiaocha));
            }
            errorMeanLeft.add(Tools.sumOfList(errorlist));
        }
        return Tools.mean(errorMeanLeft);
    }




}


