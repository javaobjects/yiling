package com.yiling.dataflow.spda.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 药监局企业数据
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-01-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("spda_enterprise_data")
public class SpdaEnterpriseDataDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 企业名称
     */
    private String name;

    /**
     * 一级标签
     */
    private String firstTag;

    /**
     * 二级标签
     */
    private String secondTag;

    /**
     * 药品经营许可证
     */
    private String licence;

    /**
     * 社会统一信用代码
     */
    private String licenseNumber;

    /**
     * 修正药品经营许可证
     */
    private String fixLicence;

    /**
     * 企业地址
     */
    private String address;

    /**
     * 省份
     */
    private String province;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
