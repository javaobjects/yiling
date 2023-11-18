package com.yiling.export.imports.handler;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.yiling.export.excel.handler.BaseImportHandler;
import com.yiling.export.imports.model.ImportAbnormalCustomerExcel;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.api.ErpCustomerApi;
import com.yiling.open.erp.dto.ErpCustomerDTO;
import com.yiling.open.erp.dto.request.SaveEnterpriseCustomerRequest;
import com.yiling.open.erp.enums.SyncStatus;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 ImportAbnormalCustomerHandler
 * @描述
 * @创建时间 2022/12/22
 * @修改人 shichen
 * @修改时间 2022/12/22
 **/
@Component
@Slf4j
public class ImportAbnormalCustomerHandler extends BaseImportHandler<ImportAbnormalCustomerExcel> {
    @DubboReference
    private ErpCustomerApi erpCustomerApi;

    @Override
    public ExcelVerifyHandlerResult verifyHandler(ImportAbnormalCustomerExcel importAbnormalCustomerExcel) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);
        ErpCustomerDTO erpCustomerDTO = erpCustomerApi.findById(importAbnormalCustomerExcel.getId());
        if(null==erpCustomerDTO){
            return this.error("未找到ID对应的异常客户信息");
        }
        if(!SyncStatus.FAIL.getCode().equals(erpCustomerDTO.getSyncStatus())){
            return this.error("ID对应的异常客户信息不是异常状态");
        }
        if(StringUtils.isBlank(importAbnormalCustomerExcel.getName())){
            return this.error("企业名称不能为空");
        }
        if(StringUtils.isBlank(importAbnormalCustomerExcel.getCustomerType())){
            return this.error("企业类型不能为空");
        }
        if(StringUtils.isBlank(importAbnormalCustomerExcel.getLicenseNo())){
            return this.error("统一社会信用代码/医疗机构许可证不能为空");
        }
        if(StringUtils.isBlank(importAbnormalCustomerExcel.getContact())){
            return this.error("联系人不能为空");
        }
        if(StringUtils.isBlank(importAbnormalCustomerExcel.getProvince())){
            return this.error("省不能为空");
        }
        if(StringUtils.isBlank(importAbnormalCustomerExcel.getCity())){
            return this.error("市不能为空");
        }
        if(StringUtils.isBlank(importAbnormalCustomerExcel.getRegion())){
            return this.error("区不能为空");
        }
        if(StringUtils.isBlank(importAbnormalCustomerExcel.getAddress())){
            return this.error("详细地址不能为空");
        }
        return result;
    }

    @Override
    public List<ImportAbnormalCustomerExcel> execute(List<ImportAbnormalCustomerExcel> object, Map<String, Object> paramMap) {
        for(ImportAbnormalCustomerExcel form:object){
            try {
                ErpCustomerDTO sourceDTO = erpCustomerApi.findById(form.getId());
                ErpCustomerDTO erpCustomerDTO = PojoUtils.map(form, ErpCustomerDTO.class);
                erpCustomerDTO.setSuId(sourceDTO.getSuId());
                erpCustomerDTO.setSuDeptNo(sourceDTO.getSuDeptNo());
                erpCustomerDTO.setInnerCode(sourceDTO.getInnerCode());
                erpCustomerDTO.setSn(sourceDTO.getSn());
                erpCustomerDTO.setGroupName(sourceDTO.getGroupName());
                SaveEnterpriseCustomerRequest request = new SaveEnterpriseCustomerRequest();
                request.setErpCustomer(erpCustomerDTO);
                request.setOpUserId((Long) paramMap.getOrDefault(MyMetaHandler.FIELD_OP_USER_ID, 0));
                Boolean b = erpCustomerApi.maintain(request);
                if(!b){
                    ErpCustomerDTO errorCustomer = erpCustomerApi.findById(form.getId());
                    form.setErrorMsg(errorCustomer.getSyncMsg());
                    form.setStatus("失败");
                }
            }catch (BusinessException be){
                form.setErrorMsg(be.getMessage());
                form.setStatus("失败");
            }catch (Exception e){
                form.setErrorMsg(ResultCode.EXCEL_DATA_SAVING_FAILED.getMessage());
                form.setStatus("失败");
                log.error("数据保存出错：{}", e.getMessage(), e);
            }
        }
        return object;
    }
}
