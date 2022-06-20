package com.example.project_x.apis;

public class contractinfo {
    public String contract_number;
    public String staff_name;
    public String client_enterprise;
    public String supply_center;
    public String product_model;
    public String order_quantity;
    public String unit_price;
    public String estimated_delivery_time;
    public String lodgement_date;

    public contractinfo(String contract_number, String staff_name, String client_enterprise, String supply_center, String product_model, String order_quantity, String unit_price, String estimated_delivery_time, String lodgement_date) {
        this.contract_number = contract_number;
        this.staff_name = staff_name;
        this.client_enterprise = client_enterprise;
        this.supply_center = supply_center;
        this.product_model = product_model;
        this.order_quantity = order_quantity;
        this.unit_price = unit_price;
        this.estimated_delivery_time = estimated_delivery_time;
        this.lodgement_date = lodgement_date;
    }

    public String getContract_number() {
        return contract_number;
    }

    public void setContract_number(String contract_number) {
        this.contract_number = contract_number;
    }

    public String getStaff_name() {
        return staff_name;
    }

    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }

    public String getClient_enterprise() {
        return client_enterprise;
    }

    public void setClient_enterprise(String client_enterprise) {
        this.client_enterprise = client_enterprise;
    }

    public String getSupply_center() {
        return supply_center;
    }

    public void setSupply_center(String supply_center) {
        this.supply_center = supply_center;
    }

    public String getProduct_model() {
        return product_model;
    }

    public void setProduct_model(String product_model) {
        this.product_model = product_model;
    }

    public String getOrder_quantity() {
        return order_quantity;
    }

    public void setOrder_quantity(String order_quantity) {
        this.order_quantity = order_quantity;
    }

    public String getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(String unit_price) {
        this.unit_price = unit_price;
    }

    public String getEstimated_delivery_time() {
        return estimated_delivery_time;
    }

    public void setEstimated_delivery_time(String estimated_delivery_time) {
        this.estimated_delivery_time = estimated_delivery_time;
    }

    public String getLodgement_date() {
        return lodgement_date;
    }

    public void setLodgement_date(String lodgement_date) {
        this.lodgement_date = lodgement_date;
    }

    public contractinfo() {

    }

    @Override
    public String toString() {
        return "{" +
                "contract_number='" + contract_number + '\'' +
                ", staff_name='" + staff_name + '\'' +
                ", client_enterprise='" + client_enterprise + '\'' +
                ", supply_center='" + supply_center + '\'' +
                ", product_model='" + product_model + '\'' +
                ", order_quantity='" + order_quantity + '\'' +
                ", unit_price='" + unit_price + '\'' +
                ", estimated_delivery_time='" + estimated_delivery_time + '\'' +
                ", lodgement_date='" + lodgement_date + '\'' +
                '}';
    }
}
