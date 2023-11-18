package com.yiling.admin.data.center.standard.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yiling.export.excel.handler.ImportDataHandler;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.yiling.admin.data.center.standard.form.StandardGoodsImportExcelForm;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.standard.api.StandardGoodsApi;
import com.yiling.goods.standard.dto.request.StandardGoodsImportExcelRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: wei.wang
 * @date: 2021/5/28
 */
@Component
@Slf4j
public class StandardGoodsImportDataHandler implements ImportDataHandler<StandardGoodsImportExcelForm> {
    @DubboReference
    StandardGoodsApi standardGoodsApi;

    @Override
    public List<StandardGoodsImportExcelForm> execute(List<StandardGoodsImportExcelForm> object, Map<String,Object> paramMap) {
        List<StandardGoodsImportExcelForm> ids=new ArrayList<>();
        for (StandardGoodsImportExcelForm one:object) {
            try {
                StandardGoodsImportExcelRequest request = PojoUtils.map(one, StandardGoodsImportExcelRequest.class);
                request.setOpUserId((Long) paramMap.getOrDefault(MyMetaHandler.FIELD_OP_USER_ID,0));
                Long gid = standardGoodsApi.saveStandardGoodAllInfoOne(request);
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
