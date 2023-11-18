package com.yiling.user.agreementv2.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 分页查询协议乙方签订人 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAgreementSecondUserPageRequest extends QueryPageListRequest {

    /**
     * 联系人
     */
    private String name;

    /**
     * 电话
     */
    private String mobile;

    /**
     * 乙方名称
     */
    private String secondName;

}
