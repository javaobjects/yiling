package com.yiling.basic.location.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.yiling.basic.location.bo.IPLocationBO;
import com.yiling.basic.location.enums.IPLocationDataSourceEnum;
import com.yiling.basic.location.service.IPLocationQueryService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 百度IP归属地查询实现类
 *
 * @author: xuan.zhou
 * @date: 2022/10/18
 */
@Slf4j
@Service("baiduIPLocationQueryService")
public class BaiduIPLocationQueryServiceImpl implements IPLocationQueryService {

    private String url = "https://sp1.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php";

    @Value("${ip.location.verify.ip}")
    private String ip;
    @Value("${ip.location.verify.result.baidu}")
    private String result;

    @Override
    public IPLocationBO query(String ip) {
        Map<String, Object> params = new HashMap<>();
        params.put("query", ip);
        params.put("resource_id", "5809");
        params.put("ie", "utf8");
        params.put("oe", "gbk");
        params.put("format", "json");
        params.put("tn", "baidu");
        params.put("t", DateUtil.currentSeconds());

        String jsonStr = HttpUtil.post(url, params);
        if (JSONUtil.isJson(jsonStr)) {
            Result result = JSONUtil.toBean(jsonStr, Result.class);
            if (result.getStatus() == 0 && result.getResultCode() == 0) {
                List<Data> dataList = result.getData();
                if (CollUtil.isNotEmpty(dataList)) {
                    Data data = dataList.get(0);
                    String location = data.getLocation();
                    if (StrUtil.isNotEmpty(location)) {
                        String[] array = location.split(" ");

                        IPLocationBO ipLocationBO = new IPLocationBO(ip, array[0], array.length > 1 ? array[1] : "", IPLocationDataSourceEnum.BAIDU);
                        return ipLocationBO;
                    }
                }
            }
        }

        return null;
    }

    /**
     * 调用查询方法验证IP归属地接口是否正确
     *
     * @return
     */
    @Override
    public Boolean verify() {
        IPLocationBO ipLocationBO = this.query(ip);
        return ipLocationBO.toString().equals(result);
    }

    @lombok.Data
    private static class Result {
        private Integer status;
        private Integer ResultCode;
        private List<BaiduIPLocationQueryServiceImpl.Data> data;
    }

    @lombok.Data
    private static class Data {
        private String location;
    }
}
