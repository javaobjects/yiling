package com.yiling.goods.standard.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.goods.standard.bo.StandardSpecificationGoodsInfoBO;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationDTO;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationPicDTO;
import com.yiling.goods.standard.dto.request.IndexStandardGoodsSpecificationPageRequest;
import com.yiling.goods.standard.dto.request.SaveStandardSpecificationRequest;
import com.yiling.goods.standard.dto.request.StandardSpecificationPageRequest;

/**
 * @author wei.wang
 * @date 2021/5/21
 */
public interface StandardGoodsSpecificationApi {

    /**
     * 获取标准商品的信息
     * @param request
     * @return
     */
    Page<StandardGoodsSpecificationPicDTO> getIndexStandardGoodsSpecificationInfoPage(IndexStandardGoodsSpecificationPageRequest request);

    /**
     * b2b分页查询标准规格
     * @param request
     * @return
     */
    Page<StandardGoodsSpecificationPicDTO> querySpecificationByB2b(IndexStandardGoodsSpecificationPageRequest request);

    /**
     * 通过销售规格ID
     *
     * @param specificationId
     * @return
     */
    StandardGoodsSpecificationDTO getStandardGoodsSpecification(Long specificationId);

    /**
     * 批量获取规格根据standardId
     *
     * @param ids
     * @return
     */
    List<StandardGoodsSpecificationDTO> getListStandardGoodsSpecification(List<Long> ids);

    /**
     * 根据标准库id返回分页规格信息
     *
     * @param request
     * @return
     */
    Page<StandardGoodsSpecificationDTO> getSpecificationPage(StandardSpecificationPageRequest request);

    /**
     * 根据标准库id和标准库商品名称返回分页规格信息
     * @param request
     * @return
     */
    Page<StandardGoodsSpecificationDTO> getSpecificationPageByGoods(StandardSpecificationPageRequest request);

    /**
     *  保存规格型号
     * @param request
     * @return
     */
    Long saveStandardGoodsSpecificationOne(SaveStandardSpecificationRequest request);

    /**
     * 根据规格id查询数据
     * @param specificationIds 规格id
     * @return
     */
    List<StandardGoodsSpecificationDTO> getListStandardGoodsSpecificationByIds(List<Long> specificationIds);

    /**
     * 查询规格商品信息
     * @param request
     * @return
     */
    Page<StandardSpecificationGoodsInfoBO> getSpecificationGoodsInfoPage(StandardSpecificationPageRequest request);
}
