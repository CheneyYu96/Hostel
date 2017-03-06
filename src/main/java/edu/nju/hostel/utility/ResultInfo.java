package edu.nju.hostel.utility;

/**
 *
 * @author yuminchen
 * @date 2017/3/5
 * @version V1.0
 */
public class ResultInfo {

    private boolean result;

    private String info;

    public ResultInfo(boolean result) {
        this.result = result;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getInfo() {
        return info;
    }

    public boolean isSuccess(){
        return result;
    }
}
