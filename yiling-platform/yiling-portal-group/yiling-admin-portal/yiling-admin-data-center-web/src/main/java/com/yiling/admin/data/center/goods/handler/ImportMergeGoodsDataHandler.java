package com.yiling.admin.data.center.goods.handler;

import java.util.List;
import java.util.Map;

import com.yiling.export.excel.handler.ImportDataHandler;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.yiling.admin.data.center.goods.form.ImportMergeGoodsForm;
import com.yiling.dataflow.statistics.api.FlowBalanceStatisticsApi;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.MergeGoodsRequest;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2021/5/28
 */
@Component
@Slf4j
public class ImportMergeGoodsDataHandler implements ImportDataHandler<ImportMergeGoodsForm> {

    @DubboReference
    GoodsApi goodsApi;

    @DubboReference
    FlowBalanceStatisticsApi flowBalanceStatisticsApi;

    @Override
    public List<ImportMergeGoodsForm> execute(List<ImportMergeGoodsForm> object, Map<String,Object> paramMap) {
        for (ImportMergeGoodsForm importMergeGoodsForm : object) {
            try {
                MergeGoodsRequest mergeGoodsRequest = PojoUtils.map(importMergeGoodsForm, MergeGoodsRequest.class);
                mergeGoodsRequest.setOpUserId((Long) paramMap.getOrDefault(MyMetaHandler.FIELD_OP_USER_ID,0));
                String msg;
                if(flowBalanceStatisticsApi.isUsedSpecificationId(mergeGoodsRequest.getSpecificationsId())){
                    msg = "包装规格已绑定流向数据无法合并";
                }else {
                    msg=goodsApi.goodsMerge(mergeGoodsRequest);
                }
                if(StrUtil.isNotEmpty(msg)){
                    importMergeGoodsForm.setErrorMsg(msg);
                }
            } catch (BusinessException e) {
                importMergeGoodsForm.setErrorMsg(e.getMessage());
                log.error("供应商商品导入数据库报错", e);
            } catch (Exception e) {
                importMergeGoodsForm.setErrorMsg(ResultCode.EXCEL_DATA_SAVING_FAILED.getMessage());
                log.error("数据保存出错：{}", e.getMessage(), e);
            }
        }
        return object;
    }
}
