package com.yiling.framework.common.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @description: EsAggregationDTO <br>
 * @date: 2020/4/25 9:11 <br>
 * @author feiwu
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class EsAggregationDTO<T> implements Serializable {

    private static final long serialVersionUID = 2634561443669102443L;

    @ApiModelProperty(value = "总数")
    private long total;

    @ApiModelProperty(value = "每页显示条数")
    private Integer size = 10;

    @ApiModelProperty(value = "当前页")
    private Integer current = 1;

    List<T> data;

    Map<String, Object> aggregation = new HashMap<>();



}
