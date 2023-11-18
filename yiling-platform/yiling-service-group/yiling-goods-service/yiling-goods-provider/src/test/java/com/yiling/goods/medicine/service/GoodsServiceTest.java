package com.yiling.goods.medicine.service;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.yiling.goods.BaseTest;
import com.yiling.goods.inventory.dto.InventorySubscriptionDTO;
import com.yiling.goods.medicine.bo.EnterpriseGoodsCountBO;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.medicine.dto.request.SaveGoodsLineRequest;
import com.yiling.goods.medicine.dto.request.SaveOrUpdateGoodsRequest;
import com.yiling.goods.medicine.enums.GoodsSkuStatusEnum;

/**
 * @author: shuang.zhang
 * @date: 2021/5/19
 */
public class GoodsServiceTest extends BaseTest {

    @Autowired
    private B2bGoodsService b2bGoodsService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private PopGoodsService popGoodsService;
    @Autowired
    private GoodsSkuService goodsSkuService;

    @Test
    public void testSaveGoods() throws FileNotFoundException {
		SaveOrUpdateGoodsRequest request =new SaveOrUpdateGoodsRequest();
		request.setName("阿托伐他汀钙片(阿乐）");
		request.setAuditStatus(4);
		request.setCommonName("阿托伐他汀钙片(阿乐）");
		request.setLicenseNo("国药准字H19990258");
		request.setManufacturer("北京嘉林药业股份有限公司");
		request.setSellSpecificationsId(5L);
		request.setStandardId(3L);
		request.setCategoryId1(1L);
		request.setCategoryId2(6L);
		request.setIsCn(1);
		request.setOtcType(4);
		request.setIsYb(3);
		request.setPrice(new BigDecimal("18.9"));
		request.setQty(100L);
		request.setOpUserId(1L);
		Long id=goodsService.saveGoods(request);
		System.err.println(id);
    }


    @Test
    public void testB2bGoodsCount(){
        List<Long> eidList= Lists.newArrayList(222846L,222814L,202163L
                ,33474L,53L,27351L,27344L,2914L,7L);
        long start = System.currentTimeMillis();

        Map<Long,EnterpriseGoodsCountBO> count1 = b2bGoodsService.getGoodsCountByEidList(eidList);
        long end1 = System.currentTimeMillis();

        System.out.println("耗时1："+(end1-start));
        List<EnterpriseGoodsCountBO> count2=Lists.newArrayList();
        eidList.forEach(eid->{
            EnterpriseGoodsCountBO count = b2bGoodsService.getGoodsCountByEid(eid);
            count2.add(count);
        });
        long end2 = System.currentTimeMillis();
        System.out.println("耗时2："+(end2-end1));
        System.out.println(count1);
    }

    @Test
    public void testUpdateSkuStatus(){
        Boolean flag = goodsService.updateSkuStatusByEidAndInSn(35L, "", GoodsSkuStatusEnum.DISABLE.getCode(), 0L);
        System.out.println(flag);
    }

    @Test
    public void testErpGoodsSync(){
        SaveOrUpdateGoodsRequest request=new SaveOrUpdateGoodsRequest();
        request.setEid(7L);
        request.setPopEidList(Arrays.asList(2L,3L,4L));
        SaveGoodsLineRequest request1=new SaveGoodsLineRequest();
        request1.setPopFlag(1);
        request1.setMallFlag(1);
        request.setGoodsLineInfo(request1);
        request.setInSn("11111");
        request.setMiddlePackage(1L);
        request.setCanSplit(1);
        request.setGoodsStatus(1);
        request.setSource(3);
        request.setName("氯霉素片");
        request.setLicenseNo("国药准字H61021237");
        request.setManufacturer("陕西兴邦药业");
        request.setSpecifications("0.25g*100片");
        request.setOutReason(3);
        request.setPrice(new BigDecimal(10));
        goodsService.saveGoodsByErp(request);
    }

    @Test
    public void findPopGoodsBySpecId(){
        List<GoodsListItemBO> goods = popGoodsService.findGoodsBySpecificationIdAndEids(19999L, Lists.newArrayList(2L, 3L, 4L, 5L));
        System.out.println(goods);
    }

//    @Test
//    public void updateSubscriptionSkuQtyTest(){
//        List<GoodsSkuDTO> skuDTOList = goodsSkuService.getGoodsSkuByGoodsId(10049L);
//        List<InventorySubscriptionDTO> subscriptionList = goodsSkuService.getInventorySubscriptionBySku(skuDTOList);
//        System.out.println(skuDTOList);
//        System.out.println(subscriptionList);
//        List<InventorySubscriptionDTO> detailBySkuId = goodsService.getInventoryDetailBySkuId(280L);
//        System.out.println(detailBySkuId);
//    }
}
