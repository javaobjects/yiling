package com.yiling.user.member.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.member.dto.request.ImportOpenMemberRequest;
import com.yiling.user.member.entity.MemberImportOpenDO;

/**
 * <p>
 * B2B-会员导入批量开通表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-12
 */
public interface MemberImportOpenService extends BaseService<MemberImportOpenDO> {

    /**
     * 导入开通会员
     *
     * @return
     */
    boolean importBuyMember(ImportOpenMemberRequest request);

}
