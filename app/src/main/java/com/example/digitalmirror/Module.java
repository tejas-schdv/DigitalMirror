package com.example.digitalmirror;

public class Module {
    private String name;
    private String logo;
    private String checkBox;

    public Module(String name, String logo, String checkBox) {
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

    public String getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(String checkBox) {
        this.checkBox = checkBox;
    }
}
