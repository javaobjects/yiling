package com.yiling.sjms.crm.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.api.CrmGoodsCategoryApi;
import com.yiling.dataflow.crm.api.CrmGoodsInfoApi;
import com.yiling.dataflow.crm.api.CrmGoodsTagApi;
import com.yiling.dataflow.crm.dto.CrmGoodsCategoryDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsInfoDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsTagDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsInfoPageRequest;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateCrmGoodsInfoRequest;
import com.yiling.dataflow.crm.enums.CrmGoodsStatusEnum;
import com.yiling.dataflow.crm.enums.CrmGoodsTagTypeEnum;
import com.yiling.dataflow.utils.BackupUtil;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.crm.form.QueryBakGoodsInfoPageForm;
import com.yiling.sjms.crm.form.QueryGoodsInfoPageForm;
import com.yiling.sjms.crm.form.SaveOrUpdateGoodsInfoForm;
import com.yiling.sjms.crm.vo.CrmGoodsInfoVO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 CrmGoodsController
 * @描述
 * @创建时间 2023/3/6
 * @修改人 shichen
 * @修改时间 2023/3/6
 **/
@Slf4j
@RestController
@RequestMapping("/crmGoods")
@Api(tags = "标准商品管理")
public class CrmGoodsController extends BaseController {

    @DubboReference
    private CrmGoodsInfoApi crmGoodsInfoApi;

    @DubboReference
    private CrmGoodsCategoryApi crmGoodsCategoryApi;

    @DubboReference
    private CrmGoodsTagApi crmGoodsTagApi;


