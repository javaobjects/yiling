package com.yiling.b2b.admin.goods.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.util.ExeclImportUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.b2b.admin.goods.form.DeleteGoodsPriceLimitForm;
import com.yiling.b2b.admin.goods.form.ImportLimitGoodsForm;
import com.yiling.b2b.admin.goods.form.QueryB2bGoodsPageListForm;
import com.yiling.b2b.admin.goods.form.QueryCustomerPageListForm;
import com.yiling.b2b.admin.goods.form.QueryGoodsLimitPageListForm;
import com.yiling.b2b.admin.goods.form.QueryGoodsPriceLimitPageListForm;
import com.yiling.b2b.admin.goods.form.SaveGoodsPriceLimitForm;
import com.yiling.b2b.admin.goods.form.SaveOrDeleteGoodsLimitForm;
import com.yiling.b2b.admin.goods.form.UpdateCustomerLimitForm;
import com.yiling.b2b.admin.goods.form.UpdateCustomerRecommendationForm;
import com.yiling.b2b.admin.goods.form.UpdateGoodsPriceLimitForm;
import com.yiling.b2b.admin.goods.handler.ImportLimitGoodsDataHandler;
import com.yiling.b2b.admin.goods.handler.ImportLimitGoodsVerifyHandler;
import com.yiling.b2b.admin.goods.vo.CustomerLimitPriceListItemVO;
import com.yiling.b2b.admin.goods.vo.GoodsListItemPageVO;
import com.yiling.b2b.admin.goods.vo.GoodsListItemVO;
import com.yiling.b2b.admin.goods.vo.GoodsPriceLimitPageVO;
import com.yiling.b2b.admin.goods.vo.GoodsPriceLimitVO;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.common.web.goods.utils.PictureUrlUtils;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.vo.ImportResultVO;
import com.yiling.goods.medicine.api.B2bGoodsApi;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.GoodsLimitPriceApi;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.dto.CustomerPriceLimitDTO;
import com.yiling.goods.medicine.dto.request.AddOrDeleteCustomerGoodsLimitRequest;
import com.yiling.goods.medicine.dto.request.BatchAddCustomerGoodsLimitRequest;
import com.yiling.goods.medicine.dto.request.DeleteGoodsPriceLimitRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsLimitPageListRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsPriceLimitPageRequest;
import com.yiling.goods.medicine.dto.request.QueryLimitFlagRequest;
import com.yiling.goods.medicine.dto.request.SaveOrUpdateGoodsPriceLimitRequest;
import com.yiling.goods.medicine.dto.request.UpdateCustomerLimitRequest;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.user.control.api.ControlApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.EnterpriseTagApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseTagDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2021/10/21
 */
@RestController
@RequestMapping("limit/price")
@Api(tags = "控价模块接口")
@Slf4j
public class GoodsLimitPriceController {

    @DubboReference
    private ControlApi                  controlApi;
    @DubboReference
    private EnterpriseApi               enterpriseApi;
    @DubboReference
    private GoodsLimitPriceApi          goodsLimitPriceApi;
    @DubboReference
    private UserApi                       userApi;
    @DubboReference
    private GoodsApi                      goodsApi;
    @DubboReference
    private LocationApi                   locationApi;
    @DubboReference
    private EnterpriseTagApi              enterpriseTagApi;
    @DubboReference
    private B2bGoodsApi                   b2bGoodsApi;
    @Autowired
    private PictureUrlUtils               pictureUrlUtils;
    @Autowired
    private ImportLimitGoodsDataHandler   importLimitGoodsDataHandler;
    @Autowired
    private ImportLimitGoodsVerifyHandler importLimitGoodsVerifyHandler;

