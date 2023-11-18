package com.yiling.goods.standard.api;


import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

/**
 * @author: wei.wang
 * @date: 2021/5/20
 */
public interface StandardGoodsApi {

    /**
     * 获取标准商品的信息
     * @param request
     * @return
     */
    Page<StandardGoodsInfoDTO> getStandardGoodsInfo(StandardGoodsInfoRequest request);

    /**
     * 根据批准文号查询Id
     *
     * @param licenseNo
     * @return
     */
    Long getStandardGoodsByLicenseNo(String licenseNo);

    /**
     * 根据入参 查询名称模糊 或者批准文号精准匹配药品信息
     * @param name
     * @return
     */
    List<StandardGoodsBasicInfoDTO> getStandardGoodsByLicenseNoAndName(String name);


    /**
     *  根据批准文号和类型查询Id
     * @param licenseNo
     * @param goodsType
     * @return
     */
    Long getStandardIdByLicenseNoAndType(String licenseNo,Integer goodsType);

    /**
     * 根据Id查询标准商品
     *
     * @param id
     * @return
     */
    StandardGoodsAllInfoDTO getStandardGoodsById(Long id);

    /**
     * 保存所有标准商品信息
     *
     * @param request
     */
    Long saveStandardGoodAllInfo(StandardGoodsSaveInfoRequest request);

    /**
     * 保存药品
     * @param one
     * @return
     */
    Long saveStandardGoodAllInfoOne (StandardGoodsImportExcelRequest one);

    /**
     * 保存中药饮品
     * @param one
     * @return
     */
    Long saveStandardDecoctionAllInfoOne(StandardDecotionImportExcelRequest one);

    /**
     * 保存保健食品信息
     * @param one
     * @return
     */
    Long saveStandardHealthAllInfoOne(StandardHealthImportExcelRequest one);

    /**
     * 保存消杀品信息
     * @param one
     * @return
     */
    Long saveStandardDisinfectionAllInfoOne(StandardDisinfectionImportExcelRequest one);

    /**
     * 保存中药材信息
     * @param one
     * @return
     */
    Long saveStandardMaterialsAllInfoOne(StandardMaterialsImportExcelRequest one);

    /**
     * 保存食品信息
     * @param one
     * @return
     */
    Long saveStandardFoodsAllInfoOne(StandardFoodsImportExcelRequest one);

    /**
     * 保存医疗器械信息
     * @param one
     * @return
     */
    Long saveStandardMedicalInstrumentAllInfoOne(StandardMedicalInstrumentImportExcelRequest one);

    /**
     * 保存配方颗粒信息
     * @param one
     * @return
     */
    Long saveStandardDispensingGranuleAllInfoOne(StandardDispensingGranuleImportExcelRequest one);
    /**
     * 获取标准库以岭品信息
     * @param request
     * @return
     */
    Page<StandardGoodsInfoDTO> getYilingStandardGoodsInfo(StandardGoodsInfoRequest request);

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    StandardGoodsDTO getOneById(Long id);

    /**
     * id 批量查询StandardGoods
     * @param ids
     * @return
     */
    List<StandardGoodsDTO> getStandardGoodsByIds(List<Long> ids);

}
