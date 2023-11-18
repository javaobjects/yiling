package com.yiling.admin.b2b.common.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.framework.common.base.BaseController;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.user.enterprise.api.EnterpriseApi;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

/**
 * 精选药品查询
 *
 * @author: yong.zhang
 * @date: 2021/10/29
 */
@Slf4j
@Api(tags = "精选药品接口")
@RestController
@RequestMapping("/hotGoods")
public class HotGoodsController extends BaseController {
    @DubboReference
    GoodsApi      goodsApi;
    @DubboReference
    EnterpriseApi enterpriseApi;

//    @ApiOperation(value = "app首页精选药品查询")
//    @GetMapping("/queryHotGoods")
//    public Result<Page<HotGoodsVO>> query(@CurrentUser CurrentAdminInfo staffInfo) {
//        List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByParentId(Constants.YILING_EID);
//        List<Long> eidList = enterpriseDTOList.stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
//        QueryGoodsPageListRequest request = new QueryGoodsPageListRequest();
//        request.setEidList(eidList);
//        request.setGoodsStatus(1);
//        request.setAuditStatus(1);
//        Page<GoodsListItemBO> goodsListItemBOPage = goodsApi.queryB2bGoodsPageList(request);
//        // todo 在售商家数量
//        Page<HotGoodsVO> goodsVOPage = PojoUtils.map(goodsListItemBOPage, HotGoodsVO.class);
//        return Result.success(goodsVOPage);
//    }
}
