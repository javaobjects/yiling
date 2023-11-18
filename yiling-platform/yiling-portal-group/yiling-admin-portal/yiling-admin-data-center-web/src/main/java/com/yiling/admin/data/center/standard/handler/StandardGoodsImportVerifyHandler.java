package com.yiling.admin.data.center.standard.handler;

import com.yiling.admin.data.center.standard.form.StandardGoodsImportExcelForm;
import com.yiling.framework.common.util.SpringUtils;
import com.yiling.goods.standard.api.StandardGoodsApi;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;

/**
 * 自定义验证(我们会在这里做唯一性的验证)
 *
 * @author:wei.wang
 * @date:2021/5/25
 */

public class StandardGoodsImportVerifyHandler implements IExcelVerifyHandler<StandardGoodsImportExcelForm> {

    @Override
    public ExcelVerifyHandlerResult verifyHandler(StandardGoodsImportExcelForm obj) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);
        StandardGoodsApi standardGoodsApi = (StandardGoodsApi) SpringUtils.getBean(StandardGoodsApi.class);

        Long id = standardGoodsApi.getStandardIdByLicenseNoAndType(obj.getLicenseNo(), 1);
        if (id != null) {
            result.setSuccess(false);
            result.setMsg("标准库商品信息已经存在");
            return result;
        }
        return result;
    }

}
