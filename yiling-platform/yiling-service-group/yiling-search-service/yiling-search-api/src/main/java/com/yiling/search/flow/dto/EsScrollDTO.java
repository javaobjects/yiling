package com.yiling.search.flow.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class EsScrollDTO<T> implements Serializable {

    private static final long serialVersionUID = 2634561443669102443L;

    private String scrollId = "";

    private long total;

    private List<T> data;

}
