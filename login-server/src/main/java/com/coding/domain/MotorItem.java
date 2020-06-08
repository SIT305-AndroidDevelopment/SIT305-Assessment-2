package com.coding.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "tab_motor_item")
public class MotorItem extends BaseDO {

    private String name;
    private Integer horsePower;
    private Integer torque;
    private Integer weight;
    private Integer displacement;
    private Integer tankVolume;
    private String picRes;
    private Integer price;
    @ApiModelProperty("品牌")
    private Long brandId;

    @ApiModelProperty("是否收藏")
    @Transient
    private boolean collected;

}
