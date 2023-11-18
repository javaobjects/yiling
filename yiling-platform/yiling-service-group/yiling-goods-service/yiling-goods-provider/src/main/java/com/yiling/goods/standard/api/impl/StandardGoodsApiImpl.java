package com.yiling.goods.standard.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.standard.api.StandardGoodsApi;
import com.yiling.goods.standard.dto.StandardGoodsAllInfoDTO;
import com.yiling.goods.standard.dto.StandardGoodsBasicInfoDTO;
import com.yiling.goods.standard.dto.StandardGoodsDTO;
import com.yiling.goods.standard.dto.StandardGoodsInfoDTO;
import com.yiling.goods.standard.dto.request.StandardDecotionImportExcelRequest;
import com.yiling.goods.standard.dto.request.StandardDisinfectionImportExcelRequest;
import com.yiling.goods.standard.dto.request.StandardDispensingGranuleImportExcelRequest;
import com.yiling.goods.standard.dto.request.StandardFoodsImportExcelRequest;
import com.yiling.goods.standard.dto.request.StandardGoodsImportExcelRequest;
import com.yiling.goods.standard.dto.request.StandardGoodsInfoRequest;
import com.yiling.goods.standard.dto.request.StandardGoodsSaveInfoRequest;
import com.yiling.goods.standard.dto.request.StandardHealthImportExcelRequest;
import com.yiling.goods.standard.dto.request.StandardMaterialsImportExcelRequest;
import com.yiling.goods.standard.dto.request.StandardMedicalInstrumentImportExcelRequest;
import com.yiling.goods.standard.entity.StandardGoodsDO;
import com.yiling.goods.standard.service.StandardGoodsService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * 库存管理
 *
 * @author: wei.wang
 * @date: 2021/5/20
 */
@DubboService
public class StandardGoodsApiImpl implements StandardGoodsApi {

    @Autowired
    private StandardGoodsService standardGoodsService;

    /**
     * 获取标准商品的信息
     *
     * @param request
     * @return
     */
    @Override
    public Page<StandardGoodsInfoDTO> getStandardGoodsInfo(StandardGoodsInfoRequest request) {

        return standardGoodsService.getStandardGoodsInfo(request);
    }

    /**
     * 根据批准文号查询Id
     *
     * @param licenseNo
     * @return
     */
    @Override
    public Long getStandardGoodsByLicenseNo(String licenseNo) {

        return standardGoodsService.getStandardGoodsByLicenseNo(licenseNo);
    }

    /**
     * 名称模糊批准文号精准匹配药品信息
     *
     * @param name
     * @return
     */
    @Override
    public List<StandardGoodsBasicInfoDTO> getStandardGoodsByLicenseNoAndName(String name) {
        return standardGoodsService.getStandardGoodsByLicenseNoAndName(name);
    }

    /**
     * 根据批准文号和类型查询Id
     *
     * @param licenseNo
     * @param goodsType
     * @return
     */
    @Override
    public Long getStandardIdByLicenseNoAndType(String licenseNo, Integer goodsType) {
        return standardGoodsService.getStandardIdByLicenseNoAndType(licenseNo,goodsType);
    }

    /**
     * 根据Id查询标准商品
     *
     * @param id
     * @return
     */
    @Override
    public StandardGoodsAllInfoDTO getStandardGoodsById(Long id) {
        return standardGoodsService.getStandardGoodsById(id);
    }

    /**
     * 保存所有标准商品信息
     *
     * @param request
     */
    @Override
    public Long saveStandardGoodAllInfo(StandardGoodsSaveInfoRequest request) {
        return standardGoodsService.saveStandardGoodAllInfo(request);
    }

    /**
     * 保存药品
     * @param one
     * @return
     */
    @Override
    public Long saveStandardGoodAllInfoOne(StandardGoodsImportExcelRequest one) {
        return standardGoodsService.saveStandardGoodAllInfoOne(one);
    }

    /**
     * 保存中药饮品
     *
     * @param one
     */
    @Override
    public Long saveStandardDecoctionAllInfoOne(StandardDecotionImportExcelRequest one) {
        return standardGoodsService.saveStandardDecoctionAllInfoOne(one);
    }

    /**
     * 保存保健食品信息
     * @param one
     * @return
     */
    @Override
    public Long saveStandardHealthAllInfoOne(StandardHealthImportExcelRequest one) {
        return standardGoodsService.saveStandardHealthAllInfoOne(one);
    }

    /**
     * 保存消杀品信息
     * @param one
     * @return
     */
    @Override
    public Long saveStandardDisinfectionAllInfoOne(StandardDisinfectionImportExcelRequest one) {
        return standardGoodsService.saveStandardDisinfectionAllInfoOne(one);
    }

    /**
     * 保存中药材信息
     *
     * @param one
     */
    @Override
    public Long saveStandardMaterialsAllInfoOne(StandardMaterialsImportExcelRequest one) {
        return standardGoodsService.saveStandardMaterialsAllInfoOne(one);
    }

    /**
     * 保存食品信息
     * @param one
     * @return
     */
    @Override
    public Long saveStandardFoodsAllInfoOne(StandardFoodsImportExcelRequest one) {
        return standardGoodsService.saveStandardFoodsAllInfoOne(one);
    }

    @Override
    public Long saveStandardMedicalInstrumentAllInfoOne(StandardMedicalInstrumentImportExcelRequest one) {
        return standardGoodsService.saveStandardMedicalInstrumentAllInfoOne(one);
    }

    @Override
    public Long saveStandardDispensingGranuleAllInfoOne(StandardDispensingGranuleImportExcelRequest one) {
        return standardGoodsService.saveStandardDispensingGranuleAllInfoOne(one);
    }

    @Override
    public Page<StandardGoodsInfoDTO> getYilingStandardGoodsInfo(StandardGoodsInfoRequest request) {
        return standardGoodsService.getYilingStandardGoodsInfo(request);
    }

    @Override
    public StandardGoodsDTO getOneById(Long id) {
        StandardGoodsDO standardGoodsDO = standardGoodsService.getById(id);
        return PojoUtils.map(standardGoodsDO,StandardGoodsDTO.class);
    }

    @Override
    public List<StandardGoodsDTO> getStandardGoodsByIds(List<Long> ids) {
        return standardGoodsService.getStandardGoodsByIds(ids);
    }

}
