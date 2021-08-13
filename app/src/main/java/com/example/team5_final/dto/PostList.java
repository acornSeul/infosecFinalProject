package com.example.team5_final.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostList {
    private String in_num;
    private String address;
    private String name;
    private String state;
    private String tag_id;
    private boolean isSelected;

    public PostList(String in_num, String address, String name, String state, String tag_id) {
        this.in_num = in_num;
        this.address = address;
        this.name = name;
        this.state = state;
        this.tag_id = tag_id;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}


