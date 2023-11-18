package com.yiling.open.order;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.open.BaseTest;
import com.yiling.open.erp.enums.OpenErrorCode;
import com.yiling.open.erp.util.OpenConstants;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2021/9/28
 */
@Slf4j
public class OrderAttachmentServiceTest extends BaseTest {

    @Autowired
    private FileService fileService;

    @Test
    public void fileKeyRequestTest() {
        Result<String> result = getByFileKeyListTest();
        System.out.println(">>>>> result: " + JSON.toJSONString(result));
    }

    public Result<String> getByFileKeyListTest(){
        String body = "{\"fileKeyList\":[\"dev/orderContract/2021/09/24/7089bd53dcbe4772ac9cc3c34bb57977.png\",\"dev/orderReceiveOneReceipt/2021/09/28/802eaf8be97c4d9083a754ebea5e6da8.png\"]}";

        if (StringUtils.isBlank(body)) {
            return Result.failed(OpenErrorCode.Parameter_Parameter.getCode(), "参数" + OpenConstants.DATA_PARAM + "不存在");
        }

        JSONObject jsonBody = JSON.parseObject(body);
        if (CollectionUtils.isEmpty(jsonBody)) {
            return Result.failed(OpenErrorCode.Parameter_Body_Null.getCode(), OpenConstants.DATA_PARAM + "值为空");
        }

        Map<String,String> orderContractMap = new HashMap<>();
        try {
            // 订单销售合同key
            JSONArray fileKeyList = jsonBody.getJSONArray("fileKeyList");
            if(ObjectUtil.isNull(fileKeyList) || fileKeyList.size() == 0){
                return Result.failed(OpenErrorCode.Parameter_Parameter.getCode(), "参数" + " fileKeyList 不存在");
            }

            for (Object fileKeyObject : fileKeyList) {
                String fileKey = (String) fileKeyObject;
                // 查询订单销售合同
                String url = fileService.getUrl(fileKey, FileTypeEnum.ORDER_SALES_CONTRACT);
                if(!orderContractMap.containsKey(fileKey)){
                    orderContractMap.put(fileKey, url);
                }
            }
        } catch (Exception e) {
            log.error("获取订单销售合同接口报错", e);
            return Result.failed(OpenErrorCode.Remote_Service_Error);
        }
        return Result.success(JSONArray.toJSONString(orderContractMap));
    }

    @Test
    public void fileKeyResponseTest() {
        String result = JSONObject.toJSONString(getByFileKeyListTest());
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONObject data = jsonObject.getJSONObject("data");
        if(ObjectUtil.isNotNull(data)){
            String file1 = data.getString("dev/orderContract/2021/09/24/7089bd53dcbe4772ac9cc3c34bb57977.png");
            String file2 = data.getString("dev/orderReceiveOneReceipt/2021/09/28/802eaf8be97c4d9083a754ebea5e6da8.png");

            System.out.println(">>>>> key1: " + file1);
            System.out.println(">>>>> key2: " + file2);
        }
    }

}
