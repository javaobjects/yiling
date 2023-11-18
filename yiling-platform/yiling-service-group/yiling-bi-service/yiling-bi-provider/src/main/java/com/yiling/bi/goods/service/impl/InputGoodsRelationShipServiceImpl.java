package com.yiling.bi.goods.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.bi.goods.dao.InputGoodsRelationShipMapper;
import com.yiling.bi.goods.dto.InputGoodsRelationShipDTO;
import com.yiling.bi.goods.entity.InputGoodsRelationShipDO;
import com.yiling.bi.goods.service.InputGoodsRelationShipService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品关系表 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-09-20
 */
@Service
public class InputGoodsRelationShipServiceImpl extends BaseServiceImpl<InputGoodsRelationShipMapper, InputGoodsRelationShipDO> implements InputGoodsRelationShipService {

    @Override
    public InputGoodsRelationShipDTO getInputGoodsRelationShipBySpecId(String SpecId) {
        return null;
    }

    @Override
    public List<InputGoodsRelationShipDTO> getInputGoodsRelationShipAll() {
        return PojoUtils.map(this.list(),InputGoodsRelationShipDTO.class);
    }

    @Override
    public List<InputGoodsRelationShipDTO> getByCrmGoodsIdAndLikeAppId(String crmGoodsid, String applicationId) {
        LambdaQueryWrapper<InputGoodsRelationShipDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InputGoodsRelationShipDO::getCrmGoodsid, crmGoodsid);
        wrapper.like(InputGoodsRelationShipDO::getApplicationId, "%" + applicationId + "%");
        return PojoUtils.map(baseMapper.selectList(wrapper), InputGoodsRelationShipDTO.class);
    }

    @Override
    public List<InputGoodsRelationShipDTO> getByLsSpecAndLikeAppId(String lsSpec, String applicationId) {
        LambdaQueryWrapper<InputGoodsRelationShipDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InputGoodsRelationShipDO::getLsSpec, lsSpec);
        wrapper.like(InputGoodsRelationShipDO::getApplicationId, "%" + applicationId + "%");
        return PojoUtils.map(baseMapper.selectList(wrapper), InputGoodsRelationShipDTO.class);
    }
}
