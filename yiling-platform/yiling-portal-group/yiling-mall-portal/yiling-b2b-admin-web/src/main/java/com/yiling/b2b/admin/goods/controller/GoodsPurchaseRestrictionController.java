package com.yiling.b2b.admin.goods.controller;

import java.util.Arrays;
import java.util.Date;
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
import com.yiling.b2b.admin.enterprisecustomer.vo.EnterpriseCustomerListItemVO;
import com.yiling.b2b.admin.goods.form.QueryGoodsPageListForm;
import com.yiling.b2b.admin.goods.form.SavePurchaseRestrictionForm;
import com.yiling.b2b.admin.goods.form.SaveRestrictionCustomerForm;
import com.yiling.b2b.admin.goods.vo.PurchaseRestrictionCustomerVO;
import com.yiling.b2b.admin.goods.vo.PurchaseRestrictionGoodsVO;
import com.yiling.b2b.admin.goods.vo.PurchaseRestrictionVO;
import com.yiling.b2b.admin.goods.form.QueryRestrictionCustomerPageForm;
import com.yiling.common.web.goods.utils.PictureUrlUtils;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.B2bGoodsApi;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.enums.GoodsLineEnum;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.goods.restriction.api.GoodsPurchaseRestrictionApi;
import com.yiling.goods.restriction.dto.GoodsPurchaseRestrictionCustomerDTO;
import com.yiling.goods.restriction.dto.GoodsPurchaseRestrictionDTO;
import com.yiling.goods.restriction.dto.request.DeleteRestrictionCustomerRequest;
import com.yiling.goods.restriction.dto.request.QueryRestrictionCustomerRequest;
import com.yiling.goods.restriction.dto.request.SavePurchaseRestrictionRequest;
import com.yiling.goods.restriction.dto.request.SaveRestrictionCustomerRequest;
import com.yiling.goods.restriction.enums.CustomerSettingTypeEnum;
import com.yiling.goods.restriction.enums.RestrictionTimeTypeEnum;
import com.yiling.user.common.enums.EnterpriseCustomerLineEnum;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.CustomerGroupApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseCustomerDTO;
import com.yiling.user.enterprise.dto.EnterpriseCustomerGroupDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryCustomerPageListRequest;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author shichen
 * @类名 GoodsPurchaseRestrictionController
 * @描述
 * @创建时间 2022/12/7
 * @修改人 shichen
 * @修改时间 2022/12/7
 **/
@RestController
@Api(tags = "商品限购模块")
@RequestMapping("goodsPurchaseRestriction")
public class GoodsPurchaseRestrictionController {
    @DubboReference
    GoodsApi goodsApi;

    @DubboReference
    B2bGoodsApi b2bGoodsApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference(timeout = 1000 * 10)
    CustomerApi customerApi;

    @DubboReference
    CustomerGroupApi customerGroupApi;

    @DubboReference
    GoodsPurchaseRestrictionApi goodsPurchaseRestrictionApi;

    @Autowired
    PictureUrlUtils pictureUrlUtils;

