package com.yiling.goods.ylprice;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.goods.BaseTest;
import com.yiling.goods.ylprice.dto.GoodsYilingPriceDTO;
import com.yiling.goods.ylprice.dto.request.QueryReportPricePageRequest;
import com.yiling.goods.ylprice.entity.GoodsYilingPriceDO;
import com.yiling.goods.ylprice.service.GoodsYilingPriceService;

import cn.hutool.core.date.DateUtil;

/**
 * @author: shuang.zhang
 * @date: 2021/5/19
 */
public class GoodsYiLingPriceServiceTest extends BaseTest {

    @Autowired
    private GoodsYilingPriceService goodsYilingPriceService;


    @Test
    public void listReportPricePageTest(){
        QueryReportPricePageRequest queryReportPricePageRequest = new QueryReportPricePageRequest();
        queryReportPricePageRequest.setParamId(3L);
        queryReportPricePageRequest.setSize(100);
        queryReportPricePageRequest.setCurrent(1);
        Page<GoodsYilingPriceDTO> reportPriceDTOPage = goodsYilingPriceService.listReportPricePage(queryReportPricePageRequest);
        System.out.println(reportPriceDTOPage.getRecords().toString());
    }

    @Test
    public void getPriceBySpecificationIdListTest() {
        List<Long> specificationIdList = Arrays.asList(41145L, 1327L, 454L, 3466L, 16467L, 494L, 10321L);
        Long paramId = 3L;
        Date date = DateUtil.parse(DateUtil.today());
        List<GoodsYilingPriceDO> goodsYilingPriceDOList = goodsYilingPriceService.getPriceBySpecificationIdList(specificationIdList, paramId, date);

        System.out.println("goodsYilingPriceDOList.size = " + goodsYilingPriceDOList.size());
    }

}
