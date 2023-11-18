package com.yiling.bi.goods.service;

import java.util.List;

import com.yiling.bi.goods.dto.InputGoodsRelationShipDTO;
import com.yiling.bi.goods.entity.InputGoodsRelationShipDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 商品关系表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-09-20
 */
public interface InputGoodsRelationShipService extends BaseService<InputGoodsRelationShipDO> {

    InputGoodsRelationShipDTO getInputGoodsRelationShipBySpecId(String SpecId);

    List<InputGoodsRelationShipDTO> getInputGoodsRelationShipAll();

    List<InputGoodsRelationShipDTO> getByCrmGoodsIdAndLikeAppId(String crmGoodsid, String applicationId);

    List<InputGoodsRelationShipDTO> getByLsSpecAndLikeAppId(String lsSpec, String applicationId);
}
