package com.yiling.export.export.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.ss.usermodel.Workbook;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.dict.api.DictApi;
import com.yiling.basic.dict.bo.DictBO;
import com.yiling.dataflow.flow.excel.FlowWashSaleReportExcel;
import com.yiling.dataflow.report.api.FlowWashReportApi;
import com.yiling.dataflow.report.dto.FlowWashSaleReportDTO;
import com.yiling.dataflow.report.dto.request.FlowWashSaleReportPageRequest;
import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.export.export.service.ExcelExportBigDataStylerImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import lombok.SneakyThrows;

/**
 * 流向销售合并报表导出
 *
 * @author zhigang.guo
 * @date: 2023/3/17
 */
@Service("flowWashSaleReportExportService")
public class FlowWashSaleReportExportServiceImpl implements BaseExportQueryDataService<FlowWashSaleReportPageRequest> {
    private static  final Logger logger = LoggerFactory.getLogger(FlowWashSaleReportExportServiceImpl.class);
    @DubboReference
    FlowWashReportApi flowWashReportApi;
    @DubboReference
    SjmsUserDatascopeApi userDatascopeApi;

    @DubboReference
    DictApi dictApi;

    private ThreadLocal<List<DictBO>>  dictThreadLocal = new ThreadLocal<>();

    /**
     * 获取数据字典
     * @return
     */
    private List<DictBO> getDictDataList() {

        List<DictBO> dictBOList = dictThreadLocal.get();

        if (dictBOList == null) {
            List<DictBO> dictApiEnabledList = dictApi.getEnabledList();
            dictThreadLocal.set(dictApiEnabledList);
            dictBOList = dictApiEnabledList;
        }

        return dictBOList;
    }


    /**
     * 获取数据字典值
     * @param key
     * @param data
     * @return
     */
    private String getDictDataValue(String key,Integer data) {

        if (data == null || data == 0) {

            return "--";
        }

        List<DictBO> dataList = this.getDictDataList();
        if (CollectionUtil.isEmpty(dataList)) {
            return "--";
        }

        Map<String, DictBO> dictBOMap = dataList.stream().collect(Collectors.toMap(DictBO::getName, Function.identity()));
        DictBO dictBO = dictBOMap.get(key);
        if (dictBO == null) {
            return "--";
        }

        List<DictBO.DictData> dictBODataList = dictBO.getDataList();
        if (CollectionUtil.isEmpty(dictBODataList)) {

            return "--";
        }

        Map<String, DictBO.DictData> dictDataMap = dictBODataList.stream().collect(Collectors.toMap(DictBO.DictData::getValue, Function.identity(),(k1, k2) -> k1));

        return Optional.ofNullable(dictDataMap.get(data + "")).map(t -> t.getLabel()).orElse("--");
    }


