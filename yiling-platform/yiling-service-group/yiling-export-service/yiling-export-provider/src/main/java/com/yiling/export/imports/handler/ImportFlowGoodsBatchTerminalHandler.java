package com.yiling.export.imports.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.api.CrmGoodsInfoApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsInfoDTO;
import com.yiling.dataflow.flow.api.FlowEnterpriseGoodsMappingApi;
import com.yiling.dataflow.flow.dto.FlowEnterpriseGoodsMappingDTO;
import com.yiling.dataflow.utils.NumberCheckUtil;
import com.yiling.dataflow.wash.api.FlowInTransitOrderApi;
import com.yiling.dataflow.wash.api.FlowTerminalOrderApi;
import com.yiling.dataflow.wash.dto.request.SaveFlowGoodsBatchTransitRequest;
import com.yiling.dataflow.wash.enums.FlowGoodsBatchTransitTypeEnum;
import com.yiling.export.excel.handler.AbstractExcelImportHandler;
import com.yiling.export.imports.model.ImportFlowGoodsBatchTerminalModel;
import com.yiling.export.imports.util.ImportConstants;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 终端库存导入
 *
 * @author: houjie.sun
 * @date: 2023/3/10
 */
@Slf4j
@Service
public class ImportFlowGoodsBatchTerminalHandler extends AbstractExcelImportHandler<ImportFlowGoodsBatchTerminalModel> {

    @DubboReference
    FlowTerminalOrderApi flowTerminalOrderApi;
    @DubboReference
    CrmEnterpriseApi crmEnterpriseApi;
    @DubboReference
    CrmGoodsInfoApi crmGoodsInfoApi;
    @DubboReference
    FlowEnterpriseGoodsMappingApi flowEnterpriseGoodsMappingApi;
    @DubboReference
    FlowInTransitOrderApi flowInTransitOrderApi;

    /**
     * 登录用户负责的机构权限列表
     */
    private List<Long> crmIds;

    /**
     * 登录用户负责的经销商所属省份编码列表
     */
    List<String> crmProvinceCodeList;

    public List<Long> getCrmIds() {
        return crmIds;
    }

    public void setCrmIds(List<Long> crmIds) {
        this.crmIds = crmIds;
    }

    public List<String> getCrmProvinceCodeList() {
        return crmProvinceCodeList;
    }

    public void setCrmProvinceCodeList(List<String> crmProvinceCodeList) {
        this.crmProvinceCodeList = crmProvinceCodeList;
    }

