package com.yiling.goods.standard.api.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.standard.api.StandardGoodsSpecificationApi;
import com.yiling.goods.standard.bo.StandardSpecificationGoodsInfoBO;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationDTO;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationPicDTO;
import com.yiling.goods.standard.dto.request.IndexStandardGoodsSpecificationPageRequest;
import com.yiling.goods.standard.dto.request.SaveStandardSpecificationRequest;
import com.yiling.goods.standard.dto.request.StandardSpecificationPageRequest;
import com.yiling.goods.standard.entity.StandardGoodsSpecificationDO;
import com.yiling.goods.standard.enums.StandardResultCode;
import com.yiling.goods.standard.service.StandardGoodsSpecificationService;

/**
 * @author:wei.wang
 * @date:2021/5/21
 */
@DubboService
public class StandardGoodsSpecificationApiImpl implements StandardGoodsSpecificationApi {

    @Autowired
    private StandardGoodsSpecificationService standardGoodsSpecificationService;

    @Override
    public Page<StandardGoodsSpecificationPicDTO> getIndexStandardGoodsSpecificationInfoPage(IndexStandardGoodsSpecificationPageRequest request) {
        return standardGoodsSpecificationService.getIndexStandardGoodsSpecificationInfoPage(request);
    }

    @Override
    public Page<StandardGoodsSpecificationPicDTO> querySpecificationByB2b(IndexStandardGoodsSpecificationPageRequest request) {
        return standardGoodsSpecificationService.querySpecificationByB2b(request);
    }

    @Override
    public StandardGoodsSpecificationDTO getStandardGoodsSpecification(Long specificationId) {
        return standardGoodsSpecificationService.getStandardGoodsSpecification(specificationId);
    }

    /**
     * 批量获取规格根据standardId
     *
     * @param ids
     * @return
     */
    @Override
    public List<StandardGoodsSpecificationDTO> getListStandardGoodsSpecification(List<Long> ids) {
        List<StandardGoodsSpecificationDO> result = standardGoodsSpecificationService.getListStandardGoodsSpecification(ids);
        return PojoUtils.map(result,StandardGoodsSpecificationDTO.class);
    }

    /**
     * 根据标准库id返回分页规格信息
     *
     * @param request
     * @return
     */
    @Override
    public Page<StandardGoodsSpecificationDTO> getSpecificationPage(StandardSpecificationPageRequest request) {
        Page<StandardGoodsSpecificationDO> specification = standardGoodsSpecificationService.getSpecificationPage(request);
        Page<StandardGoodsSpecificationDTO> specificationPage = PojoUtils.map(specification, StandardGoodsSpecificationDTO.class);
        return specificationPage;
    }

    @Override
    public Page<StandardGoodsSpecificationDTO> getSpecificationPageByGoods(StandardSpecificationPageRequest request) {
        return standardGoodsSpecificationService.getSpecificationPageByGoods(request);
    }

    @Override
    public Long saveStandardGoodsSpecificationOne(SaveStandardSpecificationRequest request) {
        List<StandardGoodsSpecificationDO> list = standardGoodsSpecificationService.getStandardGoodsSpecificationBySpecificationOrBarcode(request.getStandardId(), request.getSellSpecifications(),request.getBarcode());
        if(CollectionUtils.isNotEmpty(list)){
            StandardGoodsSpecificationDO filter = list.stream().filter(spec -> spec.getBarcode().equals(request.getBarcode())).findFirst().orElse(null);
            if(null!=filter){
                throw new BusinessException(StandardResultCode.STANDARD_SPECIFICATION_DUPLICATION,"条形码"+request.getBarcode()+"已存在");
            }else {
                throw new BusinessException(StandardResultCode.STANDARD_SPECIFICATION_DUPLICATION,"该商品已存在"+request.getSellSpecifications()+"规格");
            }
        }
        StandardGoodsSpecificationDO standardGoodsSpecificationDO=PojoUtils.map(request,StandardGoodsSpecificationDO.class);
        standardGoodsSpecificationDO.setUpdateTime(request.getOpTime());
        standardGoodsSpecificationDO.setUpdateUser(request.getOpUserId());
        return standardGoodsSpecificationService.saveStandardGoodsSpecificationOne(standardGoodsSpecificationDO);
    }

    @Override
    public List<StandardGoodsSpecificationDTO> getListStandardGoodsSpecificationByIds(List<Long> specificationIds) {
        return standardGoodsSpecificationService.getListStandardGoodsSpecificationByIds(specificationIds);
    }

    @Override
    public Page<StandardSpecificationGoodsInfoBO> getSpecificationGoodsInfoPage(StandardSpecificationPageRequest request) {
        return standardGoodsSpecificationService.getSpecificationGoodsInfoPage(request);
    }
}