    @Override
    public List<Object> selectListForExcelExport(Object queryParams, int page) {

        if (!(queryParams instanceof FlowWashSaleReportPageRequest)) {

            return Collections.emptyList();
        }

        FlowWashSaleReportPageRequest request = (FlowWashSaleReportPageRequest) queryParams;
        request.setCurrent(page);
        request.setSize(2000);
        Page<FlowWashSaleReportDTO> flowWashSaleReportDTOPage = flowWashReportApi.saleReportPageList(request);

        if (flowWashSaleReportDTOPage == null || CollectionUtil.isEmpty(flowWashSaleReportDTOPage.getRecords())) {

            return Collections.emptyList();
        }

        List result = flowWashSaleReportDTOPage.getRecords().stream().map(t ->{

            FlowWashSaleReportExcel z =   PojoUtils.map(t,FlowWashSaleReportExcel.class);

            // 数据字典过滤
            z.setSupplierLevelStr(getDictDataValue("crm_supplier_level",t.getSupplierLevel()));
            z.setSupplierAttributeStr(getDictDataValue("crm_supplier_attribute",t.getSupplierAttribute()));
            z.setGeneralMedicineLevelStr(getDictDataValue("crm_supplier_general_medicine_level",t.getGeneralMedicineLevel()));
            z.setSupplierSaleTypeStr(getDictDataValue("crm_supplier_sale_type",t.getSupplierSaleType()));
            z.setBaseSupplierInfoStr(getDictDataValue("crm_supplier_base_supplier_info",t.getBaseSupplierInfo()));
            z.setHeadChainFlagStr(Constants.IS_YES == t.getHeadChainFlag() ? "是" : "否");
            z.setChainAttributeStr(getDictDataValue("crm_supplier_chain_attribute",t.getChainAttribute()));
            z.setChainKaFlagStr(Constants.IS_YES == t.getChainKaFlag() ? "是" : "否");
            z.setIsChainFlagStr(Constants.IS_YES == t.getIsChainFlag() ? "是" : "否");
            z.setCustomerSupplierLevelStr(getDictDataValue("crm_supplier_level",t.getCustomerSupplierLevel()));
            z.setCustomerGeneralMedicineLevelStr(getDictDataValue("crm_supplier_general_medicine_level",t.getCustomerGeneralMedicineLevel()));
            z.setCustomerSupplierAttributeStr(getDictDataValue("crm_supplier_attribute",t.getCustomerSupplierAttribute()));
            z.setCustomerHeadChainFlagStr(Constants.IS_YES == t.getCustomerHeadChainFlag() ? "是" : "否");
            z.setCustomerChainAttributeStr(getDictDataValue("crm_supplier_chain_attribute",t.getCustomerChainAttribute()));
            z.setCustomerChainKaFlagStr(Constants.IS_YES == t.getCustomerChainKaFlag() ? "是" : "否");
            z.setCustomerSupplierSaleTypeStr(getDictDataValue("crm_supplier_sale_type",t.getCustomerSupplierSaleType()));
            z.setCustomerBaseSupplierInfoStr(getDictDataValue("crm_supplier_base_supplier_info",t.getCustomerBaseSupplierInfo()));
            z.setCustomerSupplyChainRoleStr(getDictDataValue("crm_supply_chain_role",t.getCustomerSupplyChainRole()));
            z.setCustomerMedicalNatureStr(getDictDataValue("crm_hospital_medical_nature",t.getCustomerMedicalNature()));
            z.setCustomerMedicalTypeStr(getDictDataValue("crm_hospital_medical_type",t.getCustomerMedicalType()));
            z.setCustomerYlLevelStr(getDictDataValue("crm_hospital_yl_level",t.getCustomerYlLevel()));
            z.setCustomerNationalGradeStr(getDictDataValue("crm_hospital_national_grade",t.getCustomerNationalGrade()));
            z.setBaseMedicineFlagStr(Constants.IS_YES == t.getBaseMedicineFlag() ? "是" : "否");
            z.setPharmacyTypeStr(getDictDataValue("crm_pharmacy_type",t.getPharmacyType()));
            z.setPharmacyLevelStr(getDictDataValue("crm_pharmacy_level",t.getPharmacyLevel()));
            z.setPharmacyChainAttributeStr(getDictDataValue("crm_supplier_chain_attribute",t.getPharmacyChainAttribute()));
            z.setLabelAttributeStr(getDictDataValue("crm_pharmacy_label_attribute",t.getLabelAttribute()));
            z.setTargetFlagStr(Constants.IS_YES == t.getTargetFlag() ? "是" : "否");
            z.setFlowClassifyStr(getDictDataValue("flow_month_wash_task_classify",t.getFlowClassify()));
            z.setCollectionMethodStr(getDictDataValue("flow_month_wash_task_collection_method",t.getCollectionMethod()));
            z.setComplainTypeStr(getDictDataValue("sales_appeal_type",t.getComplainType()));

            return z;

        } ).collect(Collectors.toList());

        return result;
    }


    @Override
    @SneakyThrows
    public byte[] getExportByte(FlowWashSaleReportPageRequest request,String file) {

        ExportParams exportParams = new ExportParams();
        exportParams.setType(ExcelType.XSSF);
        exportParams.setCreateHeadRows(true);
        exportParams.setStyle(ExcelExportBigDataStylerImpl.class);
        Workbook workbook = ExcelExportUtil.exportBigExcel(exportParams, FlowWashSaleReportExcel.class, this, request);
        String tmpDirPath = FileUtil.getTmpDirPath() + File.separator + "FlowWashSaleReportExportTaskExport";
        File tmpExcelDir = FileUtil.newFile(tmpDirPath + File.separator + "excel");

        if (!tmpExcelDir.isDirectory()) {
            tmpExcelDir.mkdirs();
        }

        file = file.substring(0, file.lastIndexOf(".zip"));
        String fileName = file + Constants.SEPARATOR_UNDERLINE +  DateUtil.format(new Date(), DatePattern.PURE_DATE_FORMAT) + ".xlsx";

        File excelDir = FileUtil.newFile(tmpExcelDir.getPath() + File.separator + fileName);

        if (excelDir.exists()) {
            FileUtil.del(excelDir);
        }

        excelDir.createNewFile();
        FileOutputStream fileOutputStream  = new FileOutputStream(excelDir);
        workbook.write(fileOutputStream);

        try {
            File zipFile = ZipUtil.zip(tmpExcelDir);
            if (ObjectUtil.isNull(zipFile)) {
                return null;
            }
            return FileUtil.readBytes(zipFile);
        } finally {
            IoUtil.close(fileOutputStream);
            FileUtil.del(tmpDirPath);
            dictThreadLocal.remove();
        }
    }





