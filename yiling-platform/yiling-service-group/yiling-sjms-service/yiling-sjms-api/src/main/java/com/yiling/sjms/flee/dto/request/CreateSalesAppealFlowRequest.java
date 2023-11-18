package com.yiling.sjms.flee.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shixing.sun
 * @date: 2023/3/16 0016
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreateSalesAppealFlowRequest extends BaseRequest {

    /**
     * form表主键
     */
    private Long formId;

    /**
     * 生成流向表单具体信息
    */
    private List<CreateSalesGoodsFlowRequest> fleeGoodsFlowFormList;


    private String empId;

    /**
     * 确认时候备注
     */
    private String confirmDescribe;

    /**
     *  传输类型：1、上传excel; 2、选择流向
     */
    private Integer transferType;

}
