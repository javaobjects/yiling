package com.yiling.admin.data.center.goods.handler;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.yiling.export.excel.handler.ImportDataHandler;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.yiling.admin.data.center.goods.form.ImportEnterpriseGoodsForm;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.request.SaveGoodsLineRequest;
import com.yiling.goods.medicine.dto.request.SaveOrUpdateGoodsRequest;
import com.yiling.goods.medicine.enums.GoodsErrorCode;
import com.yiling.goods.medicine.enums.GoodsSourceEnum;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterprisePlatformDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2021/5/28
 */
@Component
@Slf4j
public class ImportEnterpriseGoodsDataHandler implements ImportDataHandler<ImportEnterpriseGoodsForm> {

    @DubboReference
    GoodsApi goodsApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @Override
    public List<ImportEnterpriseGoodsForm> execute(List<ImportEnterpriseGoodsForm> object, Map<String,Object> paramMap) {
        List<Long> eidList = object.stream().map(ImportEnterpriseGoodsForm::getEid).distinct().collect(Collectors.toList());
        Map<Long, EnterpriseDTO> enterpriseDTOMap = enterpriseApi.getMapByIds(eidList);
        Map<Long, EnterprisePlatformDTO> platformDTOMap = enterpriseApi.getEnterprisePlatforms(eidList).stream().collect(Collectors.toMap(EnterprisePlatformDTO::getEid, Function.identity(), (k1, k2) -> k2));
        List<Long> popEidList=enterpriseApi.listSubEids(Constants.YILING_EID);
        for (ImportEnterpriseGoodsForm importGoodsForm : object) {
            try {
                SaveOrUpdateGoodsRequest saveOrUpdateGoodsRequest = PojoUtils.map(importGoodsForm, SaveOrUpdateGoodsRequest.class);
                saveOrUpdateGoodsRequest.setOpUserId((Long) paramMap.getOrDefault(MyMetaHandler.FIELD_OP_USER_ID,0));
                saveOrUpdateGoodsRequest.setSource(GoodsSourceEnum.IMPORT.getCode());
                EnterpriseDTO enterpriseDTO=enterpriseDTOMap.get(importGoodsForm.getEid());
                //获取商家所开通的产品线
                EnterprisePlatformDTO enterprisePlatformDTO=platformDTOMap.get(importGoodsForm.getEid());

                if(null==enterpriseDTO){
                    importGoodsForm.setErrorMsg("企业信息不存在");
                }else if(null==enterprisePlatformDTO){
                    importGoodsForm.setErrorMsg(GoodsErrorCode.GOODS_NOT_SET_LINE.getMessage());
                }else {
                    saveOrUpdateGoodsRequest.setEname(enterpriseDTO.getName());
                    saveOrUpdateGoodsRequest.setGoodsStatus(GoodsStatusEnum.UP_SHELF.getCode());
                    SaveGoodsLineRequest goodsLineInfo = new SaveGoodsLineRequest();
                    goodsLineInfo.setPopFlag(enterprisePlatformDTO.getPopFlag());
                    goodsLineInfo.setMallFlag(enterprisePlatformDTO.getMallFlag());
                    saveOrUpdateGoodsRequest.setGoodsLineInfo(goodsLineInfo);
                    saveOrUpdateGoodsRequest.setPopEidList(popEidList);
                    Long gid = goodsApi.saveGoods(saveOrUpdateGoodsRequest);
                    if (gid <= 0) {
                        importGoodsForm.setErrorMsg(ResultCode.EXCEL_DATA_SAVING_FAILED.getMessage());
                    }
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