    @ApiOperation(value = "b2b后台控价模块-控价列表")
    @PostMapping("/getCustomerList")
    public Result<Page<CustomerLimitPriceListItemVO>> getCustomerList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody QueryCustomerPageListForm form) {
        QueryEnterprisePageListRequest request=PojoUtils.map(form, QueryEnterprisePageListRequest.class);
        Page<CustomerLimitPriceListItemVO> newPage=new Page<>();
        if(StrUtil.isNotEmpty(form.getTagName())){
            List<Long> eidList = enterpriseTagApi.getEidListByTagsName(form.getTagName(), true);
            if(CollUtil.isNotEmpty(eidList)){
                request.setIds(eidList);
            }else{
                return Result.success(newPage);
            }
        }
        request.setMallFlag(1);
        request.setInTypeList(Arrays.asList(EnterpriseTypeEnum.BUSINESS.getCode()));
        Page<EnterpriseDTO> page = enterpriseApi.pageList(request);
        if (CollUtil.isNotEmpty(page.getRecords())) {
            List<Long> customerEids = page.getRecords().stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
            //获取客户信息
            List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByIds(customerEids);
            Map<Long, EnterpriseDTO> enterpriseDtoMap = enterpriseDTOList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, Function.identity()));

            //获取客户是否管控
            QueryLimitFlagRequest limitFlagRequest = new QueryLimitFlagRequest();
            limitFlagRequest.setEid(staffInfo.getCurrentEid());
            limitFlagRequest.setCustomerEids(customerEids);
            List<CustomerPriceLimitDTO> customerPriceLimitDTOList = goodsLimitPriceApi.getCustomerLimitFlagByEidAndCustomerEid(limitFlagRequest);
            Map<Long, CustomerPriceLimitDTO> flagMap = customerPriceLimitDTOList.stream().collect(Collectors.toMap(CustomerPriceLimitDTO::getCustomerEid, Function.identity()));

            //获取用户信息
            List<Long> userIds = customerPriceLimitDTOList.stream().map(e -> e.getUpdateUser()).collect(Collectors.toList());
            Map<Long, UserDTO> userDTOMap = MapUtil.newHashMap();
            if (CollUtil.isNotEmpty(userIds)) {
                List<UserDTO> userDTOList = userApi.listByIds(userIds);
                userDTOMap = userDTOList.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));
            }
            //客户标签设置
            Map<Long, List<EnterpriseTagDTO>> enterpriseTagMap = enterpriseTagApi.listByEidList(customerEids);

            Map<Long, UserDTO> finalUserDTOMap = userDTOMap;
            List<CustomerLimitPriceListItemVO> list=new ArrayList<>();
            page.getRecords().forEach(e -> {
                CustomerLimitPriceListItemVO customerLimitPriceListItemVO=new CustomerLimitPriceListItemVO();
                EnterpriseDTO customerEnterpriseDTO = enterpriseDtoMap.get(e.getId());
                if (customerEnterpriseDTO != null) {
                    customerLimitPriceListItemVO.setCustomerEid(e.getId());
                    customerLimitPriceListItemVO.setCustomerName(customerEnterpriseDTO.getName());
                    customerLimitPriceListItemVO.setCustomerType(EnterpriseTypeEnum.getByCode(customerEnterpriseDTO.getType()).getName());
                    customerLimitPriceListItemVO.setContactor(customerEnterpriseDTO.getContactor());
                    customerLimitPriceListItemVO.setContactorPhone(customerEnterpriseDTO.getContactorPhone());
                    customerLimitPriceListItemVO.setAddress(new StringJoiner(" ")
                            .add(customerEnterpriseDTO.getProvinceName())
                            .add(customerEnterpriseDTO.getCityName())
                            .add(customerEnterpriseDTO.getRegionName())
                            .add(customerEnterpriseDTO.getAddress())
                            .toString());
                }

                if (flagMap.containsKey(customerLimitPriceListItemVO.getCustomerEid())) {
                    if (flagMap.get(customerLimitPriceListItemVO.getCustomerEid()).getLimitFlag() == 1) {
                        customerLimitPriceListItemVO.setLimitFlag(1);
                    } else {
                        customerLimitPriceListItemVO.setLimitFlag(0);
                    }
                    if (flagMap.get(customerLimitPriceListItemVO.getCustomerEid()).getRecommendationFlag() == 1) {
                        customerLimitPriceListItemVO.setRecommendationFlag(1);
                    } else {
                        customerLimitPriceListItemVO.setRecommendationFlag(0);
                    }
                    UserDTO userDTO = finalUserDTOMap.get(flagMap.get(customerLimitPriceListItemVO.getCustomerEid()).getUpdateUser());
                    if (userDTO != null) {
                        customerLimitPriceListItemVO.setOperatorName(userDTO.getName());
                        customerLimitPriceListItemVO.setOperatorTime(flagMap.get(customerLimitPriceListItemVO.getCustomerEid()).getUpdateTime());
                    }
                } else {
                    customerLimitPriceListItemVO.setLimitFlag(0);
                    customerLimitPriceListItemVO.setRecommendationFlag(0);
                }

                List<EnterpriseTagDTO> enterpriseTagDTOList = enterpriseTagMap.get(customerLimitPriceListItemVO.getCustomerEid());
                if (CollUtil.isNotEmpty(enterpriseTagDTOList)) {
                    List<String> nameList = enterpriseTagDTOList.stream().map(t -> t.getName()).collect(Collectors.toList());
                    customerLimitPriceListItemVO.setLabelNameList(nameList);
                }
                list.add(customerLimitPriceListItemVO);
            });
            newPage.setRecords(list);
            newPage.setSize(page.getSize());
            newPage.setCurrent(page.getCurrent());
            newPage.setTotal(page.getTotal());
        }
        return Result.success(newPage);
    }

    @ApiOperation(value = "b2b后台控价模块-开启或者关闭控价")
    @PostMapping("/updateCustomerLimit")
    @Log(title = "控价开启或者关闭",businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> updateCustomerLimit(@CurrentUser CurrentStaffInfo
                                                       staffInfo, @RequestBody @Valid UpdateCustomerLimitForm form) {
        UpdateCustomerLimitRequest request = PojoUtils.map(form, UpdateCustomerLimitRequest.class);
        request.setCustomerEids(form.getCustomerEids());
        request.setEid(staffInfo.getCurrentEid());
        request.setOpUserId(staffInfo.getCurrentUserId());
        return Result.success(goodsLimitPriceApi.updateCustomerLimitByEidAndCustomerEid(request));
    }

    @ApiOperation(value = "b2b后台控价模块-开启或者关闭推荐")
    @PostMapping("/updateCustomerRecommendation")
    public Result<Boolean> updateCustomerRecommendation(@CurrentUser CurrentStaffInfo
                                                       staffInfo, @RequestBody @Valid UpdateCustomerRecommendationForm form) {
        UpdateCustomerLimitRequest request = PojoUtils.map(form, UpdateCustomerLimitRequest.class);
        request.setCustomerEids(form.getCustomerEids());
        request.setEid(staffInfo.getCurrentEid());
        request.setOpUserId(staffInfo.getCurrentUserId());
        return Result.success(goodsLimitPriceApi.updateCustomerLimitByEidAndCustomerEid(request));
    }

    @ApiOperation(value = "b2b后台控价模块-客户控价商品列表")
    @PostMapping("/goodsList")
    public Result<Page<GoodsListItemVO>> goodsList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryGoodsLimitPageListForm form) {
        QueryGoodsLimitPageListRequest request = PojoUtils.map(form, QueryGoodsLimitPageListRequest.class);
        Page<GoodsListItemBO> page = goodsLimitPriceApi.pageLimitList(request);
        if (page != null) {
            GoodsListItemPageVO<GoodsListItemVO> newPage = new GoodsListItemPageVO();
            List<GoodsListItemVO> goodsListItemVOList = PojoUtils.map(page.getRecords(), GoodsListItemVO.class);
            goodsListItemVOList.forEach(e -> {
                e.setPicUrl(pictureUrlUtils.getGoodsPicUrl(e.getPic()));
            });
            newPage.setRecords(goodsListItemVOList);
            newPage.setSize(page.getSize());
            newPage.setCurrent(page.getCurrent());
            newPage.setTotal(page.getTotal());
            return Result.success(newPage);
        }
        return Result.failed(ResultCode.FAILED);
    }

    @ApiOperation(value = "b2b后台控价模块-添加商品控价")
    @PostMapping("/addGoodsLimit")
    @Log(title = "添加商品控价",businessType = BusinessTypeEnum.INSERT)
    public Result<Boolean> addGoodsLimit(@CurrentUser CurrentStaffInfo
                                                 staffInfo, @RequestBody @Valid SaveOrDeleteGoodsLimitForm form) {
        AddOrDeleteCustomerGoodsLimitRequest request = PojoUtils.map(form, AddOrDeleteCustomerGoodsLimitRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setEid(staffInfo.getCurrentEid());
        return Result.success(goodsLimitPriceApi.addCustomerGoodsLimitByCustomerEid(request));
    }

    @ApiOperation(value = "b2b后台控价模块-批量添加商品控价")
    @PostMapping("/batchAddGoodsLimit")
    @Log(title = "批量添加商品控价",businessType = BusinessTypeEnum.INSERT)
    public Result<Boolean> batchAddGoodsLimit(@CurrentUser CurrentStaffInfo
                                                 staffInfo, @RequestBody @Valid QueryB2bGoodsPageListForm form) {
        QueryGoodsPageListRequest request = PojoUtils.map(form, QueryGoodsPageListRequest.class);
        if (CollUtil.isNotEmpty(form.getEidList())) {
            request.setEidList(form.getEidList());
        } else {
            if (Constants.YILING_EID.equals(staffInfo.getCurrentEid())) {
                List<Long> list = enterpriseApi.listSubEids(staffInfo.getCurrentEid());
                request.setEidList(list);
            } else {
                request.setEidList(Arrays.asList(staffInfo.getCurrentEid()));
            }
        }
        request.setAuditStatus(GoodsStatusEnum.AUDIT_PASS.getCode());
        request.setName(form.getGoodsName());
        request.setOpUserId(staffInfo.getCurrentUserId());
        BatchAddCustomerGoodsLimitRequest batchRequest=PojoUtils.map(request,BatchAddCustomerGoodsLimitRequest.class);
        batchRequest.setCustomerEid(form.getCustomerEid());
        batchRequest.setEid(staffInfo.getCurrentEid());
        return Result.success(goodsLimitPriceApi.batchAddCustomerGoodsLimitByCustomerEid(batchRequest));
    }

    @ApiOperation(value = "b2b后台控价模块-限价商品导入")
    @PostMapping(value = "/importGoodsLimit",headers = "content-type=multipart/form-data")
    @Log(title = "限价商品导入",businessType = BusinessTypeEnum.IMPORT)
    public Result<ImportResultVO> importGoodsLimit(@RequestParam(value = "file", required = true) MultipartFile file,@RequestParam(value = "customerEid", required = true) Long customerEid, @CurrentUser CurrentStaffInfo
                                                 staffInfo) {
        ImportParams params = new ImportParams();
        params.setNeedSave(true);
        params.setSaveUrl(ExeclImportUtils.EXECL_PATH);
        params.setNeedVerify(true);
        params.setVerifyHandler(importLimitGoodsVerifyHandler);
        params.setKeyIndex(0);
        InputStream in;
        try {
            in = file.getInputStream();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.UPLOAD_FILE_FAILED);
        }

        ImportResultModel importResultModel;
        try {
            //包含了插入数据库失败的信息
            Long start = System.currentTimeMillis();
            Map<String,Object> paramMap=new HashMap<>();
            paramMap.put(MyMetaHandler.FIELD_OP_USER_ID,staffInfo.getCurrentUserId());
            paramMap.put("customerEid",customerEid);
            importResultModel = ExeclImportUtils.importExcelMore(in, ImportLimitGoodsForm.class, params, importLimitGoodsDataHandler, paramMap);
            log.info("限价商品导入耗时：{}", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.EXCEL_PARSING_ERROR);
        }
        return Result.success(PojoUtils.map(importResultModel, ImportResultVO.class));
    }

    @ApiOperation(value = "b2b后台控价模块-移除商品控价")
    @PostMapping("/deleteGoodsLimit")
    @Log(title = "移除商品控价",businessType = BusinessTypeEnum.DELETE)
    public Result<Boolean> deleteGoodsLimit(@CurrentUser CurrentStaffInfo
                                                    staffInfo, @RequestBody @Valid SaveOrDeleteGoodsLimitForm form) {
        AddOrDeleteCustomerGoodsLimitRequest request = PojoUtils.map(form, AddOrDeleteCustomerGoodsLimitRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setEid(staffInfo.getCurrentEid());
        return Result.success(goodsLimitPriceApi.deleteCustomerGoodsLimitByCustomerEid(request));
    }

    @ApiOperation(value = "b2b后台控价模块-商品限价条件列表")
    @PostMapping("/goodsPriceLimit")
    public Result<Page<GoodsPriceLimitPageVO>> goodsPriceLimit(@CurrentUser CurrentStaffInfo
                                                                       staffInfo, @RequestBody @Valid QueryGoodsPriceLimitPageListForm form) {
        QueryGoodsPriceLimitPageRequest request = PojoUtils.map(form, QueryGoodsPriceLimitPageRequest.class);
        Page<GoodsPriceLimitPageVO> page = PojoUtils.map(goodsLimitPriceApi.pageList(request), GoodsPriceLimitPageVO.class);
        if (CollUtil.isNotEmpty(page.getRecords())) {
            //获取用户信息
            List<Long> userIds = page.getRecords().stream().map(e -> e.getUpdateUser()).collect(Collectors.toList());
            List<UserDTO> userDTOList = userApi.listByIds(userIds);
            Map<Long, UserDTO> userDTOMap = userDTOList.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));

            page.getRecords().forEach(e -> {
                e.setUpdateUserName(userDTOMap.get(e.getUpdateUser()).getName());
                String[] names = locationApi.getNamesByCodes(e.getProvinceCode(), e.getCityCode(), e.getRegionCode());
                String customerTypeDesc="";
                if(e.getCustomerType() == null||e.getCustomerType() == 0){
                    customerTypeDesc="全部";
                }else{
                    customerTypeDesc=EnterpriseTypeEnum.getByCode(e.getCustomerType()).getName();
                }
                e.setDescribe(new StringJoiner("-")
                        .add(StrUtil.isEmpty(names[0]) ? "全部" : names[0])
                        .add(StrUtil.isEmpty(names[1]) ? "全部" : names[1])
                        .add(StrUtil.isEmpty(names[2]) ? "全部" : names[2])
                        .add(customerTypeDesc)
                        .toString());
            });
        }
        return Result.success(page);
    }


    @ApiOperation(value = "b2b后台控价模块-添加商品控价条件")
    @PostMapping("/addGoodsPriceLimit")
    @Log(title = "添加商品控价条件",businessType = BusinessTypeEnum.INSERT)
    public Result<Boolean> addGoodsPriceLimit(@CurrentUser CurrentStaffInfo
                                                      staffInfo, @RequestBody @Valid SaveGoodsPriceLimitForm form) {
        SaveOrUpdateGoodsPriceLimitRequest request = PojoUtils.map(form, SaveOrUpdateGoodsPriceLimitRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        return Result.success(goodsLimitPriceApi.addGoodsPriceLimit(request));
    }

    @ApiOperation(value = " b2b后台控价模块-商品控价条件详情")
    @GetMapping("/getGoodsPriceLimit")
    public Result<GoodsPriceLimitVO> getGoodsPriceLimit(@CurrentUser CurrentStaffInfo
                                                                staffInfo, @RequestParam Long id) {
        return Result.success(PojoUtils.map(goodsLimitPriceApi.getGoodsPriceLimitById(id), GoodsPriceLimitVO.class));
    }

    @ApiOperation(value = "b2b后台控价模块-移除商品控价条件")
    @PostMapping("/deleteGoodsPriceLimit")
    @Log(title = "移除商品控价条件",businessType = BusinessTypeEnum.DELETE)
    public Result<Boolean> deleteGoodsPriceLimit(@CurrentUser CurrentStaffInfo
                                                         staffInfo, @RequestBody @Valid DeleteGoodsPriceLimitForm form) {
        DeleteGoodsPriceLimitRequest request = PojoUtils.map(form, DeleteGoodsPriceLimitRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        return Result.success(goodsLimitPriceApi.deleteGoodsPriceLimit(request));
    }

    @ApiOperation(value = "b2b后台控价模块-编辑商品控价条件")
    @PostMapping("/updateGoodsPriceLimit")
    @Log(title = "编辑商品控价条件",businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> updateGoodsPriceLimit(@CurrentUser CurrentStaffInfo
                                                         staffInfo, @RequestBody @Valid UpdateGoodsPriceLimitForm form) {
        SaveOrUpdateGoodsPriceLimitRequest request = PojoUtils.map(form, SaveOrUpdateGoodsPriceLimitRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        return Result.success(goodsLimitPriceApi.updateGoodsPriceLimit(request));
    }

}
