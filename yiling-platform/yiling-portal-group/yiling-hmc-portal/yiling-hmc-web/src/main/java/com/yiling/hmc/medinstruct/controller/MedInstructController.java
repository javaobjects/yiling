package com.yiling.hmc.medinstruct.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.medicine.api.GoodsHmcApi;
import com.yiling.goods.medicine.dto.StandardGoodsBasicDTO;
import com.yiling.goods.standard.api.StandardGoodsApi;
import com.yiling.goods.standard.dto.StandardGoodsAllInfoDTO;
import com.yiling.goods.standard.dto.StandardGoodsInfoDTO;
import com.yiling.goods.standard.dto.StandardGoodsPicDTO;
import com.yiling.goods.standard.dto.request.StandardGoodsInfoRequest;
import com.yiling.goods.standard.enums.StandardGoodsTypeEnum;
import com.yiling.hmc.medinstruct.form.StandardGoodsInfoDetailForm;
import com.yiling.hmc.medinstruct.form.StandardGoodsInfoForm;
import com.yiling.hmc.medinstruct.vo.StandardGoodsAllInfoVO;
import com.yiling.hmc.medinstruct.vo.StandardGoodsInfoVO;
import com.yiling.hmc.order.api.OrderApi;
import com.yiling.hmc.order.dto.OrderDTO;
import com.yiling.hmc.wechat.api.InsuranceFetchPlanDetailApi;
import com.yiling.hmc.wechat.dto.InsuranceFetchPlanDetailDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用药指导控制器
 *
 * @author fan.shen
 * @date 2022/4/14
 */
@Api(tags = "用药指导控制器")
@RestController
@RequestMapping("/med/instruct")
public class MedInstructController extends BaseController {

    @DubboReference
    StandardGoodsApi standardGoodsApi;

    @Autowired
    FileService fileService;

    @DubboReference
    OrderApi orderApi;

    @DubboReference
    InsuranceFetchPlanDetailApi fetchPlanDetailApi;

    @DubboReference
    GoodsHmcApi goodsHmcApi;

    @ApiOperation("药品列表")
    @PostMapping("/page_list")
    public Result<Page<StandardGoodsInfoVO>> pageList(@RequestBody StandardGoodsInfoForm form) {
        if (StrUtil.isBlank(form.getName()) && Objects.isNull(form.getOrderId())) {
            return Result.failed("请输入商品名称");
        }

        // 1、优先根据输入名称查询
        if (StrUtil.isNotBlank(form.getName())) {
            StandardGoodsInfoRequest request = PojoUtils.map(form, StandardGoodsInfoRequest.class);
            // 这里限定只查药品(处方 + 非处方)
            request.setGoodsType(StandardGoodsTypeEnum.GOODS_TYPE.getCode());
            Page<StandardGoodsInfoDTO> goodsInfo = standardGoodsApi.getStandardGoodsInfo(request);
            Page<StandardGoodsInfoVO> page = PojoUtils.map(goodsInfo, StandardGoodsInfoVO.class);

            if (CollUtil.isEmpty(page.getRecords())) {
                return Result.success(page);
            }

            page.getRecords().forEach(item -> {
                // 商品图片
                String url = fileService.getUrl(item.getPic(), FileTypeEnum.GOODS_PICTURE);
                item.setPic(url);
            });

            return Result.success(page);
        }

        Page<StandardGoodsInfoVO> page = new Page<>();

        // 2、如果订单id不为空 -> 查询订单下的药品
        if (Objects.nonNull(form.getOrderId())) {
            OrderDTO orderDTO = orderApi.queryById(form.getOrderId());

            if (Objects.isNull(orderDTO)) {
                return Result.failed("根据订单id未查询到订单信息");
            }

            List<InsuranceFetchPlanDetailDTO> fetchPlanDetailDTOList = fetchPlanDetailApi.getByInsuranceRecordId(orderDTO.getInsuranceRecordId());
            List<Long> goodsIdList = fetchPlanDetailDTOList.stream().map(InsuranceFetchPlanDetailDTO::getGoodsId).collect(Collectors.toList());

            List<StandardGoodsBasicDTO> standardGoodsBasicList = goodsHmcApi.batchQueryStandardGoodsBasic(goodsIdList);

            List<StandardGoodsInfoVO> goodsInfoVOS = standardGoodsBasicList.stream().map(item -> {
                StandardGoodsInfoVO standardGoodsInfoVO = new StandardGoodsInfoVO();
                standardGoodsInfoVO.setId(item.getStandardId());
                standardGoodsInfoVO.setName(item.getName());
                standardGoodsInfoVO.setLicenseNo(item.getLicenseNo());
                String url = fileService.getUrl(item.getPic(), FileTypeEnum.GOODS_PICTURE);
                standardGoodsInfoVO.setPic(url);
                return standardGoodsInfoVO;
            }).collect(Collectors.toList());

            page.setRecords(goodsInfoVOS);
        }

        return Result.success(page);

    }

    @ApiOperation("药品详情")
    @PostMapping("/med_detail")
    public Result<StandardGoodsAllInfoVO> medDetail(@RequestBody StandardGoodsInfoDetailForm form) {
        StandardGoodsAllInfoDTO dto = standardGoodsApi.getStandardGoodsById(form.getId());
        StandardGoodsAllInfoVO result = PojoUtils.map(dto, StandardGoodsAllInfoVO.class);
        List<StandardGoodsPicDTO> picList = dto.getPicBasicsInfoList();
        if (CollUtil.isNotEmpty(picList)) {
            Optional<StandardGoodsPicDTO> first = picList.stream().filter(item -> item.getPicDefault().equals(1)).findFirst();
            String pic;
            if (first.isPresent()) {
                pic = first.get().getPic();
            } else {
                pic = picList.get(0).getPic();
            }
            result.setPicUrl(fileService.getUrl(pic, FileTypeEnum.GOODS_PICTURE));
        }
        return Result.success(result);
    }

}
