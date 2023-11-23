package com.alejandro.test.application.model.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderGroupByItemType {

    private String itemType;
    private Long total;

    public OrderGroupByItemType(String itemType, Long total) {
        this.itemType = itemType;
        this.total = total;
    }
}
