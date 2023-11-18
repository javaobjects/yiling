package com.yiling.sjms.gb.dto;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CustomerDTO extends BaseDTO {

    /**
     * 名称
     */
    private String name;

    /**
     * 省份名称
     */
    private String provinceName;

    /**
     * 省份编号
     */
    private String provinceCode;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 区县名称
     */
    private String regionName;

    /**
     * 区县编码
     */
    private String regionCode;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 社会统一信用代码
     */
    private String creditCode;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态：1-启用 2-停用
     */
    private Integer status;

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


