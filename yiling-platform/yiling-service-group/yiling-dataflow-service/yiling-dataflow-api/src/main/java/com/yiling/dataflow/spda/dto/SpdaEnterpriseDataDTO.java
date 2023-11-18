package com.yiling.dataflow.spda.dto;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.base.BaseDTO;
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
public class SpdaEnterpriseDataDTO extends BaseDTO {

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

}
