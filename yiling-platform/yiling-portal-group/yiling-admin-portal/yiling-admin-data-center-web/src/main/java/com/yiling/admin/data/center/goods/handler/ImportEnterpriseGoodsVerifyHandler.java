package com.yiling.admin.data.center.goods.handler;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.yiling.admin.data.center.goods.form.ImportEnterpriseGoodsForm;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.user.enterprise.api.EnterpriseApi;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;

/**
 * 自定义验证(我们会在这里做唯一性的验证)
 *
 * @author shuan
 */
@Component
public class ImportEnterpriseGoodsVerifyHandler implements IExcelVerifyHandler<ImportEnterpriseGoodsForm> {

    @DubboReference
    GoodsApi goodsApi;

    @DubboReference
    PopGoodsApi popGoodsApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @Override
    public ExcelVerifyHandlerResult verifyHandler(ImportEnterpriseGoodsForm obj) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);

        //判断企业ID是否存在
//        if (obj.getEid() != null && obj.getEid() != 0) {
//            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(obj.getEid());
//            if (enterpriseDTO == null) {
//                result.setSuccess(false);
//                result.setMsg("企业信息不存在");
//                return result;
//            }
//        }
        //判断商品ID是否存在
//        if (obj.getGid() != null && obj.getGid() != 0) {
//            GoodsInfoDTO goodsInfoDTO = popGoodsApi.queryInfo(obj.getGid());
//            if (goodsInfoDTO == null) {
//                result.setSuccess(false);
//                result.setMsg("商品信息不存在");
//                return result;
//            }
//        }
        return result;
    }

}
