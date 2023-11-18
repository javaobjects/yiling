package com.yiling.open.cms.goods.controller;

import java.util.List;

import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.goods.standard.api.StandardGoodsSpecificationApi;
import com.yiling.goods.standard.dto.StandardGoodsDTO;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationDTO;
import com.yiling.open.cms.diagnosis.form.GetStandardGoodsForm;
import com.yiling.open.cms.diagnosis.form.GetStandardGoodsSpecificationForm;
import com.yiling.open.cms.diagnosis.vo.StandardGoodsSpecificationVO;
import com.yiling.open.cms.diagnosis.vo.StandardGoodsVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.goods.standard.api.StandardGoodsApi;
import com.yiling.goods.standard.dto.StandardGoodsInfoDTO;
import com.yiling.goods.standard.dto.request.StandardGoodsInfoRequest;
import com.yiling.open.cms.goods.form.QueryGoodsPageForm;
import com.yiling.open.cms.goods.vo.GoodsInfoVO;
import com.yiling.open.cms.goods.vo.StandardGoodsInfoVO;
import com.yiling.user.enterprise.api.EnterpriseApi;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;

/**
 * @author: hongyang.zhang
 * @data: 2022/06/07
 */
@Api(tags = "药品相关")
@RestController
@RequestMapping("/goods")
public class GoodsController extends BaseController {

    @DubboReference
    GoodsApi goodsApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @Autowired
    private FileService fileService;

    @DubboReference
    private StandardGoodsApi standardGoodsApi;

    @DubboReference
    StandardGoodsSpecificationApi standardGoodsSpecificationApi;


    @ApiOperation(value = "查询商品")
    @GetMapping("queryGoods")
    public Result<CollectionObject<GoodsInfoVO>> queryGoods(String key) {
        QueryGoodsPageListRequest request = new QueryGoodsPageListRequest();
        request.setAuditStatus(GoodsStatusEnum.AUDIT_PASS.getCode());
        request.setSize(100);
        request.setName(key);
        request.setEidList(enterpriseApi.listSubEids(Constants.YILING_EID));
        // request.setGoodsLine(GoodsLineEnum.B2B.getCode());
        Page<GoodsListItemBO> page = goodsApi.queryPageListGoods(request);
        List<GoodsInfoVO> list = PojoUtils.map(page.getRecords(), GoodsInfoVO.class);
        list.forEach(e -> {
            e.setPic(fileService.getUrl(e.getPic(), FileTypeEnum.GOODS_PICTURE));
        });
        CollectionObject<GoodsInfoVO> result = new CollectionObject(list);
        return Result.success(result);
    }


    @ApiOperation(value = "查询商品")
    @PostMapping("queryStandardGoods")
    public Result<Page<StandardGoodsInfoVO>> StandardGoods(@RequestBody QueryGoodsPageForm form) {
        StandardGoodsInfoRequest request = new StandardGoodsInfoRequest();
        request.setSize(form.getSize()).setCurrent(form.getCurrent());
        request.setName(form.getGoodsName());
        Page<StandardGoodsInfoDTO> standardGoodsInfo = standardGoodsApi.getStandardGoodsInfo(request);
        Page<StandardGoodsInfoVO> result = PojoUtils.map(standardGoodsInfo, StandardGoodsInfoVO.class);
        return Result.success(result);
    }

    @ApiOperation("根据标准库id获取商品信息")
    @PostMapping("getStandardGoodsByIds")
    @Log(title = "根据标准库id获取商品信息", businessType = BusinessTypeEnum.OTHER)
    public Result<List<StandardGoodsVO>> getStandardGoodsByIds(@RequestBody @Valid GetStandardGoodsForm form) {
        List<StandardGoodsDTO> standardGoodsByIds = standardGoodsApi.getStandardGoodsByIds(form.getIds());
        List<StandardGoodsVO> goodsVOS = PojoUtils.map(standardGoodsByIds, StandardGoodsVO.class);
        return Result.success(goodsVOS);
    }

    @ApiOperation("根据规格id获取商品规格信息")
    @PostMapping("getStandardGoodsSpecificationByIds")
    @Log(title = "根据规格id获取商品规格信息", businessType = BusinessTypeEnum.OTHER)
    public Result<List<StandardGoodsSpecificationVO>> getStandardGoodsSpecificationByIds(@RequestBody @Valid GetStandardGoodsSpecificationForm form) {
        List<StandardGoodsSpecificationDTO> listStandardGoodsSpecificationByIds = standardGoodsSpecificationApi.getListStandardGoodsSpecificationByIds(form.getIds());
        List<StandardGoodsSpecificationVO> goodsVOS = PojoUtils.map(listStandardGoodsSpecificationByIds, StandardGoodsSpecificationVO.class);
        return Result.success(goodsVOS);
    }
}
