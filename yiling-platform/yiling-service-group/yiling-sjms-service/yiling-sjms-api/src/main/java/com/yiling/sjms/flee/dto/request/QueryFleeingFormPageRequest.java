package com.yiling.sjms.flee.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/3/11 0011
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFleeingFormPageRequest extends QueryPageListRequest {

    /**
     * 申报编号
     */
    private String code;

    /**
     * 申报类型 1-电商、2-非电商
     */
    private Integer reportType;

    /**
     * 状态 ：字典gb_form_status
     */
    private Integer status;

    /**
     * 确认状态：1-待确认 2-生成中 3-生成成功 4-生成失败
     */
    private Integer confirmStatus;

    /**
     * 发起人工号
     */
    private String empId;

    /**
     * 文件类型 1-申报 2-申报确认
     */
    private Integer importFileType;
}
