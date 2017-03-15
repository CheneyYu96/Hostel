package edu.nju.hostel.utility;

import edu.nju.hostel.entity.InRecord;
import edu.nju.hostel.entity.Order;
import edu.nju.hostel.vo.LiveIn;
import edu.nju.hostel.vo.RoomTypePie;
import edu.nju.hostel.vo.Translator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yuminchen
 * @version V1.0
 * @date 2017/3/15
 */
public class Calculator {

    public static List<LiveIn> getLineW(List<Translator> translators, LocalDate begin, LocalDate end ){
        List<LiveIn> liveInList = new ArrayList<>();
        for(;begin.isBefore(end);begin = begin.plusWeeks(1)){
            LocalDate finalBegin = begin;
            LiveIn reduce = translators
                    .stream()
                    .filter(translator -> DateUtil.inNextWeek(finalBegin, translator.begin))
                    .map(translator -> new LiveIn(1, translator.pay, finalBegin))
                    .reduce(new LiveIn(0,0,finalBegin), (in1, in2) ->
                    {
                        in2.amount = in1.amount + in2.amount;
                        in2.money = in1.money + in2.money;
                        return in2;
                    });
            liveInList.add(reduce);
        }
        return liveInList;
    }

    public static List<LiveIn> getLineD(List<Translator> translators, LocalDate begin, LocalDate end ){
        List<LiveIn> liveInList = new ArrayList<>();
        for(;begin.isBefore(end);begin = begin.plusDays(1)){
            LocalDate finalBegin = begin;
            LiveIn reduce = translators
                    .stream()
                    .filter(translator -> translator.begin.isEqual(finalBegin))
                    .map(translator -> new LiveIn(1, translator.pay, finalBegin))
                    .reduce(new LiveIn(0,0,finalBegin), (in1, in2) ->
                    {
                        in2.amount = in1.amount + in2.amount;
                        in2.money = in1.money + in2.money;
                        return in2;
                    });
            liveInList.add(reduce);
        }
        return liveInList;
    }

    public static List<LiveIn> getLineM(List<Translator> translators, LocalDate begin, LocalDate end ){
        List<LiveIn> liveInList = new ArrayList<>();
        for(;begin.isBefore(end);begin = begin.plusMonths(1)){
            LocalDate finalBegin = begin;
            LiveIn reduce = translators
                    .stream()
                    .filter(translator -> DateUtil.inNextMonth(finalBegin, translator.begin))
                    .map(translator -> new LiveIn(1,translator.pay, finalBegin))
                    .reduce(new LiveIn(0,0,finalBegin), (in1, in2) ->
                    {
                        in2.amount = in1.amount + in2.amount;
                        in2.money = in1.money + in2.money;
                        return in2;
                    });
            liveInList.add(reduce);
        }
        return liveInList;
    }

    public static RoomTypePie getInPie(List<InRecord> inRecords) {
        RoomTypePie pie = new RoomTypePie();

        for (InRecord inRecord : inRecords) {
            switch (inRecord.getType()) {
                case 单床房:
                    pie.singleRoom++;
                    break;
                case 双床房:
                    pie.doubleRoom++;
                    break;
                case 大床房:
                    pie.bigRoom++;
                    break;
                case 套间:
                    pie.wholeRoom++;
                    break;
            }
        }
        return pie;
    }
    public static RoomTypePie getBookPie(List<Order> orderList) {

        RoomTypePie pie = new RoomTypePie();

        for (Order order : orderList) {
            switch (order.getType()) {
                case 单床房:
                    pie.singleRoom++;
                    break;
                case 双床房:
                    pie.doubleRoom++;
                    break;
                case 大床房:
                    pie.bigRoom++;
                    break;
                case 套间:
                    pie.wholeRoom++;
                    break;
            }
        }
        return pie;
    }

}
