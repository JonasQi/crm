package com.jqcool.crm.dto;

public class TreeDto {

    private Integer id;
    private String name;
    private String pId;
    private boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public TreeDto() {
    }

    public TreeDto(Integer id, String name, String pId) {
        this.id = id;
        this.name = name;
        this.pId = pId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    @Override
    public String toString() {
        return "TreeDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pId='" + pId + '\'' +
                ", checked=" + checked +
                '}';
    }
}
