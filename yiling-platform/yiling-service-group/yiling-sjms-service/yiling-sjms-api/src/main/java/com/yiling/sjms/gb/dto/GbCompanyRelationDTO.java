package com.yiling.sjms.gb.dto;

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
 * @date 2022-11-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GbCompanyRelationDTO extends BaseDO {

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
    

}
