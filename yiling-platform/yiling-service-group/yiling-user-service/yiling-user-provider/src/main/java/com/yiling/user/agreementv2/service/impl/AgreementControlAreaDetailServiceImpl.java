package com.yiling.user.agreementv2.service.impl;

import java.util.List;
import java.util.Objects;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.user.agreementv2.dto.request.QueryAgreementControlAreaDetailRequest;
import com.yiling.user.agreementv2.entity.AgreementControlAreaDetailDO;
import com.yiling.user.agreementv2.dao.AgreementControlAreaDetailMapper;
import com.yiling.user.agreementv2.service.AgreementControlAreaDetailService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import cn.hutool.core.collection.ListUtil;

/**
 * <p>
 * 协议控销区域详情表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
@Service
public class AgreementControlAreaDetailServiceImpl extends BaseServiceImpl<AgreementControlAreaDetailMapper, AgreementControlAreaDetailDO> implements AgreementControlAreaDetailService {

    @Override
    public List<AgreementControlAreaDetailDO> getControlAreaDetailList(QueryAgreementControlAreaDetailRequest request){
        if (Objects.isNull(request.getAgreementId()) || request.getAgreementId() == 0) {
            return ListUtil.toList();
        }

        LambdaQueryWrapper<AgreementControlAreaDetailDO> wrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(request.getAgreementId()) && request.getAgreementId() != 0) {
            wrapper.eq(AgreementControlAreaDetailDO::getAgreementId, request.getAgreementId());
        }
        if (Objects.nonNull(request.getControlGoodsGroupId()) && request.getControlGoodsGroupId() != 0) {
            wrapper.eq(AgreementControlAreaDetailDO::getControlGoodsGroupId, request.getControlGoodsGroupId());
        }

        return this.list(wrapper);
    }

}
