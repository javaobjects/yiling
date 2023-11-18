package com.yiling.dataflow.crm.dto.request;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/3/14 0014
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCrmEnterpriseBackUpPageRequest extends QueryPageListRequest {

    /**
     * 标准机构编码
     */
    private Long crmEnterpriseId;

    /**
     * 供应链角色：1-商业公司 2-医疗机构 3-零售机构
     */
    private Integer supplyChainRole;

    /**
     * 机构名称
     */
    private String name;

    /**
     * 年月(yyyyMM 格式)
     */
    private String soMonth;

    /**
     * 业务状态 1有效 2失效
     */
    private Integer businessCode;
}
