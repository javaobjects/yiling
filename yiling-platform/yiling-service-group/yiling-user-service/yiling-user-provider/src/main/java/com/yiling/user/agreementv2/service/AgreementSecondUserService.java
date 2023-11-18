package com.yiling.user.agreementv2.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.agreementv2.dto.AgreementSecondUserDTO;
import com.yiling.user.agreementv2.dto.request.QueryAgreementSecondUserPageRequest;
import com.yiling.user.agreementv2.dto.request.SaveAgreementSecondUserRequest;
import com.yiling.user.agreementv2.entity.AgreementSecondUserDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 协议乙方签订人表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-08
 */
public interface AgreementSecondUserService extends BaseService<AgreementSecondUserDO> {

    /**
     * 分页查询乙方签订人
     *
     * @param request
     * @return
     */
    Page<AgreementSecondUserDTO> querySecondUserListPage(QueryAgreementSecondUserPageRequest request);

    /**
     * 保存乙方签订人
     *
     * @param request
     * @return
     */
    Boolean saveAgreementSecondUser(SaveAgreementSecondUserRequest request);
}
