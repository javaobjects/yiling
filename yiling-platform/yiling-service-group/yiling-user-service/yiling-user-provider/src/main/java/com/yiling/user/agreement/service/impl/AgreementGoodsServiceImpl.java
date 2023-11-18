package com.yiling.user.agreement.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreement.bo.AgreementGoodsPurchaseRelationBO;
import com.yiling.user.agreement.bo.SupplementaryAgreementGoodsBO;
import com.yiling.user.agreement.bo.TempAgreementGoodsBO;
import com.yiling.user.agreement.dao.AgreementGoodsMapper;
import com.yiling.user.agreement.dto.AgreementGoodsDTO;
import com.yiling.user.agreement.dto.request.AgreementGoodsDetailsPageRequest;
import com.yiling.user.agreement.dto.request.AgreementPurchaseGoodsPageRequest;
import com.yiling.user.agreement.dto.request.AgreementPurchaseGoodsRequest;
import com.yiling.user.agreement.dto.request.QueryAgreementGoodsPageRequest;
import com.yiling.user.agreement.dto.request.SalePurchaseGoodsRequest;
import com.yiling.user.agreement.dto.request.SaveAgreementGoodsRequest;
import com.yiling.user.agreement.entity.AgreementGoodsDO;
import com.yiling.user.agreement.service.AgreementGoodsPurchaseRelationService;
import com.yiling.user.agreement.service.AgreementGoodsService;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 协议商品表 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-06-03
 */
@Service
@Slf4j
public class AgreementGoodsServiceImpl extends BaseServiceImpl<AgreementGoodsMapper, AgreementGoodsDO> implements AgreementGoodsService {

	@Autowired
	AgreementGoodsPurchaseRelationService agreementGoodsPurchaseRelationService;

    @Override
    public Page<AgreementGoodsDTO> agreementGoodsListPage(QueryAgreementGoodsPageRequest request) {
        LambdaQueryWrapper<AgreementGoodsDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(AgreementGoodsDO::getAgreementId, request.getAgreementId());
        String goodsName = request.getGoodsName();
        if (StringUtils.isNotEmpty(goodsName)) {
            wrapper.like(AgreementGoodsDO::getGoodsName, request.getGoodsName());
        }
        String licenseNo = request.getLicenseNo();
        if (StringUtils.isNotEmpty(licenseNo)) {
            wrapper.like(AgreementGoodsDO::getLicenseNo, request.getLicenseNo());
        }
        Integer isPatent = request.getIsPatent();
        if (isPatent != null&&isPatent!=0) {
            wrapper.eq(AgreementGoodsDO::getIsPatent, request.getIsPatent());
        }
        return PojoUtils.map(this.page(request.getPage(), wrapper), AgreementGoodsDTO.class);
    }

