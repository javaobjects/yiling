package com.yiling.bi.goods.api.impl;

import java.util.List;

import com.yiling.bi.goods.api.InputGoodsRelationShipApi;
import com.yiling.bi.goods.dto.InputGoodsRelationShipDTO;
import com.yiling.bi.goods.service.InputGoodsRelationShipService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: shuang.zhang
 * @date: 2022/9/21
 */
@DubboService
@Slf4j
public class InputGoodsRelationShipApiImpl implements InputGoodsRelationShipApi {

    @Autowired
    private InputGoodsRelationShipService inputGoodsRelationShipService;

    @Override
    public InputGoodsRelationShipDTO getInputGoodsRelationShipBySpecId(String SpecId) {
        return inputGoodsRelationShipService.getInputGoodsRelationShipBySpecId(SpecId);
    }

    @Override
    public List<InputGoodsRelationShipDTO> getInputGoodsRelationShipAll() {
        return inputGoodsRelationShipService.getInputGoodsRelationShipAll();
    }

    @Override
    public List<InputGoodsRelationShipDTO> getByCrmGoodsIdAndLikeAppId(String crmGoodsid, String applicationId) {
        return inputGoodsRelationShipService.getByCrmGoodsIdAndLikeAppId(crmGoodsid, applicationId);
    }

    @Override
    public List<InputGoodsRelationShipDTO> getByLsSpecAndLikeAppId(String lsSpec, String applicationId) {
        return inputGoodsRelationShipService.getByLsSpecAndLikeAppId(lsSpec, applicationId);
    }


}
