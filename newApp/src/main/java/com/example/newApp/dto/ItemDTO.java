package com.example.newApp.dto;

public class ItemDTO {
    private String name;
    private String itemCode;
    private String itemName;
    private String itemGroup;
    private String stockUom;
    private Double lastPurchaseRate;
    private String countryOfOrigin;
    private Boolean isPurchaseItem;

    // Getters et Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

    public String getStockUom() {
        return stockUom;
    }

    public void setStockUom(String stockUom) {
        this.stockUom = stockUom;
    }

    public Double getLastPurchaseRate() {
        return lastPurchaseRate;
    }

    public void setLastPurchaseRate(Double lastPurchaseRate) {
        this.lastPurchaseRate = lastPurchaseRate;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public Boolean getIsPurchaseItem() {
        return isPurchaseItem;
    }

    public void setIsPurchaseItem(Boolean isPurchaseItem) {
        this.isPurchaseItem = isPurchaseItem;
    }
}
