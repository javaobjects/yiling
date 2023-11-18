package com.yiling.search.goods.dto.request;

import java.io.Serializable;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.framework.common.enums.SortEnum;

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
public class EsGoodsSearchRequest extends QueryPageListRequest implements Serializable {

    private static final long serialVersionUID = 291479953511743718L;

    /**
     * 搜索词
     */
    private String key;

    /**
     * 企业ID
     */
    private List<Long> eids;

    private String ename;

    /**
     * 生产厂家
     */
    private List<String> manufacturer;

    /**
     * 一级分类名称
     */
    private List<String> gcName1;

    /**
     * 二级分类名称
     */
    private List<String> gcName2;

    /**
     * 当前登录的企业 id
     */
    private String buyerGather;

    private List<String> dosageName;

    /**
     * 是否有库存 0查看所有商品 1查询有库存商品
     */
    private Integer hasB2bStock;

    private String sortField;

    private SortEnum sortEnum;

    /**
     * 不接收页面参数
     */
    private Integer mallFlag;
    private Integer popFlag;
    private Integer mallStatus;
    private Integer popStatus;
    private Integer auditStatus;
    private List<Long> excludeEids;
    /**
     * 根据指定字段合并数据
     */
    private String collapseField;
}
