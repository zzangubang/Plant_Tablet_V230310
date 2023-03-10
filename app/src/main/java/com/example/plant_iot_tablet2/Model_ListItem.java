package com.example.plant_iot_tablet2;

public class Model_ListItem {
    String name, model;
    String id;

    public Model_ListItem(String name, String model, String id) {
        this.name = name;
        this.model = model;
        this.id = id;
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
}
