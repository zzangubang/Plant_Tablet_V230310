package com.example.plant_iot_tablet2;

public class MasterActivity_Work_User_Parent_ListItem {
    String name, phone, date;
    public MasterActivity_Work_User_Parent_ListItem(String name, String phone, String date) {
        this.name = name;
        this.phone = phone;
        this.date = date;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}
