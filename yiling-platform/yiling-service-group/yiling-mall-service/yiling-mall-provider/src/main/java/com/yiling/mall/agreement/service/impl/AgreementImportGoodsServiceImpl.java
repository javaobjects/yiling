package com.yiling.mall.agreement.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.standard.api.StandardGoodsApi;
import com.yiling.goods.standard.dto.StandardGoodsDTO;
import com.yiling.mall.agreement.entity.AgreementImportGoodsDO;
import com.yiling.mall.agreement.dao.AgreementImportGoodsMapper;
import com.yiling.mall.agreement.entity.AgreementImportTaskDO;
import com.yiling.mall.agreement.service.AgreementImportGoodsService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.agreement.dto.request.SaveAgreementGoodsRequest;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 协议商品 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2023-04-18
 */
@Service
public class AgreementImportGoodsServiceImpl extends BaseServiceImpl<AgreementImportGoodsMapper, AgreementImportGoodsDO> implements AgreementImportGoodsService {
    @DubboReference
    GoodsApi goodsApi;
    @DubboReference
    StandardGoodsApi standardGoodsApi;

    @Override
    public List<SaveAgreementGoodsRequest> queryGoodsListByTaskCode(String taskCode, Long subjectId) {
        List<SaveAgreementGoodsRequest> result;
        if (StrUtil.isBlank(taskCode)|| ObjectUtil.isNull(subjectId)||ObjectUtil.equal(subjectId,0L)){
            return ListUtil.toList();
        }
        LambdaQueryWrapper<AgreementImportGoodsDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(AgreementImportGoodsDO::getTaskCode,taskCode);
        wrapper.eq(AgreementImportGoodsDO::getSubjectId,subjectId);
        List<AgreementImportGoodsDO> list = list(wrapper);
        if (CollUtil.isEmpty(list)){
            return ListUtil.toList();
        }
        result=ListUtil.toList();
        List<Long> goodsIdList = list.stream().map(AgreementImportGoodsDO::getGoodsId).distinct().collect(Collectors.toList());
        List<GoodsDTO> goodsDTOS = goodsApi.batchQueryInfo(goodsIdList);
        List<Long> standardIdList = goodsDTOS.stream().map(GoodsDTO::getStandardId).distinct().collect(Collectors.toList());
        List<StandardGoodsDTO> standardGoodsDTOList = standardGoodsApi.getStandardGoodsByIds(standardIdList);
        Map<Long, String> standardNameMap = standardGoodsDTOList.stream().collect(Collectors.toMap(StandardGoodsDTO::getId, StandardGoodsDTO::getName));
        Map<Long, String> standardLicenseNoMap = standardGoodsDTOList.stream().collect(Collectors.toMap(StandardGoodsDTO::getId, StandardGoodsDTO::getLicenseNo));
        goodsDTOS.forEach(e->{
            SaveAgreementGoodsRequest var= PojoUtils.map(e,SaveAgreementGoodsRequest.class);
            var.setGoodsId(e.getId());
            var.setGoodsName(e.getName());
            var.setStandardGoodsName(standardNameMap.get(e.getStandardId()));
            var.setStandardLicenseNo(standardLicenseNoMap.get(e.getStandardId()));
            result.add(var);
        });
        return result;
    }
}
