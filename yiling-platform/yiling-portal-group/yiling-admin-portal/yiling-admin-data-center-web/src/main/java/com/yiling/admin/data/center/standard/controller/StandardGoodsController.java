package com.yiling.admin.data.center.standard.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.admin.data.center.standard.form.StandardDispensingGranuleImportExcelForm;
import com.yiling.admin.data.center.standard.handler.StandardDispensingGranuleImportDataHandler;
import com.yiling.export.excel.handler.ImportDataHandler;
import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.util.ExeclImportUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.common.utils.PictureUrlUtils;
import com.yiling.admin.data.center.standard.form.SaveStandardGoodsSpecificationForm;
import com.yiling.admin.data.center.standard.form.StandardDecotionImportExcelForm;
import com.yiling.admin.data.center.standard.form.StandardDisinfectionImportExcelForm;
import com.yiling.admin.data.center.standard.form.StandardFoodsImportExcelForm;
import com.yiling.admin.data.center.standard.form.StandardGoodsImportExcelForm;
import com.yiling.admin.data.center.standard.form.StandardGoodsInfoForm;
import com.yiling.admin.data.center.standard.form.StandardGoodsSaveInfoForm;
import com.yiling.admin.data.center.standard.form.StandardHealthImportExcelForm;
import com.yiling.admin.data.center.standard.form.StandardMaterialsImportExcelForm;
import com.yiling.admin.data.center.standard.form.StandardMedicalInstrumentImportExcelForm;
import com.yiling.admin.data.center.standard.handler.StandardDecoctionImportDataHandler;
import com.yiling.admin.data.center.standard.handler.StandardDisinfectionImportDataHandler;
import com.yiling.admin.data.center.standard.handler.StandardFoodsImportDataHandler;
import com.yiling.admin.data.center.standard.handler.StandardGoodsImportDataHandler;
import com.yiling.admin.data.center.standard.handler.StandardGoodsImportVerifyHandler;
import com.yiling.admin.data.center.standard.handler.StandardHealthImportDataHandler;
import com.yiling.admin.data.center.standard.handler.StandardMaterialsImportDataHandler;
import com.yiling.admin.data.center.standard.handler.StandardMedicalInstrumentImportDataHandler;
import com.yiling.admin.data.center.standard.handler.StandardMedicalInstrumentImportVerifyHandler;
import com.yiling.admin.data.center.standard.vo.StandardGoodsAllInfoVO;
import com.yiling.admin.data.center.standard.vo.StandardGoodsInfoVO;
import com.yiling.admin.data.center.standard.vo.StandardGoodsPicVO;
import com.yiling.admin.data.center.standard.vo.StandardSpecificationInfoVO;
import com.yiling.admin.data.center.standard.vo.StandardSpecificationVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.framework.common.web.rest.IdObject;
import com.yiling.goods.standard.api.StandardGoodsApi;
import com.yiling.goods.standard.api.StandardGoodsSpecificationApi;
import com.yiling.goods.standard.api.StandardGoodsTagApi;
import com.yiling.goods.standard.dto.StandardGoodsAllInfoDTO;
import com.yiling.goods.standard.dto.StandardGoodsInfoDTO;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationDTO;
import com.yiling.goods.standard.dto.StandardGoodsTagDTO;
import com.yiling.goods.standard.dto.request.SaveStandardSpecificationRequest;
import com.yiling.goods.standard.dto.request.StandardGoodsInfoRequest;
import com.yiling.goods.standard.dto.request.StandardGoodsSaveInfoRequest;
import com.yiling.goods.standard.enums.StandardGoodsTypeEnum;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 质量监管 Controller
 *
 * @author: wei.wang
 * @date: 2021/5/12
 */
@RestController
@RequestMapping("/standard/goods")
@Api(tags = "基础药品管理")
@Slf4j
public class StandardGoodsController extends BaseController {

    @DubboReference
    StandardGoodsApi standardGoodsApi;

    @DubboReference
    StandardGoodsSpecificationApi standardGoodsSpecificationApi;

    @DubboReference
    UserApi userApi;

    @DubboReference
    StandardGoodsTagApi standardGoodsTagApi;

    @Autowired
    StandardGoodsImportDataHandler standardGoodsImportDataHandler;

    @Autowired
    StandardMaterialsImportDataHandler standardMaterialsImportDataHandler;

    @Autowired
    StandardDecoctionImportDataHandler standardDecoctionImportDataHandler;

