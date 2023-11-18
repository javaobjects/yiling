package com.yiling.sjms.wash.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
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
public class UnlockCustomerClassDetailVO extends BaseVO {

    /**
     * 经销商编码
     */
    @ApiModelProperty(value = "经销商编码")
    private Long crmEnterpriseId;

    /**
     * 经销商名称
     */
    @ApiModelProperty(value = "经销商名称")
    private String ename;

    /**
     * 原始客户名称
     */
    @ApiModelProperty(value = "原始客户名称")
    private String customerName;

    /**
     * 是否分类：0-未分类 1-已分类
     */
    @ApiModelProperty(value = "是否分类：0-未分类 1-已分类")
    private Integer classFlag;

    /**
     * 非锁客户分类：1-零售机构 2-商业公司 3-医疗机构 4-政府机构
     */
    @ApiModelProperty(value = "非锁客户分类：1-零售机构 2-商业公司 3-医疗机构 4-政府机构")
    private Integer customerClassification;

    /**
     * 非锁客户分类：1-规则 2-人工
     */
    @ApiModelProperty(value = "非锁客户分类：1-规则 2-人工")
    private Integer classGround;

    /**
     * 最后操作时间
     */
    @ApiModelProperty(value = "最后操作时间")
    private Date lastOpTime;

    /**
     * 最后操作人id
     */
    @ApiModelProperty(value = "操作人id")
    private Long lastOpUser;

    @ApiModelProperty(value = "操作人姓名")
    private String lastOpUserName;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
}
