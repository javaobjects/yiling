package com.yiling.admin.b2b.goods.handler;

import java.util.List;
import java.util.Map;

import com.yiling.export.excel.handler.ImportDataHandler;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.yiling.admin.b2b.goods.form.ImportGoodsForm;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.request.SaveGoodsLineRequest;
import com.yiling.goods.medicine.dto.request.SaveOrUpdateGoodsRequest;
import com.yiling.goods.medicine.enums.GoodsErrorCode;
import com.yiling.goods.medicine.enums.GoodsOutReasonEnum;
import com.yiling.goods.medicine.enums.GoodsSourceEnum;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterprisePlatformDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2021/5/28
 */
@Component
@Slf4j
public class ImportGoodsDataHandler implements ImportDataHandler<ImportGoodsForm> {

    @DubboReference(timeout = 1000*60)
    GoodsApi      goodsApi;
    @DubboReference
    EnterpriseApi enterpriseApi;

    @Override
    public List<ImportGoodsForm> execute(List<ImportGoodsForm> object, Map<String,Object> paramMap) {
        for (ImportGoodsForm importGoodsForm : object) {
            try {
                SaveOrUpdateGoodsRequest saveOrUpdateGoodsRequest = PojoUtils.map(importGoodsForm, SaveOrUpdateGoodsRequest.class);
                saveOrUpdateGoodsRequest.setOpUserId((Long) paramMap.getOrDefault(MyMetaHandler.FIELD_OP_USER_ID,0));
                saveOrUpdateGoodsRequest.setSource(GoodsSourceEnum.IMPORT.getCode());
                saveOrUpdateGoodsRequest.setCanSplit(0);
                saveOrUpdateGoodsRequest.setId(importGoodsForm.getGid());
                GoodsDTO goodsDTO = goodsApi.queryInfo(importGoodsForm.getGid());
                saveOrUpdateGoodsRequest.setEid(goodsDTO.getEid());
                EnterprisePlatformDTO enterprisePlatformDTO = enterpriseApi.getEnterprisePlatform(goodsDTO.getEid());
                if (enterprisePlatformDTO.getMallFlag() == 1) {
                    SaveGoodsLineRequest goodsLineInfo = new SaveGoodsLineRequest();
                    goodsLineInfo.setMallFlag(1);
                    saveOrUpdateGoodsRequest.setGoodsLineInfo(goodsLineInfo);
                    if(GoodsStatusEnum.UN_SHELF.getCode().equals(importGoodsForm.getGoodsStatus())){
                        saveOrUpdateGoodsRequest.setOutReason(GoodsOutReasonEnum.QUALITY_CONTROL.getCode());
                    }
                    Long gid = goodsApi.saveGoodsByErp(saveOrUpdateGoodsRequest);
                    if (gid <= 0) {
                        importGoodsForm.setErrorMsg(ResultCode.EXCEL_DATA_SAVING_FAILED.getMessage());
                    }
                }else{
                    importGoodsForm.setErrorMsg(GoodsErrorCode.GOODS_NOT_SET_LINE.getMessage());
                }
            } catch (BusinessException e) {
                importGoodsForm.setErrorMsg(e.getMessage());
                log.error("供应商商品导入数据库报错", e);
            } catch (Exception e) {
                importGoodsForm.setErrorMsg(ResultCode.EXCEL_DATA_SAVING_FAILED.getMessage());
                log.error("数据保存出错：{}", e.getMessage(), e);
            }
        }
        return object;
    }
}
