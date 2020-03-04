package com.example.digitalmirror;

import android.widget.CheckBox;

public class Module {
    private String name;
    private String logo;
    private CheckBox checkBox;

    public Module(String name, String logo, CheckBox checkBox) {
        this.name = name;
        this.logo = logo;
        this.checkBox = checkBox;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public boolean getCheckBoxValue() {
        return checkBox.isChecked();
    }

    public CheckBox getCheckBox(){
        return this.checkBox;
    }

    public void setCheckBox(CheckBox checkBox){
        this.checkBox = checkBox;
    }

    public void setCheckBoxValue(boolean bool) {
        if (bool)
            this.checkBox.setChecked(true);
        else
            this.checkBox.setChecked(false);
    }
}
