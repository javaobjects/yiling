package com.yiling.marketing.promotion.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/11/4
 */
@Data
@Accessors(chain = true)
public class PromotionSpecialActivitySaveRequest extends BaseRequest implements Serializable {

    /**
     * 专场活动id
     */
    private String id;

    /**
     * 专场活动名称
     */
    private String specialActivityName;

    /**
     * 专场活动开始时间
     */
    private Date startTime;

    /**
     * 专场活动结束时间
     */
    private Date endTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建人手机号
     */
    private String mobile;

    /**
     * 活动类型（1-满赠,2-特价,3-秒杀,4-组合包）
     */
    private Integer type;

    /**
     * 活动状态（1-启用；2-停用；）
     */
    private Integer status;
}
