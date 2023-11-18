package com.yiling.admin.hmc.insurance.controller;


import java.util.List;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.hmc.insurance.form.InsuranceDetailListForm;
import com.yiling.admin.hmc.insurance.vo.GoodsControllerListItemVO;
import com.yiling.admin.hmc.insurance.vo.GoodsDisableVO;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.goods.api.GoodsApi;
import com.yiling.hmc.goods.api.GoodsControlApi;
import com.yiling.hmc.goods.bo.GoodsControlBO;
import com.yiling.hmc.goods.dto.HmcGoodsDTO;
import com.yiling.hmc.insurance.api.InsuranceDetailApi;
import com.yiling.hmc.insurance.dto.InsuranceDetailDTO;
import com.yiling.hmc.insurance.dto.request.InsuranceDetailListRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 保险商品明细表 前端控制器
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-23
 */
@Api(tags = "保险商品明细接口")
@RestController
@RequestMapping("/insurance/detail")
public class InsuranceDetailController extends BaseController {

    @DubboReference
    InsuranceDetailApi insuranceDetailApi;

    @DubboReference
    GoodsControlApi goodsControlApi;

    @DubboReference
    GoodsApi goodsApi;

    @ApiOperation(value = "保险商品明细分页查询")
    @PostMapping("/pageList")
    public Result<Page<GoodsControllerListItemVO>> pageList(@RequestBody InsuranceDetailListForm form) {
        InsuranceDetailListRequest request = PojoUtils.map(form, InsuranceDetailListRequest.class);
        Page<InsuranceDetailDTO> detailDTOPage = insuranceDetailApi.pageByInsuranceNameAndStatus(request);
        List<Long> controlIdList = detailDTOPage.getRecords().stream().map(InsuranceDetailDTO::getControlId).collect(Collectors.toList());
        List<GoodsControlBO> goodsControlBOS = goodsControlApi.getGoodsControlInfoByIds(controlIdList);
        List<GoodsControllerListItemVO> goodsControlVOList = PojoUtils.map(goodsControlBOS, GoodsControllerListItemVO.class);
        Page<GoodsControllerListItemVO> voPage = PojoUtils.map(detailDTOPage, GoodsControllerListItemVO.class);
        voPage.setRecords(goodsControlVOList);

        // 返回的数据需要新增一个是否已经选中过的字段
        List<HmcGoodsDTO> hmcGoodsDTOList = goodsApi.listByEid(form.getEid());
        List<Long> longIdList = hmcGoodsDTOList.stream().map(HmcGoodsDTO::getSellSpecificationsId).collect(Collectors.toList());
        voPage.getRecords().forEach(e -> {
            GoodsDisableVO goodsDisableVO = new GoodsDisableVO();
            goodsDisableVO.setIsAllowSelect(0);
            if (longIdList.contains(e.getSellSpecificationsId())) {
                goodsDisableVO.setIsAllowSelect(1);
            }
            e.setGoodsDisableVO(goodsDisableVO);
        });
        return Result.success(voPage);
    }
}
