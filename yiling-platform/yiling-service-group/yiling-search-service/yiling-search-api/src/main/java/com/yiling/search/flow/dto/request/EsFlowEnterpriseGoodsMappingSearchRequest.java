package com.yiling.search.flow.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.framework.common.enums.SortEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author shichen
 * @类名 EsFlowEnterpriseGoodsMappingSearchRequest
 * @描述
 * @创建时间 2023/2/28
 * @修改人 shichen
 * @修改时间 2023/2/28
 **/
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EsFlowEnterpriseGoodsMappingSearchRequest extends QueryPageListRequest {

    /**
     * 流向原始名称
     */
    private String flowGoodsName;

    /**
     * 流向原始规格
     */
    private String flowSpecification;

    /**
     * crm标准商品编码
     */
    private Long crmGoodsCode;

    /**
     * 标准商品名称
     */
    private String goodsName;

    /**
     * 经销商名称
     */
    private String enterpriseName;

    /**
     * 经销商编码
     */
    private Long crmEnterpriseId;

    /**
     * 指定经销商列表（权限使用）
     */
    private List<Long> crmEnterpriseIds;

    /**
     * 指定省份（权限使用）
     */
    private List<String> provinceCodes;
    /**
     * 开始更新时间
     */
    private Date startUpdateTime;

    /**
     * 结束更新时间
     */
    private Date endUpdateTime;

    /**
     * 开始最后上传时间
     */
    private Date startLastUploadTime;

    /**
     * 结束最后上传时间
     */
    private Date endLastUploadTime;

    /**
     * 排序字段
     */
    private String sortField;

    private SortEnum sortEnum;

    /**
     * 机构权限范围：1-无权限 2-部分权限 3-全部权限
     */
    private Integer orgDatascope;
}
