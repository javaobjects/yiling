package com.yiling.goods.standard.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.entity.GoodsDO;
import com.yiling.goods.medicine.service.GoodsService;
import com.yiling.goods.standard.dao.StandardGoodsMapper;
import com.yiling.goods.standard.dto.StandardGoodsAllInfoDTO;
import com.yiling.goods.standard.dto.StandardGoodsBasicInfoDTO;
import com.yiling.goods.standard.dto.StandardGoodsDTO;
import com.yiling.goods.standard.dto.StandardGoodsInfoDTO;
import com.yiling.goods.standard.dto.StandardGoodsPicDTO;
import com.yiling.goods.standard.dto.StandardInstructionsDecoctionDTO;
import com.yiling.goods.standard.dto.StandardInstructionsDisinfectionDTO;
import com.yiling.goods.standard.dto.StandardInstructionsDispensingGranuleDTO;
import com.yiling.goods.standard.dto.StandardInstructionsFoodsDTO;
import com.yiling.goods.standard.dto.StandardInstructionsGoodsDTO;
import com.yiling.goods.standard.dto.StandardInstructionsHealthDTO;
import com.yiling.goods.standard.dto.StandardInstructionsMaterialsDTO;
import com.yiling.goods.standard.dto.StandardInstructionsMedicalInstrumentDTO;
import com.yiling.goods.standard.dto.StandardSpecificationInfoDTO;
import com.yiling.goods.standard.dto.request.StandardDecotionImportExcelRequest;
import com.yiling.goods.standard.dto.request.StandardDisinfectionImportExcelRequest;
import com.yiling.goods.standard.dto.request.StandardDispensingGranuleImportExcelRequest;
import com.yiling.goods.standard.dto.request.StandardFoodsImportExcelRequest;
import com.yiling.goods.standard.dto.request.StandardGoodsBasicInfoRequest;
import com.yiling.goods.standard.dto.request.StandardGoodsImportExcelRequest;
import com.yiling.goods.standard.dto.request.StandardGoodsInfoRequest;
import com.yiling.goods.standard.dto.request.StandardGoodsPicRequest;
import com.yiling.goods.standard.dto.request.StandardGoodsSaveInfoRequest;
import com.yiling.goods.standard.dto.request.StandardHealthImportExcelRequest;
import com.yiling.goods.standard.dto.request.StandardMaterialsImportExcelRequest;
import com.yiling.goods.standard.dto.request.StandardMedicalInstrumentImportExcelRequest;
import com.yiling.goods.standard.dto.request.StandardSpecificationInfoRequest;
import com.yiling.goods.standard.entity.StandardGoodsCategoryDO;
import com.yiling.goods.standard.entity.StandardGoodsDO;
import com.yiling.goods.standard.entity.StandardGoodsPicDO;
import com.yiling.goods.standard.entity.StandardGoodsSpecificationDO;
import com.yiling.goods.standard.entity.StandardInstructionsDecoctionDO;
import com.yiling.goods.standard.entity.StandardInstructionsDisinfectionDO;
import com.yiling.goods.standard.entity.StandardInstructionsDispensingGranuleDO;
import com.yiling.goods.standard.entity.StandardInstructionsFoodsDO;
import com.yiling.goods.standard.entity.StandardInstructionsGoodsDO;
import com.yiling.goods.standard.entity.StandardInstructionsHealthDO;
import com.yiling.goods.standard.entity.StandardInstructionsMaterialsDO;
import com.yiling.goods.standard.entity.StandardInstructionsMedicalInstrumentDO;
import com.yiling.goods.standard.enums.StandardGoodsTypeEnum;
import com.yiling.goods.standard.enums.StandardResultCode;
import com.yiling.goods.standard.service.StandardGoodsCategoryService;
import com.yiling.goods.standard.service.StandardGoodsPicService;
import com.yiling.goods.standard.service.StandardGoodsService;
import com.yiling.goods.standard.service.StandardGoodsSpecificationService;
import com.yiling.goods.standard.service.StandardInstructionsDecoctionService;
import com.yiling.goods.standard.service.StandardInstructionsDisinfectionService;
import com.yiling.goods.standard.service.StandardInstructionsDispensingGranuleService;
import com.yiling.goods.standard.service.StandardInstructionsFoodsService;
import com.yiling.goods.standard.service.StandardInstructionsGoodsService;
import com.yiling.goods.standard.service.StandardInstructionsHealthService;
import com.yiling.goods.standard.service.StandardInstructionsMaterialsService;
import com.yiling.goods.standard.service.StandardInstructionsMedicalInstrumentService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 商品标准表 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-19
 */
@Slf4j
@Service
public class StandardGoodsServiceImpl extends BaseServiceImpl<StandardGoodsMapper, StandardGoodsDO> implements StandardGoodsService {

    @Autowired
    private StandardGoodsMapper standardGoodsMapper;

    @Autowired
    private StandardGoodsSpecificationService standardGoodsSpecificationService;

    @Autowired
    private StandardGoodsCategoryService standardGoodsCateService;

    @Autowired
    private StandardInstructionsGoodsService standardInstructionsGoodsService;

    @Autowired
    private StandardInstructionsDecoctionService standardInstructionsDecoctionService;

    @Autowired
    private StandardInstructionsMaterialsService standardInstructionsMaterialsService;

    @Autowired
    private StandardInstructionsDisinfectionService standardInstructionsDisinfectionService;

    @Autowired
    private StandardInstructionsHealthService standardInstructionsHealthService;

    @Autowired
    private StandardInstructionsFoodsService standardInstructionsFoodsService;

    @Autowired
    private StandardInstructionsMedicalInstrumentService standardInstructionsMedicalInstrumentService;

