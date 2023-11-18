package com.yiling.user.member.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-会员购买条件分页 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryMemberBuyStagePageRequest extends QueryPageListRequest {

    /**
     * 会员ID集合
     */
    private List<Long> memberIdList;

    /**
     * 会员购买条件ID集合
     */
    private List<Long> stageIdList;

    /**
     * 会员名称
     */
    private String memberName;

}