    @ApiOperation(value = "通过code或者名称下拉查询标准商品信息", httpMethod = "GET")
    @GetMapping("/getGoodsByCodeOrName")
    public Result<List<CrmGoodsInfoVO>> getGoodsByCodeOrName(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "code",required = false)Long crmGoodsCode, @RequestParam(value = "name",required = false)String goodsName, @RequestParam(value = "showAll",required = false)Integer showAll){
        QueryCrmGoodsInfoPageRequest request = new QueryCrmGoodsInfoPageRequest();
        List<CrmGoodsInfoDTO> goodsInfoList = ListUtil.toList();
        if(null!= crmGoodsCode && crmGoodsCode >0){
            request.setGoodsCode(crmGoodsCode);
        }else if(StringUtils.isNotBlank(goodsName)){
            request.setGoodsName(goodsName);
            request.setSize(100);
        }else {
            return Result.success(ListUtil.empty());
        }
        if(null==showAll || showAll !=1 ){
            request.setStatus(CrmGoodsStatusEnum.NORMAL.getCode());
        }
        goodsInfoList = crmGoodsInfoApi.getPage(request).getRecords();
        List<CrmGoodsInfoVO> list = PojoUtils.map(goodsInfoList, CrmGoodsInfoVO.class);

        if(CollectionUtil.isNotEmpty(list)){
            List<Long> categoryIds = list.stream().filter(goods -> goods.getCategoryId() > 0).map(CrmGoodsInfoVO::getCategoryId).collect(Collectors.toList());
            List<CrmGoodsCategoryDTO> categoryList = crmGoodsCategoryApi.findByIds(categoryIds);
            if(CollectionUtil.isNotEmpty(categoryList)){
                Map<Long, String> categoryMap = categoryList.stream().collect(Collectors.toMap(CrmGoodsCategoryDTO::getId, CrmGoodsCategoryDTO::getName));
                list.forEach(goods->{
                    String category = categoryMap.getOrDefault(goods.getCategoryId(),"");
                    goods.setCategory(category);
                });
            }
        }
        return Result.success(list);
    }

    @ApiOperation(value = "通过code或者名称下拉查询标准商品备份信息", httpMethod = "POST")
    @PostMapping("/getBakGoodsByCodeOrName")
    public Result<List<CrmGoodsInfoVO>> getBakGoodsByCodeOrName(@CurrentUser CurrentSjmsUserInfo userInfo,@RequestBody QueryBakGoodsInfoPageForm form){
        QueryCrmGoodsInfoPageRequest request = new QueryCrmGoodsInfoPageRequest();
        request.setCurrent(1);
        List<CrmGoodsInfoDTO> goodsInfoList = ListUtil.toList();
        if(null==form.getDataDate()){
            Result.success(ListUtil.empty());
        }
        if(null!= form.getGoodsCode() && form.getGoodsCode() >0){
            request.setGoodsCode(form.getGoodsCode());
        }else if(StringUtils.isNotBlank(form.getGoodsName())){
            request.setGoodsName(form.getGoodsName());
            request.setSize(100);
        }else {
            return Result.success(ListUtil.empty());
        }
        int year = DateUtil.year(form.getDataDate());
        int month = DateUtil.month(form.getDataDate())+1;
        String tableSuffix = BackupUtil.generateTableSuffix(year, month);
        goodsInfoList = crmGoodsInfoApi.getBakPage(request,tableSuffix).getRecords();
        List<CrmGoodsInfoVO> list = PojoUtils.map(goodsInfoList, CrmGoodsInfoVO.class);
        return Result.success(list);
    }

    @ApiOperation(value = "分页查询商品", httpMethod = "POST")
    @PostMapping("/queryGoodsInfoPage")
    public Result<Page<CrmGoodsInfoVO>> queryGoodsInfoPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QueryGoodsInfoPageForm form){
        QueryCrmGoodsInfoPageRequest request = PojoUtils.map(form, QueryCrmGoodsInfoPageRequest.class);
        Page<CrmGoodsInfoDTO> page = crmGoodsInfoApi.getPage(request);
        Page<CrmGoodsInfoVO> voPage = PojoUtils.map(page, CrmGoodsInfoVO.class);
        if(CollectionUtil.isNotEmpty(voPage.getRecords())){
            List<Long> categoryIds = voPage.getRecords().stream().filter(goods -> goods.getCategoryId() > 0).map(CrmGoodsInfoVO::getCategoryId).collect(Collectors.toList());
            List<CrmGoodsCategoryDTO> categoryList = crmGoodsCategoryApi.findByIds(categoryIds);
            if(CollectionUtil.isNotEmpty(categoryList)){
                Map<Long, String> categoryMap = categoryList.stream().collect(Collectors.toMap(CrmGoodsCategoryDTO::getId, CrmGoodsCategoryDTO::getName));
                voPage.getRecords().forEach(goods->{
                    String category = categoryMap.getOrDefault(goods.getCategoryId(),"");
                    goods.setCategory(category);
                });
            }
        }
        return Result.success(voPage);
    }

    @ApiOperation(value = "id获取商品详情", httpMethod = "GET")
    @GetMapping("/getGoodsDetail")
    public Result<CrmGoodsInfoVO> getGoodsDetail(@CurrentUser CurrentSjmsUserInfo userInfo,@RequestParam("goodsId") Long goodsId){
        CrmGoodsInfoDTO goodsInfoDTO = crmGoodsInfoApi.findById(goodsId);
        if(null == goodsInfoDTO){
            return Result.failed("产品不存在");
        }
        CrmGoodsInfoVO goodsInfoVO = PojoUtils.map(goodsInfoDTO, CrmGoodsInfoVO.class);
        if(goodsInfoDTO.getCategoryId()>0){
            CrmGoodsCategoryDTO categoryDTO = crmGoodsCategoryApi.findById(goodsInfoDTO.getCategoryId());
            goodsInfoVO.setCategory(categoryDTO.getName());
        }
        List<CrmGoodsTagDTO> tags = crmGoodsTagApi.findTagByGoodsId(goodsId);
        Map<Integer, List<CrmGoodsTagDTO>> tagMap = tags.stream().collect(Collectors.groupingBy(CrmGoodsTagDTO::getType));
        List<Long> notLockTagIdList = tagMap.getOrDefault(CrmGoodsTagTypeEnum.NOT_LOCK.getType(), ListUtil.empty()).stream().map(CrmGoodsTagDTO::getId).collect(Collectors.toList());
        List<Long> groupPurchaseTagIdList = tagMap.getOrDefault(CrmGoodsTagTypeEnum.GROUP_PURCHASE.getType(), ListUtil.empty()).stream().map(CrmGoodsTagDTO::getId).collect(Collectors.toList());
        goodsInfoVO.setNotLockTagIdList(notLockTagIdList);
        goodsInfoVO.setGroupPurchaseTagIdList(groupPurchaseTagIdList);
        return Result.success(goodsInfoVO);
    }

    @ApiOperation(value = "新增商品", httpMethod = "POST")
    @PostMapping("/saveGoods")
    public Result saveGoods(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody SaveOrUpdateGoodsInfoForm form){
        SaveOrUpdateCrmGoodsInfoRequest request = PojoUtils.map(form, SaveOrUpdateCrmGoodsInfoRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        Long goodsId = crmGoodsInfoApi.saveGoodsInfo(request);
        List<Long> tagIds = ListUtil.toList();
        tagIds.addAll(form.getNotLockTagIdList());
        if(null != request.getIsGroupPurchase()
                && request.getIsGroupPurchase()==1
                && CollectionUtil.isNotEmpty(form.getGroupPurchaseTagIdList())){
            tagIds.addAll(form.getGroupPurchaseTagIdList());
        }
        if(CollectionUtil.isNotEmpty(tagIds)){
            crmGoodsTagApi.batchSaveTagsByGoods(tagIds,goodsId,userInfo.getCurrentUserId());
        }
        return Result.success(goodsId);
    }

    @ApiOperation(value = "编辑商品", httpMethod = "POST")
    @PostMapping("/editGoods")
    public Result editGoods(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody SaveOrUpdateGoodsInfoForm form){
        if(null==form.getId() || form.getId()==0){
            return Result.failed("产品id为空");
        }
        SaveOrUpdateCrmGoodsInfoRequest request = PojoUtils.map(form, SaveOrUpdateCrmGoodsInfoRequest.class);
        if(null==request.getCategoryId()){
            request.setCategoryId(0L);
        }
        request.setGoodsCode(null);
        request.setOpUserId(userInfo.getCurrentUserId());
        crmGoodsInfoApi.editGoodsInfo(request);
        List<Long> tagIds = ListUtil.toList();
        tagIds.addAll(form.getNotLockTagIdList());
        CrmGoodsInfoDTO resultDto = crmGoodsInfoApi.findById(form.getId());
        if(resultDto.getIsGroupPurchase() == 1){
            tagIds.addAll(form.getGroupPurchaseTagIdList());
        }
        crmGoodsTagApi.batchSaveTagsByGoods(tagIds,form.getId(),userInfo.getCurrentUserId());
        return Result.success(form.getId());
    }
}
