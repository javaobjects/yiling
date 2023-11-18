package com.yiling.b2b.admin.goods.handler;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.yiling.export.excel.handler.ImportDataHandler;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.yiling.b2b.admin.goods.form.ImportLimitGoodsForm;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.util.Constants;
import com.yiling.goods.medicine.api.GoodsLimitPriceApi;
import com.yiling.goods.medicine.dto.request.AddOrDeleteCustomerGoodsLimitRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * 客户定价信息导入 处理类
 * @author: yuecheng.chen
 * @date: 2021/6/22
 */
@Component
@Slf4j
public class ImportLimitGoodsDataHandler implements ImportDataHandler<ImportLimitGoodsForm> {

    @DubboReference
    GoodsLimitPriceApi goodsLimitPriceApi;

    @Override
    public List<ImportLimitGoodsForm> execute(List<ImportLimitGoodsForm> object, Map<String,Object> paramMap) {
        for (ImportLimitGoodsForm form : object) {
            try {
                AddOrDeleteCustomerGoodsLimitRequest request = new AddOrDeleteCustomerGoodsLimitRequest();
                request.setOpUserId((Long) paramMap.getOrDefault(MyMetaHandler.FIELD_OP_USER_ID,0));
                request.setEid(Constants.YILING_EID);
                request.setCustomerEid((Long) paramMap.get("customerEid"));
                request.setGoodsIds(Arrays.asList(form.getGoodsId()));
                goodsLimitPriceApi.addCustomerGoodsLimitByCustomerEid(request);
            } catch (BusinessException e) {
                form.setErrorMsg(e.getMessage());
                log.error("商品定价导入数据库报错", e);
            } catch (Exception e) {
                form.setErrorMsg(ResultCode.EXCEL_DATA_SAVING_FAILED.getMessage());
                log.error("数据保存出错：{}", e.getMessage(), e);
            }
        }
        return object;
    }
}
