package com.yiling.sjms.gb.entity;


import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 团购出库终端和商业关系
 * </p>
 *
 * @author wei.wang
 * @date 2023-03-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("gb_company_relation")
public class GbCompanyRelationDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 表单ID
     */
    private Long formId;

    /**
     * 团购出库终端ID
     */
    private Long termainalCompanyId;
    /**
     * 团购出库终端编码
     */
    private String termainalCompanyCode;

    /**
     * 团购出库终端名称
     */
    private String termainalCompanyName;

    /**
     * 购出库终端省区名称
     */
    private String termainalCompanyProvince;

    /**
     * 购出库终端市区名称
     */
    private String termainalCompanyCity;

    /**
     * 购出库终端区县名称
     */
    private String termainalCompanyRegion;

    /**
     * 团购出库商业ID
     */
    private Long businessCompanyId;

    /**
     * 团购出库商业编码
     */
    private String businessCompanyCode;

    /**
     * 团购出库商业名称
     */
    private String businessCompanyName;

    /**
     * 团购出库商业省区名称
     */
    private String businessCompanyProvince;

    /**
     * 团购出库商业市区名称
     */
    private String businessCompanyCity;

    /**
     * 团购出库商业区县名称
     */
    private String businessCompanyRegion;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;


}