    @Autowired
    private StandardInstructionsDispensingGranuleService standardInstructionsDispensingGranuleService;

    @Autowired
    private StandardGoodsPicService standardGoodsPicService;

    @Autowired
    private GoodsService goodsService;
    /**
     * 获取标准库信息
     *
     * @param request
     * @return
     */
    @Override
    public Page<StandardGoodsInfoDTO> getStandardGoodsInfo(StandardGoodsInfoRequest request) {
        Page<StandardGoodsInfoDTO> dtoPage = new Page<>();
        Page<StandardGoodsDO> page = new Page();
        page.setSize(request.getSize());
        page.setCurrent(request.getCurrent());
        Page<StandardGoodsDO> standardGoodsPage = standardGoodsMapper.standardGoodsInfoPage(page, request);
        List<StandardGoodsDO> records = standardGoodsPage.getRecords();
        Map<Long, Integer> mapSpecification = new HashMap<>();

        if (CollectionUtils.isNotEmpty(records)) {
            List<Long> setStandardId = new ArrayList<>();
            Set<Long> setUserId = new HashSet<>();
            for (StandardGoodsDO one : records) {
                setStandardId.add(one.getId());
                setUserId.add(one.getUpdateUser());
            }
            List<StandardGoodsSpecificationDO> specificationList = standardGoodsSpecificationService.getListStandardGoodsSpecification(setStandardId);
            if (CollectionUtils.isNotEmpty(specificationList)) {
                for (StandardGoodsSpecificationDO one : specificationList) {
                    if (mapSpecification.containsKey(one.getStandardId())) {
                        Integer num = mapSpecification.get(one.getStandardId()) + 1;
                        mapSpecification.put(one.getStandardId(), num);
                    } else {
                        mapSpecification.put(one.getStandardId(), 1);
                    }

                }
            }

            dtoPage = PojoUtils.map(standardGoodsPage, StandardGoodsInfoDTO.class);
            for (StandardGoodsInfoDTO dto : dtoPage.getRecords()) {
                String sagName1 = dto.getStandardCategoryName1();
                String sagName2 = dto.getStandardCategoryName2();
                StringBuilder sagName = new StringBuilder();
                if (StringUtils.isNotBlank(sagName1)) {
                    sagName.append(sagName1);
                }
                if (StringUtils.isNotBlank(sagName2)) {
                    sagName.append("-").append(sagName2);
                }
                dto.setStandardCategoryName(sagName.toString());
                dto.setSpecificationNumber(mapSpecification.get(dto.getId()) != null ? mapSpecification.get(dto.getId()) : 0);
                dto.setPic(goodsService.getDefaultUrl(dto.getId(), 0L));
            }

        }

        return dtoPage;
    }


