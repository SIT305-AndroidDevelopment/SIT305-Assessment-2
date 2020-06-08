package com.coding.service;

import ch.qos.logback.core.util.CloseUtil;
import com.coding.config.AppProperties;
import com.google.common.collect.Lists;
import com.guanweiming.common.utils.StringUtil;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import io.minio.errors.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Service
public class MinIOFileService {

    private final AppProperties appProperties;
    private static final List<String> imageList = Lists.newArrayList("jpg", "png");

    public String upload(InputStream inputStream, String name, String contentType, String md5) throws InvalidPortException,
            InvalidEndpointException,
            IOException,
            InvalidKeyException,
            NoSuchAlgorithmException,
            InsufficientDataException,
            InvalidResponseException,
            InternalException,
            XmlParserException,
            InvalidBucketNameException,
            ErrorResponseException,
            RegionConflictException {
        String endPoint = appProperties.getEndPoint();
        String ak = appProperties.getAk();
        String sk = appProperties.getSk();
        String bn = appProperties.getBn();
        MinioClient minioClient = new MinioClient(endPoint, ak, sk);

        // Check if the bucket already exists.
        boolean isExist = minioClient.bucketExists(bn);
        if (isExist) {
            System.out.println("Bucket already exists.");
        } else {
            // Make a new bucket called asiatrip to hold a zip file of photos.
            minioClient.makeBucket(bn);
        }
        int available = inputStream.available();
        PutObjectOptions options = new PutObjectOptions(available, 5 * 1024 * 1024);

        String suffix = StringUtil.getExtend(name);
        if (imageList.contains(suffix)) {
            options.setContentType("image/png");
        }
        if (StringUtils.isBlank(md5)) {
            md5 = UUID.randomUUID().toString().replace("-", "");
        }
        // 上传文件流。
        String fileName = suffix + "/" + md5 + "." + suffix;
        if (StringUtils.isBlank(fileName)) {
            fileName = "files/" + md5+".file";
        }
        // Upload the zip file to the bucket with putObject
        minioClient.putObject(bn, fileName, inputStream, options);
        CloseUtil.closeQuietly(inputStream);
        return "/" + bn + "/" + fileName;
    }
}
