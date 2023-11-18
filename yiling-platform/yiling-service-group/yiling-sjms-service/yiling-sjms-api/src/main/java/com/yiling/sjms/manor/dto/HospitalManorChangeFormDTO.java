package com.yiling.sjms.manor.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 医院辖区变更表单
 * </p>
 *
 * @author gxl
 * @date 2023-05-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HospitalManorChangeFormDTO extends BaseDTO {


    private static final long serialVersionUID = -5387633753735258351L;
    /**
     * 医院id
     */
    private Long crmEnterpriseId;

    /**
     * 医院名称
     */
    private String enterpriseName;

    /**
     * 品种id
     */
    private Long categoryId;
    /**
     * 品种名称
     */
    private String categoryName;

    /**
     * 旧辖区id
     */
    private Long manorId;

    private String manorNo;
    /**
     * 辖区名称
     */
    private String manorName;

    /**
     * 新辖区id
     */
    private Long newManorId;
    /**
     * 新辖区名称
     */
    private String newManorName;
    /**
     * 新辖区编码
     */
    private String newManorNo;

    /**
     * form表主键
     */
    private Long formId;


    /**
     * 备注
     */
    private String remark;

    /**
     * 数据归档：1-开启 2-关闭
     */
    private Integer archiveStatus;
}