    /**
     * 根据批准文号查询Id
     *
     * @param licenseNo
     * @return
     */
    @Override
    public Long getStandardGoodsByLicenseNo(String licenseNo) {
        QueryWrapper<StandardGoodsDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StandardGoodsDO::getLicenseNo, licenseNo).last("limit 1");
        StandardGoodsDO goodsDO = this.getOne(wrapper);
        if (goodsDO != null) {
            return goodsDO.getId();
        }
        return null;
    }

    /**
     * 根据批准文号和商品名称查询Id
     *
     * @param licenseNo
     * @param name
     * @return
     */
    @Override
    public Long getStandardGoodsByLicenseNoAndName(String licenseNo,String name) {
        QueryWrapper<StandardGoodsDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StandardGoodsDO::getLicenseNo, licenseNo).eq(StandardGoodsDO::getName,name).last("limit 1");
        StandardGoodsDO goodsDO = this.getOne(wrapper);
        if (goodsDO != null) {
            return goodsDO.getId();
        }
        return null;
    }

    /**
     * 根据批准问号和类型查询Id
     *
     * @param licenseNo
     * @param goodsType
     * @return
     */
    @Override
    public Long getStandardIdByLicenseNoAndType(String licenseNo, Integer goodsType) {
        QueryWrapper<StandardGoodsDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StandardGoodsDO::getLicenseNo, licenseNo).
                eq(StandardGoodsDO::getGoodsType, goodsType)
                .last("limit 1");
        StandardGoodsDO goodsDO = this.getOne(wrapper);
        if (goodsDO != null) {
            return goodsDO.getId();
        }
        return null;
    }

    /**
     * 根据入参 批准文号精准和名称模糊查询
     * @param name
     * @return
     */
    @Override
    public List<StandardGoodsBasicInfoDTO> getStandardGoodsByLicenseNoAndName(String name) {
        QueryWrapper<StandardGoodsDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(StandardGoodsDO::getGoodsType,new ArrayList<Integer>(){{
            add(StandardGoodsTypeEnum.GOODS_TYPE.getCode());
            add(StandardGoodsTypeEnum.HEALTH_TYPE.getCode());
        }
        }).and(m -> m.like(StandardGoodsDO :: getName,name).or().eq(StandardGoodsDO :: getLicenseNo,name));
        return PojoUtils.map(list(wrapper),StandardGoodsBasicInfoDTO.class);
    }

    /**
     * 根据Id查询货物
     *
     * @return
     */
    @Override
    public StandardGoodsAllInfoDTO getStandardGoodsById(Long id) {
        StandardGoodsDO standardGoodsDO = this.getById(id);
        StandardGoodsBasicInfoDTO basicInfoDTO = PojoUtils.map(standardGoodsDO, StandardGoodsBasicInfoDTO.class);
        if (basicInfoDTO != null) {
            StandardGoodsAllInfoDTO result = new StandardGoodsAllInfoDTO();
            StandardGoodsTypeEnum goodsTypeEnum = StandardGoodsTypeEnum.find(basicInfoDTO.getGoodsType());
            switch (goodsTypeEnum){
                case GOODS_TYPE:
                    //1普通药品
                    StandardInstructionsGoodsDO goodsByStandardId = standardInstructionsGoodsService.getInstructionsGoodsByStandardId(basicInfoDTO.getId());
                    StandardInstructionsGoodsDTO goodsDTO = PojoUtils.map(goodsByStandardId, StandardInstructionsGoodsDTO.class);
                    result.setGoodsInstructionsInfo(goodsDTO);
                    break;
                case DECOCTION_TYPE:
                    //2中药饮片
                    StandardInstructionsDecoctionDO decoction = standardInstructionsDecoctionService.getInstructionsDecoctionByStandardId(basicInfoDTO.getId());
                    StandardInstructionsDecoctionDTO decoctionDTO = PojoUtils.map(decoction, StandardInstructionsDecoctionDTO.class);
                    result.setDecoctionInstructionsInfo(decoctionDTO);
                    break;
                case MATERIAL_TYPE:
                    //3中药材
                    StandardInstructionsMaterialsDO materialsDO = standardInstructionsMaterialsService.getInstructionsMaterialsByStandardId(basicInfoDTO.getId());
                    StandardInstructionsMaterialsDTO materialsDTO = PojoUtils.map(materialsDO, StandardInstructionsMaterialsDTO.class);
                    result.setMaterialsInstructionsInfo(materialsDTO);
                    break;
                case DISINFECTION_TYPE:
                    //4消杀
                    StandardInstructionsDisinfectionDO disinfection = standardInstructionsDisinfectionService.getInstructionsDisinfectionByStandardId(basicInfoDTO.getId());
                    StandardInstructionsDisinfectionDTO disinfectionDTO = PojoUtils.map(disinfection, StandardInstructionsDisinfectionDTO.class);
                    result.setDisinfectionInstructionsInfo(disinfectionDTO);
                    break;
                case HEALTH_TYPE:
                    //5保健食品
                    StandardInstructionsHealthDO health = standardInstructionsHealthService.getInstructionsHealthByStandardId(basicInfoDTO.getId());
                    StandardInstructionsHealthDTO healthDTO = PojoUtils.map(health, StandardInstructionsHealthDTO.class);
                    result.setHealthInstructionsInfo(healthDTO);
                    break;
                case FOODS_TYPE:
                    //6食品
                    StandardInstructionsFoodsDO foods = standardInstructionsFoodsService.getInstructionsFoodsByStandardId(basicInfoDTO.getId());
                    StandardInstructionsFoodsDTO foodsDTO = PojoUtils.map(foods, StandardInstructionsFoodsDTO.class);
                    result.setFoodsInstructionsInfo(foodsDTO);
                    break;
                case MEDICAL_INSTRUMENT_TYPE:
                    //7医疗器械
                    StandardInstructionsMedicalInstrumentDO medicalInstrument = standardInstructionsMedicalInstrumentService.getInstructionsMedicalInstrumentByStandardId(basicInfoDTO.getId());
                    StandardInstructionsMedicalInstrumentDTO medicalInstrumentDTO = PojoUtils.map(medicalInstrument, StandardInstructionsMedicalInstrumentDTO.class);
                    result.setMedicalInstrumentInfo(medicalInstrumentDTO);
                    break;
                case DISPENSING_GRANULE_TYPE:
                    //8配方颗粒
                    StandardInstructionsDispensingGranuleDO dispensingGranule = standardInstructionsDispensingGranuleService.getInstructionsDispensingGranuleByStandardId(basicInfoDTO.getId());
                    StandardInstructionsDispensingGranuleDTO dispensingGranuleDTO = PojoUtils.map(dispensingGranule, StandardInstructionsDispensingGranuleDTO.class);
                    result.setDispensingGranuleInfo(dispensingGranuleDTO);
                    break;
            }
            List<StandardGoodsSpecificationDO> goodsSpecification = standardGoodsSpecificationService.getListStandardGoodsSpecification(new ArrayList<Long>() {{
                add(basicInfoDTO.getId());
            }});
            if (CollectionUtils.isNotEmpty(goodsSpecification)) {
                List<StandardSpecificationInfoDTO> specificationLists = PojoUtils.map(goodsSpecification, StandardSpecificationInfoDTO.class);
                for (StandardSpecificationInfoDTO one : specificationLists) {
                    List<StandardGoodsPicDO> goodsPic = standardGoodsPicService.getStandardGoodsPic(one.getStandardId(), one.getId());
                    List<StandardGoodsPicDTO> picDTO = PojoUtils.map(goodsPic, StandardGoodsPicDTO.class);
                    one.setPicInfoList(picDTO);
                }
                result.setSpecificationInfo(specificationLists);
            }

            List<StandardGoodsPicDO> goodsBasicsPic = standardGoodsPicService.getStandardGoodsPic(id, 0L);
            if (CollectionUtils.isNotEmpty(goodsBasicsPic)) {
                List<StandardGoodsPicDTO> picBasicsDTO = PojoUtils.map(goodsBasicsPic, StandardGoodsPicDTO.class);
                result.setPicBasicsInfoList(picBasicsDTO);
            }
            result.setBaseInfo(basicInfoDTO);
            return result;
        }
        return null;
    }

    /**
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveStandardGoodAllInfo(StandardGoodsSaveInfoRequest request) {
        StandardGoodsBasicInfoRequest baseIfo = request.getBaseInfo();
        StandardGoodsDO standardGoodsDO = PojoUtils.map(baseIfo, StandardGoodsDO.class);
        standardGoodsDO.setOpUserId(request.getOpUserId());
        standardGoodsDO.setOpTime(request.getOpTime());
        if (StandardGoodsTypeEnum.GOODS_TYPE.getCode().equals(standardGoodsDO.getGoodsType())
                ||StandardGoodsTypeEnum.MEDICAL_INSTRUMENT_TYPE.getCode().equals(standardGoodsDO.getGoodsType())) {
            Long id = getStandardIdByLicenseNoAndType(standardGoodsDO.getLicenseNo(), standardGoodsDO.getGoodsType());
            if (id != null && !id.equals(standardGoodsDO.getId())) {
                if(StandardGoodsTypeEnum.GOODS_TYPE.getCode().equals(standardGoodsDO.getGoodsType())){
                    throw new BusinessException(StandardResultCode.STANDARD_DATA_DUPLICATION);
                }else if(StandardGoodsTypeEnum.MEDICAL_INSTRUMENT_TYPE.getCode().equals(standardGoodsDO.getGoodsType())){
                    throw new BusinessException(StandardResultCode.STANDARD_DATA_DUPLICATION,"标准库医疗器械注册证编号重复");
                }

            }
        }
        Map<Long, String> cateMap = new HashMap<>();
        List<StandardGoodsCategoryDO> cateList = standardGoodsCateService.getStandardGoodsCateList(new HashSet<Long>() {{
            add(standardGoodsDO.getStandardCategoryId1());
            add(standardGoodsDO.getStandardCategoryId2());
        }});

        if (CollectionUtils.isNotEmpty(cateList)) {
            cateMap = cateList.stream().collect(Collectors.toMap(StandardGoodsCategoryDO::getId, StandardGoodsCategoryDO::getName, (k1, k2) -> k1));
        }
        standardGoodsDO.setStandardCategoryName1(cateMap.get(standardGoodsDO.getStandardCategoryId1()));
        standardGoodsDO.setStandardCategoryName2(cateMap.get(standardGoodsDO.getStandardCategoryId2()));

        this.saveOrUpdate(standardGoodsDO);
        StandardGoodsTypeEnum goodsTypeEnum = StandardGoodsTypeEnum.find(standardGoodsDO.getGoodsType());
        switch (goodsTypeEnum){
            case GOODS_TYPE:
                StandardInstructionsGoodsDO goodsDO = PojoUtils.map(request.getGoodsInstructionsInfo(), StandardInstructionsGoodsDO.class);
                goodsDO.setStandardId(standardGoodsDO.getId());
                standardInstructionsGoodsService.saveInstructionsGoodsOne(goodsDO);
                break;
            case DECOCTION_TYPE:
                StandardInstructionsDecoctionDO decoctionDO = PojoUtils.map(request.getDecoctionInstructionsInfo(), StandardInstructionsDecoctionDO.class);
                decoctionDO.setStandardId(standardGoodsDO.getId());
                standardInstructionsDecoctionService.saveInstructionsDecoctionOne(decoctionDO);
                break;
            case MATERIAL_TYPE:
                StandardInstructionsMaterialsDO materialsDO = PojoUtils.map(request.getMaterialsInstructionsInfo(), StandardInstructionsMaterialsDO.class);
                materialsDO.setStandardId(standardGoodsDO.getId());
                standardInstructionsMaterialsService.saveInstructionsMaterialsOne(materialsDO);
                break;
            case DISINFECTION_TYPE:
                StandardInstructionsDisinfectionDO disinfectionDO = PojoUtils.map(request.getDisinfectionInstructionsInfo(), StandardInstructionsDisinfectionDO.class);
                disinfectionDO.setStandardId(standardGoodsDO.getId());
                standardInstructionsDisinfectionService.saveInstructionsDisinfectionOne(disinfectionDO);
                break;
            case HEALTH_TYPE:
                StandardInstructionsHealthDO healthDO = PojoUtils.map(request.getHealthInstructionsInfo(), StandardInstructionsHealthDO.class);
                healthDO.setStandardId(standardGoodsDO.getId());
                standardInstructionsHealthService.saveInstructionsHealthOne(healthDO);
                break;
            case FOODS_TYPE:
                StandardInstructionsFoodsDO foodsDO = PojoUtils.map(request.getFoodsInstructionsInfo(), StandardInstructionsFoodsDO.class);
                foodsDO.setStandardId(standardGoodsDO.getId());
                standardInstructionsFoodsService.saveInstructionsFoodsOne(foodsDO);
                break;
            case MEDICAL_INSTRUMENT_TYPE:
                StandardInstructionsMedicalInstrumentDO medicalInstrumentDO = PojoUtils.map(request.getMedicalInstrumentInfo(), StandardInstructionsMedicalInstrumentDO.class);
                medicalInstrumentDO.setStandardId(standardGoodsDO.getId());
                standardInstructionsMedicalInstrumentService.saveInstructionsMedicalInstrumentOne(medicalInstrumentDO);
                break;
            case DISPENSING_GRANULE_TYPE:
                StandardInstructionsDispensingGranuleDO dispensingGranuleDO = PojoUtils.map(request.getDispensingGranuleInfo(), StandardInstructionsDispensingGranuleDO.class);
                dispensingGranuleDO.setStandardId(standardGoodsDO.getId());
                standardInstructionsDispensingGranuleService.saveInstructionsDispensingGranuleOne(dispensingGranuleDO);
                break;
        }
        //删除图片
        standardGoodsPicService.deleteStandardGoodsPicByStandardId(standardGoodsDO.getId(),request.getOpUserId());
        List<StandardGoodsPicRequest> picBasicsInfoList = request.getPicBasicsInfoList();
        if(CollectionUtils.isNotEmpty(picBasicsInfoList)){
            List<StandardGoodsPicDO> picDos = new ArrayList<>();
            for(StandardGoodsPicRequest one : picBasicsInfoList){
                StandardGoodsPicDO picDO = PojoUtils.map(one, StandardGoodsPicDO.class);
                picDO.setStandardId(standardGoodsDO.getId());
                picDO.setUpdateUser(request.getOpUserId());
                picDO.setUpdateTime(request.getOpTime());
                picDos.add(picDO);
            }
            standardGoodsPicService.saveStandardGoodsPicBatch(picDos);
        }

        List<StandardSpecificationInfoRequest> specificationInfoDTO = request.getSpecificationInfo();
        if (CollectionUtils.isNotEmpty(specificationInfoDTO)) {
            List<StandardGoodsPicDO> listPicKeyList = new ArrayList<>();
            for (StandardSpecificationInfoRequest one : specificationInfoDTO) {
                if (StringUtils.isNotBlank(one.getSellSpecifications())) {
                    StandardGoodsSpecificationDO specificationDO = PojoUtils.map(one, StandardGoodsSpecificationDO.class);
                    List<StandardGoodsSpecificationDO> list = standardGoodsSpecificationService.getStandardGoodsSpecificationBySpecificationOrBarcode(standardGoodsDO.getId(), specificationDO.getSellSpecifications(),specificationDO.getBarcode());
                    if(CollectionUtils.isNotEmpty(list)){
                        List<StandardGoodsSpecificationDO> specFilterList = list.stream().filter(spec -> !spec.getId().equals(specificationDO.getId())).collect(Collectors.toList());
                        if(CollectionUtils.isNotEmpty(specFilterList)){
                            StandardGoodsSpecificationDO filter = null;
                            if(StringUtils.isNotBlank(specificationDO.getBarcode())){
                                filter = list.stream().filter(spec->specificationDO.getBarcode().equals(spec.getBarcode())).findFirst().orElse(null);
                            }
                            if(null!=filter){
                                throw new BusinessException(StandardResultCode.STANDARD_SPECIFICATION_DUPLICATION,"条形码"+specificationDO.getBarcode()+"已存在");
                            }else {
                                throw new BusinessException(StandardResultCode.STANDARD_SPECIFICATION_DUPLICATION,"该商品已存在"+specificationDO.getSellSpecifications()+"规格");
                            }
                        }
                    }
                    specificationDO.setStandardId(standardGoodsDO.getId());
                    specificationDO.setManufacturer(standardGoodsDO.getManufacturer());
                    specificationDO.setLicenseNo(standardGoodsDO.getLicenseNo());
                    specificationDO.setName(standardGoodsDO.getName());
                    specificationDO.setUpdateUser(request.getOpUserId());
                    specificationDO.setUpdateTime(request.getOpTime());
                    standardGoodsSpecificationService.saveStandardGoodsSpecificationOne(specificationDO);
                    if(specificationDO.getId() != null){
                        QueryWrapper<GoodsDO> goodsWrapper = new QueryWrapper<>();
                        goodsWrapper.lambda().eq(GoodsDO :: getSellSpecificationsId,specificationDO.getId());
                        GoodsDO goodsOne = new GoodsDO();
                        goodsOne.setSellSpecifications(specificationDO.getSellSpecifications());
                        goodsOne.setSellUnit(specificationDO.getUnit());
                        goodsService.update(goodsOne,goodsWrapper);
                    }
                    List<StandardGoodsPicRequest> picDTOList = one.getPicInfoList();
                    if (CollectionUtils.isNotEmpty(picDTOList)) {

                        List<StandardGoodsPicDO> picDos = PojoUtils.map(picDTOList, StandardGoodsPicDO.class);
                        for (StandardGoodsPicDO picDO : picDos) {
                            picDO.setSellSpecificationsId(specificationDO.getId());
                            picDO.setStandardId(standardGoodsDO.getId());
                            picDO.setUpdateUser(request.getOpUserId());
                            picDO.setUpdateTime(request.getOpTime());
                            listPicKeyList.add(picDO);
                        }

                    }
                }
            }
            if(CollectionUtil.isNotEmpty(listPicKeyList)){
                standardGoodsPicService.saveStandardGoodsPicBatch(listPicKeyList);
            }

        }
        return standardGoodsDO.getId();
    }

    /**
     * 批量保存药品信息
     *
     * @param one
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveStandardGoodAllInfoOne(StandardGoodsImportExcelRequest one) {

        one.setGoodsType(StandardGoodsTypeEnum.GOODS_TYPE.getCode());
        StandardGoodsCategoryDO first = getStandardGoodsCateByName(one.getStandardCategoryName1(), 1);
        StandardGoodsCategoryDO second = getStandardGoodsCateByName(one.getStandardCategoryName2(), 2);
        if (first != null && second != null && second.getParentId().equals(first.getId()) ) {
            StandardGoodsDO standardGoodsDO = PojoUtils.map(one, StandardGoodsDO.class);
            standardGoodsDO.setStandardCategoryId1(first.getId());
            standardGoodsDO.setStandardCategoryId2(second.getId());
            standardGoodsDO.setUpdateUser(one.getOpUserId());
            standardGoodsDO.setCreateTime(one.getOpTime());
            this.save(standardGoodsDO);
            StandardInstructionsGoodsDO instructionsGoodsDO = PojoUtils.map(one, StandardInstructionsGoodsDO.class);
            instructionsGoodsDO.setStandardId(standardGoodsDO.getId());
            instructionsGoodsDO.setUpdateUser(one.getOpUserId());
            instructionsGoodsDO.setCreateTime(one.getOpTime());
            standardInstructionsGoodsService.save(instructionsGoodsDO);
            StandardGoodsSpecificationDO specificationDO = PojoUtils.map(one, StandardGoodsSpecificationDO.class);
            specificationDO.setStandardId(standardGoodsDO.getId());
            specificationDO.setUpdateUser(one.getOpUserId());
            specificationDO.setCreateTime(one.getOpTime());
            standardGoodsSpecificationService.save(specificationDO);
            return standardGoodsDO.getId();
        }
        return 0L;
    }


    /**
     * 保存中药饮品信息
     *
     * @param one
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveStandardDecoctionAllInfoOne(StandardDecotionImportExcelRequest one) {

        one.setGoodsType(StandardGoodsTypeEnum.DECOCTION_TYPE.getCode());
        StandardGoodsCategoryDO first = getStandardGoodsCateByName(one.getStandardCategoryName1(), 1);
        StandardGoodsCategoryDO second = getStandardGoodsCateByName(one.getStandardCategoryName2(), 2);
        if (first != null && second != null && second.getParentId().equals(first.getId()) ){
            StandardGoodsDO standardGoodsDO = PojoUtils.map(one, StandardGoodsDO.class);
            standardGoodsDO.setStandardCategoryId1(first.getId());
            standardGoodsDO.setStandardCategoryId2(second.getId());
            standardGoodsDO.setUpdateUser(one.getOpUserId());
            standardGoodsDO.setUpdateTime(one.getOpTime());
            this.save(standardGoodsDO);
            StandardInstructionsDecoctionDO decoctionDO = PojoUtils.map(one, StandardInstructionsDecoctionDO.class);
            decoctionDO.setStandardId(standardGoodsDO.getId());
            decoctionDO.setUpdateUser(one.getOpUserId());
            decoctionDO.setUpdateTime(one.getOpTime());
            standardInstructionsDecoctionService.save(decoctionDO);
            StandardGoodsSpecificationDO specificationDO = PojoUtils.map(one, StandardGoodsSpecificationDO.class);
            specificationDO.setStandardId(standardGoodsDO.getId());
            specificationDO.setUpdateUser(one.getOpUserId());
            specificationDO.setUpdateTime(one.getOpTime());
            standardGoodsSpecificationService.save(specificationDO);
            return standardGoodsDO.getId();
        }
        return 0L;
    }

    /**
     * 保存保健食品信息
     *
     * @param one
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveStandardHealthAllInfoOne(StandardHealthImportExcelRequest one) {
        one.setGoodsType(StandardGoodsTypeEnum.HEALTH_TYPE.getCode());
        StandardGoodsCategoryDO first = getStandardGoodsCateByName(one.getStandardCategoryName1(), 1);
        StandardGoodsCategoryDO second = getStandardGoodsCateByName(one.getStandardCategoryName2(), 2);
        if (first != null && second != null && second.getParentId().equals(first.getId()) ){
            StandardGoodsDO standardGoodsDO = PojoUtils.map(one, StandardGoodsDO.class);
            standardGoodsDO.setStandardCategoryId1(first.getId());
            standardGoodsDO.setStandardCategoryId2(second.getId());
            standardGoodsDO.setUpdateUser(one.getOpUserId());
            standardGoodsDO.setCreateTime(one.getOpTime());
            this.save(standardGoodsDO);
            StandardInstructionsHealthDO healthDO = PojoUtils.map(one, StandardInstructionsHealthDO.class);
            healthDO.setStandardId(standardGoodsDO.getId());
            healthDO.setUpdateUser(one.getOpUserId());
            healthDO.setCreateTime(one.getOpTime());
            standardInstructionsHealthService.save(healthDO);
            StandardGoodsSpecificationDO specificationDO = PojoUtils.map(one, StandardGoodsSpecificationDO.class);
            specificationDO.setStandardId(standardGoodsDO.getId());
            specificationDO.setUpdateUser(one.getOpUserId());
            specificationDO.setCreateTime(one.getOpTime());
            standardGoodsSpecificationService.save(specificationDO);
            return standardGoodsDO.getId();
        }
        return 0L;
    }


    /**
     * 保存消杀品信息
     *
     * @param one
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveStandardDisinfectionAllInfoOne(StandardDisinfectionImportExcelRequest one) {
        one.setGoodsType(StandardGoodsTypeEnum.DISINFECTION_TYPE.getCode());
        StandardGoodsCategoryDO first = getStandardGoodsCateByName(one.getStandardCategoryName1(), 1);
        StandardGoodsCategoryDO second = getStandardGoodsCateByName(one.getStandardCategoryName2(), 2);
        if (first != null && second != null && second.getParentId().equals(first.getId()) ){
            StandardGoodsDO standardGoodsDO = PojoUtils.map(one, StandardGoodsDO.class);
            standardGoodsDO.setStandardCategoryId1(first.getId());
            standardGoodsDO.setStandardCategoryId2(second.getId());
            standardGoodsDO.setUpdateTime(one.getOpTime());
            standardGoodsDO.setUpdateUser(one.getOpUserId());
            this.save(standardGoodsDO);
            StandardInstructionsDisinfectionDO disinfectionDO = PojoUtils.map(one, StandardInstructionsDisinfectionDO.class);
            disinfectionDO.setStandardId(standardGoodsDO.getId());
            disinfectionDO.setUpdateTime(one.getOpTime());
            disinfectionDO.setUpdateUser(one.getOpUserId());
            standardInstructionsDisinfectionService.save(disinfectionDO);
            StandardGoodsSpecificationDO specificationDO = PojoUtils.map(one, StandardGoodsSpecificationDO.class);
            specificationDO.setStandardId(standardGoodsDO.getId());
            specificationDO.setUpdateTime(one.getOpTime());
            specificationDO.setUpdateUser(one.getOpUserId());
            standardGoodsSpecificationService.save(specificationDO);
            return standardGoodsDO.getId();
        }
        return 0L;
    }


    /**
     * 保存中药材信息
     *
     * @param one
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveStandardMaterialsAllInfoOne(StandardMaterialsImportExcelRequest one) {
        one.setGoodsType(StandardGoodsTypeEnum.MATERIAL_TYPE.getCode());
        StandardGoodsCategoryDO first = getStandardGoodsCateByName(one.getStandardCategoryName1(), 1);
        StandardGoodsCategoryDO second= getStandardGoodsCateByName(one.getStandardCategoryName2(), 2);
        if (first != null && second != null && second.getParentId().equals(first.getId()) ){
            StandardGoodsDO standardGoodsDO = PojoUtils.map(one, StandardGoodsDO.class);
            standardGoodsDO.setStandardCategoryId1(first.getId());
            standardGoodsDO.setStandardCategoryId2(second.getId());
            standardGoodsDO.setUpdateUser(one.getOpUserId());
            standardGoodsDO.setCreateTime(one.getOpTime());
            this.save(standardGoodsDO);
            StandardInstructionsMaterialsDO materialsDO = PojoUtils.map(one, StandardInstructionsMaterialsDO.class);
            materialsDO.setStandardId(standardGoodsDO.getId());
            materialsDO.setUpdateUser(one.getOpUserId());
            materialsDO.setCreateTime(one.getOpTime());
            standardInstructionsMaterialsService.save(materialsDO);
            StandardGoodsSpecificationDO specificationDO = PojoUtils.map(one, StandardGoodsSpecificationDO.class);
            specificationDO.setStandardId(standardGoodsDO.getId());
            specificationDO.setUpdateUser(one.getOpUserId());
            specificationDO.setCreateTime(one.getOpTime());
            standardGoodsSpecificationService.save(specificationDO);
            return standardGoodsDO.getId();
        }
        return 0L;
    }


    /**
     * 保存食品信息
     *
     * @param one
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveStandardFoodsAllInfoOne(StandardFoodsImportExcelRequest one) {
        one.setGoodsType(StandardGoodsTypeEnum.FOODS_TYPE.getCode());
        StandardGoodsCategoryDO first = getStandardGoodsCateByName(one.getStandardCategoryName1(), 1);
        StandardGoodsCategoryDO second = getStandardGoodsCateByName(one.getStandardCategoryName2(), 2);
        if (first != null && second != null && second.getParentId().equals(first.getId()) ){
            StandardGoodsDO standardGoodsDO = PojoUtils.map(one, StandardGoodsDO.class);
            standardGoodsDO.setStandardCategoryId1(first.getId());
            standardGoodsDO.setStandardCategoryId2(second.getId());
            standardGoodsDO.setUpdateTime(one.getOpTime());
            standardGoodsDO.setUpdateUser(one.getOpUserId());
            this.save(standardGoodsDO);
            StandardInstructionsFoodsDO foodsDO = PojoUtils.map(one, StandardInstructionsFoodsDO.class);
            foodsDO.setStandardId(standardGoodsDO.getId());
            foodsDO.setUpdateTime(one.getOpTime());
            foodsDO.setUpdateUser(one.getOpUserId());
            standardInstructionsFoodsService.save(foodsDO);
            StandardGoodsSpecificationDO specificationDO = PojoUtils.map(one, StandardGoodsSpecificationDO.class);
            specificationDO.setStandardId(standardGoodsDO.getId());
            specificationDO.setUpdateTime(one.getOpTime());
            specificationDO.setUpdateUser(one.getOpUserId());
            standardGoodsSpecificationService.save(specificationDO);
            return standardGoodsDO.getId();
        }
        return 0L;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveStandardMedicalInstrumentAllInfoOne(StandardMedicalInstrumentImportExcelRequest one) {
        one.setGoodsType(StandardGoodsTypeEnum.MEDICAL_INSTRUMENT_TYPE.getCode());
        StandardGoodsCategoryDO first = getStandardGoodsCateByName(one.getStandardCategoryName1(), 1);
        StandardGoodsCategoryDO second = getStandardGoodsCateByName(one.getStandardCategoryName2(), 2);
        if (first != null && second != null && second.getParentId().equals(first.getId()) ){
            StandardGoodsDO standardGoodsDO = PojoUtils.map(one, StandardGoodsDO.class);
            standardGoodsDO.setStandardCategoryId1(first.getId());
            standardGoodsDO.setStandardCategoryId2(second.getId());
            standardGoodsDO.setUpdateTime(one.getOpTime());
            standardGoodsDO.setUpdateUser(one.getOpUserId());
            this.save(standardGoodsDO);
            StandardInstructionsMedicalInstrumentDO medicalInstrumentDO = PojoUtils.map(one, StandardInstructionsMedicalInstrumentDO.class);
            medicalInstrumentDO.setStandardId(standardGoodsDO.getId());
            medicalInstrumentDO.setUpdateTime(one.getOpTime());
            medicalInstrumentDO.setUpdateUser(one.getOpUserId());
            standardInstructionsMedicalInstrumentService.save(medicalInstrumentDO);
            StandardGoodsSpecificationDO specificationDO = PojoUtils.map(one, StandardGoodsSpecificationDO.class);
            specificationDO.setStandardId(standardGoodsDO.getId());
            specificationDO.setUpdateTime(one.getOpTime());
            specificationDO.setUpdateUser(one.getOpUserId());
            standardGoodsSpecificationService.save(specificationDO);
            return standardGoodsDO.getId();
        }
        return 0L;
    }

    @Override
    public Long saveStandardDispensingGranuleAllInfoOne(StandardDispensingGranuleImportExcelRequest one) {
        one.setGoodsType(StandardGoodsTypeEnum.DISPENSING_GRANULE_TYPE.getCode());
        StandardGoodsCategoryDO first = getStandardGoodsCateByName(one.getStandardCategoryName1(), 1);
        StandardGoodsCategoryDO second = getStandardGoodsCateByName(one.getStandardCategoryName2(), 2);
        if (first != null && second != null && second.getParentId().equals(first.getId()) ){
            StandardGoodsDO standardGoodsDO = PojoUtils.map(one, StandardGoodsDO.class);
            standardGoodsDO.setStandardCategoryId1(first.getId());
            standardGoodsDO.setStandardCategoryId2(second.getId());
            this.save(standardGoodsDO);
            StandardInstructionsDispensingGranuleDO dispensingGranuleDO = PojoUtils.map(one, StandardInstructionsDispensingGranuleDO.class);
            dispensingGranuleDO.setStandardId(standardGoodsDO.getId());
            standardInstructionsDispensingGranuleService.save(dispensingGranuleDO);
            StandardGoodsSpecificationDO specificationDO = PojoUtils.map(one, StandardGoodsSpecificationDO.class);
            specificationDO.setStandardId(standardGoodsDO.getId());
            standardGoodsSpecificationService.save(specificationDO);
            return standardGoodsDO.getId();
        }
        return 0L;
    }


    /**
     * 根据分类id修改分类名称
     * @param name 名称
     * @param categoryId 分类id
     * @param oUserId 操作人
     */
    @Override
    public void updateCategoryName1ById(String name,Long categoryId,Long oUserId) {
        QueryWrapper<StandardGoodsDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StandardGoodsDO :: getStandardCategoryId1,categoryId);
        StandardGoodsDO one = new StandardGoodsDO();
        one.setStandardCategoryName1(name);
        one.setUpdateUser(oUserId);
        update(one,wrapper);
    }

    /**
     * 根据分类id修改分类名称
     * @param name 名称
     * @param categoryId 分类id
     * @param opUserId 操作人
     */
    @Override
    public void updateCategoryName2ById(String name, Long categoryId,Long opUserId) {
        QueryWrapper<StandardGoodsDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StandardGoodsDO :: getStandardCategoryId2,categoryId);
        StandardGoodsDO one = new StandardGoodsDO();
        one.setStandardCategoryName2(name);
        one.setUpdateUser(opUserId);
        update(one,wrapper);
    }

    /**
     * 修改父类id，和名称
     *
     * @param parentId 新分类
     * @param categoryId
     * @param name     父类名称
     * @return
     */
    @Override
    public Boolean updateCategoryId(Long parentId, Long categoryId, String name,Long opUserId) {
        QueryWrapper<StandardGoodsDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StandardGoodsDO :: getStandardCategoryId2,categoryId);
        StandardGoodsDO one = new StandardGoodsDO();
        one.setStandardCategoryId1(parentId);
        one.setStandardCategoryName1(name);
        one.setUpdateUser(opUserId);
        return  update(one,wrapper);
    }

    @Override
    public Boolean updateYlFlagById(Long standardId, Integer ylFlag) {
        StandardGoodsDO one = new StandardGoodsDO();
        one.setId(standardId);
        one.setYlFlag(ylFlag);
        return updateById(one);
    }

    @Override
    public Page<StandardGoodsInfoDTO> getYilingStandardGoodsInfo(StandardGoodsInfoRequest request) {
        Page<StandardGoodsInfoDTO> dtoPage = new Page<>();
        request.setYlFlag(1);
        Page<StandardGoodsDO> standardGoodsPage = standardGoodsMapper.standardGoodsInfoPage(request.getPage(), request);
        dtoPage = PojoUtils.map(standardGoodsPage, StandardGoodsInfoDTO.class);
        if(CollectionUtils.isNotEmpty(dtoPage.getRecords())){
            for (StandardGoodsInfoDTO dto : dtoPage.getRecords()) {
                String categoryName1 = dto.getStandardCategoryName1();
                String categoryName2 = dto.getStandardCategoryName2();
                StringBuilder sagName = new StringBuilder();
                if (StringUtils.isNotBlank(categoryName1)) {
                    sagName.append(categoryName1);
                }
                if (StringUtils.isNotBlank(categoryName2)) {
                    sagName.append("-").append(categoryName2);
                }
                dto.setStandardCategoryName(sagName.toString());
                dto.setPic(goodsService.getDefaultUrl(dto.getId(), 0L));
            }
        }
        return dtoPage;
    }

    @Override
    public List<StandardGoodsDTO> getStandardGoodsByIds(List<Long> ids) {
        if(CollectionUtil.isEmpty(ids)){
            return ListUtil.empty();
        }
        List<StandardGoodsDTO> dtoList = PojoUtils.map(this.listByIds(ids), StandardGoodsDTO.class);
        if(CollectionUtil.isNotEmpty(dtoList)){
            dtoList.forEach(dto->{
                dto.setPic(goodsService.getDefaultUrl(dto.getId(), 0L));
            });
        }
        return dtoList;
    }

    private StandardGoodsCategoryDO getStandardGoodsCateByName(String scgName1, Integer grade) {
        if (1 == grade) {
            List<StandardGoodsCategoryDO> cateInfoFirst = standardGoodsCateService.getFirstCateInfo();
            for (StandardGoodsCategoryDO one : cateInfoFirst) {
                if (one.getName().equals(scgName1)) {
                    return one;
                }
            }
        } else if (2 == grade) {
            List<StandardGoodsCategoryDO> secondCateInfo = standardGoodsCateService.getSecondCateInfo();
            for (StandardGoodsCategoryDO one : secondCateInfo) {
                if (one.getName().equals(scgName1)) {
                    return one;
                }
            }
        }
        return null;
    }

}
