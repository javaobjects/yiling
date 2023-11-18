package com.yiling.goods.standard.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.goods.standard.bo.StandardSpecificationGoodsInfoBO;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationDTO;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationPicDTO;
import com.yiling.goods.standard.dto.request.IndexStandardGoodsSpecificationPageRequest;
import com.yiling.goods.standard.dto.request.StandardSpecificationPageRequest;
import com.yiling.goods.standard.entity.StandardGoodsSpecificationDO;

/**
 * <p>
 * 商品规格标准表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-19
 */
public interface StandardGoodsSpecificationService extends BaseService<StandardGoodsSpecificationDO> {

    Page<StandardGoodsSpecificationPicDTO> getIndexStandardGoodsSpecificationInfoPage(IndexStandardGoodsSpecificationPageRequest request);

    /**
     * b2b分页查询标准规格
     * @param request
     * @return
     */
    Page<StandardGoodsSpecificationPicDTO> querySpecificationByB2b(IndexStandardGoodsSpecificationPageRequest request);
    /**
     * 通过标准规格ID获取规格信息
     * @param specificationId
     * @return
     */
    StandardGoodsSpecificationDTO getStandardGoodsSpecification(Long specificationId);

    /**
     * 批量获取规格根据standardId
     * @param ids
     * @return list
     */
    List<StandardGoodsSpecificationDO> getListStandardGoodsSpecification(List<Long> ids);

    /**
     * 批量获取规格根据standardId
     * @param specificationIds
     * @return list
     */
    List<StandardGoodsSpecificationDTO> getListStandardGoodsSpecificationByIds(List<Long> specificationIds);

    /**
     *  保存规格型号
     * @param specificationDO
     * @return
     */
    Long saveStandardGoodsSpecificationOne(StandardGoodsSpecificationDO specificationDO);

    /**
     * 根据标准库id返回分页规格信息
     * @param request
     * @return
     */
    Page<StandardGoodsSpecificationDO> getSpecificationPage(StandardSpecificationPageRequest request);

    /**
     * 通过条形码或者规格名获取规格
     * @param standardId
     * @param specificationName
     * @param barcode
     * @return
     */
    List<StandardGoodsSpecificationDO> getStandardGoodsSpecificationBySpecificationOrBarcode(Long standardId,String specificationName,String barcode);

    /**
     * 根据标准库id返回分页规格信息
     * @param request
     * @return
     */
    Page<StandardGoodsSpecificationDTO> getSpecificationPageByGoods(StandardSpecificationPageRequest request);



    /**
     * 根据条件返回分页规格信息
     * @param request
     * @return
     */
    Page<StandardGoodsSpecificationDTO> querySpecificationPage(StandardSpecificationPageRequest request);


    /**
     * 获取标准规格商品信息
     * @param request
     * @return
     */
    Page<StandardSpecificationGoodsInfoBO> getSpecificationGoodsInfoPage(StandardSpecificationPageRequest request);

}
