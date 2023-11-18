package com.yiling.hmc.goods;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.hutool.json.JSONUtil;
import com.yiling.hmc.enterprise.dto.request.SyncGoodsSaveDetailRequest;
import com.yiling.hmc.enterprise.dto.request.SyncGoodsSaveRequest;
import com.yiling.hmc.goods.dto.SyncGoodsDTO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.hmc.BaseTest;
import com.yiling.hmc.enterprise.dto.request.GoodsSaveListRequest;
import com.yiling.hmc.enterprise.dto.request.GoodsSaveRequest;
import com.yiling.hmc.goods.bo.EnterpriseGoodsCountBO;
import com.yiling.hmc.goods.bo.HmcGoodsBO;
import com.yiling.hmc.goods.dto.HmcGoodsDTO;
import com.yiling.hmc.goods.dto.request.HmcGoodsPageRequest;
import com.yiling.hmc.goods.dto.request.QueryHmcGoodsRequest;
import com.yiling.hmc.goods.enums.GoodsStatusEnum;
import com.yiling.hmc.goods.service.GoodsService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 GoodsServiceTest
 * @描述
 * @创建时间 2022/4/2
 * @修改人 shichen
 * @修改时间 2022/4/2
 **/
@Slf4j
public class GoodsServiceTest extends BaseTest {
    @Autowired
    private GoodsService goodsService;

    @Test
    public void sysTest() {
        SyncGoodsSaveRequest request = new SyncGoodsSaveRequest();
        List<SyncGoodsSaveDetailRequest> detailList = new ArrayList<>();
        SyncGoodsSaveDetailRequest detailRequest = new SyncGoodsSaveDetailRequest();
        detailRequest.setIhEid(8L);
        detailRequest.setIhPharmacyGoodsId(2943L);
        detailRequest.setIhCPlatformId(2939L);
        detailRequest.setSellSpecificationsId(41621L);
        detailRequest.setStandardId(171120L);
        detailList.add(detailRequest);
        request.setDetailList(detailList);
        List<SyncGoodsDTO> syncGoodsDTOS = goodsService.syncGoodsToHmc(request);
        log.info(JSONUtil.toJsonPrettyStr(syncGoodsDTOS));
    }

    @Test
    public void saveGoodsTest() {
        GoodsSaveListRequest request = new GoodsSaveListRequest();
        request.setEid(35L);
        request.setEname("武汉光谷企业天地有限公司");
        List<GoodsSaveRequest> list = Lists.newArrayList();
        GoodsSaveRequest saveRequest = new GoodsSaveRequest();
        saveRequest.setEnterpriseType(2);
//        saveRequest.setInsurancePrice(new BigDecimal("99.99"));
        saveRequest.setStandardId(52L);
        saveRequest.setSellSpecificationsId(41087L);
        saveRequest.setGoodsName("野牡丹止痢片");
        list.add(saveRequest);
        request.setGoodsRequest(list);
        goodsService.saveGoodsList(request);
    }

    @Test
    public void queryGoodsTest() {
        QueryHmcGoodsRequest request1 = new QueryHmcGoodsRequest();
        request1.setSellSpecificationsId(1394L);
        request1.setGoodsStatus(GoodsStatusEnum.UP.getCode());
        List<HmcGoodsDTO> list1 = goodsService.findBySpecificationsId(request1);
        HmcGoodsPageRequest pageRequest1 = new HmcGoodsPageRequest();
        pageRequest1.setEid(35L);
        pageRequest1.setCurrent(1);
        pageRequest1.setSize(10);
        Page<HmcGoodsBO> page1 = goodsService.pageListByEid(pageRequest1);
        List<HmcGoodsBO> list2 = goodsService.batchQueryGoodsInfo(Lists.newArrayList(1L));
        List<EnterpriseGoodsCountBO> list3 = goodsService.countGoodsByEids(Lists.newArrayList(35L));
        log.info("list1:{},list2:{},list3:{},page1:{}", list1, list2, list3, page1);
    }


}
