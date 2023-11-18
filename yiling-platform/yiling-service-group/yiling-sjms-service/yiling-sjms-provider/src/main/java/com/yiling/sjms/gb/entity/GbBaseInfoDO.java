package com.yiling.sjms.gb.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 团购基本信息
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-11-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("gb_base_info")
public class GbBaseInfoDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 团购ID
     */
    private Long gbId;

    /**
     * 省区名称
     */
    private String provinceName;

    /**
     * 事业部ID
     */
    private Long orgId;

    /**
     * 事业部名称
     */
    private String orgName;

    /**
     * 销量计入人工号
     */
    private String sellerEmpId;

    /**
     * 销量计入人姓名
     */
    private String sellerEmpName;

    /**
     * 销量计入人区办ID
     */
    private Long sellerDeptId;

    /**
     * 销量计入人区办名称
     */
    private String sellerDeptName;

    /**
     * 销量计入人部门名称
     */
    private String sellerYxDeptName;

    /**
     * 团购负责人工号
     */
    private String managerEmpId;

    /**
     * 团购负责人姓名
     */
    private String managerEmpName;

    /**
     * 团购负责人部门名称
     */
    private String managerYxDeptName;


}
