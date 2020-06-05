package com.coding.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "tab_motor_item")
public class MotorItem extends BaseDO {

    private String name;
    private String torque;
    private String weight;
    private String displacement;
    private String tankVolume;
    private String picRes;
    private String price;
    private String horsePower;

}
