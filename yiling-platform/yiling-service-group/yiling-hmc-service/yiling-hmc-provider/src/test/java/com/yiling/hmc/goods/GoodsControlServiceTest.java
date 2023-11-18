package com.yiling.hmc.goods;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.yiling.hmc.order.mq.listener.HmcUpdateGoodsTagListener;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.hmc.BaseTest;
import com.yiling.hmc.control.dto.GoodsPurchaseControlDTO;
import com.yiling.hmc.control.service.GoodsPurchaseControlService;
import com.yiling.hmc.goods.bo.GoodsControlBO;
import com.yiling.hmc.goods.dto.GoodsControlDTO;
import com.yiling.hmc.goods.dto.request.GoodsControlPageRequest;
import com.yiling.hmc.goods.dto.request.GoodsControlSaveRequest;
import com.yiling.hmc.goods.service.GoodsControlService;

/**
 * @author shichen
 * @类名 GoodsControlServiceTest
 * @描述
 * @创建时间 2022/3/30
 * @修改人 shichen
 * @修改时间 2022/3/30
 **/
public class GoodsControlServiceTest extends BaseTest {

    @Autowired
    private GoodsControlService goodsControlService;

    @Autowired
    private GoodsPurchaseControlService goodsPurchaseControlService;

    @Autowired
    HmcUpdateGoodsTagListener tagListener;

    @Test
    public void testTag(){
        int standardId=52;
        MessageExt messageExt= new MessageExt();
        messageExt.setBody(String.valueOf(standardId).getBytes(StandardCharsets.UTF_8));
        tagListener.consume(messageExt,null);
    }

    @Test
    public void saveTest(){
        GoodsControlSaveRequest request = new GoodsControlSaveRequest();
        request.setSellSpecificationsId(41087L);
        request.setMarketPrice(new BigDecimal("109"));
        request.setInsurancePrice(new BigDecimal("99"));
        goodsControlService.saveOrUpdateGoodsControl(request);
    }

    @Test
    public void pageTest(){
        GoodsControlPageRequest request = new GoodsControlPageRequest();
        request.setCurrent(1);
        request.setSize(10);
        Page<GoodsControlBO> page = goodsControlService.pageList(request);
        System.out.println(page);
    }

    @Test
    public void batchTest(){
        List<GoodsControlDTO> goodsControlDTOS = goodsControlService.batchGetGoodsControlBySpecificationsIds(Lists.newArrayList(410L));
        System.out.println(goodsControlDTOS);
    }

    @Test
    public void queryGoodsPurchaseControlListBySpecificationsId(){
        List<Long> ids = Lists.newArrayList();
        ids.add(410L);
        List<GoodsPurchaseControlDTO> goodsPurchaseControlDTOS = goodsPurchaseControlService.queryControlListBySpecificationsId(ids);
        System.out.println(goodsPurchaseControlDTOS.toString());
    }

}
