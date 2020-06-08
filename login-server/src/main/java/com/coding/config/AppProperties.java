package com.coding.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author guanweiming
 */
@Data
@ConfigurationProperties("app")
public class AppProperties {
    /**
     * appId
     */
    private String appId;

    private String endPoint = "http://49.235.23.118:9000";
    private String ak = "admin";
    private String sk = "12345678Aa";
    private String bn = "files";
}
