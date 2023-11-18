package com.yiling.goods.standard.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
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

/**
 * <p>
 * 商品标准表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-19
 */
public interface StandardGoodsService extends BaseService<StandardGoodsDO> {

    /**
     * 获取标准库信息
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
     * 根据批准文号和名称查询标准库Id
     * @param licenseNo
     * @param name
     * @return
     */
    Long getStandardGoodsByLicenseNoAndName(String licenseNo,String name);

    /**
     * 根据批准问号和类型查询Id
     *
     * @param licenseNo
     * @param goodsType
     * @return
     */
    Long getStandardIdByLicenseNoAndType(String licenseNo,Integer goodsType);

    /**
     * 根据入参 批准文号精准和名称模糊查询
     * @param name
     * @return
     */
    List<StandardGoodsBasicInfoDTO> getStandardGoodsByLicenseNoAndName(String name);

    /**
     * 根据Id查询标准商品
     *
     * @param id
     * @return
     */
    StandardGoodsAllInfoDTO getStandardGoodsById(Long id);

    /**
     * 保存标准库信息
     *
     * @param request
     * @return
     */
    Long saveStandardGoodAllInfo(StandardGoodsSaveInfoRequest request);

    /**
     * 批量保存药品信息
     * @param one
     * @return
     */
    Long saveStandardGoodAllInfoOne(StandardGoodsImportExcelRequest one);

    /**
     * 保存中药饮品信息
     * @param one
     * @return
     */
    Long saveStandardDecoctionAllInfoOne(StandardDecotionImportExcelRequest one);

    /**
     * 保存保健品消息
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
     *
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
     * 根据分类id修改分类名称
     * @param name 名称
     * @param categoryId 分类id
     * @param opUserId 操作人
     */
    void updateCategoryName1ById(String name,Long categoryId,Long opUserId);

    /**
     * 根据分类id修改分类名称
     * @param name 名称
     * @param categoryId 分类id
     * @param opUserId 操作人
     */
    void updateCategoryName2ById(String name,Long categoryId,Long opUserId);


    /**
     * 修改父类id，和名称
     * @param parentId 新分类
     * @param categoryId 分类id
     * @param name 父类名称
     * @return
     */
    Boolean updateCategoryId(Long parentId,Long categoryId,String name,Long opUserId);

    /**
     * id 批量修改以岭标识
     * @param standardId
     * @param ylFlag
     * @return
     */
    Boolean updateYlFlagById(Long standardId,Integer ylFlag);

    /**
     * 获取标准库以岭品信息
     * @param request
     * @return
     */
    Page<StandardGoodsInfoDTO> getYilingStandardGoodsInfo(StandardGoodsInfoRequest request);

    /**
     * id 批量查询StandardGoods
     * @param ids
     * @return
     */
    List<StandardGoodsDTO> getStandardGoodsByIds(List<Long> ids);


}
