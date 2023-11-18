package com.yiling.dataflow.wash.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UnlockCustomerClassDetailDTO extends BaseDTO {

    private static final long serialVersionUID = -4471232659490136931L;

    /**
     * 经销商编码
     */
    private Long crmEnterpriseId;

    /**
     * 经销商名称
     */
    private String ename;

    /**
     * 原始客户名称
     */
    private String customerName;

    /**
     * 是否分类：0-未分类 1-已分类
     */
    private Integer classFlag;

    /**
     * 非锁客户分类：1-零售机构 2-商业公司 3-医疗机构 4-政府机构
     */
    private Integer customerClassification;

    /**
     * 非锁客户分类：1-规则 2-人工
     */
    private Integer classGround;

    /**
     * 规则id
     */
    private Long ruleId;

    /**
     * 最后操作时间
     */
    private Date lastOpTime;

    /**
     * 最后操作人id
     */
    private Long lastOpUser;

    /**
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;
}
