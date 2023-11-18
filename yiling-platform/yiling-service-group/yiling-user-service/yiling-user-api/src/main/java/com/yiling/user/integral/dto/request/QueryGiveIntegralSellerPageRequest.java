package com.yiling.user.integral.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.user.common.util.bean.Eq;
import com.yiling.user.common.util.bean.In;
import com.yiling.user.common.util.bean.Like;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单送积分-指定商家-已添加商家分页列表查询 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGiveIntegralSellerPageRequest extends QueryPageListRequest {

    /**
     * 发放规则ID
     */
    @Eq
    @NotNull
    private Long giveRuleId;

    /**
     * 企业ID
     */
    @Eq
    private Long eid;

    /**
     * 企业ID集合
     */
    @In(name = "eid")
    private List<Long> eidList;


}
