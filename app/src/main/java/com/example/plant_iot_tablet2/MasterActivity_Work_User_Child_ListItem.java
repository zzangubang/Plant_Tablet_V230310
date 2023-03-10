package com.example.plant_iot_tablet2;

public class MasterActivity_Work_User_Child_ListItem {
    String model, date;
    public MasterActivity_Work_User_Child_ListItem(String model, String date) {
        this.model = model;
        this.date = date;
    }

    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}
