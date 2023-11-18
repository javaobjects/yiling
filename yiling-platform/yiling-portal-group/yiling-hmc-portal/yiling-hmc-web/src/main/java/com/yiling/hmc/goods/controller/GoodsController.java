package com.yiling.hmc.goods.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsFullDTO;
import com.yiling.goods.standard.dto.StandardGoodsPicDTO;
import com.yiling.hmc.config.HmcWebProperties;
import com.yiling.hmc.goods.vo.GoodsVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: hongyang.zhang
 * @data: 2023/02/21
 */
@RestController
@RequestMapping("/goods")
@Api(tags = "药品信息")
@Slf4j
public class GoodsController extends BaseController {


    @DubboReference
    GoodsApi goodsApi;

    @Autowired
    FileService fileService;

    @Autowired
    HmcWebProperties hmcWebProperties;

    @ApiOperation(value = "获取详情")
    @GetMapping("/goodsInfo")
    public Result<GoodsVO> queryMarketOrder() {
        GoodsFullDTO goodsFullDTO = goodsApi.queryFullInfo(hmcWebProperties.getGoodsId());
        GoodsVO vo = PojoUtils.map(goodsFullDTO, GoodsVO.class);
        List<StandardGoodsPicDTO> picBasicsInfoList = goodsFullDTO.getStandardGoodsAllInfo().getPicBasicsInfoList();
        picBasicsInfoList.forEach(e -> {
            e.setPic(fileService.getUrl(e.getPic(), FileTypeEnum.GOODS_PICTURE));
        });
        List<String> collect = picBasicsInfoList.stream().map(StandardGoodsPicDTO::getPic).collect(Collectors.toList());
        vo.setPicBasicsInfoList(collect);
        String indications = goodsFullDTO.getStandardGoodsAllInfo().getGoodsInstructionsInfo().getIndications();
        vo.setIndications(indications);
        vo.setPic(fileService.getUrl(vo.getPic(), FileTypeEnum.GOODS_PICTURE));

        return Result.success(vo);
    }
}
