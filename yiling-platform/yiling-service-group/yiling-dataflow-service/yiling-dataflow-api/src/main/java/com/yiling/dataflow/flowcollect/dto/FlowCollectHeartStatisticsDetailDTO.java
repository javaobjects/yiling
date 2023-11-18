package com.yiling.dataflow.flowcollect.dto;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 日流向心跳统计明细表
 * </p>
 *
 * @author xueli.ji
 * @date 2023-06-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowCollectHeartStatisticsDetailDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 日流向心跳统计主键ID
     */
    private Long fchsId;

    /**
     * 时间
     */
    private Date dateTime;

    /**
     * 流向状态：1-未上传 2-已上传
     */
    private Integer flowStatus;

}
