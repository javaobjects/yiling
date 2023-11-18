package com.yiling.bi.goods;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.yiling.bi.BaseTest;
import com.yiling.bi.goods.dto.InputGoodsRelationShipDTO;
import com.yiling.bi.goods.service.InputGoodsRelationShipService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: shuang.zhang
 * @date: 2022/9/21
 */
@Slf4j
public class InputGoodsRelationShipTest extends BaseTest {

    @Autowired
    private InputGoodsRelationShipService inputGoodsRelationShipService;

    @Test
    public void test() {
        List<InputGoodsRelationShipDTO> list=inputGoodsRelationShipService.getInputGoodsRelationShipAll();

        Map<String, String> goodsIdMap = list.stream().collect(Collectors.toMap(InputGoodsRelationShipDTO::getCrmGoodsid, InputGoodsRelationShipDTO::getB2bGoodsid, (a, b) -> a));
        System.out.println(JSON.toJSONString(list));

    }
}
