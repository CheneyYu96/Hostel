package edu.nju.hostel.utility;

/**
 *
 * @author yuminchen
 * @date 2017/3/5
 * @version V1.0
 */
public class FormatHelper {

    /**
     * format 7 bits string id to integer id
     * @param id
     * @return
     */
    public static int String2Id(String id){
        if(id.length()!=7){
            return -1;
        }

        int index = 0;
        for(;index<7;index++){
            if(id.charAt(index)!='0'){
                break;
            }
        }

        String realId =id.substring(index);
        return Integer.parseInt(realId);
    }

    /**
     * convert int to 7 bits String
     * @param id
     * @return
     */
    public static String Id2String(int id){
        if(id>9999999){
            return "too long";
        }
        String result =id+"";
        for(int i = result.length();i < 7; i++){
            result = "0"+result;
        }
        return result;
    }
}
