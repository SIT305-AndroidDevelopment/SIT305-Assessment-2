package com.coding.controller;

import com.coding.common.Const;
import com.coding.config.AppProperties;
import com.coding.domain.Text;
import com.coding.domain.User;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @author guanweiming
 */
@Slf4j
@Api(tags = "用户接口")
@AllArgsConstructor
@RequestMapping(Const.API + "user")
@RestController
public class UserController {

    private final UserService userService;
    private final MinIOFileService fileService;
    private final UserMapper userMapper;
    private final AppProperties appProperties;
    private final TextMapper textMapper;

    @ApiOperation("登陆接口，返回用户数据")
    @PostMapping("login")
    public Result<User> login(
            @RequestParam String username,
            @RequestParam String password) {
        return userService.login(username, password);
    }


    @ApiOperation("post数据")
    @PostMapping("text")
    public Result<Text> text(
            @RequestParam String text) {
        Text record = new Text();
        record.setText(text);
        textMapper.insertSelective(record);
        return Result.createBySuccess();
    }


    @ApiOperation("注册用户接口")
    @PostMapping("register")
    public Result<User> register(@RequestParam String username,
                                 @RequestParam String password, String email) {
        return userService.addUser(username, password, email);
    }


    @ApiOperation("上传图片")
    @PostMapping("upload")
    public Result<String> userInfo(MultipartFile file) throws IOException, NoSuchAlgorithmException, RegionConflictException, InvalidKeyException, InvalidPortException, InvalidResponseException, ErrorResponseException, XmlParserException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, InternalException {
        String url = appProperties.getEndPoint() + fileService.upload(file.getInputStream(), file.getOriginalFilename(), file.getContentType(), null);
        Result<String> bySuccess = Result.createBySuccess(url);
        log.info("url:{}", bySuccess);
        return bySuccess;
    }

    @ApiOperation("查询所有用户")
    @GetMapping("allUser")
    public Result<List<User>> allUser() {
        return Result.createBySuccess(userMapper.selectAll());
    }


}
