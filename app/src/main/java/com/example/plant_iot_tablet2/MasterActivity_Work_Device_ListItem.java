package com.example.plant_iot_tablet2;

public class MasterActivity_Work_Device_ListItem {
    String model, lastDate, id, name, addDate;

    public MasterActivity_Work_Device_ListItem(String model, String lastDate, String id, String name, String addDate) {
        this.model = model;
        this.lastDate = lastDate;
        this.id = id;
        this.name = name;
        this.addDate = addDate;
    }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getLastDate() { return lastDate; }
    public void setLastDate(String lastDate) { this.lastDate = lastDate; }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddDate() { return addDate; }
    public void setAddDate(String addDate) { this.addDate = addDate; }
}
