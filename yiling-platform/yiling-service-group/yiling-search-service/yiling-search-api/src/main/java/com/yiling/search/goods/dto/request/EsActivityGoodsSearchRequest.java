package com.yiling.search.goods.dto.request;

import java.io.Serializable;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @description: GoodsSearchEsRequest <br>
 * @date: 2021/6/10 10:16 <br>
 * @author: fei.wu <br>
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EsActivityGoodsSearchRequest extends QueryPageListRequest implements Serializable {

    private static final long serialVersionUID = 291479953511743718L;
    /**
     * 搜索词
     */
    private String key;
    /**
     * 是否所有企业商品（true-是 false-否）
     */
    private Boolean allEidFlag;
    /**
     * 企业id
     */
    private List<Long> eidList;
    /**
     * 商品id
     */
    private List<Long> goodsIdList;

    /**
     * 商品id
     */
    private List<Long> sellSpecificationsIdList;

    private Integer mallFlag;
    private Integer mallStatus;
    private Integer auditStatus;
    private List<Long> excludeEids;
    /**
     * 根据指定字段合并数据
     */
    private String collapseField;

    /**
     * 优先排序的企业
     */
    private List<Long> sortEid;
}