    @Autowired
    StandardDisinfectionImportDataHandler standardDisinfectionImportDataHandler;

    @Autowired
    StandardHealthImportDataHandler standardHealthImportDataHandler;

    @Autowired
    StandardFoodsImportDataHandler standardFoodsImportDataHandler;

    @Autowired
    StandardMedicalInstrumentImportDataHandler standardMedicalInstrumentImportDataHandler;

    @Autowired
    StandardDispensingGranuleImportDataHandler standardDispensingGranuleImportDataHandler;

    @Autowired
    PictureUrlUtils pictureUrlUtils;

    @Autowired
    @Lazy
    StandardGoodsController _this;

    @DubboReference
    MqMessageSendApi mqMessageSendApi;



    @ApiOperation(value = "获取标准商品信息")
    @PostMapping("/pageList")
    public Result<Page<StandardGoodsInfoVO>> pageList(@CurrentUser CurrentAdminInfo staffInfo,@RequestBody StandardGoodsInfoForm form) {
        StandardGoodsInfoRequest request = PojoUtils.map(form, StandardGoodsInfoRequest.class);
        Page<StandardGoodsInfoDTO> goodsInfo = standardGoodsApi.getStandardGoodsInfo(request);
        List<Long> userList = goodsInfo.getRecords().stream().map(StandardGoodsInfoDTO::getUpdateUser).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(userList)) {
            List<UserDTO> userDto = userApi.listByIds(userList);
            Map<Long, String> map = userDto.stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getUsername, (k1, k2) -> k1));
            for (StandardGoodsInfoDTO one : goodsInfo.getRecords()) {
                one.setUserName(map.get(one.getUpdateUser()));
            }
        }
        Page<StandardGoodsInfoVO> page = PojoUtils.map(goodsInfo, StandardGoodsInfoVO.class);

        List<Long> idList = page.getRecords().stream().map(StandardGoodsInfoVO::getId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(idList)) {
            // 企业标签信息
            Map<Long, List<StandardGoodsTagDTO>> standardTagDTOMap = standardGoodsTagApi.listByStandardIdList(idList);
            List<StandardGoodsSpecificationDTO> standardGoodsSpecificationList = standardGoodsSpecificationApi.getListStandardGoodsSpecification(idList);
            Map<Long, List<StandardGoodsSpecificationDTO>> groupBy = standardGoodsSpecificationList.stream().collect(Collectors.groupingBy(StandardGoodsSpecificationDTO::getStandardId));
            page.getRecords().forEach(e -> {
                // 标准库标签信息
                List<StandardGoodsTagDTO> standardGoodsTagDTOList = standardTagDTOMap.get(e.getId());
                if (CollUtil.isNotEmpty(standardGoodsTagDTOList)) {
                    e.setTagNames(standardGoodsTagDTOList.stream().map(StandardGoodsTagDTO::getName).distinct().collect(Collectors.toList()));
                }
                if (groupBy.get(e.getId()) != null) {
                    e.setSpecificationsCount(groupBy.get(e.getId()).size());
                }
            });
        }

        return Result.success(page);
    }

    @ApiOperation(value = "获取标准商品规格信息")
    @GetMapping("/get/specification")
    public Result<CollectionObject<List<StandardSpecificationVO>>> getStandardSpecificationStandardId(@RequestParam("id") Long id){
        List<Long> list = new ArrayList<Long>(){{add(id);}};
        List<StandardGoodsSpecificationDTO> standardGoodsSpecificationList = standardGoodsSpecificationApi.getListStandardGoodsSpecification(list);
        List<StandardSpecificationVO> result = PojoUtils.map(standardGoodsSpecificationList, StandardSpecificationVO.class);

        return Result.success(new CollectionObject(result));
    }

    @ApiOperation(value = "获取标准商品详细信息")
    @GetMapping("/get")
    public Result<StandardGoodsAllInfoVO> getStandardGoodsInfo(@RequestParam("id") Long id) {
        StandardGoodsAllInfoDTO dto = standardGoodsApi.getStandardGoodsById(id);
        StandardGoodsAllInfoVO result = PojoUtils.map(dto, StandardGoodsAllInfoVO.class);
        if(CollectionUtil.isNotEmpty(result.getPicBasicsInfoList())){
            for(StandardGoodsPicVO one : result.getPicBasicsInfoList()){
                one.setPicUrl(pictureUrlUtils.getGoodsPicUrl(one.getPic()));
            }
        }
        if(CollectionUtil.isNotEmpty(result.getSpecificationInfo())){
            for(StandardSpecificationInfoVO one : result.getSpecificationInfo()){
                if(CollectionUtil.isNotEmpty(one.getPicInfoList())){
                    for(StandardGoodsPicVO picOne : one.getPicInfoList()){
                        picOne.setPicUrl(pictureUrlUtils.getGoodsPicUrl(picOne.getPic()));
                    }
                }
            }
        }
        return Result.success(result);

    }

    @ApiOperation(value = "保存标准商品详细信息")
    @PostMapping("/save")
    public Result<BoolObject> saveStandardGoodsInfo(@CurrentUser CurrentAdminInfo staffInfo ,@Valid @RequestBody StandardGoodsSaveInfoForm form) {
        StandardGoodsSaveInfoRequest request = PojoUtils.map(form, StandardGoodsSaveInfoRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOpTime(new Date());
        Long standardId = standardGoodsApi.saveStandardGoodAllInfo(request);
        List<StandardGoodsTagDTO> tags = standardGoodsTagApi.listByStandardId(standardId);
        if(CollectionUtil.isNotEmpty(tags)){
            List<String> tagNames = tags.stream().map(StandardGoodsTagDTO::getName).collect(Collectors.toList());
            if(tagNames.contains("2C平台药房")){
                _this.sendMq(Constants.TOPIC_HMC_UPDATE_GOODS_TAG,Constants.TAG_HMC_UPDATE_GOODS_TAG,String.valueOf(standardId),standardId);
            }
        }
        return Result.success(new BoolObject(true));
    }

    @ApiOperation(value = "保存售卖规格信息")
    @PostMapping("/saveSpecification")
    public Result<IdObject> saveSpecification(@CurrentUser CurrentAdminInfo staffInfo , @Valid @RequestBody SaveStandardGoodsSpecificationForm form) {
        SaveStandardSpecificationRequest request = PojoUtils.map(form, SaveStandardSpecificationRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        StandardGoodsAllInfoDTO standardGoodsAllInfoDTO=standardGoodsApi.getStandardGoodsById(form.getStandardId());
        request.setName(standardGoodsAllInfoDTO.getBaseInfo().getName());
        request.setLicenseNo(standardGoodsAllInfoDTO.getBaseInfo().getLicenseNo());
        request.setManufacturer(standardGoodsAllInfoDTO.getBaseInfo().getManufacturer());
        Long id=standardGoodsSpecificationApi.saveStandardGoodsSpecificationOne(request);
        List<StandardGoodsTagDTO> tags = standardGoodsTagApi.listByStandardId(form.getStandardId());
        if(CollectionUtil.isNotEmpty(tags)){
            List<String> tagNames = tags.stream().map(StandardGoodsTagDTO::getName).collect(Collectors.toList());
            if(tagNames.contains("2C平台药房")){
                _this.sendMq(Constants.TOPIC_HMC_UPDATE_GOODS_TAG,Constants.TAG_HMC_UPDATE_GOODS_TAG,String.valueOf(form.getStandardId()),form.getStandardId());
            }
        }
        return Result.success(new IdObject(id));
    }


    @ApiOperation(value = "管理后台标准商品信息导入", httpMethod = "POST")
    @PostMapping(value = "/import", headers = "content-type=multipart/form-data")
    public Result<ImportResultModel<Object>> standardGoodsImport(@RequestParam(value = "file", required = true) MultipartFile file,
                                                                 @CurrentUser CurrentAdminInfo adminInfo) {
        try {
            Workbook workBook = getWorkBook(file);
            List<Class<?>> classList = new ArrayList<>();
            List<ImportParams> paramsList = new ArrayList<>();
            List<ImportDataHandler> importDataHandlerList = new ArrayList<>();
            List<String> sheetNameList = new ArrayList<>();
            for (int num = 0; num < workBook.getNumberOfSheets(); num++) {
                Sheet sheet = workBook.getSheetAt(num);
                ImportParams params = new ImportParams();
                params.setNeedVerify(true);
                params.setStartSheetIndex(num);
                params.setKeyIndex(0);

                if (StandardGoodsTypeEnum.GOODS_TYPE.getName().equals(sheet.getSheetName())) {
                    params.setVerifyHandler(new StandardGoodsImportVerifyHandler());
                    classList.add(StandardGoodsImportExcelForm.class);
                    paramsList.add(params);
                    importDataHandlerList.add(standardGoodsImportDataHandler);
                    sheetNameList.add(sheet.getSheetName());

                    continue;
                } else if (StandardGoodsTypeEnum.MATERIAL_TYPE.getName().equals(sheet.getSheetName())) {
                    classList.add(StandardMaterialsImportExcelForm.class);
                    paramsList.add(params);
                    importDataHandlerList.add(standardMaterialsImportDataHandler);
                    sheetNameList.add(sheet.getSheetName());

                    continue;

                } else if (StandardGoodsTypeEnum.DECOCTION_TYPE.getName().equals(sheet.getSheetName())) {
                    classList.add(StandardDecotionImportExcelForm.class);
                    paramsList.add(params);
                    importDataHandlerList.add(standardDecoctionImportDataHandler);
                    sheetNameList.add(sheet.getSheetName());

                    continue;
                } else if (StandardGoodsTypeEnum.DISINFECTION_TYPE.getName().equals(sheet.getSheetName())) {
                    classList.add(StandardDisinfectionImportExcelForm.class);
                    paramsList.add(params);
                    importDataHandlerList.add(standardDisinfectionImportDataHandler);
                    sheetNameList.add(sheet.getSheetName());

                    continue;

                } else if (StandardGoodsTypeEnum.HEALTH_TYPE.getName().equals(sheet.getSheetName())) {
                    classList.add(StandardHealthImportExcelForm.class);
                    paramsList.add(params);
                    importDataHandlerList.add(standardHealthImportDataHandler);
                    sheetNameList.add(sheet.getSheetName());

                    continue;

                } else if (StandardGoodsTypeEnum.FOODS_TYPE.getName().equals(sheet.getSheetName())) {
                    classList.add(StandardFoodsImportExcelForm.class);
                    paramsList.add(params);
                    importDataHandlerList.add(standardFoodsImportDataHandler);
                    sheetNameList.add(sheet.getSheetName());

                    continue;
                }
                else if (StandardGoodsTypeEnum.MEDICAL_INSTRUMENT_TYPE.getName().equals(sheet.getSheetName())) {
                    params.setVerifyHandler(new StandardMedicalInstrumentImportVerifyHandler());
                    classList.add(StandardMedicalInstrumentImportExcelForm.class);
                    paramsList.add(params);
                    importDataHandlerList.add(standardMedicalInstrumentImportDataHandler);
                    sheetNameList.add(sheet.getSheetName());

                    continue;
                }else if (StandardGoodsTypeEnum.DISPENSING_GRANULE_TYPE.getName().equals(sheet.getSheetName())) {
                    classList.add(StandardDispensingGranuleImportExcelForm.class);
                    paramsList.add(params);
                    importDataHandlerList.add(standardDispensingGranuleImportDataHandler);
                    sheetNameList.add(sheet.getSheetName());
                    continue;
                }
            }
            Map<String,Object> paramMap=new HashMap<>();
            paramMap.put(MyMetaHandler.FIELD_OP_USER_ID,adminInfo.getCurrentUserId());
            ImportResultModel<Object> objectImportResultModel = ExeclImportUtils.importExcelMore(file, classList, paramsList, importDataHandlerList, sheetNameList, paramMap);
            return Result.success(objectImportResultModel);
        } catch (Exception e) {
            log.error("标准商品导入错误:{}", e);
            return Result.failed(ResultCode.EXCEL_PARSING_ERROR);
        }
    }


    public static Workbook getWorkBook(MultipartFile file) throws IOException {

        Workbook hssfWorkbook = null;
        try {
            hssfWorkbook = new HSSFWorkbook(file.getInputStream());
        } catch (Exception ex) {
            hssfWorkbook = new XSSFWorkbook(file.getInputStream());
        }
        return hssfWorkbook;
    }

    public boolean sendMq(String topic,String topicTag,String msg,Long id) {
        Integer intId=null;
        if(null !=id && 0<id){
            intId=id.intValue();
        }
        MqMessageBO mqMessageBO = _this.sendPrepare(topic, topicTag, msg, intId);
        mqMessageSendApi.send(mqMessageBO);
        return true;
    }

    /**
     * 消息持久化
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    public MqMessageBO sendPrepare(String topic,String topicTag,String msg,Integer id) {
        MqMessageBO mqMessageBO = new MqMessageBO(topic, topicTag, msg,id);
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
        return mqMessageBO;
    }


}



