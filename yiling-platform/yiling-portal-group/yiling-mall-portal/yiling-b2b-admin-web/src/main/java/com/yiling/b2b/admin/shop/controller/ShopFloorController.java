package com.yiling.b2b.admin.shop.controller;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.b2b.admin.goods.vo.GoodsSkuVO;
import com.yiling.b2b.admin.shop.form.QueryFloorGoodsPageForm;
import com.yiling.b2b.admin.shop.form.QueryShopFloorPageForm;
import com.yiling.b2b.admin.shop.form.SaveShopFloorForm;
import com.yiling.b2b.admin.shop.form.UpdateShopFloorStatusForm;
import com.yiling.b2b.admin.shop.vo.ShopFloorGoodsItemVO;
import com.yiling.b2b.admin.shop.vo.ShopFloorItemVO;
import com.yiling.b2b.admin.shop.vo.ShopFloorVO;
import com.yiling.common.web.goods.utils.PictureUrlUtils;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.goods.medicine.api.B2bGoodsApi;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.medicine.enums.GoodsLineEnum;
import com.yiling.user.shop.api.ShopFloorApi;
import com.yiling.user.shop.bo.ShopFloorBO;
import com.yiling.user.shop.dto.ShopFloorDTO;
import com.yiling.user.shop.dto.ShopFloorGoodsDTO;
import com.yiling.user.shop.dto.request.QueryFloorGoodsPageRequest;
import com.yiling.user.shop.dto.request.QueryShopFloorPageRequest;
import com.yiling.user.shop.dto.request.SaveShopFloorRequest;
import com.yiling.user.shop.dto.request.UpdateShopFloorStatusRequest;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 店铺楼层表 前端控制器
 * </p>
 *
 * @author lun.yu
 * @date 2023-02-20
 */
@RestController
@Api(tags = "店铺楼层接口")
@RequestMapping("/shopFloor")
public class ShopFloorController extends BaseController {

    @DubboReference
    ShopFloorApi shopFloorApi;
    @DubboReference
    B2bGoodsApi b2bGoodsApi;
    @DubboReference
    GoodsApi goodsApi;

    @Autowired
    PictureUrlUtils pictureUrlUtils;

    @ApiOperation(value = "查询楼层分页列表")
    @PostMapping("/queryListPage")
    public Result<Page<ShopFloorItemVO>> queryListPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryShopFloorPageForm form) {
        QueryShopFloorPageRequest request = PojoUtils.map(form, QueryShopFloorPageRequest.class);
        request.setEid(staffInfo.getCurrentEid());
        Page<ShopFloorBO> floorBOPage = shopFloorApi.queryListPage(request);
        return Result.success(PojoUtils.map(floorBOPage, ShopFloorItemVO.class));
    }

    @ApiOperation(value = "保存楼层")
    @PostMapping("/save")
    public Result<Boolean> save(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid SaveShopFloorForm form) {
        SaveShopFloorRequest request = PojoUtils.map(form, SaveShopFloorRequest.class);
        request.setEid(staffInfo.getCurrentEid());
        request.setOpUserId(staffInfo.getCurrentUserId());
        return Result.success(shopFloorApi.saveFloor(request));
    }

    @ApiOperation(value = "修改状态")
    @PostMapping("/updateStatus")
    public Result<Boolean> updateStatus(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid UpdateShopFloorStatusForm form) {
        UpdateShopFloorStatusRequest request = PojoUtils.map(form, UpdateShopFloorStatusRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        return Result.success(shopFloorApi.updateStatus(request));
    }

    @ApiOperation(value = "删除楼层")
    @GetMapping("/delete")
    public Result<Boolean> delete(@CurrentUser CurrentStaffInfo staffInfo, @ApiParam("ID") @RequestParam("id") Long id) {
        return Result.success(shopFloorApi.deleteFloor(id, staffInfo.getCurrentUserId()));
    }

    @ApiOperation(value = "查看楼层详情")
    @GetMapping("/get")
    public Result<ShopFloorVO> get(@CurrentUser CurrentStaffInfo staffInfo, @ApiParam("ID") @RequestParam("id") Long id) {
        ShopFloorDTO shopFloorDTO = shopFloorApi.get(id);
        return Result.success(PojoUtils.map(shopFloorDTO, ShopFloorVO.class));
    }

    @ApiOperation(value = "查看楼层商品列表")
    @GetMapping("/queryFloorGoodsList")
    public Result<CollectionObject<ShopFloorGoodsItemVO>> queryFloorGoodsList(@CurrentUser CurrentStaffInfo staffInfo, @ApiParam("楼层ID") @RequestParam("floorId") Long floorId) {
        List<ShopFloorGoodsDTO> floorGoodsDTOList = shopFloorApi.queryFloorGoodsList(floorId);

        List<ShopFloorGoodsItemVO> goodsItemVOList = PojoUtils.map(floorGoodsDTOList, ShopFloorGoodsItemVO.class);

        // 查询商品信息
        List<Long> goodsIdList = floorGoodsDTOList.stream().map(ShopFloorGoodsDTO::getGoodsId).distinct().collect(Collectors.toList());
        if (CollUtil.isEmpty(goodsIdList)) {
            return Result.success(new CollectionObject<>(goodsItemVOList));
        }
        List<GoodsInfoDTO> goodsInfoDTOList = b2bGoodsApi.batchQueryInfo(goodsIdList);
        Map<Long, GoodsInfoDTO> goodsInfoDTOMap = goodsInfoDTOList.stream().collect(Collectors.toMap(BaseDTO::getId, Function.identity()));

        // 根据商品ID获取销售包装信息
        List<GoodsSkuDTO> goodsSkuDTOList = goodsApi.getGoodsSkuByGoodsIds(goodsIdList);
        Map<Long, List<GoodsSkuDTO>> goodsSkuMap = goodsSkuDTOList.stream().filter(e -> e.getGoodsLine().equals(GoodsLineEnum.B2B.getCode())).collect(Collectors.groupingBy(GoodsSkuDTO::getGoodsId));

        goodsItemVOList.forEach(shopFloorGoodsItemVO -> {
            GoodsInfoDTO goodsInfoDTO = goodsInfoDTOMap.getOrDefault(shopFloorGoodsItemVO.getGoodsId(), new GoodsInfoDTO());
            shopFloorGoodsItemVO.setManufacturer(goodsInfoDTO.getManufacturer());
            shopFloorGoodsItemVO.setName(goodsInfoDTO.getName());
            shopFloorGoodsItemVO.setPrice(goodsInfoDTO.getPrice());
            shopFloorGoodsItemVO.setLicenseNo(goodsInfoDTO.getLicenseNo());
            shopFloorGoodsItemVO.setSellSpecifications(goodsInfoDTO.getSellSpecifications());
            shopFloorGoodsItemVO.setPic(pictureUrlUtils.getGoodsPicUrl(goodsInfoDTO.getPic()));
            shopFloorGoodsItemVO.setGoodsSkuList(PojoUtils.map(goodsSkuMap.get(shopFloorGoodsItemVO.getGoodsId()), GoodsSkuVO.class));
        });

        return Result.success(new CollectionObject<>(goodsItemVOList));
    }

}
