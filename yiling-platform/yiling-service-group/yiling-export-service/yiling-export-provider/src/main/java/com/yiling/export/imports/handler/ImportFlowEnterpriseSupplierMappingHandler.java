package com.yiling.export.imports.handler;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.enums.CrmSupplyChainRoleEnum;
import com.yiling.dataflow.flow.api.FlowEnterpriseSupplierMappingApi;
import com.yiling.dataflow.flow.dto.FlowEnterpriseSupplierMappingDTO;
import com.yiling.dataflow.flow.dto.request.SaveFlowEnterpriseSupplierMappingRequest;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.dataflow.wash.api.FlowMonthWashControlApi;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.dataflow.wash.enums.FlowMonthWashControlMappingStatusEnum;
import com.yiling.export.excel.handler.AbstractExcelImportHandler;
import com.yiling.export.imports.model.ImportFlowEnterpriseCustomerMappingModel;
import com.yiling.export.imports.model.ImportFlowEnterpriseSupplierMappingModel;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.util.PojoUtils;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 ImportFlowEnterpriseSupplierMappingHandler
 * @描述
 * @创建时间 2023/6/1
 * @修改人 shichen
 * @修改时间 2023/6/1
 **/
@Slf4j
@Service
public class ImportFlowEnterpriseSupplierMappingHandler extends AbstractExcelImportHandler<ImportFlowEnterpriseSupplierMappingModel> {

    @DubboReference
    private FlowEnterpriseSupplierMappingApi flowEnterpriseSupplierMappingApi;

    @DubboReference
    private CrmEnterpriseApi crmEnterpriseApi;

    @DubboReference
    private FlowMonthWashControlApi flowMonthWashControlApi;

    /**
     * 权限相关参数
     */
    @Setter
    @Getter
    private SjmsUserDatascopeBO userDatascopeBO;


    @Override
    protected ExcelVerifyHandlerResult verify(ImportFlowEnterpriseSupplierMappingModel model) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);
        CrmEnterpriseDTO orgEnterprise = crmEnterpriseApi.getCrmEnterpriseById(model.getCrmOrgId());
        if(null == orgEnterprise){
            return this.error("标准机构编码未找到对应机构");
        }else {
            if(!orgEnterprise.getName().equals(model.getOrgName())){
                return this.error("标准机构编码与标准机构名称不匹配");
            }
        }
        CrmEnterpriseDTO enterprise = crmEnterpriseApi.getCrmEnterpriseById(model.getCrmEnterpriseId());
        if(null == enterprise){
            return this.error("经销商编码未找到对应企业");
        }
        if(null == this.userDatascopeBO || OrgDatascopeEnum.NONE.getCode().equals(this.userDatascopeBO.getOrgDatascope())){
            return this.error("没有操作经销商权限");
        }
        if(OrgDatascopeEnum.PORTION.getCode().equals(this.userDatascopeBO.getOrgDatascope())){
            if(!this.userDatascopeBO.getOrgPartDatascopeBO().getCrmEids().contains(enterprise.getId())
                    && !this.userDatascopeBO.getOrgPartDatascopeBO().getProvinceCodes().contains(enterprise.getProvinceCode())){
                return this.error("没有操作该经销商权限");
            }
        }
        if(!enterprise.getName().equals(model.getEnterpriseName())){
            return this.error("经销商编码与经销商名称不匹配");
        }
        if(!CrmSupplyChainRoleEnum.DISTRIBUTOR.getCode().equals(enterprise.getSupplyChainRole())){
            return this.error("经销商不是商业公司档案");
        }
        if(model.getCrmOrgId().equals(model.getCrmEnterpriseId())){
            return this.error("经销商编码和标准机构编码相同");
        }
        model.setProvince(enterprise.getProvinceName());
        model.setProvinceCode(enterprise.getProvinceCode());
        return result;
    }

    @Override
    protected List<ImportFlowEnterpriseSupplierMappingModel> importData(List<ImportFlowEnterpriseSupplierMappingModel> object, Map<String, Object> paramMap) {
        Date currentDate = new Date();
        Date lastMonthTime = DateUtil.offsetMonth(currentDate, -1);
        int lastMonth=DateUtil.month(lastMonthTime)+1;
        int lastYear = DateUtil.year(lastMonthTime);
        //获取上月清洗日程配置
        FlowMonthWashControlDTO lastMonthControlDTO = flowMonthWashControlApi.getByYearAndMonth(lastYear, lastMonth);
        Boolean freshFlowFlag =false;
        if(null!=lastMonthControlDTO && 1==lastMonthControlDTO.getWashStatus()){
            freshFlowFlag = true;
        }
        for(ImportFlowEnterpriseSupplierMappingModel form:object){
            try {
                SaveFlowEnterpriseSupplierMappingRequest request = PojoUtils.map(form, SaveFlowEnterpriseSupplierMappingRequest.class);
                request.setOpUserId((Long) paramMap.getOrDefault(MyMetaHandler.FIELD_OP_USER_ID, 0));
                FlowEnterpriseSupplierMappingDTO mappingDTO = flowEnterpriseSupplierMappingApi.findBySupplierNameAndCrmEnterpriseId(request.getFlowSupplierName(), request.getCrmEnterpriseId());
                if(null != mappingDTO){
                    request.setId(mappingDTO.getId());
                }
                Long mappingId = flowEnterpriseSupplierMappingApi.save(request);
                if(freshFlowFlag){
                    if(null == mappingDTO){
                        mappingDTO = new FlowEnterpriseSupplierMappingDTO();
                        mappingDTO.setId(mappingId);
                        mappingDTO.setCrmOrgId(0L);
                    }
                    //发送更新前数值给上月月流向刷新
                    flowEnterpriseSupplierMappingApi.sendRefreshSupplierFlowMq(ListUtil.toList(mappingDTO));
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
