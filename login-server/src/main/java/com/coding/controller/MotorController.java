package com.coding.controller;

import com.coding.common.Const;
import com.coding.config.AppProperties;
import com.coding.domain.*;
import com.coding.mapper.*;
import com.coding.service.MinIOFileService;
import com.coding.service.UserService;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.guanweiming.common.utils.Result;
import io.minio.errors.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author guanweiming
 */
@Slf4j
@Api(tags = "motor接口")
@AllArgsConstructor
@RequestMapping(Const.API + "motor")
@RestController
public class MotorController {

    private final MotorItemMapper motorItemMapper;
    private final MotorCollectMapper motorCollectMapper;
    private final MotorFootMapper motorFootMapper;
    private final BrandMapper brandMapper;
    private final BrandCollectMapper brandCollectMapper;

    @ApiOperation("添加接口")
    @PostMapping("add")
    public Result<MotorItem> addMotor(MotorItem param) {
        motorItemMapper.insertSelective(param);
        return Result.createBySuccess();
    }

    @ApiOperation("添加品牌接口")
    @PostMapping("addBrand")
    public Result<Brand> addBrand(Brand param) {
        brandMapper.insertSelective(param);
        return Result.createBySuccess();
    }

    @ApiOperation("品牌列表接口")
    @GetMapping("brandList")
    public Result<List<Brand>> brandList() {
        List<Brand> list = brandMapper.selectAll();
        return Result.createBySuccess(list);
    }


    @ApiOperation("详情接口")
    @PostMapping("detail")
    public Result<MotorItem> detailMotor(@RequestParam Long motorId, Long userId) {
        MotorItem motorItem = motorItemMapper.selectByPrimaryKey(motorId);
        if (userId != null) {
            MotorFoot motorFoot = new MotorFoot();
            motorFoot.setUserId(userId);
            motorFoot.setMotorId(motorId);
            motorFootMapper.insertSelective(motorFoot);
        }
        if (motorItem != null) {
            MotorCollect motorCollect = new MotorCollect();
            motorCollect.setMotorId(motorId);
            motorCollect.setUserId(userId);
            int count = motorCollectMapper.selectCount(motorCollect);
            motorItem.setCollected(count > 0);
        }
        return Result.createBySuccess(motorItem);
    }

    @ApiOperation("添加接口")
    @PostMapping("update")
    public Result<MotorItem> updateMotor(MotorItem param) {
        motorItemMapper.updateByPrimaryKeySelective(param);
        return Result.createBySuccess();
    }

    @ApiOperation("摩托车列表接口")
    @GetMapping("list")
    public Result<List<MotorItem>> list(Long brandId) {
        if (brandId != null) {
            MotorItem record = new MotorItem();
            record.setBrandId(brandId);
            return Result.createBySuccess(motorItemMapper.select(record));
        }
        List<MotorItem> motorItems = motorItemMapper.selectAll();
        return Result.createBySuccess(motorItems);
    }

    @ApiOperation("添加收藏接口")
    @PostMapping("addCollect")
    public Result<MotorItem> addCollect(MotorCollect param) {
        if (param.getMotorId() != null) {
            MotorItem motorItem = motorItemMapper.selectByPrimaryKey(param.getMotorId());
            if (motorItem != null) {
                BrandCollect record = new BrandCollect();
                record.setUserId(param.getUserId());
                record.setBrandId(motorItem.getBrandId());
                brandCollectMapper.insertSelective(record);
            }
        }
        motorCollectMapper.insertSelective(param);
        return Result.createBySuccess();
    }

    @ApiOperation("删除收藏接口")
    @PostMapping("removeCollect")
    public Result<MotorItem> removeCollect(@RequestParam Long collectId, @RequestParam Long userId) {
        MotorCollect motorCollect = new MotorCollect();
        motorCollect.setUserId(userId);
        motorCollect.setMotorId(collectId);
        motorCollectMapper.delete(motorCollect);
        return Result.createBySuccess();
    }


    @ApiOperation("收藏列表接口")
    @GetMapping("collectList")
    public Result<List<MotorItem>> collectList(@RequestParam Long userId) {
        if (userId == null) {
            return Result.createBySuccess(Lists.newArrayList());
        }
        MotorCollect motorCollect = new MotorCollect();
        motorCollect.setUserId(userId);
        PageHelper.orderBy("id desc");
        List<Long> idList = motorCollectMapper.select(motorCollect).stream().map(MotorCollect::getMotorId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(idList)) {
            return Result.createBySuccess(Lists.newArrayList());
        }
        List<MotorItem> motorItems = motorItemMapper.selectByIdList(idList);
        return Result.createBySuccess(motorItems);
    }

    @ApiOperation("关注接口")
    @GetMapping("collectBrandList")
    public Result<List<Brand>> collectBrandList(@RequestParam Long userId) {
        if (userId == null) {
            return Result.createBySuccess(Lists.newArrayList());
        }
        BrandCollect motorCollect = new BrandCollect();
        motorCollect.setUserId(userId);
        PageHelper.orderBy("id desc");
        List<Long> idList = brandCollectMapper.select(motorCollect).stream().map(BrandCollect::getBrandId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(idList)) {
            return Result.createBySuccess(Lists.newArrayList());
        }
        List<Brand> motorItems = brandMapper.selectByIdList(idList);
        return Result.createBySuccess(motorItems);
    }

    @ApiOperation("足迹接口")
    @GetMapping("footList")
    public Result<List<MotorItem>> footList(@RequestParam Long userId) {
        MotorFoot motorFoot = new MotorFoot();
        motorFoot.setUserId(userId);
        PageHelper.orderBy("id desc");
        List<Long> idList = motorFootMapper.select(motorFoot).stream().map(MotorFoot::getMotorId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(idList)) {
            return Result.createBySuccess(Lists.newArrayList());
        }
        List<MotorItem> motorItems = motorItemMapper.selectByIdList(idList);
        return Result.createBySuccess(motorItems);
    }


}
