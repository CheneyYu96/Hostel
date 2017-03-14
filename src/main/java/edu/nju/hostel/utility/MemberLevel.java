package edu.nju.hostel.utility;

public enum MemberLevel {
    顶级(3000),
    高级(1000),
    普通(0);

    private int amount;

    MemberLevel(int amount) {
        this.amount = amount;
    }

    public static MemberLevel getLevel(int amount){
        if(amount >= 3000){
            return 顶级;
        }
        if(amount >= 1000){
            return 高级;
        }
        return 普通;
    }

    public static int getDiscount(int amount){
        if(amount >= 3000){
            return 70;
        }
        if(amount >= 1000){
            return 80;
        }
        return 90;
    }


}
