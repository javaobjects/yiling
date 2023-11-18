package com.yiling.basic.tianyancha.util;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yiling.basic.tianyancha.bo.TycResultBO;
import com.yiling.basic.tianyancha.dto.TycEnterpriseInfoDTO;
import com.yiling.basic.tianyancha.dto.TycResultDTO;
import com.yiling.basic.tianyancha.enums.TycErrorCode;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * @author shichen
 * @类名 TycHttpUtils
 * @描述
 * @创建时间 2022/1/11
 * @修改人 shichen
 * @修改时间 2022/1/11
 **/
@Slf4j
public class TycHttpUtils {

    public static <T> TycResultBO<T> get(String url, TypeReference<TycResultBO<T>> type, HashMap<String, Object> params, String token,Boolean tycSwitch){
        if(tycSwitch){
            String returnBody = HttpRequest.get(url)
                    .header("Authorization", token)
                    .form(params)
                    .timeout(10000).execute().body();
            return JSON.parseObject(returnBody, type);
        }else {
            TycResultBO<T> resultBO = new TycResultBO();
            resultBO.setError_code(TycErrorCode.CLOSE.getCode());
            resultBO.setReason(TycErrorCode.CLOSE.getMessage());
            return resultBO;
        }
    }
}
