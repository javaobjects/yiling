package com.yiling.b2b.admin.goods.handler;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.yiling.b2b.admin.goods.form.ImportGoodsGroupPriceForm;
import com.yiling.goods.medicine.api.B2bGoodsApi;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.user.enterprise.api.CustomerGroupApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseCustomerGroupDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;

/**
 * 商品定价自定义验证(我们会在这里做唯一性的验证)
 *
 * @author yuecheng.chen
 * @date: 2021/6/22
 */
@Component
public class ImportGoodsGroupPriceVerifyHandler implements IExcelVerifyHandler<ImportGoodsGroupPriceForm> {

    @DubboReference
    GoodsApi         goodsApi;
    @DubboReference
    B2bGoodsApi      b2bGoodsApi;
    @DubboReference
    EnterpriseApi    enterpriseApi;
    @DubboReference
    CustomerGroupApi customerGroupApi;

    @Override
    public ExcelVerifyHandlerResult verifyHandler(ImportGoodsGroupPriceForm obj) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);
        // 具体价格与浮动点位也是唯一都有值报错
        if (obj.getSpecificPriceValue() != null && obj.getFloatPriceValue() != null) {
            result.setSuccess(false);
            result.setMsg("具体价格与浮动价格只能填写一个！");
            return result;
        }
        // 判断企业ID是否存在
        if (obj.getEid() != null && obj.getEid() != 0) {
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(obj.getEid());
            if (enterpriseDTO == null) {
                result.setSuccess(false);
                result.setMsg("企业信息不存在");
                return result;
            }
        }
        // 判断商品ID是否存在
        if (obj.getGoodsId() != null && obj.getGoodsId() != 0) {
            GoodsInfoDTO goodsInfoDTO = b2bGoodsApi.queryInfo(obj.getGoodsId());
            if (goodsInfoDTO == null) {
                result.setSuccess(false);
                result.setMsg("商品信息不存在");
                return result;
            }
        }

        // 判断客户分组是否存在
        if (obj.getCustomerGroupId() != null && obj.getCustomerGroupId() != 0) {
            EnterpriseCustomerGroupDTO groupDTO = customerGroupApi.getById(obj.getCustomerGroupId());
            if (groupDTO == null) {
                result.setSuccess(false);
                result.setMsg("客户分组信息不存在");
                return result;
            }
        }
        return result;
    }

}
