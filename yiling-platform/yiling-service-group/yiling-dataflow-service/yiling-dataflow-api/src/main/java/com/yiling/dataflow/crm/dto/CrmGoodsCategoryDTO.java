package com.yiling.dataflow.crm.dto;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 CrmGoodsCategoryDTO
 * @描述
 * @创建时间 2023/4/6
 * @修改人 shichen
 * @修改时间 2023/4/6
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CrmGoodsCategoryDTO extends BaseDTO {

    /**
     * 品类编码
     */
    private String code;

    /**
     * 品类名称
     */
    private String name;

    /**
     * 品类级别
     */
    private Integer categoryLevel;

    /**
     * 上级id
     */
    private Long parentId;

    /**
     * 是否末级 0：非末级，1：末级
     */
    private Integer finalStageFlag;

    /**
     * 修改时间
     */
    private Date updateTime;
}
