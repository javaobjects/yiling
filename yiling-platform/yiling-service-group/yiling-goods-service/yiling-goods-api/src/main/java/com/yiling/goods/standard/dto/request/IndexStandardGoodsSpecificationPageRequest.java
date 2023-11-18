package com.yiling.goods.standard.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: wei.wang
 * @date: 2021/5/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class IndexStandardGoodsSpecificationPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = -33371030428332221L;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 是否以岭品  0非以岭品  1以岭品
     */
    private Integer ylFlag;

    private List<Long> includeEids;

    private  Long buyerEid;

}
