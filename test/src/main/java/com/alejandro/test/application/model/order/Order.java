package com.alejandro.test.application.model.order;

import com.alejandro.test.application.domain.PagerLinksDTO;
import com.google.gson.annotations.SerializedName;
import org.hibernate.MappingException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "order", schema = "public")
public class Order {

    @Id
    private String id;

    private String uuid;
    private String region;
    private String country;
    @Column(name = "item_type")
    private String itemType;
    @Column(name = "sales_channel")
    private String salesChannel;
    private String priority;
    @Column(name = "order_date")
    private LocalDate orderDate;
    @Column(name = "ship_date")
    private LocalDate shipDate;
    @Column(name = "units_sold")
    private Integer unitsSold;
    @Column(name = "unit_price")
    private Double unitPrice;
    @Column(name = "unit_cost")
    private Double unitCost;
    @Column(name = "total_revenue")
    private Double totalRevenue;
    @Column(name = "total_cost")
    private Double totalCost;
    @Column(name = "total_profit")
    private Double totalProfit;


    public Order() {
    }

    @Override
    public String toString() {
        List<String> returnString = new ArrayList<>(Arrays.asList(
                this.getId(), this.getPriority(), this.formattedDate(this.getOrderDate()), this.getRegion(), this.getCountry(), this.getItemType(),
                this.getSalesChannel(), this.formattedDate(this.getShipDate()), String.valueOf(this.getUnitsSold()),
                String.valueOf(this.getUnitPrice()), String.valueOf(this.getUnitCost()),
                String.valueOf(this.getTotalRevenue()), String.valueOf(this.getTotalCost()), String.valueOf(this.getTotalProfit())));
        return returnString.stream().collect(Collectors.joining(","));
    }

    public String formattedDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getSalesChannel() {
        return salesChannel;
    }

    public void setSalesChannel(String salesChannel) {
        this.salesChannel = salesChannel;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = LocalDate.parse(orderDate, DateTimeFormatter.ofPattern("M/d/yyyy"));
    }

    public LocalDate getShipDate() {
        return shipDate;
    }

    public void setShipDate(LocalDate shipDate) {
        this.shipDate = shipDate;
    }

    public void setShipDate(String shipDate) throws Exception{
        this.shipDate = LocalDate.parse(shipDate, DateTimeFormatter.ofPattern("M/d/yyyy"));
    }

    public Integer getUnitsSold() {
        return unitsSold;
    }

    public void setUnitsSold(Integer unitsSold) {
        this.unitsSold = unitsSold;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(Double unitCost) {
        this.unitCost = unitCost;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public Double getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(Double totalProfit) {
        this.totalProfit = totalProfit;
    }
}
