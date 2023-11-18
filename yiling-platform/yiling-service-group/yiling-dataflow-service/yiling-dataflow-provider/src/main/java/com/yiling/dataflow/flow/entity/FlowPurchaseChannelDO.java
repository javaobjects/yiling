package com.yiling.dataflow.flow.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 FlowPurchaseChannelDO
 * @描述
 * @创建时间 2023/3/1
 * @修改人 shichen
 * @修改时间 2023/3/1
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("flow_purchase_channel")
public class FlowPurchaseChannelDO extends BaseDO {

    /**
     * 机构编码
     */
    private Long crmOrgId;

    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 省份
     */
    private String province;

    /**
     * 省份代码
     */
    private String provinceCode;

    /**
     * 市
     */
    private String city;

    /**
     * 市代码
     */
    private String cityCode;

    /**
     * 区
     */
    private String region;

    /**
     * 区代码
     */
    private String regionCode;

    /**
     * 采购渠道机构编码
     */
    private Long crmPurchaseOrgId;

    /**
     * 采购渠道机构名称
     */
    private String purchaseOrgName;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

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
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 备注
     */
    private String remark;

}
