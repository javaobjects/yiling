package com.yiling.goods.standard.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/5/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StandardInstructionsGoodsDTO  extends BaseDTO {

    /**
     * 标准库ID
     */
    private Long standardId;

    /**
     * 药品成分
     */
    private String drugDetails;

    /**
     * 药品性状
     */
    private String drugProperties;

    /**
     * 适应症
     */
    private String indications;

    /**
     * 用法与用量
     */
    private String usageDosage;

    /**
     * 不良反应
     */
    private String adverseEvents;

    /**
     * 禁忌症
     */
    private String contraindication;

    /**
     * 注意事项
     */
    private String noteEvents;

    /**
     * 药物相互作用
     */
    private String interreaction;

    /**
     * 存储条件
     */
    private String storageConditions;

    /**
     * 包装
     */
    private String packingInstructions;

    /**
     * 保质期
     */
    private String shelfLife;

    /**
     * 执行标准
     */
    private String executiveStandard;

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