    @Override
    public Page<AgreementGoodsDTO> getAgreementGoodsByAgreementIdPage(AgreementGoodsDetailsPageRequest request) {
        QueryWrapper<AgreementGoodsDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AgreementGoodsDO::getAgreementId, request.getAgreementId());
        Page<AgreementGoodsDO> page = this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
        Page<AgreementGoodsDTO> agreementGoodsPage = PojoUtils.map(page, AgreementGoodsDTO.class);
        return agreementGoodsPage;
    }

    /**
     * 根据AgreementId查询所有协议商品
     *
     * @param agreementId
     * @return
     */
    @Override
    public List<AgreementGoodsDTO> getAgreementGoodsAgreementIdByList(Long agreementId) {
        QueryWrapper<AgreementGoodsDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AgreementGoodsDO::getAgreementId, agreementId);
        List<AgreementGoodsDO> doList = this.list(wrapper);
        return PojoUtils.map(doList, AgreementGoodsDTO.class);
    }

    @Override
    public Map<Long, List<AgreementGoodsDTO>> getAgreementGoodsListByAgreementIds(List<Long> agreementIds) {
        QueryWrapper<AgreementGoodsDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(AgreementGoodsDO::getAgreementId, agreementIds);
        List<AgreementGoodsDTO> doList = PojoUtils.map(this.list(wrapper), AgreementGoodsDTO.class);

        Map<Long, List<AgreementGoodsDTO>> resultMap = new HashMap<>();
        for (AgreementGoodsDTO agreementGoodsDTO : doList) {
            if (resultMap.containsKey(agreementGoodsDTO.getAgreementId())) {
                resultMap.get(agreementGoodsDTO.getAgreementId()).add(agreementGoodsDTO);
            } else {
                List<AgreementGoodsDTO> dtoList = new ArrayList<>();
                dtoList.add(agreementGoodsDTO);
                resultMap.put(agreementGoodsDTO.getAgreementId(), dtoList);
            }
        }
        return resultMap;
    }

    @Override
    public Boolean updateByGoodsListAndAgreementId(List<SaveAgreementGoodsRequest> goodsList, Long agreementId, Long opUserId) {
        QueryWrapper<AgreementGoodsDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AgreementGoodsDO::getAgreementId, agreementId);

        AgreementGoodsDO agreementGoodsDO = new AgreementGoodsDO();
        agreementGoodsDO.setOpUserId(opUserId);
        this.batchDeleteWithFill(agreementGoodsDO, wrapper);

        //新增
        List<AgreementGoodsDO> agreementGoodsList = PojoUtils.map(goodsList, AgreementGoodsDO.class);
        agreementGoodsList.forEach(e -> {
            e.setOpUserId(opUserId);
            e.setAgreementId(agreementId);
        });
        return this.saveBatch(agreementGoodsList);
    }

    @Override
    public Boolean deleteByGoodsIdsAndAgreementId(List<Long> goodsIds, List<Long> agreementIds, Long opUserId) {
        QueryWrapper<AgreementGoodsDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(AgreementGoodsDO::getAgreementId, agreementIds);
        wrapper.lambda().in(AgreementGoodsDO::getGoodsId, goodsIds);

        AgreementGoodsDO agreementGoodsDO = new AgreementGoodsDO();
        agreementGoodsDO.setOpUserId(opUserId);
        return this.batchDeleteWithFill(agreementGoodsDO, wrapper) > 0;
    }

    @Override
    public List<AgreementGoodsPurchaseRelationBO> getBuyerGatherByGoodsIds(List<Long> goodsIds) {
        return this.baseMapper.getBuyerGatherByGoodsIds(goodsIds);
    }

    /**
     * 查询采购商可以采购的年度协议商品信息
     *
     * @param request
     * @return
     */
    @Override
    public Page<AgreementGoodsDTO> getYearPurchaseGoodsPageList(AgreementPurchaseGoodsPageRequest request) {
        Page<AgreementGoodsDO> page = this.baseMapper.getYearPurchaseGoodsPageList(new Page<>(request.getCurrent(), request.getSize()), request);
        return PojoUtils.map(page, AgreementGoodsDTO.class);
    }

    @Override
    public Page<SupplementaryAgreementGoodsBO> getSupplementarySaleGoodPageList(AgreementPurchaseGoodsPageRequest request) {
        return this.baseMapper.getSupplementarySaleGoodPageList(request.getPage(),request);
    }

    /**
     * 销售助手查询可采购的年度协议商品信息，区别销售助手查询的为可售商品，必须有采购关系
     * @param request
     * @return
     */
    @Override
    public List<TempAgreementGoodsBO> getPurchaseGoodsListToSaleAssistant(SalePurchaseGoodsRequest request) {
        return this.baseMapper.getPurchaseGoodsListToSaleAssistant(request);
    }

    /**
     * 查询采购商可以采购的补充协议商品信息
     *
     * @param request
     * @return
     */
    @Override
    public Page<TempAgreementGoodsBO> getTempPurchaseGoodsPageList(AgreementPurchaseGoodsPageRequest request) {
        Page<TempAgreementGoodsBO> page = this.baseMapper.getTempPurchaseGoodsPageList(new Page<>(request.getCurrent(), request.getSize()), request);
        return page;
    }

    /**
     * 通过名称或者批次号获取可以采购的商品
     *
     * @param request
     * @return
     */
    @Override
    public List<AgreementGoodsDTO> getYearPurchaseGoodsList(AgreementPurchaseGoodsRequest request) {
        return PojoUtils.map(this.baseMapper.getYearPurchaseGoodsList(request), AgreementGoodsDTO.class);
    }

    @Override
    public List<AgreementGoodsDTO> getTempPurchaseGoodsList(AgreementPurchaseGoodsRequest request) {
        return PojoUtils.map(this.baseMapper.getTempPurchaseGoodsList(request), AgreementGoodsDTO.class);
    }

    @Override
    public List<AgreementGoodsDTO> getTempPurchaseGoodsByDistributionList(AgreementPurchaseGoodsRequest request){
        return PojoUtils.map(this.baseMapper.getTempPurchaseGoodsByDistributionList(request), AgreementGoodsDTO.class);
    }

    @Override
    public Long getAgreementIdByPurchaseGoodsList(Long buyerEid, Long goodsId) {
        AgreementPurchaseGoodsRequest request = new AgreementPurchaseGoodsRequest();
        request.setPurchaseEid(buyerEid);
        request.setGoodsId(goodsId);
        List<AgreementGoodsDO> agreementGoodsList = this.baseMapper.getYearPurchaseGoodsList(request);
        if (CollUtil.isNotEmpty(agreementGoodsList)) {
            return agreementGoodsList.get(0).getAgreementId();
        }
        return 0L;
    }

    @Override
    public List<Long> getAgreementIdsByPurchaseGoodsList(Long buyerEid, List<Long> goodsIds) {
        return this.baseMapper.getAgreementIdsByPurchaseGoodsList(buyerEid,goodsIds);
    }

//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public Boolean addAgreementGoods(AddAgreementGoodsRequest request) {
//		Boolean result;
//		AgreementGoodsDO agreementGoods = PojoUtils.map(request, AgreementGoodsDO.class);
//		result = save(agreementGoods);
//		if (AgreementCategoryEnum.YEAR_AGREEMENT.getCode().equals(request.getCategory())) {
//			//添加商品和渠道商的关系
//			if (result) {
//				List<Long> goodsIds = ListUtil.toList(request.getGoodsId());
//				result = agreementGoodsPurchaseRelationService.updateBuyerGatherByGid(goodsIds, request.getEid(), request.getOpUserId());
//
//			} else {
//				log.error("协议新增商品时向协议商品表新增失败");
//			}
//		}
//		return result;
//	}

}
