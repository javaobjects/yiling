package com.yiling.cms.evaluate.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 健康测评结果
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HealthEvaluateResultDTO extends BaseDTO {

    private static final long serialVersionUID = -7863296268309963238L;

    /**
     * cms_health_evaluate主键
     */
    private Long healthEvaluateId;

    /**
     * 结果排序
     */
    private Integer resultRank;

    /**
     * 分值区间类型 1-小于，2-小于等于，3-等于，4-大于等于，5-大于
     */
    private Integer scoreStartType;

    /**
     * 开始区间
     */
    private BigDecimal scoreStart;

    /**
     * 分值区间类型 1-小于，2-小于等于，3-等于，4-大于等于，5-大于
     */
    private Integer scoreEndType;

    /**
     * 结束区间
     */
    private BigDecimal scoreEnd;

    /**
     * 测评结果
     */
    private String evaluateResult;

    /**
     * 结果描述
     */
    private String resultDesc;

    /**
     * 健康小贴士
     */
    private String healthTip;

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