    @Override
    public ExcelVerifyHandlerResult verify(ImportFlowGoodsBatchTerminalModel model) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);
        // 经销商编码/机构编码
        Long crmEnterpriseId = model.getCrmEnterpriseId();
        if (ObjectUtil.isNull(crmEnterpriseId)) {
            return this.error("经销商编码/机构编码不能为空");
        }
        if (0 == crmEnterpriseId.intValue()) {
            return this.error("经销商编码/机构编码不能0");
        }

        // 经销商编码/机构编码，在系统中是否存在
        CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getCrmEnterpriseById(crmEnterpriseId);
        if (ObjectUtil.isNull(crmEnterpriseDTO)) {
            return this.error("经销商编码/机构编码不存在");
        }

        // 数据权限
        boolean crmIdListFlag = CollUtil.isNotEmpty(this.crmIds) ? true : false;
        boolean crmProvinceCodeListFlag = CollUtil.isNotEmpty(this.crmProvinceCodeList) ? true : false;
        if (crmIdListFlag && crmProvinceCodeListFlag){
            if (!this.crmIds.contains(crmEnterpriseId) && !this.crmProvinceCodeList.contains(crmEnterpriseDTO.getProvinceCode())) {
                return this.error("该机构没有操作权限");
            }
        } else if (crmIdListFlag){
            if(!this.crmIds.contains(crmEnterpriseId)){
                return this.error("该机构没有操作权限");
            }
        } else if (crmProvinceCodeListFlag){
            if (!this.crmProvinceCodeList.contains(crmEnterpriseDTO.getProvinceCode())) {
                return this.error("该机构没有操作权限");
            }
        }

        // 原始商品信息、标准产品编码，至少有一个
        // 原始商品信息：原始商品名称、原始商品规格
        String gbName = model.getGbName();
        String gbSpecifications = model.getGbSpecifications();
        // 标准产品编码
        Long crmGoodsCode = model.getCrmGoodsCode();
        boolean crmGoodsCodeFlag = (ObjectUtil.isNotNull(crmGoodsCode) && 0 != crmGoodsCode.intValue()) ? true : false;
        if (!crmGoodsCodeFlag && (StrUtil.isBlank(gbName) || StrUtil.isBlank(gbSpecifications))) {
            return this.error("原始商品信息和标准产品编码至少填写一个");
        }
        // 库存数量
        BigDecimal gbNumber = model.getGbNumber();
        if (ObjectUtil.isNull(gbNumber)) {
            return this.error("库存数量不能为空");
        }
        if (!NumberCheckUtil.positiveInteger8BitNumber(gbNumber.toString())) {
            return this.error("库存数量必须为大于0的1至8位正整数");
        }

        // 设置机构名称
        model.setName(crmEnterpriseDTO.getName());

        // 有标准产品编码时，不需要查询产品对照关系、不进行系数转换
        if (crmGoodsCodeFlag) {
            // 标准产品编码，在系统中是否存在
            List<CrmGoodsInfoDTO> crmGoodsInfoList = crmGoodsInfoApi.findByCodeList(ListUtil.toList(crmGoodsCode));
            if (CollUtil.isEmpty(crmGoodsInfoList)) {
                return this.error("标准产品编码不存在");
            }
            // 设置标准商品名称、规格
            CrmGoodsInfoDTO crmGoodsInfoDTO = crmGoodsInfoList.get(0);
            if (StrUtil.isBlank(model.getCrmGoodsName())) {
                model.setCrmGoodsName(crmGoodsInfoDTO.getGoodsName());
            }
            if (StrUtil.isBlank(model.getCrmGoodsSpecifications())) {
                model.setCrmGoodsSpecifications(crmGoodsInfoDTO.getGoodsSpec());
            }
        } else {
            FlowEnterpriseGoodsMappingDTO flowEnterpriseGoodsMappingDTO = flowEnterpriseGoodsMappingApi.findByFlowGoodsNameAndFlowSpecificationAndCrmEnterpriseId(gbName, gbSpecifications, crmEnterpriseId);
            if (ObjectUtil.isNull(flowEnterpriseGoodsMappingDTO)) {
                return this.error("商品对照关系不存在");
            }
            Long crmGoodsCodeMapping = flowEnterpriseGoodsMappingDTO.getCrmGoodsCode();
            if (ObjectUtil.isNull(crmGoodsCodeMapping) || 0 == crmGoodsCodeMapping.intValue()) {
                return this.error("商品对照关系中的标准商品编码不存在");
            }
            if (StrUtil.isBlank(flowEnterpriseGoodsMappingDTO.getGoodsName())) {
                return this.error("商品对照关系中的标准商品名称不存在");
            }
            if (StrUtil.isBlank(flowEnterpriseGoodsMappingDTO.getGoodsSpecification())) {
                return this.error("商品对照关系中的标准商品规格不存在");
            }

            // 设置标准商品编码、名称、规格
            if (!crmGoodsCodeFlag) {
                model.setCrmGoodsCode(crmGoodsCodeMapping);
            }
            if (StrUtil.isBlank(model.getCrmGoodsName())) {
                model.setCrmGoodsName(flowEnterpriseGoodsMappingDTO.getGoodsName());
            }
            if (StrUtil.isBlank(model.getCrmGoodsSpecifications())) {
                model.setCrmGoodsSpecifications(flowEnterpriseGoodsMappingDTO.getGoodsSpecification());
            }
            // 设置单位转换
            model.setConvertUnit(flowEnterpriseGoodsMappingDTO.getConvertUnit());
            model.setConvertNumber(flowEnterpriseGoodsMappingDTO.getConvertNumber());
        }

        return result;
    }

    @Override
    public List<ImportFlowGoodsBatchTerminalModel> importData(List<ImportFlowGoodsBatchTerminalModel> object, Map<String, Object> paramMap) {
        if (CollUtil.isEmpty(object)) {
            return ListUtil.empty();
        }

        String param = (String) paramMap.get(ImportConstants.PARAM);
        String gbDetailMonth = "";
        Long currentUserId = 0L;
        if (StrUtil.isNotBlank(param)) {
            JSONObject jsonObject = JSONUtil.parseObj(param);
            gbDetailMonth = jsonObject.getStr("gbDetailMonth", "");
            currentUserId = jsonObject.getLong(CurrentSjmsUserInfo.CURRENT_USER_ID, 0L);
        }

        /* 三者关系id */
        // 机构id、标准商品编码 map
        Map<Long, Long> crmEnterpriseIdGoodsCodeMap = new HashMap<>();
        object.forEach(model -> {
            crmEnterpriseIdGoodsCodeMap.put(model.getCrmEnterpriseId(), model.getCrmGoodsCode());
        });
        Map<Long, Long> crmEnterpriseIdCersIdMap = flowInTransitOrderApi.getCrmEnterpriseRelationShipMap(crmEnterpriseIdGoodsCodeMap, 0, gbDetailMonth);

        /* 保存 */
        List<SaveFlowGoodsBatchTransitRequest> saveRequesList = new ArrayList<>();
        Date date = new Date();
        try {
            for (ImportFlowGoodsBatchTerminalModel model : object) {
                // 构建保存明细
                SaveFlowGoodsBatchTransitRequest request = buildSaveRequest(currentUserId, date, gbDetailMonth, crmEnterpriseIdCersIdMap, model);
                saveRequesList.add(request);
            }
            if (CollUtil.isNotEmpty(saveRequesList)) {
                flowTerminalOrderApi.batchSave(saveRequesList);
            }

        } catch (Exception e) {
            log.error("流向清洗-终端库存导入, 数据保存出错：{}", e.getMessage(), e);
        }
        return object;
    }


    private SaveFlowGoodsBatchTransitRequest buildSaveRequest(Long currentUserId, Date date, String gbDetailMonth, Map<Long, Long> crmEnterpriseRelationMap, ImportFlowGoodsBatchTerminalModel model) {
        SaveFlowGoodsBatchTransitRequest saveRequest = new SaveFlowGoodsBatchTransitRequest();
        // 所属年月
        saveRequest.setGbDetailMonth(gbDetailMonth);
        // 经销商
        saveRequest.setCrmEnterpriseId(model.getCrmEnterpriseId());
        saveRequest.setName(model.getName());
        // 标准商品
        saveRequest.setCrmGoodsCode(model.getCrmGoodsCode());
        saveRequest.setCrmGoodsName(model.getCrmGoodsName());
        saveRequest.setCrmGoodsSpecifications(model.getCrmGoodsSpecifications());
        // 批号
        saveRequest.setGbBatchNo(model.getGbBatchNo());
        // 库存数量，根据商品对照关系的系数计算库存
        BigDecimal calculateGbNumber = calculateGbNumberByGoodsMapping(model);
        saveRequest.setGbNumber(calculateGbNumber);
        // 库存类型
        saveRequest.setGoodsBatchType(FlowGoodsBatchTransitTypeEnum.TERMINAL.getCode());
        // 经销商三者关系ID
        Long crmEnterpriseRelationId = crmEnterpriseRelationMap.get(model.getCrmEnterpriseId());
        if (ObjectUtil.isNotNull(crmEnterpriseRelationId)) {
            saveRequest.setEnterpriseCersId(crmEnterpriseRelationId);
        }
        // 创建人
        saveRequest.setCreateUser(currentUserId);
        saveRequest.setCreateTime(date);
        return saveRequest;
    }

    private BigDecimal calculateGbNumberByGoodsMapping(ImportFlowGoodsBatchTerminalModel model) {
        BigDecimal gbNumber = model.getGbNumber();
        Integer convertUnit = model.getConvertUnit();
        BigDecimal convertNumber = model.getConvertNumber();
        BigDecimal calculateGbNumber = gbNumber;
        if (ObjectUtil.isNotNull(convertUnit) && convertUnit.intValue() > 0 && ObjectUtil.isNotNull(convertNumber) && convertNumber.compareTo(BigDecimal.ZERO) > 0) {
            if (convertUnit.intValue() == 1) {
                // 乘
                calculateGbNumber = gbNumber.multiply(convertNumber);
            } else if (convertUnit.intValue() == 2) {
                // 除
                calculateGbNumber = gbNumber.divide(convertNumber);
            }
        }
        return calculateGbNumber;
    }
}
