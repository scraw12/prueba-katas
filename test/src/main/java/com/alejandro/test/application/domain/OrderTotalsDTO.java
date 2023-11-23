package com.alejandro.test.application.domain;

import com.alejandro.test.application.model.order.Order;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class OrderTotalsDTO {

    private Map<String, Integer> regionTotalMap = new HashMap<>();
    private Map<String, Integer> countryTotalMap = new HashMap<>();
    private Map<String, Integer> itemTypeTotalMap = new HashMap<>();
    private Map<String, Integer> salesChannelTotalMap = new HashMap<>();
    private Map<String, Integer> orderPriorityTotalMap = new HashMap<>();

    public void addTotals(Order o) {
        this.regionTotalMap.put(o.getRegion(), this.regionTotalMap .getOrDefault(o.getRegion(),0)+1);
        this.countryTotalMap.put(o.getCountry(),this.countryTotalMap.getOrDefault(o.getCountry(),0)+1);
        this.itemTypeTotalMap.put(o.getItemType(), this.itemTypeTotalMap.getOrDefault(o.getItemType(),0)+1);
        this.salesChannelTotalMap.put(o.getSalesChannel(), this.salesChannelTotalMap.getOrDefault(o.getSalesChannel(),0)+1);
        this.orderPriorityTotalMap.put(o.getPriority(), this.orderPriorityTotalMap.getOrDefault(o.getPriority(),0)+1);
    }
}
