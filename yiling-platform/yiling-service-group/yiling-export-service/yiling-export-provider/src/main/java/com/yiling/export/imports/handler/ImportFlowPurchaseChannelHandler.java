package com.yiling.export.imports.handler;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.enums.CrmSupplyChainRoleEnum;
import com.yiling.dataflow.flow.api.FlowEnterpriseCustomerMappingApi;
import com.yiling.dataflow.flow.api.FlowPurchaseChannelApi;
import com.yiling.dataflow.flow.dto.FlowPurchaseChannelDTO;
import com.yiling.dataflow.flow.dto.request.SaveFlowEnterpriseCustomerMappingRequest;
import com.yiling.dataflow.flow.dto.request.SaveFlowPurchaseChannelRequest;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.export.excel.handler.BaseImportHandler;
import com.yiling.export.imports.model.ImportFlowEnterpriseCustomerMappingModel;
import com.yiling.export.imports.model.ImportFlowPurchaseChannelModel;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.hutool.core.collection.CollectionUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 ImportFlowPurchaseChannelHandler
 * @描述
 * @创建时间 2023/3/1
 * @修改人 shichen
 * @修改时间 2023/3/1
 **/
@Slf4j
@Service
public class ImportFlowPurchaseChannelHandler extends BaseImportHandler<ImportFlowPurchaseChannelModel> {

    @DubboReference
    private FlowPurchaseChannelApi flowPurchaseChannelApi;

    @DubboReference
    private CrmEnterpriseApi crmEnterpriseApi;

    /**
     * 权限相关参数
     */
    @Setter
    @Getter
    private SjmsUserDatascopeBO userDatascopeBO;

    @Override
    public ExcelVerifyHandlerResult verifyHandler(ImportFlowPurchaseChannelModel model) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);
        if(null==model.getCrmOrgId()){
            return this.error("机构编码不能为空");
        }
        CrmEnterpriseDTO org = crmEnterpriseApi.getCrmEnterpriseById(model.getCrmOrgId());
        if(null == org){
            return this.error("机构编码未找到对应机构");
        }
        if(null == this.userDatascopeBO || OrgDatascopeEnum.NONE.getCode().equals(this.userDatascopeBO.getOrgDatascope())){
            return this.error("没有操作机构权限");
        }
        if(OrgDatascopeEnum.PORTION.getCode().equals(this.userDatascopeBO.getOrgDatascope())){
            if(!this.userDatascopeBO.getOrgPartDatascopeBO().getCrmEids().contains(org.getId())
                    && !this.userDatascopeBO.getOrgPartDatascopeBO().getProvinceCodes().contains(org.getProvinceCode())){
                return this.error("没有操作该机构权限");
            }
        }
        if(!org.getName().equals(model.getOrgName())){
            return this.error("机构编码与标准机构名称不匹配");
        }
        if(null==model.getCrmPurchaseOrgId()){
            return this.error("采购渠道机构编码不能为空");
        }
        CrmEnterpriseDTO purchaseOrg = crmEnterpriseApi.getCrmEnterpriseById(model.getCrmPurchaseOrgId());
        if(null == purchaseOrg){
            return this.error("采购渠道机构编码未找到对应机构");
        }else {
            if(!purchaseOrg.getName().equals(model.getPurchaseOrgName())){
                return this.error("采购渠道机构编码与采购渠道机构名称不匹配");
            }
            if(!CrmSupplyChainRoleEnum.DISTRIBUTOR.getCode().equals(purchaseOrg.getSupplyChainRole())){
                return this.error("采购渠道机构不是商业公司档案");
            }
        }
        if(model.getCrmOrgId().equals(model.getCrmPurchaseOrgId())){
            return this.error("机构编码和采购渠道机构编码不能相同");
        }
        FlowPurchaseChannelDTO purchaseChannelDTO = flowPurchaseChannelApi.findByOrgIdAndPurchaseOrgId(model.getCrmOrgId(), model.getCrmPurchaseOrgId());
        if(null != purchaseChannelDTO){
            return this.error("机构和采购渠道机构对应的采购渠道已存在");
        }
        model.setProvince(org.getProvinceName());
        model.setCity(org.getCityName());
        model.setRegion(org.getRegionName());
        model.setProvinceCode(org.getProvinceCode());
        model.setCityCode(org.getCityCode());
        model.setRegionCode(org.getRegionCode());
        return result;
    }

    @Override
    public List<ImportFlowPurchaseChannelModel> execute(List<ImportFlowPurchaseChannelModel> object, Map<String, Object> paramMap) {
        for(ImportFlowPurchaseChannelModel form:object){
            try {
                SaveFlowPurchaseChannelRequest request = PojoUtils.map(form, SaveFlowPurchaseChannelRequest.class);
                request.setOpUserId((Long) paramMap.getOrDefault(MyMetaHandler.FIELD_OP_USER_ID, 0));
                flowPurchaseChannelApi.save(request);
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
