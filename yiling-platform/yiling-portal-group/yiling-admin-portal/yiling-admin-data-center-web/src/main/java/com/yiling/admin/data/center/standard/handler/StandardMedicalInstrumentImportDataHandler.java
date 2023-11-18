package com.yiling.admin.data.center.standard.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yiling.export.excel.handler.ImportDataHandler;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.yiling.admin.data.center.standard.form.StandardMedicalInstrumentImportExcelForm;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.standard.api.StandardGoodsApi;
import com.yiling.goods.standard.dto.request.StandardMedicalInstrumentImportExcelRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 StandardMedicalInstrumentImportDataHandler
 * @描述
 * @创建时间 2022/8/10
 * @修改人 shichen
 * @修改时间 2022/8/10
 **/
@Component
@Slf4j
public class StandardMedicalInstrumentImportDataHandler implements ImportDataHandler<StandardMedicalInstrumentImportExcelForm> {
    @DubboReference
    StandardGoodsApi standardGoodsApi;

    @Override
    public List<StandardMedicalInstrumentImportExcelForm> execute(List<StandardMedicalInstrumentImportExcelForm> object, Map<String, Object> paramMap) {
        List<StandardMedicalInstrumentImportExcelForm> ids=new ArrayList<>();
        for (StandardMedicalInstrumentImportExcelForm one:object) {
            try {
                StandardMedicalInstrumentImportExcelRequest request = PojoUtils.map(one, StandardMedicalInstrumentImportExcelRequest.class);
                request.setOpUserId((Long) paramMap.getOrDefault(MyMetaHandler.FIELD_OP_USER_ID,0));
                Long gid = standardGoodsApi.saveStandardMedicalInstrumentAllInfoOne(request);
                if (gid <= 0) {
                    one.setErrorMsg("因逻辑错误不能导入数据");
                    ids.add(one);
                }
            }catch (Exception e){
                log.error("供应商商品导入数据库报错",e);
                one.setErrorMsg(e.getMessage());
                ids.add(one);
            }
        }
        return ids;
    }
}
