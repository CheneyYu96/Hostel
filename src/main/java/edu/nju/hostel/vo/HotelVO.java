package edu.nju.hostel.vo;

import java.util.List;

/**
 * @author yuminchen
 * @version V1.0
 * @date 2017/3/15
 */
public class HotelVO {
    public int id;
    public String name;
    public String address;
    public List<Integer> plan;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Integer> getPlan() {
        return plan;
    }

    public void setPlan(List<Integer> plan) {
        this.plan = plan;
    }
}
