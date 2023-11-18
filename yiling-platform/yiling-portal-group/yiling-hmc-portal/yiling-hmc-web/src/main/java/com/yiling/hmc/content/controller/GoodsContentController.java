package com.yiling.hmc.content.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.yiling.cms.content.enums.LineEnum;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.api.ContentApi;
import com.yiling.cms.content.dto.AppContentDTO;
import com.yiling.cms.content.dto.AppContentDetailDTO;
import com.yiling.cms.content.dto.request.QueryAppContentPageRequest;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.medicine.constant.GoodsConstant;
import com.yiling.goods.standard.api.StandardGoodsApi;
import com.yiling.goods.standard.dto.StandardGoodsAllInfoDTO;
import com.yiling.goods.standard.dto.StandardInstructionsGoodsDTO;
import com.yiling.hmc.config.HmcWebProperties;
import com.yiling.hmc.content.form.QueryContentPageForm;
import com.yiling.hmc.content.vo.ContentDetailVO;
import com.yiling.hmc.content.vo.ContentVO;
import com.yiling.hmc.remind.form.MedsScienceBaseForm;
import com.yiling.hmc.remind.vo.StandardGoodsVO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 内容
 *
 * @author: gxl
 * @date: 2022/3/28
 */
@Api(tags = "用药科普")
@RestController
@RequestMapping("/goods/content")
@Slf4j
public class GoodsContentController extends BaseController {

    @DubboReference
    StandardGoodsApi standardGoodsApi;

    @DubboReference
    ContentApi contentApi;

    @Autowired
    private FileService fileService;

    @Autowired
    HmcWebProperties hmcWebProperties;

    @ApiOperation(value = "01、药品科普")
    @PostMapping("list")
    @Log(title = "药品科普", businessType = BusinessTypeEnum.OTHER)
    public Result<List<StandardGoodsVO>> list() {
        List<StandardGoodsAllInfoDTO> standardGoodsInfoList = hmcWebProperties.getStandardIdList().stream().map(item -> standardGoodsApi.getStandardGoodsById(item)).collect(Collectors.toList());
        List<StandardGoodsVO> result = standardGoodsInfoList.stream().map(item -> {
            StandardGoodsVO goodsVO = StandardGoodsVO.builder().name(item.getBaseInfo().getName()).licenseNo(item.getBaseInfo().getLicenseNo()).build();
            goodsVO.setId(item.getBaseInfo().getId());
            if (CollUtil.isNotEmpty(item.getPicBasicsInfoList())) {
                goodsVO.setPicUrl(fileService.getUrl(item.getPicBasicsInfoList().get(0).getPic(), FileTypeEnum.GOODS_PICTURE));
            } else {
                goodsVO.setPicUrl(fileService.getUrl(GoodsConstant.GOODS_DEFAULT_PIC, FileTypeEnum.GOODS_PICTURE));
            }
            return goodsVO;
        }).collect(Collectors.toList());
        log.info("[list]药品科普返回值：{}", JSONUtil.toJsonStr(result));
        return Result.success(result);
    }

    @ApiOperation(value = "02、科普详情")
    @PostMapping("detail")
    @Log(title = "科普详情", businessType = BusinessTypeEnum.OTHER)
    public Result<StandardGoodsVO> detail(@RequestBody @Valid MedsScienceBaseForm form) {
        StandardGoodsAllInfoDTO standardGoods = standardGoodsApi.getStandardGoodsById(form.getId());
        StandardGoodsVO goodsVO = StandardGoodsVO.builder().name(standardGoods.getBaseInfo().getName()).licenseNo(standardGoods.getBaseInfo().getLicenseNo()).build();
        if (CollUtil.isNotEmpty(standardGoods.getPicBasicsInfoList())) {
            goodsVO.setPicUrl(fileService.getUrl(standardGoods.getPicBasicsInfoList().get(0).getPic(), FileTypeEnum.GOODS_PICTURE));
        } else {
            goodsVO.setPicUrl(fileService.getUrl(GoodsConstant.GOODS_DEFAULT_PIC, FileTypeEnum.GOODS_PICTURE));
        }

        // 适应症
        goodsVO.setIndications(Optional.ofNullable(standardGoods.getGoodsInstructionsInfo()).map(StandardInstructionsGoodsDTO::getIndications).orElse(null));

        // 用法与用量
        goodsVO.setUsageDosage(Optional.ofNullable(standardGoods.getGoodsInstructionsInfo()).map(StandardInstructionsGoodsDTO::getUsageDosage).orElse(null));

        log.info("[detail]药品科普返回值：{}", JSONUtil.toJsonStr(goodsVO));
        return Result.success(goodsVO);
    }

    @ApiOperation("03.文献列表")
    @GetMapping("queryContentPage")
    @Log(title = "文献列表", businessType = BusinessTypeEnum.OTHER)
    public Result<Page<ContentVO>> queryContentPage(@Valid QueryContentPageForm queryContentPageForm) {
        QueryAppContentPageRequest request = new QueryAppContentPageRequest();
        PojoUtils.map(queryContentPageForm, request);
        //c端用户侧
        request.setLineId(1L);
        request.setIsOpen(1);
        request.setStandardGoodsId(request.getStandardGoodsId());
        Page<AppContentDTO> contentDTOPage = contentApi.listAppContentPageBySql(request);
        Page<ContentVO> contentVOPage = PojoUtils.map(contentDTOPage, ContentVO.class);
        if (contentVOPage.getTotal() == 0) {
            return Result.success(contentVOPage);
        }
        contentVOPage.getRecords().forEach(contentVO -> {
            if(StrUtil.isNotEmpty(contentVO.getCover())){
                contentVO.setCover(fileService.getUrl(contentVO.getCover(),FileTypeEnum.CONTENT_COVER));
            }
            if (StrUtil.isNotEmpty(contentVO.getVedioFileUrl())) {
                contentVO.setVedioFileUrl(fileService.getUrl(contentVO.getVedioFileUrl(), FileTypeEnum.VEDIO_CONTENT));
            }
        });
        return Result.success(contentVOPage);
    }

    @ApiOperation("04.文献详情")
    @GetMapping("getContentDetail")
    @Log(title = "文献详情", businessType = BusinessTypeEnum.OTHER)
    public Result<ContentDetailVO> getContentDetail(@RequestParam Long id) {
        AppContentDetailDTO contentDetail = contentApi.getContentDetail(id, LineEnum.HMC);
        ContentDetailVO contentDetailVO = PojoUtils.map(contentDetail, ContentDetailVO.class);
        if(StrUtil.isNotEmpty(contentDetail.getCover())){
            contentDetailVO.setCover(fileService.getUrl(contentDetail.getCover(),FileTypeEnum.CONTENT_COVER));
        }
        if (StrUtil.isNotEmpty(contentDetail.getVedioFileUrl())) {
            contentDetailVO.setVedioFileUrl(fileService.getUrl(contentDetail.getVedioFileUrl(), FileTypeEnum.VEDIO_CONTENT));
        }
        return Result.success(contentDetailVO);
    }
}