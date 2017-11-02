package com.prod.sudesi.lotusherbalsnew.Models;

/**
 * Created by Admin on 01-11-2017.
 */

public class StockModel {

    String A_id;
    String ba_code;
    String stock_received;
    String opening_stock;
    String sold_stock;
    String close_bal;
    String total_gross_amount;
    String discount;
    String total_net_amount;
    String currentdate;
    String stroutletcode;

    public String getA_id() {
        return A_id;
    }

    public void setA_id(String a_id) {
        A_id = a_id;
    }

    public String getBa_code() {
        return ba_code;
    }

    public void setBa_code(String ba_code) {
        this.ba_code = ba_code;
    }

    public String getStock_received() {
        return stock_received;
    }

    public void setStock_received(String stock_received) {
        this.stock_received = stock_received;
    }

    public String getOpening_stock() {
        return opening_stock;
    }

    public void setOpening_stock(String opening_stock) {
        this.opening_stock = opening_stock;
    }

    public String getSold_stock() {
        return sold_stock;
    }

    public void setSold_stock(String sold_stock) {
        this.sold_stock = sold_stock;
    }

    public String getClose_bal() {
        return close_bal;
    }

    public void setClose_bal(String close_bal) {
        this.close_bal = close_bal;
    }

    public String getTotal_gross_amount() {
        return total_gross_amount;
    }

    public void setTotal_gross_amount(String total_gross_amount) {
        this.total_gross_amount = total_gross_amount;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTotal_net_amount() {
        return total_net_amount;
    }

    public void setTotal_net_amount(String total_net_amount) {
        this.total_net_amount = total_net_amount;
    }

    public String getCurrentdate() {
        return currentdate;
    }

    public void setCurrentdate(String currentdate) {
        this.currentdate = currentdate;
    }

    public String getStroutletcode() {
        return stroutletcode;
    }

    public void setStroutletcode(String stroutletcode) {
        this.stroutletcode = stroutletcode;
    }



}
