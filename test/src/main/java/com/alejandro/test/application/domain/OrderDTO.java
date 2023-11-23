package com.alejandro.test.application.domain;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class OrderDTO {

    private String uuid;
    private String id;
    private String region;
    private String country;
    @SerializedName("item_type")
    private String itemType;
    @SerializedName("sales_channel")
    private String salesChannel;
    private String priority;
    @SerializedName("date")
    private String orderDate;
    @SerializedName("ship_date")
    private String shipDate;
    @SerializedName("units_sold")
    private Integer unitsSold;
    @SerializedName("unit_price")
    private Double unitPrice;
    @SerializedName("unit_cost")
    private Double unitCost;
    @SerializedName("total_revenue")
    private Double totalRevenue;
    @SerializedName("total_cost")
    private Double totalCost;
    @SerializedName("total_profit")
    private Double totalProfit;
    private PagerLinksDTO links;
}
