package com.example.plant_iot_tablet2;

public class Model_Delete_ListItem {
    String name, model;
    String id;

    String check;

    public Model_Delete_ListItem(String name, String model, String id, String check) {
        this.name = name;
        this.model = model;
        this.id = id;
        this.check = check;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }
}
