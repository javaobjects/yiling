package com.yiling.bi.goods.api;

import java.util.List;

import com.yiling.bi.goods.dto.InputGoodsRelationShipDTO;

/**
 * DEMO API
 *
 * @author xuan.zhou
 * @date 2022/5/12
 */
public interface InputGoodsRelationShipApi {

    InputGoodsRelationShipDTO getInputGoodsRelationShipBySpecId(String SpecId);

    List<InputGoodsRelationShipDTO> getInputGoodsRelationShipAll();

    List<InputGoodsRelationShipDTO> getByCrmGoodsIdAndLikeAppId(String crmGoodsid, String applicationId);

    List<InputGoodsRelationShipDTO> getByLsSpecAndLikeAppId(String goodsSpec, String applicationId);
}
