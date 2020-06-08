package com.coding.vo;

import lombok.Data;

/**
 * @author guanweiming
 */
@Data
public class UserVO {
    private String name;
    private String phone;

    public UserVO(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }
}
