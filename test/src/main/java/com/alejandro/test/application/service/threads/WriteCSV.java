package com.alejandro.test.application.service.threads;

import com.alejandro.test.application.domain.OrderTotalsDTO;
import com.alejandro.test.application.model.order.Order;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class WriteCSV <T> {

    private BufferedWriter bw;

    private LineNumberReader lnr;

    private static OrderTotalsDTO orderTotalsDTO;

    private List<T> dataList;

    public WriteCSV(List<T> dataList, BufferedWriter bw, LineNumberReader lnr) {
        this.dataList = dataList;
        this.bw = bw;
        this.lnr = lnr;
    }
    
//    @Override
    public void generateCSV() {
        Instant start = Instant.now();
        System.out.println("Doing Write CSV " + dataList.size());
        try {
            lnr.setLineNumber(0);
            if(dataList.get(0) instanceof Order)
                this.printOrderCSV();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        System.out.println("Finish Write CSV("+ dataList.size() +"): " + timeElapsed);
    }

    private void printOrderCSV() throws IOException {
        this.orderTotalsDTO = new OrderTotalsDTO();
        for(Order order : (List<Order>)dataList) {
            this.orderTotalsDTO.addTotals(order);
            bw.write(order.toString());
            bw.newLine();
        }
        this.printTotalsInCSV(orderTotalsDTO.getRegionTotalMap(), "Region");
        this.printTotalsInCSV(orderTotalsDTO.getCountryTotalMap(), "Country");
        this.printTotalsInCSV(orderTotalsDTO.getItemTypeTotalMap(), "ItemType");
        this.printTotalsInCSV(orderTotalsDTO.getSalesChannelTotalMap(), "Sales Channel");
        this.printTotalsInCSV(orderTotalsDTO.getOrderPriorityTotalMap(), "Order Priority");
    }
    private void printTotalsInCSV(Map<String, Integer> map, String totalName) throws IOException {
        bw.newLine();
        bw.write(String.format("%s,%s", totalName, "Total"));
        bw.newLine();
        map = map.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getValue))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (a,b)->b, LinkedHashMap::new));
        for(String key: map.keySet()) {
            bw.write(String.format("%s,%s", key, map.get(key)));
            bw.newLine();
        }
    }

    
}
