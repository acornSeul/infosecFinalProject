package com.example.test_db.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Member {
    private String mem_id;
    private String userId;
    private String password;
    private String name;
    private String phone;
    private int zipCode;
    private String birth;
    private String type;
    private String address;

    public Member(String userId, String password, String name, String phone, int zipCode, String birth, String type, String address) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.zipCode = zipCode;
        this.birth = birth;
        this.type = type;
        this.address = address;
    }
}
