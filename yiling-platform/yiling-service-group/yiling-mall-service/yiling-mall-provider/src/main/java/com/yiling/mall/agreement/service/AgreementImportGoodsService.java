package com.yiling.mall.agreement.service;

import java.util.List;

import com.yiling.mall.agreement.entity.AgreementImportGoodsDO;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.agreement.dto.request.SaveAgreementGoodsRequest;

/**
 * <p>
 * 协议商品 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2023-04-18
 */
public interface AgreementImportGoodsService extends BaseService<AgreementImportGoodsDO> {

    /**
     * 根据taskCode查询主体商品
     *
     * @param taskCode
     * @param subjectId
     * @return
     */
    List<SaveAgreementGoodsRequest> queryGoodsListByTaskCode(String taskCode, Long subjectId);
}
