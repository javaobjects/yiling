package com.yiling.b2b.admin.goods.handler;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.yiling.b2b.admin.goods.form.ImportLimitGoodsForm;
import com.yiling.framework.common.util.Constants;
import com.yiling.goods.medicine.api.B2bGoodsApi;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.user.enterprise.api.EnterpriseApi;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;

/**
 * 商品定价自定义验证(我们会在这里做唯一性的验证)
 *
 * @author yuecheng.chen
 * @date: 2021/6/22
 */
@Component
public class ImportLimitGoodsVerifyHandler implements IExcelVerifyHandler<ImportLimitGoodsForm> {

    @DubboReference
    B2bGoodsApi   b2bGoodsApi;
    @DubboReference
    EnterpriseApi enterpriseApi;

    @Override
    public ExcelVerifyHandlerResult verifyHandler(ImportLimitGoodsForm obj) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);
        // 判断商品ID是否存在
        if (obj.getGoodsId() != null && obj.getGoodsId() != 0) {
            GoodsInfoDTO goodsInfoDTO = b2bGoodsApi.queryInfo(obj.getGoodsId());
            List<Long> eids = enterpriseApi.listSubEids(Constants.YILING_EID);
            if (goodsInfoDTO == null) {
                result.setSuccess(false);
                result.setMsg("商品信息不存在");
                return result;
            }
            if (!eids.contains(goodsInfoDTO.getEid())) {
                result.setSuccess(false);
                result.setMsg("非以岭的商品");
                return result;
            }
        }
        return result;
    }

}
