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
public class StandardInstructionsHealthDTO extends BaseDTO {

    /**
     * 标准商品ID
     */
    private Long standardId;

    /**
     * 原料
     */
    private String rawMaterial;

    /**
     * 辅料
     */
    private String ingredients;

    /**
     * 适宜人群
     */
    private String suitablePeople;

    /**
     * 不适宜人群
     */
    private String unsuitablePeople;

    /**
     * 保健功能
     */
    private String healthcareFunction;

    /**
     * 保质期
     */
    private String expirationDate;

    /**
     * 食用量及食用方法
     */
    private String usageDosage;

    /**
     * 储藏
     */
    private String store;

    /**
     * 注意事项
     */
    private String noteEvents;

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
