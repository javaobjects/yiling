package com.yiling.admin.data.center.standard.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.yiling.admin.data.center.standard.form.StandardDecotionImportExcelForm;
import com.yiling.admin.data.center.standard.form.StandardDispensingGranuleImportExcelForm;
import com.yiling.export.excel.handler.ImportDataHandler;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.standard.api.StandardGoodsApi;
import com.yiling.goods.standard.dto.request.StandardDecotionImportExcelRequest;
import com.yiling.goods.standard.dto.request.StandardDispensingGranuleImportExcelRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 StandardDispensingGranuleImportDataHandler
 * @描述
 * @创建时间 2023/5/17
 * @修改人 shichen
 * @修改时间 2023/5/17
 **/
@Component
@Slf4j
public class StandardDispensingGranuleImportDataHandler implements ImportDataHandler<StandardDispensingGranuleImportExcelForm> {

    @DubboReference
    StandardGoodsApi standardGoodsApi;

    @Override
    public List<StandardDispensingGranuleImportExcelForm> execute(List<StandardDispensingGranuleImportExcelForm> object, Map<String,Object> paramMap) {
        List<StandardDispensingGranuleImportExcelForm> ids=new ArrayList<>();
        for (StandardDispensingGranuleImportExcelForm one:object) {
            try {
                StandardDispensingGranuleImportExcelRequest request = PojoUtils.map(one, StandardDispensingGranuleImportExcelRequest.class);
                request.setOpUserId((Long) paramMap.getOrDefault(MyMetaHandler.FIELD_OP_USER_ID,0));
                Long gid = standardGoodsApi.saveStandardDispensingGranuleAllInfoOne(request);
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
