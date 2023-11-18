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
 * @类名 EsFlowEnterpriseCustomerMappingSearchRequest
 * @描述
 * @创建时间 2023/3/1
 * @修改人 shichen
 * @修改时间 2023/3/1
 **/
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EsFlowEnterpriseCustomerMappingSearchRequest extends QueryPageListRequest {
    /**
     * 流向客户名称
     */
    private String flowCustomerName;

    /**
     * 标准机构编码
     */
    private Long crmOrgId;

    /**
     * 标准机构名称
     */
    private String orgName;

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
     * 省份代码
     */
    private String provinceCode;

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

    /**
     * 是否查询推荐度>90%的数据 0-否（查询全部） 1-是
     */
    private Integer recommendFlag;

    /**
     * 推荐度>90%的匹配度最高id
     */
    private Long recommendOrgCrmId;

}
