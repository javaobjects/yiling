package com.yiling.search.flow.dto.request;

import java.io.Serializable;

import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2023/5/18
 */
@Data
public class QueryScrollRequest implements Serializable {

    private static final long serialVersionUID = 291479953511743718L;

    private Integer size = 2000;

    private Long scrollTimeInMillis=1000*60L;

    private String scrollId;
}
