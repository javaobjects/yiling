package com.yiling.sjms.flee.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 窜货申诉拓展表单
 * </p>
 *
 * @author yong.zhang
 * @date 2023-03-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FleeingGoodsFormExtDTO extends BaseDTO {

    /**
     * 主流程表单id
     */
    private Long formId;

    /**
     * 申报类型 1-电商 2-非电商
     */
    private Integer reportType;

    /**
     * 附件
     */
    private String appendix;

    /**
     * 申诉描述
     */
    private String fleeingDescribe;

    /**
     * 确认状态：1-待提交 2-已提交
     */
    private Integer confirmStatus;

    /**
     * 确认时的备注意见
     */
    private String confirmDescribe;

    /**
     * 确认人id
     */
    private String confirmUserId;

    /**
     * 生成流向表单时间(提交清洗)
     */
    private Date submitWashTime;

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
