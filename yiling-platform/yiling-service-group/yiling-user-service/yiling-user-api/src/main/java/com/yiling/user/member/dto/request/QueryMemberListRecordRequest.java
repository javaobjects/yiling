package com.yiling.user.member.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.user.common.util.bean.Eq;
import com.yiling.user.common.util.bean.In;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-查询会员购买列表记录 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryMemberListRecordRequest extends BaseRequest {

    /**
     * 订单号
     */
    @Eq
    private String orderNo;

    /**
     * 企业ID
     */
    @Eq
    private Long eid;

    /**
     * 推广方ID集合
     */
    @In(name = "promoter_id")
    private List<Long> promoterIdList;

    /**
     * 会员ID集合
     */
    @In(name = "member_id")
    private List<Long> memberIdList;

    /**
     * 是否只查有效的购买记录
     */
    private Boolean validFlag;

    /**
     * 是否只查当前正在生效的购买记录
     */
    private Boolean currentValid;

}
