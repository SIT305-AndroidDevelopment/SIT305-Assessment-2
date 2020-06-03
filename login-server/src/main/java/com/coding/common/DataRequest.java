package com.coding.common;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author chezhu.xin
 */
@Data
public class DataRequest {
    @ApiParam(name = "length", required = true)
    private int length = 10;
    @ApiParam(name = "start", required = true)
    private int start = 10;
    @ApiParam(name = "draw", required = true)
    private int draw;

    private List<Map<String, Object>> order = Lists.newArrayList();

    public Integer getPage() {
        return start / length + 1;
    }

    public Integer getSize() {
        return length;
    }
}
