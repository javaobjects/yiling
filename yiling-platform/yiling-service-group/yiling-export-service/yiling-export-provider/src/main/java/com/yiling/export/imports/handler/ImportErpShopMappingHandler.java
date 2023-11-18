package com.yiling.export.imports.handler;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.yiling.export.excel.handler.BaseImportHandler;
import com.yiling.export.imports.model.ImportErpShopMappingModel;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.api.ErpShopMappingApi;
import com.yiling.open.erp.dto.ErpShopMappingDTO;
import com.yiling.open.erp.dto.request.SaveErpShopMappingRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 ImportErpShopMappingHandler
 * @描述
 * @创建时间 2023/3/21
 * @修改人 shichen
 * @修改时间 2023/3/21
 **/
@Service
@Slf4j
public class ImportErpShopMappingHandler extends BaseImportHandler<ImportErpShopMappingModel> {
    @DubboReference
    private EnterpriseApi enterpriseApi;

    @DubboReference
    private ErpShopMappingApi erpShopMappingApi;

    @Override
    public ExcelVerifyHandlerResult verifyHandler(ImportErpShopMappingModel model) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);
        if(null==model.getMainShopEid()){
            return this.error("总店ID不能为空");
        }
        EnterpriseDTO mainShop = enterpriseApi.getById(model.getMainShopEid());
        if(null==mainShop){
            return this.error("总店ID未找到企业");
        }
        if(!EnterpriseTypeEnum.BUSINESS.getCode().equals(mainShop.getType())
                &&!EnterpriseTypeEnum.CHAIN_BASE.getCode().equals(mainShop.getType())){
            return this.error("总店企业ID："+model.getMainShopEid()+"类型异常，请确认后再进行导入");
        }

        if(null==model.getShopEid()){
            return this.error("门店ID不能为空");
        }
        EnterpriseDTO shop = enterpriseApi.getById(model.getShopEid());
        if(null==shop){
            return this.error("门店ID未找到企业");
        }
        if(!EnterpriseTypeEnum.CHAIN_DIRECT.getCode().equals(shop.getType())
                &&!EnterpriseTypeEnum.CHAIN_JOIN.getCode().equals(shop.getType())){
            return this.error("门店企业ID："+model.getShopEid()+"类型异常，请确认后再进行导入");
        }
        ErpShopMappingDTO erpShopMappingDTO = erpShopMappingApi.findByMainShopAndShop(model.getMainShopEid(),model.getShopEid(),model.getShopCode());
        if(null!=erpShopMappingDTO){
            return this.error("该总店下门店id/门店编码已存在");
        }
        if(StringUtils.isBlank(model.getMainShopName())){
            model.setMainShopName(mainShop.getName());
        }
        if(StringUtils.isBlank(model.getShopName())){
            model.setShopName(shop.getName());
        }
        if(null != model.getSyncStatus()){
            if(model.getSyncStatus() !=0 && model.getSyncStatus()!=1){
                return this.error("同步状态输入有误");
            }
        }else {
            return this.error("同步状态为空");
        }
        return result;
    }

    @Override
    public List<ImportErpShopMappingModel> execute(List<ImportErpShopMappingModel> object, Map<String, Object> paramMap) {
        for(ImportErpShopMappingModel form:object){
            try {
                SaveErpShopMappingRequest request = PojoUtils.map(form, SaveErpShopMappingRequest.class);
                request.setOpUserId((Long) paramMap.getOrDefault(MyMetaHandler.FIELD_OP_USER_ID, 0));
                erpShopMappingApi.saveShopMapping(request);
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
