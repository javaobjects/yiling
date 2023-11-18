package com.yiling.export.imports.handler;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.yiling.dataflow.relation.api.FlowGoodsPriceRelationApi;
import com.yiling.dataflow.relation.dto.FlowGoodsPriceRelationDTO;
import com.yiling.dataflow.relation.dto.request.SaveFlowGoodsPriceRelationRequest;
import com.yiling.export.excel.handler.BaseImportHandler;
import com.yiling.export.imports.model.ImportFlowGoodsPriceRelationExcel;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.dto.ErpCustomerDTO;
import com.yiling.open.erp.dto.request.SaveEnterpriseCustomerRequest;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 ImportFlowGoodsPriceRelationHandler
 * @描述
 * @创建时间 2023/2/22
 * @修改人 shichen
 * @修改时间 2023/2/22
 **/
@Component
@Slf4j
public class ImportFlowGoodsPriceRelationHandler extends BaseImportHandler<ImportFlowGoodsPriceRelationExcel> {

    @DubboReference
    private FlowGoodsPriceRelationApi flowGoodsPriceRelationApi;

    @Override
    public ExcelVerifyHandlerResult verifyHandler(ImportFlowGoodsPriceRelationExcel importFlowGoodsPriceRelationExcel) {

        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);
        if(StringUtils.isBlank(importFlowGoodsPriceRelationExcel.getOldGoodsName())){
            return this.error("原始产品名称不能为空");
        }
        if(StringUtils.isBlank(importFlowGoodsPriceRelationExcel.getSpec())){
            return this.error("原始产品规格不能为空");
        }
        if(null == importFlowGoodsPriceRelationExcel.getGoodsCode() || importFlowGoodsPriceRelationExcel.getGoodsCode()==0L){
            return this.error("对应产品代码不能为空");
        }
        if(StringUtils.isBlank(importFlowGoodsPriceRelationExcel.getGoodsName())){
            return this.error("对应产品名称不能为空");
        }
        if(StringUtils.isBlank(importFlowGoodsPriceRelationExcel.getCustomerCode())){
            return this.error("经销商代码不能为空");
        }
        if(StringUtils.isBlank(importFlowGoodsPriceRelationExcel.getCustomer())){
            return this.error("经销商名称不能为空");
        }
        FlowGoodsPriceRelationDTO priceRelationDTO = flowGoodsPriceRelationApi.getByGoodsNameAndSpecAndEnterpriseCode(importFlowGoodsPriceRelationExcel.getOldGoodsName(), importFlowGoodsPriceRelationExcel.getSpec(), importFlowGoodsPriceRelationExcel.getCustomerCode());
        if(null != priceRelationDTO){
            return this.error("原始名称和原始规格已绑定该经销商代码");
        }
        return result;
    }

    @Override
    public List<ImportFlowGoodsPriceRelationExcel> execute(List<ImportFlowGoodsPriceRelationExcel> object, Map<String, Object> paramMap) {
        for(ImportFlowGoodsPriceRelationExcel form:object){
            try {
                SaveFlowGoodsPriceRelationRequest request = PojoUtils.map(form, SaveFlowGoodsPriceRelationRequest.class);
                request.setOldGoodsCode(form.getGoodsCode());
                request.setOpUserId((Long) paramMap.getOrDefault(MyMetaHandler.FIELD_OP_USER_ID, 0));
                Boolean b = flowGoodsPriceRelationApi.save(request);
                if(!b){
                    form.setErrorMsg("保存失败");
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
