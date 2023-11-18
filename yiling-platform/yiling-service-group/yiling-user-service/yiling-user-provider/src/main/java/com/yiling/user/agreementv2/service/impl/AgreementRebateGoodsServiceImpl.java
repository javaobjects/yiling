package com.yiling.user.agreementv2.service.impl;

import java.util.List;
import java.util.Objects;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreementv2.dto.AgreementRebateGoodsDTO;
import com.yiling.user.agreementv2.dto.request.QueryAgreementGoodsPageRequest;
import com.yiling.user.agreementv2.entity.AgreementRebateGoodsDO;
import com.yiling.user.agreementv2.dao.AgreementRebateGoodsMapper;
import com.yiling.user.agreementv2.service.AgreementRebateGoodsService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 协议返利商品表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
@Service
public class AgreementRebateGoodsServiceImpl extends BaseServiceImpl<AgreementRebateGoodsMapper, AgreementRebateGoodsDO> implements AgreementRebateGoodsService {

    @Override
    public Page<AgreementRebateGoodsDO> getRebateGoodsByAgreementId(QueryAgreementGoodsPageRequest request) {
        LambdaQueryWrapper<AgreementRebateGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementRebateGoodsDO::getAgreementId, request.getId());
        if (Objects.nonNull(request.getGoodsId()) && request.getGoodsId() != 0) {
            wrapper.eq(AgreementRebateGoodsDO::getGoodsId, request.getGoodsId());
        }
        if (StrUtil.isNotEmpty(request.getGoodsName())) {
            wrapper.like(AgreementRebateGoodsDO::getGoodsName, request.getGoodsName());
        }

        return this.page(request.getPage(), wrapper);
    }

    @Override
    public List<AgreementRebateGoodsDTO> getRebateGoodsByAgreementId(Long agreementId) {
        LambdaQueryWrapper<AgreementRebateGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementRebateGoodsDO::getAgreementId, agreementId);
        return PojoUtils.map(this.list(wrapper), AgreementRebateGoodsDTO.class);
    }
}
