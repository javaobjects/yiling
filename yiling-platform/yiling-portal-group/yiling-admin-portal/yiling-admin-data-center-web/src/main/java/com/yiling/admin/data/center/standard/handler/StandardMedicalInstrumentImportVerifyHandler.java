package com.yiling.admin.data.center.standard.handler;

import com.yiling.admin.data.center.standard.form.StandardMedicalInstrumentImportExcelForm;
import com.yiling.framework.common.util.SpringUtils;
import com.yiling.goods.standard.api.StandardGoodsApi;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;

/**
 * @author shichen
 * @类名 StandardMedicalInstrumentImportVerifyHandler
 * @描述
 * @创建时间 2022/8/10
 * @修改人 shichen
 * @修改时间 2022/8/10
 **/
public class StandardMedicalInstrumentImportVerifyHandler implements IExcelVerifyHandler<StandardMedicalInstrumentImportExcelForm> {
    @Override
    public ExcelVerifyHandlerResult verifyHandler(StandardMedicalInstrumentImportExcelForm obj) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);
        StandardGoodsApi standardGoodsApi = (StandardGoodsApi) SpringUtils.getBean(StandardGoodsApi.class);

        Long id = standardGoodsApi.getStandardIdByLicenseNoAndType(obj.getLicenseNo(), 7);
        if (id != null) {
            result.setSuccess(false);
            result.setMsg("标准库商品信息已经存在");
            return result;
        }
        return result;
    }
}
