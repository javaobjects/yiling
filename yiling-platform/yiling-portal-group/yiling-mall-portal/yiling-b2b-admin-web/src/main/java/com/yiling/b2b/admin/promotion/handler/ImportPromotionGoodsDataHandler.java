package com.yiling.b2b.admin.promotion.handler;

import java.util.List;
import java.util.Map;

import com.yiling.export.excel.handler.ImportDataHandler;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.yiling.b2b.admin.promotion.form.ImportPromotionGoodsForm;
import com.yiling.goods.medicine.api.B2bGoodsApi;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: yong.zhang
 * @date: 2021/11/24
 */
@Component
@Slf4j
public class ImportPromotionGoodsDataHandler implements IExcelVerifyHandler<ImportPromotionGoodsForm>, ImportDataHandler<ImportPromotionGoodsForm> {

    @DubboReference
    B2bGoodsApi b2bGoodsApi;

    @Override
    public ExcelVerifyHandlerResult verifyHandler(ImportPromotionGoodsForm form) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);

        //校验商品是否存在
        {
            Long goodsId = form.getGoodsId();
            if (goodsId != null && goodsId != 0L) {
                List<GoodsInfoDTO> goodsInfoList = b2bGoodsApi.batchQueryInfo(ListUtil.toList(goodsId));
                if (CollUtil.isEmpty(goodsInfoList)) {
                    result.setSuccess(false);
                    result.setMsg("商品ID错误");
                    return result;
                }
            }
        }

        return result;
    }

    @Override
    public List<ImportPromotionGoodsForm> execute(List<ImportPromotionGoodsForm> object, Map<String,Object> paramMap) {
        return object;
    }
}
