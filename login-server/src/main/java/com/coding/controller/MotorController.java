package com.coding.controller;

import com.coding.common.Const;
import com.coding.config.AppProperties;
import com.coding.domain.MotorCollect;
import com.coding.domain.MotorItem;
import com.coding.domain.Text;
import com.coding.domain.User;
import com.coding.mapper.MotorCollectMapper;
import com.coding.mapper.MotorItemMapper;
import com.coding.mapper.TextMapper;
import com.coding.mapper.UserMapper;
import com.coding.service.MinIOFileService;
import com.coding.service.UserService;
import com.guanweiming.common.utils.Result;
import io.minio.errors.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @ApiOperation("添加接口")
    @PostMapping("add")
    public Result<MotorItem> addMotor(MotorItem param) {
        motorItemMapper.insertSelective(param);
        return Result.createBySuccess();
    }

    @ApiOperation("添加接口")
    @PostMapping("update")
    public Result<MotorItem> updateMotor(MotorItem param) {
        motorItemMapper.updateByPrimaryKeySelective(param);
        return Result.createBySuccess();
    }

    @ApiOperation("列表接口")
    @GetMapping("list")
    public Result<List<MotorItem>> list() {
        List<MotorItem> motorItems = motorItemMapper.selectAll();
        return Result.createBySuccess(motorItems);
    }

    @ApiOperation("添加收藏接口")
    @PostMapping("addCollect")
    public Result<MotorItem> addCollect(MotorCollect param) {
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


    @ApiOperation("列表接口")
    @GetMapping("collecList")
    public Result<List<MotorItem>> collecList(@RequestParam Long userId) {
        MotorCollect motorCollect = new MotorCollect();
        motorCollect.setUserId(userId);
        List<Long> idList = motorCollectMapper.select(motorCollect).stream().map(MotorCollect::getMotorId).collect(Collectors.toList());
        List<MotorItem> motorItems = motorItemMapper.selectByIdList(idList);
        return Result.createBySuccess(motorItems);
    }


}
