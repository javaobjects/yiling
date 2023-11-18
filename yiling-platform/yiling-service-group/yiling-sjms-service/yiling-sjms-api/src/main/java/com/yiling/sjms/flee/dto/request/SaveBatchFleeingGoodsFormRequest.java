package com.yiling.sjms.flee.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/3/20 0020
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveBatchFleeingGoodsFormRequest extends BaseRequest {

    /**
     * 发起人姓名
     */
    private String empId;

    /**
     * 发起人姓名
     */
    private String empName;

    /**
     * 文件类型 1-申报 2-申报确认
     */
    private Integer importFileType;

    /**
     * 主流程表单id
     */
    private Long formId;

    /**
     * 申报类型 1-电商 2-非电商
     */
    private Integer reportType;

    /**
     * 上传的窜货申诉信息
     */
    private List<SaveFleeingGoodsRequest> saveFleeingGoodsList;
}
