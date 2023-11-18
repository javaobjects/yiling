package com.yiling.dataflow.crm.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCrmManorInfoRequest extends BaseRequest {
    private Long id;
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

    private Integer delFlag;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建日期
     */
    private Date createTime;

    /**
     * 更新人
     */
    private Long updateUser;

    /**
     * 更新日期
     */
    private Date updateTime;

    private String remark;
}
