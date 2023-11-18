package com.yiling.export.imports.handler;

import java.math.BigDecimal;
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
import com.yiling.dataflow.crm.api.CrmGoodsInfoApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsInfoDTO;
import com.yiling.dataflow.flow.api.FlowEnterpriseGoodsMappingApi;
import com.yiling.dataflow.flow.dto.FlowEnterpriseGoodsMappingDTO;
import com.yiling.dataflow.flow.dto.request.SaveFlowEnterpriseGoodsMappingRequest;
import com.yiling.dataflow.wash.api.FlowMonthWashControlApi;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.export.excel.handler.BaseImportHandler;
import com.yiling.export.imports.model.ImportFlowEnterpriseGoodsMappingModel;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.pojo.Result;
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
 * @类名 ImportFlowEnterpriseGoodsMappingHandler
 * @描述
 * @创建时间 2023/2/28
 * @修改人 shichen
 * @修改时间 2023/2/28
 **/
@Slf4j
@Service
public class ImportFlowEnterpriseGoodsMappingHandler extends BaseImportHandler<ImportFlowEnterpriseGoodsMappingModel> {
    @DubboReference
    private FlowEnterpriseGoodsMappingApi flowEnterpriseGoodsMappingApi;

    @DubboReference
    private CrmGoodsInfoApi crmGoodsInfoApi;

    @DubboReference
    private CrmEnterpriseApi crmEnterpriseApi;

    @DubboReference
    private FlowMonthWashControlApi flowMonthWashControlApi;

    private static final BigDecimal MAX_CONVERT_NUMBER=new BigDecimal("99999.99");

    /**
     * 权限相关参数
     */
    @Setter
    @Getter
    private SjmsUserDatascopeBO userDatascopeBO;

    @Override
    public ExcelVerifyHandlerResult verifyHandler(ImportFlowEnterpriseGoodsMappingModel model) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);
        if(null==model.getCrmGoodsCode()){
            return this.error("标准产品编码不能为空");
        }
        CrmGoodsInfoDTO crmGoodsInfoDTO = crmGoodsInfoApi.findByCodeAndName(model.getCrmGoodsCode(), model.getGoodsName());
        if(null==crmGoodsInfoDTO){
            return this.error("标准产品编码和标准产品名称未找到对应标准产品");
        }else {
            model.setGoodsSpecification(crmGoodsInfoDTO.getGoodsSpec());
        }
        if(null==model.getConvertUnit() || null==model.getConvertNumber()){
            return this.error("转换单位或者转换系数不能为空");
        }
        if(model.getConvertUnit()!=1 && model.getConvertUnit()!=2){
            return this.error("转换单位不正确");
        }
        if( model.getConvertNumber().compareTo(BigDecimal.ZERO)<=0){
            return this.error("转换系数必须大于0");
        }
        if(model.getConvertNumber().scale()>2){
            return this.error("转换系数最多两位小数");
        }
        if(model.getConvertNumber().compareTo(MAX_CONVERT_NUMBER)>0){
            return this.error("转换系数最大为99999.99");
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
        model.setProvince(enterprise.getProvinceName());
        model.setProvinceCode(enterprise.getProvinceCode());
        return result;
    }

    @Override
    public List<ImportFlowEnterpriseGoodsMappingModel> execute(List<ImportFlowEnterpriseGoodsMappingModel> object, Map<String, Object> paramMap) {
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

        for(ImportFlowEnterpriseGoodsMappingModel form:object){
            try {
                SaveFlowEnterpriseGoodsMappingRequest request = PojoUtils.map(form, SaveFlowEnterpriseGoodsMappingRequest.class);
                request.setOpUserId((Long) paramMap.getOrDefault(MyMetaHandler.FIELD_OP_USER_ID, 0));
                FlowEnterpriseGoodsMappingDTO mappingDTO = flowEnterpriseGoodsMappingApi.findByFlowGoodsNameAndFlowSpecificationAndCrmEnterpriseId(request.getFlowGoodsName(), request.getFlowSpecification(), request.getCrmEnterpriseId());
                if(null != mappingDTO){
                    request.setId(mappingDTO.getId());
                }
                Long mappingId = flowEnterpriseGoodsMappingApi.save(request);
                if(freshFlowFlag){
                    if(null == mappingDTO){
                        mappingDTO = new FlowEnterpriseGoodsMappingDTO();
                        mappingDTO.setId(mappingId);
                        mappingDTO.setCrmGoodsCode(0L);
                    }
                    //发送更新前数值给上月月流向刷新
                    flowEnterpriseGoodsMappingApi.sendRefreshGoodsFlowMq(ListUtil.toList(mappingDTO));
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
