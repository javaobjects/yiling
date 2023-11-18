package com.yiling.user.payment.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 账户临时额度 Form
 *
 * @author: tingwei.chen
 * @date: 2021/7/5
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class QueryShortTimeQuotaAccountRequest extends QueryPageListRequest {

    /**
     * 采购商名称
     */
    @ApiModelProperty(value = "采购商名称", required = true)
    private String customerName;
    /**
     * 审核
     */
    @NotNull
    @ApiModelProperty(value = "状态", required = true)
    private int status;

    /**
     * eidlist
     */
    private List<Long> eidList;
    /**
     * 排序类型：1-销售助手APP排序 2-POP后台排序
     */
    private Integer orderType;

}