    @Override
    public QueryExportDataDTO queryData(FlowWashSaleReportPageRequest request) { return null;}


    @Override
    public FlowWashSaleReportPageRequest getParam(Map<String, Object> map) {
        FlowWashSaleReportPageRequest request = PojoUtils.map(map, FlowWashSaleReportPageRequest.class);

        Object soMonth = map.get("soMonth");
        Object currentUserCode = map.get("currentUserCode");// 当前用户工号

        if (ObjectUtil.isNull(soMonth) || ObjectUtil.isNull(currentUserCode)) {

            throw new BusinessException(ResultCode.PARAM_VALID_ERROR, "参数异常");
        }

        logger.debug(() -> "当前用户:" + currentUserCode);

        // 获取数据权限
        SjmsUserDatascopeBO sjmsUserDatascopeBO = userDatascopeApi.getByEmpId(currentUserCode.toString());
        if (sjmsUserDatascopeBO == null || OrgDatascopeEnum.getFromCode(sjmsUserDatascopeBO.getOrgDatascope()) == OrgDatascopeEnum.NONE) {

            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        // 部分数据权限,需要过滤数据
        if (OrgDatascopeEnum.getFromCode(sjmsUserDatascopeBO.getOrgDatascope()) == OrgDatascopeEnum.PORTION) {
            request.setCrmIdList(sjmsUserDatascopeBO.getOrgPartDatascopeBO().getCrmEids());
            request.setSupplierProvinceNameList(sjmsUserDatascopeBO.getOrgPartDatascopeBO().getProvinceNames());
        }

        Date date = DateUtil.parse(soMonth.toString(), "yyyy-MM");
        request.setYear(StrUtil.toString(DateUtil.year(date)));
        request.setMonth(StrUtil.toString(DateUtil.month(date) + 1));

        Object soSpecifications = map.get("soSpecifications");

        if (ObjectUtil.isNotNull(soSpecifications)) {
            request.setSoSpecifications(soSpecifications.toString());
        }

        Object crmId = map.get("crmId");
        if (ObjectUtil.isNotNull(crmId)) {
            request.setCrmId(Long.valueOf(crmId.toString()));
        }

        Object originalEnterpriseName = map.get("originalEnterpriseName");
        if (ObjectUtil.isNotNull(originalEnterpriseName)) {
            request.setOriginalEnterpriseName(originalEnterpriseName.toString());
        }

        Object goodsCode = map.get("goodsCode");
        if (ObjectUtil.isNotNull(goodsCode)) {
            request.setGoodsCode(Long.valueOf(goodsCode.toString()));
        }

        Object department = map.get("department");
        if (ObjectUtil.isNotNull(department)) {
            request.setDepartment(department.toString());
        }

        Object businessDepartment = map.get("businessDepartment");
        if (ObjectUtil.isNotNull(businessDepartment)) {
            request.setBusinessDepartment(businessDepartment.toString());
        }

        Object provincialArea = map.get("provincialArea");
        if (ObjectUtil.isNotNull(provincialArea)) {
            request.setProvincialArea(provincialArea.toString());
        }

        Object businessProvince = map.get("businessProvince");
        if (ObjectUtil.isNotNull(businessProvince)) {
            request.setBusinessProvince(businessProvince.toString());
        }

        Object regionName = map.get("regionName");
        if (ObjectUtil.isNotNull(regionName)) {
            request.setRegionName(regionName.toString());
        }

        Object representativeCode = map.get("representativeCode");

        if (ObjectUtil.isNotNull(representativeCode)) {
            request.setRepresentativeCode(representativeCode.toString());
        }

        return request;
    }


    @Override
    public String getFileSuffix() {

        return "zip";
    }

    @Override
    public boolean isReturnData() {

        return false;
    }
}
