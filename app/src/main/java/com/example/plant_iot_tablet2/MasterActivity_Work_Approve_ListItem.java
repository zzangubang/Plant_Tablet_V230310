package com.example.plant_iot_tablet2;

public class MasterActivity_Work_Approve_ListItem {
    String name, phone, date, approve;
    int approveC, button_approve, button_delete;
    boolean enable;

    public MasterActivity_Work_Approve_ListItem(String name, String phone, String date, String approve, int approveC, int button_approve, int button_delete, boolean enable) {
        this.name = name;
        this.phone = phone;
        this.date = date;
        this.approve = approve;
        this.approveC = approveC;
        this.button_approve = button_approve;
        this.button_delete = button_delete;
        this.enable = enable;
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

    public String getApprove() {
        return approve;
    }

    public void setApprove(String approve) {
        this.approve = approve;
    }

    public int getApproveC() {
        return approveC;
    }

    public void setApproveC(int approveC) {
        this.approveC = approveC;
    }

    public int getApproveB() {
        return button_approve;
    }

    public void setApproveB(int button_approve) {
        this.button_approve = button_approve;
    }

    public int getDeleteB() {
        return button_delete;
    }

    public void setDeleteB(int button_delete) {
        this.button_delete = button_delete;
    }

    public boolean getEnable() {
        return enable;
    }
    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