    @ApiOperation(value = "查询商品分页（含限购信息）", httpMethod = "POST")
    @PostMapping("/queryGoodsPage")
    public Result<Page<PurchaseRestrictionGoodsVO>> queryGoodsPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryGoodsPageListForm form){
        QueryGoodsPageListRequest request = PojoUtils.map(form, QueryGoodsPageListRequest.class);
        if(null==form.getGoodsStatus() || form.getGoodsStatus()==0){
            request.setGoodsStatus(null);
            request.setGoodsStatusList(ListUtil.toList(GoodsStatusEnum.UP_SHELF.getCode(),GoodsStatusEnum.UN_SHELF.getCode()));
        }
        request.setGoodsLine(GoodsLineEnum.B2B.getCode());
        if (Constants.YILING_EID.equals(staffInfo.getCurrentEid())) {
            List<Long> list = enterpriseApi.listSubEids(staffInfo.getCurrentEid());
            request.setEidList(list);
        } else {
            request.setEidList(Arrays.asList(staffInfo.getCurrentEid()));
        }
        Page<PurchaseRestrictionGoodsVO> page = PojoUtils.map(b2bGoodsApi.queryB2bGoodsPageList(request), PurchaseRestrictionGoodsVO.class);
        if(CollectionUtil.isEmpty(page.getRecords())){
            return Result.success(page);
        }
        List<Long> goodsIdList = page.getRecords().stream().map(PurchaseRestrictionGoodsVO::getId).collect(Collectors.toList());
        List<GoodsPurchaseRestrictionDTO> restrictionList = goodsPurchaseRestrictionApi.getPurchaseRestrictionByGoodsIds(goodsIdList);
        Map<Long, GoodsPurchaseRestrictionDTO> restrictionMap = restrictionList.stream().collect(Collectors.toMap(GoodsPurchaseRestrictionDTO::getGoodsId, Function.identity(),(e1,e2) -> e2));
        page.getRecords().forEach(goods->{
            goods.setPic(pictureUrlUtils.getGoodsPicUrl(goods.getPic()));
            GoodsPurchaseRestrictionDTO restriction = restrictionMap.get(goods.getId());
            if(null==restriction){
                goods.setOrderRestrictionQuantity(0L);
                goods.setTimeRestrictionQuantity(0L);
            }else {
                goods.setOrderRestrictionQuantity(restriction.getOrderRestrictionQuantity());
                goods.setTimeRestrictionQuantity(restriction.getTimeRestrictionQuantity());
                goods.setTimeType(restriction.getTimeType());
                goods.setStartTime(restriction.getStartTime());
                goods.setEndTime(restriction.getEndTime());
            }
        });
        return Result.success(page);
    }

    @ApiOperation(value = "获取商品限购规则", httpMethod = "GET")
    @GetMapping("/getPurchaseRestriction")
    public Result<PurchaseRestrictionGoodsVO> getGoodsPurchaseRestriction(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("goodsId") Long goodsId){
        if(null == goodsId){
            return Result.failed("商品id不存在");
        }
        GoodsDTO goodsDTO = goodsApi.queryInfo(goodsId);
        if(null == goodsDTO){
            return Result.failed("商品不存在");
        }
        PurchaseRestrictionGoodsVO restrictionGoodsVO = PojoUtils.map(goodsDTO, PurchaseRestrictionGoodsVO.class);
        if(null != restrictionGoodsVO){
            restrictionGoodsVO.setPic(pictureUrlUtils.getGoodsPicUrl(restrictionGoodsVO.getPic()));
        }
        //获取限购信息
        GoodsPurchaseRestrictionDTO restriction = goodsPurchaseRestrictionApi.getRestrictionByGoodsId(goodsId);
        PurchaseRestrictionVO restrictionVO = new PurchaseRestrictionVO();
        if(null==restriction){
            restrictionVO.setOrderRestrictionQuantity(0L);
            restrictionVO.setTimeRestrictionQuantity(0L);
            restrictionVO.setGoodsId(goodsId);
            restrictionVO.setTimeType(RestrictionTimeTypeEnum.EVERY_DAY.getType());
            restrictionVO.setCustomerSettingType(CustomerSettingTypeEnum.ALL.getType());
        }else {
            restrictionVO = PojoUtils.map(restriction, PurchaseRestrictionVO.class);
        }
        restrictionGoodsVO.setPurchaseRestrictionVO(restrictionVO);
        return Result.success(restrictionGoodsVO);
    }

    @ApiOperation(value = "查询限购客户分页", httpMethod = "POST")
    @PostMapping("/queryRestrictionCustomerPage")
    public Result<Page<PurchaseRestrictionCustomerVO>> queryRestrictionCustomerPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryRestrictionCustomerPageForm form){
        if(null == form.getGoodsId() || form.getGoodsId()==0){
            return Result.failed("查询客户缺少商品id");
        }
        List<Long> customerEids = goodsPurchaseRestrictionApi.getCustomerEidByGoodsId(form.getGoodsId());
        if(CollectionUtil.isEmpty(customerEids)){
            return Result.success(new Page());
        }
        QueryCustomerPageListRequest request = PojoUtils.map(form, QueryCustomerPageListRequest.class);
        request.setEids(ListUtil.toList(staffInfo.getCurrentEid()));
        request.setUseLine(EnterpriseCustomerLineEnum.B2B.getCode());
        request.setCustomerGroupId((request.getCustomerGroupId()!=null && request.getCustomerGroupId() == 0 ) ? null : request.getCustomerGroupId());
        request.setCustomerEids(customerEids);
        Page<PurchaseRestrictionCustomerVO> pageVO = PojoUtils.map(customerApi.pageList(request), PurchaseRestrictionCustomerVO.class);

        if(CollectionUtil.isNotEmpty(pageVO.getRecords())){
            List<Long> customerEidList = pageVO.getRecords().stream().map(EnterpriseCustomerListItemVO::getCustomerEid).collect(Collectors.toList());
            //企业信息
            List<EnterpriseDTO> list = enterpriseApi.listByIds(customerEidList);
            Map<Long, EnterpriseDTO> enterpriseDtoMap = list.stream().collect(Collectors.toMap(BaseDTO::getId, Function.identity()));
            //客户分组信息
            List<Long> customerGroupList = pageVO.getRecords().stream().map(EnterpriseCustomerListItemVO::getCustomerGroupId).collect(Collectors.toList());
            Map<Long, String> customerGroupMap;
            if(CollUtil.isNotEmpty(customerGroupList)){
                List<EnterpriseCustomerGroupDTO> customerGroupDtoList = customerGroupApi.listByIds(customerGroupList);
                customerGroupMap = customerGroupDtoList.stream().collect(Collectors.toMap(EnterpriseCustomerGroupDTO::getId, EnterpriseCustomerGroupDTO::getName));
            }else {
                customerGroupMap = MapUtil.newHashMap();
            }
            pageVO.getRecords().forEach(restrictionCustomer->{
                EnterpriseDTO enterpriseDTO = enterpriseDtoMap.getOrDefault(restrictionCustomer.getCustomerEid(), new EnterpriseDTO());
                restrictionCustomer.setCustomerGroupName(customerGroupMap.get(restrictionCustomer.getCustomerGroupId()));
                if(null!=enterpriseDTO){
                    restrictionCustomer.setCustomerName(enterpriseDTO.getName());
                    restrictionCustomer.setProvinceName(enterpriseDTO.getProvinceName());
                    restrictionCustomer.setCityName(enterpriseDTO.getCityName());
                    restrictionCustomer.setRegionName(enterpriseDTO.getRegionName());
                    restrictionCustomer.setType(enterpriseDTO.getType());
                }
                restrictionCustomer.setIsPurchaseRestriction(true);
            });
        }
        return Result.success(pageVO);
    }

    @ApiOperation(value = "查询客户分页（含是否已添加限购）", httpMethod = "POST")
    @PostMapping("/queryCustomerPage")
    public Result queryCustomerPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryRestrictionCustomerPageForm form){
        if(null == form.getGoodsId() || form.getGoodsId()==0){
            return Result.failed("查询客户缺少商品id");
        }
        QueryCustomerPageListRequest request = PojoUtils.map(form, QueryCustomerPageListRequest.class);
        request.setEids(ListUtil.toList(staffInfo.getCurrentEid()));
        request.setUseLine(EnterpriseCustomerLineEnum.B2B.getCode());
        request.setCustomerGroupId((request.getCustomerGroupId()!=null && request.getCustomerGroupId() == 0 ) ? null : request.getCustomerGroupId());
        Page<PurchaseRestrictionCustomerVO> page = PojoUtils.map(customerApi.pageList(request),PurchaseRestrictionCustomerVO.class);
        if(CollectionUtil.isNotEmpty(page.getRecords())){
            List<Long> customerEids = page.getRecords().stream().map(PurchaseRestrictionCustomerVO::getCustomerEid).collect(Collectors.toList());
            QueryRestrictionCustomerRequest restrictionRequest = new QueryRestrictionCustomerRequest();
            restrictionRequest.setGoodsId(form.getGoodsId());
            restrictionRequest.setCustomerEidList(customerEids);
            List<GoodsPurchaseRestrictionCustomerDTO> restrictionCustomerList = goodsPurchaseRestrictionApi.queryRestrictionCustomer(restrictionRequest);
            List<Long> restrictionCustomerEids = restrictionCustomerList.stream().map(GoodsPurchaseRestrictionCustomerDTO::getCustomerEid).collect(Collectors.toList());

            List<Long> customerEidList = page.getRecords().stream().map(EnterpriseCustomerListItemVO::getCustomerEid).collect(Collectors.toList());
            //企业信息
            List<EnterpriseDTO> list = enterpriseApi.listByIds(customerEidList);
            Map<Long, EnterpriseDTO> enterpriseDtoMap = list.stream().collect(Collectors.toMap(BaseDTO::getId, Function.identity()));

            //客户分组信息
            List<Long> customerGroupList = page.getRecords().stream().map(EnterpriseCustomerListItemVO::getCustomerGroupId).collect(Collectors.toList());
            Map<Long, String> customerGroupMap;
            if(CollUtil.isNotEmpty(customerGroupList)){
                List<EnterpriseCustomerGroupDTO> customerGroupDtoList = customerGroupApi.listByIds(customerGroupList);
                customerGroupMap = customerGroupDtoList.stream().collect(Collectors.toMap(EnterpriseCustomerGroupDTO::getId, EnterpriseCustomerGroupDTO::getName));
            }else {
                customerGroupMap = MapUtil.newHashMap();
            }
            page.getRecords().forEach(record ->{
                EnterpriseDTO enterpriseDTO = enterpriseDtoMap.getOrDefault(record.getCustomerEid(), new EnterpriseDTO());
                record.setCustomerGroupName(customerGroupMap.get(record.getCustomerGroupId()));
                if(null!=enterpriseDTO){
                    record.setCustomerName(enterpriseDTO.getName());
                    record.setProvinceName(enterpriseDTO.getProvinceName());
                    record.setCityName(enterpriseDTO.getCityName());
                    record.setRegionName(enterpriseDTO.getRegionName());
                    record.setType(enterpriseDTO.getType());
                }
                if(restrictionCustomerEids.contains(record.getCustomerEid())){
                    record.setIsPurchaseRestriction(true);
                }else {
                    record.setIsPurchaseRestriction(false);
                }
            });
        }
        return Result.success(page);
    }

    @ApiOperation(value = "保存商品限购规则", httpMethod = "POST")
    @PostMapping("/savePurchaseRestriction")
    public Result savePurchaseRestriction(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid SavePurchaseRestrictionForm form){
        if(RestrictionTimeTypeEnum.CUSTOM.getType().equals(form.getTimeType())){
            if(null == form.getStartTime() || null == form.getEndTime()){
                return Result.failed("自定义时间类型限购开始时间和结束时间不能为空");
            }
            DateTime start = DateUtil.beginOfDay(form.getStartTime());
            //数据库保存时间显示的时候 结尾会有500毫秒的进位,满500ms进1s,endOfDay()处理后的时间为23:59:59 999 需要向前偏移999
            DateTime end = DateUtil.endOfDay(form.getEndTime()).offset(DateField.MILLISECOND, -999);
            if((end.getTime()-start.getTime())<=0){
                return Result.failed("开始时间必须小于结束时间");
            }
            form.setStartTime(start);
            form.setEndTime(end);
        }
        if(null == form.getOrderRestrictionQuantity()){
            form.setOrderRestrictionQuantity(0L);
        }
        if(form.getOrderRestrictionQuantity() < 0){
            return Result.failed("每单限购数量不能为负数");
        }
        if(null == form.getTimeRestrictionQuantity()){
            form.setTimeRestrictionQuantity(0L);
        }
        if(form.getTimeRestrictionQuantity() < 0){
            return Result.failed("时间内限购数量不能为负数");
        }
        if(form.getOrderRestrictionQuantity() > 0 &&  form.getTimeRestrictionQuantity()> 0){
            if(form.getOrderRestrictionQuantity()>form.getTimeRestrictionQuantity()){
                return Result.failed("时间内限购数量必须大于或等于每单限购数量");
            }
        }
        SavePurchaseRestrictionRequest request = PojoUtils.map(form, SavePurchaseRestrictionRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        goodsPurchaseRestrictionApi.saveGoodsPurchaseRestriction(request);
        return Result.success();
    }

    @ApiOperation(value = "通过查询条件批量添加限购客户", httpMethod = "POST")
    @PostMapping("/batchAddRestrictionCustomerByQuery")
    public Result batchAddRestrictionCustomerByQuery(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryRestrictionCustomerPageForm form){
        if(null == form.getGoodsId() || form.getGoodsId()==0){
            return Result.failed("添加客户缺少商品id");
        }
        QueryCustomerPageListRequest request = PojoUtils.map(form, QueryCustomerPageListRequest.class);
        request.setEids(ListUtil.toList(staffInfo.getCurrentEid()));
        request.setUseLine(EnterpriseCustomerLineEnum.B2B.getCode());
        request.setCustomerGroupId((request.getCustomerGroupId()!=null && request.getCustomerGroupId() == 0 ) ? null : request.getCustomerGroupId());
        request.setCurrent(1);
        request.setSize(2000);
        Page<EnterpriseCustomerDTO> customerPage = customerApi.pageList(request);
        if(CollectionUtil.isNotEmpty(customerPage.getRecords())){
            List<Long> customerEids = customerPage.getRecords().stream().map(EnterpriseCustomerDTO::getCustomerEid).distinct().collect(Collectors.toList());
            SaveRestrictionCustomerRequest saveRequest = new SaveRestrictionCustomerRequest();
            saveRequest.setGoodsId(form.getGoodsId());
            saveRequest.setCustomerEidList(customerEids);
            saveRequest.setOpUserId(staffInfo.getCurrentUserId());
            Boolean b = goodsPurchaseRestrictionApi.batchSaveRestrictionCustomer(saveRequest);
            if(!b){
                return Result.failed("保存客户失败");
            }
        }
        return Result.success();
    }

    @ApiOperation(value = "单条添加限购客户", httpMethod = "POST")
    @PostMapping("/addRestrictionCustomer")
    public Result addRestrictionCustomer(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid SaveRestrictionCustomerForm form){
        SaveRestrictionCustomerRequest request = PojoUtils.map(form, SaveRestrictionCustomerRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        Boolean b = goodsPurchaseRestrictionApi.saveRestrictionCustomer(request);
        if(!b){
            return Result.failed("添加失败");
        }
        return Result.success();
    }

    @ApiOperation(value = "通过查询条件批量删除限购客户", httpMethod = "POST")
    @PostMapping("/batchDeleteRestrictionCustomerByQuery")
    public Result batchDeleteRestrictionCustomerByQuery(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryRestrictionCustomerPageForm form){
        if(null == form.getGoodsId() || form.getGoodsId()==0){
            return Result.failed("删除客户缺少商品id");
        }
        List<Long> customerEids = goodsPurchaseRestrictionApi.getCustomerEidByGoodsId(form.getGoodsId());
        if(CollectionUtil.isEmpty(customerEids)){
            return Result.success();
        }
        QueryCustomerPageListRequest request = PojoUtils.map(form, QueryCustomerPageListRequest.class);
        request.setEids(ListUtil.toList(staffInfo.getCurrentEid()));
        request.setUseLine(EnterpriseCustomerLineEnum.B2B.getCode());
        request.setCustomerGroupId((request.getCustomerGroupId()!=null && request.getCustomerGroupId() == 0 ) ? null : request.getCustomerGroupId());
        request.setCustomerEids(customerEids);
        List<EnterpriseCustomerDTO> list = customerApi.queryList(request);
        if(CollectionUtil.isEmpty(list)){
            return Result.success();
        }
        List<Long> deleteCustomerEids= list.stream().map(EnterpriseCustomerDTO::getCustomerEid).collect(Collectors.toList());
        DeleteRestrictionCustomerRequest deleteRequest = new DeleteRestrictionCustomerRequest();
        deleteRequest.setGoodsId(form.getGoodsId());
        deleteRequest.setCustomerEidList(deleteCustomerEids);
        deleteRequest.setOpUserId(staffInfo.getCurrentUserId());
        goodsPurchaseRestrictionApi.batchDeleteRestrictionCustomer(deleteRequest);
        return Result.success();
    }

    @ApiOperation(value = "单条删除限购客户", httpMethod = "POST")
    @PostMapping("/deleteRestrictionCustomer")
    public Result deleteRestrictionCustomer(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid SaveRestrictionCustomerForm form){
        if(null == form.getGoodsId() || null == form.getCustomerEid()){
            return Result.failed("删除客户的信息为空");
        }
        DeleteRestrictionCustomerRequest request = PojoUtils.map(form,DeleteRestrictionCustomerRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        goodsPurchaseRestrictionApi.deleteRestrictionCustomer(request);
        return Result.success();
    }
}
