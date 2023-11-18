package com.yiling.marketing.promotion.dto;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 促销活动列表查询
 *
 * @author: yong.zhang
 * @date: 2021/11/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SpecialActivityPageDTO extends BaseDTO {

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
     * 创建人姓名
     */
    private String createrName;

    /**
     * 活动类型（1-满赠,2-特价,3-秒杀,4-组合包）
     */
    private Integer type;

    /**
     * 活动状态（1-启用；2-停用；3，未开始，4进行中，5已结束 根据时间判断）
     */
    private Integer status;

    /**
     * 活动进度1待开始，2进行中，3已结束
     */
    private Integer progress;

    /**
     * 备注
     */
    private String remark;

    /**
     * 专场活动关联的企业营销活动信息
     */
    private List<SpecialActivityEnterpriseDTO> specialActivityEnterpriseDTOS;

    /**
     * 企业id
     */
    private Long eid;
}
