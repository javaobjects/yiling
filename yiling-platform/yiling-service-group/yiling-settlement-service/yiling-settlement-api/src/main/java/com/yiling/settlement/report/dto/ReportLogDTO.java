package com.yiling.settlement.report.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2022-05-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ReportLogDTO extends BaseDTO {


    /**
     * 报表id
     */
    private Long reportId;

    /**
     * 数据id
     */
    private String dataId;

    /**
     * 日志类型：1-提交返利 2-运营确认 3-运营驳回 4-财务确认 5-财务驳回 6-调整金额 7-修改B2B订单标识 8-修改流向订单标识 9-管理员驳回 10-报表返利
     */
    private Integer type;

    /**
     * 操作值
     */
    private String opValue;

    /**
     * 操作备注
     */
    private String opRemark;

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
