package com.yiling.user.agreementv2.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议导入列表查询 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryImportAgreementListRequest extends BaseRequest {

    /**
     * 甲方名称
     */
    private String ename;


    /**
     * 乙方名称
     */
    private String secondName;

    /**
     * 协议负责人ID
     */
    private Long mainUserId;

    /**
     * 审核状态
     */
    private List<Integer> authStatusList;
}
