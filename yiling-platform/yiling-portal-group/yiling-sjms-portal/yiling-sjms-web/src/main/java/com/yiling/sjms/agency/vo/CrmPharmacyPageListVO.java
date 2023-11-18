package com.yiling.sjms.agency.vo;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/2/15 0015
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CrmPharmacyPageListVO extends CrmAgencyPageListVO {

    /**
     * 是否协议 1-是；2-否
     */
    @ApiModelProperty("是否协议 1-是；2-否")
    private Integer agreement;

    /**
     * 药店属性 1-连锁分店；2-单体药店
     */
    @ApiModelProperty("药店属性 1-连锁分店；2-单体药店")
    private Integer pharmacyAttribute;

    /**
     * 药店类型 1-直营；2-加盟
     */
    @ApiModelProperty("药店类型 1-直营；2-加盟")
    private Integer pharmacyType;

    /**
     * 药店级别 1-A级；2-B级；3-C级
     */
    @ApiModelProperty("药店级别 1-A级；2-B级；3-C级")
    private Integer pharmacyLevel;

    /**
     * 标签属性 1-社区店；2-商圈店；3-院边店；4-电商店
     */
    @ApiModelProperty("标签属性 1-社区店；2-商圈店；3-院边店；4-电商店")
    private Integer labelAttribute;

    private Date createTime;
}
