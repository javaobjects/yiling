package com.yiling.open.erp.dto;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 流向非以岭商品配置
 *
 * @author: houjie.sun
 * @date: 2022/4/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ErpFlowGoodsConfigDTO extends BaseDTO {

    /**
     * 商业ID
     */
    private Long eid;

    /**
     * 商业名称
     */
    private String ename;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品内码（供应商的ERP的商品主键）
     */
    private String goodsInSn;

    /**
     * 商品规格
     */
    private String specifications;

    /**
     * 批准文号（注册证号）
     */
    private String licenseNo;

    /**
     * 生产厂家
     */
    private String manufacturer;

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
