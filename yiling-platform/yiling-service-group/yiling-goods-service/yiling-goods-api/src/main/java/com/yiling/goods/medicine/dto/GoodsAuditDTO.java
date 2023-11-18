package com.yiling.goods.medicine.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/5/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsAuditDTO extends BaseDTO {

    private static final long serialVersionUID = -4672345329942342009L;

    /**
     * 商品ID
     */
    private Long gid;

    /**
     * 供应商ID
     */
    private Long eid;

    /**
     * 供应商名称
     */
    private String ename;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 通用名称
     */
    private String commonName;

    /**
     * 注册证号
     */
    private String licenseNo;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 生产地址
     */
    private String manufacturerAddress;

    /**
     * 销售规格
     */
    private String specifications;

    /**
     * 单位
     */
    private String unit;

    /**
     * 审核状态：0待审核1审核通过2审核不通3忽略
     */
    private Integer auditStatus;

    /**
     * 数据来源
     */
    private Integer source;

    /**
     * 驳回信息
     */
    private String rejectMessage;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改时间
     */
    private Date createTime;

}
