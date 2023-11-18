package com.yiling.cms.evaluate.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 健康测评
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HealthEvaluateMarketPromoteDTO extends BaseDTO {

    private static final long serialVersionUID = -7863296268309963238L;

    /**
     * cms_health_evaluate主键
     */
    private Long healthEvaluateId;

    /**
     * 跳转链接
     */
    private String jumpUrl;

    /**
     * 图片
     */
    private String pic;

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
