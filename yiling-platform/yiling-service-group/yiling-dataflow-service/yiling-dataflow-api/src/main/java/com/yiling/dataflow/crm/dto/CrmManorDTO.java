package com.yiling.dataflow.crm.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CrmManorDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    /**
     * 辖区编码
     */
    private String manorNo;

    /**
     * 辖区名称
     */
    private String name;

    /**
     * 机构数量
     */
    private Integer agencyNum;

    /**
     * 品类数量
     */
    private Integer categoryNum;
    /**
     * 更新人
     */
    private Long updateUser;
    private String updateUserName;

    /**
     * 更新日期
     */
    private Date updateTime;
}
