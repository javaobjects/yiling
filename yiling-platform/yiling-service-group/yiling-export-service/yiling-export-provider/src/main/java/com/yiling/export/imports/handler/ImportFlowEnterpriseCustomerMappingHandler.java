package com.yiling.export.imports.handler;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yiling.dataflow.crm.enums.CrmSupplyChainRoleEnum;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.dataflow.wash.enums.FlowMonthWashControlMappingStatusEnum;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.flow.api.FlowEnterpriseCustomerMappingApi;
import com.yiling.dataflow.flow.dto.FlowEnterpriseCustomerMappingDTO;
import com.yiling.dataflow.flow.dto.request.SaveFlowEnterpriseCustomerMappingRequest;
import com.yiling.dataflow.wash.api.FlowMonthWashControlApi;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.export.excel.handler.BaseImportHandler;
import com.yiling.export.imports.model.ImportFlowEnterpriseCustomerMappingModel;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.util.PojoUtils;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 ImportFlowEnterpriseCustomerMappingHandler
 * @描述
 * @创建时间 2023/3/1
 * @修改人 shichen
 * @修改时间 2023/3/1
 **/
@Slf4j
@Service
public class ImportFlowEnterpriseCustomerMappingHandler extends BaseImportHandler<ImportFlowEnterpriseCustomerMappingModel> {

    @DubboReference
    private FlowEnterpriseCustomerMappingApi flowEnterpriseCustomerMappingApi;

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
    public ExcelVerifyHandlerResult verifyHandler(ImportFlowEnterpriseCustomerMappingModel model) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);
        if(null==model.getCrmOrgId()){
            return this.error("标准机构编码不能为空");
        }
        CrmEnterpriseDTO orgEnterprise = crmEnterpriseApi.getCrmEnterpriseById(model.getCrmOrgId());
        if(null == orgEnterprise){
            return this.error("标准机构编码未找到对应机构");
        }else {
            if(!orgEnterprise.getName().equals(model.getOrgName())){
                return this.error("标准机构编码与标准机构名称不匹配");
            }
        }
        if(null==model.getCrmEnterpriseId()){
            return this.error("经销商编码不能为空");
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
    public List<ImportFlowEnterpriseCustomerMappingModel> execute(List<ImportFlowEnterpriseCustomerMappingModel> object, Map<String, Object> paramMap) {
        Date currentDate = new Date();
        Date lastMonthTime = DateUtil.offsetMonth(currentDate, -1);
        int lastMonth=DateUtil.month(lastMonthTime)+1;
        int lastYear = DateUtil.year(lastMonthTime);
        //获取上月清洗日程配置
        FlowMonthWashControlDTO lastMonthControlDTO = flowMonthWashControlApi.getByYearAndMonth(lastYear, lastMonth);
        Boolean freshFlowFlag =false;
        if(null!=lastMonthControlDTO && 2==lastMonthControlDTO.getWashStatus()){
            freshFlowFlag = true;
        }
        for(ImportFlowEnterpriseCustomerMappingModel form:object){
            try {
                SaveFlowEnterpriseCustomerMappingRequest request = PojoUtils.map(form, SaveFlowEnterpriseCustomerMappingRequest.class);
                request.setOpUserId((Long) paramMap.getOrDefault(MyMetaHandler.FIELD_OP_USER_ID, 0));
                FlowEnterpriseCustomerMappingDTO mappingDTO = flowEnterpriseCustomerMappingApi.findByCustomerNameAndCrmEnterpriseId(request.getFlowCustomerName(), request.getCrmEnterpriseId());
                if(null != mappingDTO){
                    request.setId(mappingDTO.getId());
                }
                Long mappingId = flowEnterpriseCustomerMappingApi.save(request);
                if(freshFlowFlag){
                    if(null == mappingDTO){
                        mappingDTO = new FlowEnterpriseCustomerMappingDTO();
                        mappingDTO.setId(mappingId);
                        mappingDTO.setCrmOrgId(0L);
                    }
                    //发送更新前数值给上月月流向刷新
                    flowEnterpriseCustomerMappingApi.sendRefreshCustomerFlowMq(ListUtil.toList(mappingDTO));
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
