package com.yiling.b2b.admin.goods.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.b2b.admin.enterprisecustomer.vo.EnterpriseCustomerListItemVO;
import com.yiling.b2b.admin.goods.form.DeleteCustomerControlForm;
import com.yiling.b2b.admin.goods.form.QueryCustomerPageListForm;
import com.yiling.b2b.admin.goods.form.SaveCustomerControlForm;
import com.yiling.b2b.admin.goods.form.SaveRegionControlForm;
import com.yiling.b2b.admin.goods.vo.CustomerControlPageVO;
import com.yiling.b2b.admin.goods.vo.CustomerDisableVO;
import com.yiling.b2b.admin.goods.vo.CustomerListItemVO;
import com.yiling.b2b.admin.goods.vo.CustomerTypeControlVO;
import com.yiling.b2b.admin.goods.vo.RegionControlVO;
import com.yiling.b2b.admin.goods.vo.RegionTypeControlVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.enums.EnterpriseCustomerLineEnum;
import com.yiling.user.control.api.ControlApi;
import com.yiling.user.control.dto.GoodsControlDTO;
import com.yiling.user.control.dto.request.BatchSaveCustomerControlRequest;
import com.yiling.user.control.dto.request.DeleteGoodsControlRequest;
import com.yiling.user.control.dto.request.QueryCustomerControlPageRequest;
import com.yiling.user.control.dto.request.SaveCustomerControlRequest;
import com.yiling.user.control.dto.request.SaveRegionControlRequest;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.CustomerGroupApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseCustomerDTO;
import com.yiling.user.enterprise.dto.EnterpriseCustomerGroupDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryCustomerPageListRequest;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2021/10/21
 */
@RestController
@RequestMapping("control")
@Api(tags = "控销模块接口")
@Slf4j
public class GoodsControlController {

    @DubboReference(timeout = 1000*60)
    private ControlApi controlApi;

    @DubboReference
    private CustomerApi customerApi;

    @DubboReference
    private EnterpriseApi enterpriseApi;

    @DubboReference
    private CustomerGroupApi customerGroupApi;

    @ApiOperation(value = "b2b后台商品控销-获取区域控销")
    @GetMapping("/getRegion")
    public Result<RegionTypeControlVO> getRegion(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("goodsId") @ApiParam(required = true, name = "goodsId", value = "商品id") Long goodsId) {
        RegionTypeControlVO regionTypeControlVO = new RegionTypeControlVO();

        CustomerTypeControlVO customerTypeControlVO = new CustomerTypeControlVO();
        GoodsControlDTO customerTypeDTO = controlApi.getCustomerTypeInfo(goodsId, staffInfo.getCurrentEid());
        if (customerTypeDTO != null) {
            customerTypeControlVO.setSetType(customerTypeDTO.getSetType());
            customerTypeControlVO.setCustomerTypes(customerTypeDTO.getConditionValue());
            customerTypeControlVO.setId(customerTypeDTO.getId());
            regionTypeControlVO.setCustomerTypeControlVO(customerTypeControlVO);
        }

        RegionControlVO regionControlVO = new RegionControlVO();
        GoodsControlDTO regionDTO = controlApi.getRegionInfo(goodsId, staffInfo.getCurrentEid());
        if (regionDTO != null) {
            regionControlVO.setSetType(regionDTO.getSetType());
            regionControlVO.setRegionIds(regionDTO.getConditionValue());
            regionControlVO.setId(regionDTO.getId());
            regionControlVO.setControlDescribe(regionDTO.getControlDescribe());
            regionTypeControlVO.setRegionControlVO(regionControlVO);
        }
        return Result.success(regionTypeControlVO);
    }

    @ApiOperation(value = "b2b后台商品控销-保存区域控销")
    @PostMapping("/saveRegion")
    public Result<Boolean> saveRegion(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid SaveRegionControlForm form) {
        SaveRegionControlRequest request = PojoUtils.map(form, SaveRegionControlRequest.class);
        request.setEid(staffInfo.getCurrentEid());
        request.setOpUserId(staffInfo.getCurrentUserId());
        return Result.success(controlApi.saveRegion(request));
    }


    @ApiOperation(value = "b2b后台商品控销-获取客户控销")
    @PostMapping("/getCustomer")
    public Result<CustomerControlPageVO<CustomerListItemVO>> getCustomer(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody QueryCustomerPageListForm form) {
        QueryCustomerControlPageRequest request = PojoUtils.map(form, QueryCustomerControlPageRequest.class);
        request.setEid(staffInfo.getCurrentEid());
        Page<Long> page = controlApi.getPageCustomerInfo(request);
        GoodsControlDTO goodsControlDTO=controlApi.getCustomerInfo(form.getGoodsId(),staffInfo.getCurrentEid());
        CustomerControlPageVO<CustomerListItemVO> newPage = new CustomerControlPageVO();
        if (CollUtil.isNotEmpty(page.getRecords())) {
            List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByIds(page.getRecords());
            Map<Long, EnterpriseDTO> enterpriseDtoMap = enterpriseDTOList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, Function.identity()));
           //客户信息
            List<EnterpriseCustomerDTO> enterpriseCustomerDTOList=customerApi.listByEidAndCustomerEids(staffInfo.getCurrentEid(),new ArrayList<>(enterpriseDtoMap.keySet()));
            Map<Long,EnterpriseCustomerDTO> enterpriseCustomerDTOMap=enterpriseCustomerDTOList.stream().collect(Collectors.toMap(EnterpriseCustomerDTO::getCustomerEid,Function.identity()));
            List<Long> customerGroupList = enterpriseCustomerDTOList.stream().map(EnterpriseCustomerDTO::getCustomerGroupId).collect(Collectors.toList());
            //客户分组信息
            Map<Long, String> customerGroupMap = MapUtil.newHashMap();
            if (CollUtil.isNotEmpty(customerGroupList)) {
                List<EnterpriseCustomerGroupDTO> customerGroupDtoList = customerGroupApi.listByIds(customerGroupList);
                customerGroupMap = customerGroupDtoList.stream().collect(Collectors.toMap(EnterpriseCustomerGroupDTO::getId, EnterpriseCustomerGroupDTO::getName));
            }

            List<CustomerListItemVO> customerListItemVOList = new ArrayList<>();
            Map<Long, String> finalCustomerGroupMap = customerGroupMap;
            page.getRecords().forEach(e -> {
                CustomerListItemVO item = new CustomerListItemVO();
                EnterpriseDTO customerEnterpriseDTO = enterpriseDtoMap.get(e);
                if (customerEnterpriseDTO != null) {
                    item.setCustomerEid(customerEnterpriseDTO.getId());
                    item.setCustomerGroupName(finalCustomerGroupMap.get(enterpriseCustomerDTOMap.get(e).getCustomerGroupId()));
                    item.setCustomerName(customerEnterpriseDTO.getName());
                    item.setCustomerType(EnterpriseTypeEnum.getByCode(customerEnterpriseDTO.getType()).getName());
                    item.setContactor(customerEnterpriseDTO.getContactor());
                    item.setContactorPhone(customerEnterpriseDTO.getContactorPhone());
                    item.setAddress(customerEnterpriseDTO.getAddress());
                    item.setRegionName(customerEnterpriseDTO.getRegionName());
                    item.setProvinceName(customerEnterpriseDTO.getProvinceName());
                    item.setCityName(customerEnterpriseDTO.getCityName());
                }
                customerListItemVOList.add(item);
            });
            newPage.setSetType(goodsControlDTO.getSetType());
            newPage.setId(goodsControlDTO.getId());
            newPage.setRecords(customerListItemVOList);
            newPage.setSize(page.getSize());
            newPage.setCurrent(page.getCurrent());
            newPage.setTotal(page.getTotal());

        }
        return Result.success(newPage);
    }

    @ApiOperation(value = "b2b后台商品控销-客户分页列表")
    @PostMapping("/pageCustomerList")
    public Result<Page<EnterpriseCustomerListItemVO>> pageCustomerList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryCustomerPageListForm form) {
        QueryCustomerPageListRequest request = PojoUtils.map(form, QueryCustomerPageListRequest.class);
        request.setEids(ListUtil.toList(staffInfo.getCurrentEid()));
        request.setUseLine(EnterpriseCustomerLineEnum.B2B.getCode());

        Page<EnterpriseCustomerDTO> page = customerApi.pageList(request);
        Page<EnterpriseCustomerListItemVO> pageVO = PojoUtils.map(page, EnterpriseCustomerListItemVO.class);

        GoodsControlDTO customerDTO = controlApi.getCustomerInfo(form.getGoodsId(), staffInfo.getCurrentEid());
        List<Long> customerIds = ListUtil.empty();
        if (customerDTO != null) {
            customerIds = customerDTO.getConditionValue();
        }

        List<Long> customerEidList = pageVO.getRecords().stream().map(EnterpriseCustomerListItemVO::getCustomerEid).collect(Collectors.toList());
        List<Long> customerGroupList = pageVO.getRecords().stream().map(EnterpriseCustomerListItemVO::getCustomerGroupId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(customerEidList)) {
            //企业信息
            List<EnterpriseDTO> list = enterpriseApi.listByIds(customerEidList);
            Map<Long, EnterpriseDTO> enterpriseDtoMap = list.stream().collect(Collectors.toMap(BaseDTO::getId, Function.identity()));

            //客户分组信息
            Map<Long, String> customerGroupMap = MapUtil.newHashMap();
            if (CollUtil.isNotEmpty(customerGroupList)) {
                List<EnterpriseCustomerGroupDTO> customerGroupDtoList = customerGroupApi.listByIds(customerGroupList);
                customerGroupMap = customerGroupDtoList.stream().collect(Collectors.toMap(EnterpriseCustomerGroupDTO::getId, EnterpriseCustomerGroupDTO::getName));
            }

            Map<Long, String> finalCustomerGroupMap = customerGroupMap;
            List<Long> finalCustomerIds = customerIds;
            pageVO.getRecords().forEach(enterpriseCustomerListItemVO -> {
                CustomerDisableVO customerDisableVO = new CustomerDisableVO();
                customerDisableVO.setControlDisable(false);
                if (finalCustomerIds.contains(enterpriseCustomerListItemVO.getCustomerEid())) {
                    customerDisableVO.setControlDisable(true);
                    customerDisableVO.setControlDesc("已添加");
                }

                EnterpriseDTO customerEnterpriseDTO = enterpriseDtoMap.get(enterpriseCustomerListItemVO.getCustomerEid());
                enterpriseCustomerListItemVO.setCustomerName(customerEnterpriseDTO.getName());
                enterpriseCustomerListItemVO.setRegionName(customerEnterpriseDTO.getRegionName());
                enterpriseCustomerListItemVO.setProvinceName(customerEnterpriseDTO.getProvinceName());
                enterpriseCustomerListItemVO.setCityName(customerEnterpriseDTO.getCityName());
                enterpriseCustomerListItemVO.setAddress(customerEnterpriseDTO.getAddress());
                enterpriseCustomerListItemVO.setCustomerType(EnterpriseTypeEnum.getByCode(customerEnterpriseDTO.getType()).getName());
                enterpriseCustomerListItemVO.setCustomerDisableVO(customerDisableVO);
                enterpriseCustomerListItemVO.setLicenseNumber(enterpriseDtoMap.getOrDefault(enterpriseCustomerListItemVO.getCustomerEid(), new EnterpriseDTO()).getLicenseNumber());
                enterpriseCustomerListItemVO.setCustomerGroupName(finalCustomerGroupMap.get(enterpriseCustomerListItemVO.getCustomerGroupId()));
            });
        }

        return Result.success(pageVO);
    }

    @ApiOperation(value = "b2b后台商品控销-保存客户控销")
    @PostMapping("/saveCustomer")
    public Result<Boolean> saveCustomer(@CurrentUser CurrentStaffInfo
                                                staffInfo, @RequestBody @Valid SaveCustomerControlForm form) {
        SaveCustomerControlRequest request = PojoUtils.map(form, SaveCustomerControlRequest.class);
        if (form.getCustomerId() != null && form.getCustomerId() != 0) {
            request.setCustomerIds(Arrays.asList(form.getCustomerId()));
        }
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setEid(staffInfo.getCurrentEid());
        return Result.success(controlApi.saveCustomer(request));
    }

    @ApiOperation(value = "b2b后台商品控销-删除客户控销")
    @PostMapping("/deleteCustomer")
    public Result<Boolean> deleteCustomer(@CurrentUser CurrentStaffInfo
                                                  staffInfo, @RequestBody @Valid DeleteCustomerControlForm form) {
        DeleteGoodsControlRequest request = PojoUtils.map(form, DeleteGoodsControlRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        return Result.success(controlApi.deleteCustomer(request));
    }

    @ApiOperation(value = "b2b后台商品控销-批量保存客户控销")
    @PostMapping("/batchSaveCustomer")
    public Result<Boolean> batchSaveCustomer(@CurrentUser CurrentStaffInfo
                                                     staffInfo, @RequestBody QueryCustomerPageListForm form) {
        BatchSaveCustomerControlRequest saveCustomerControlRequest = PojoUtils.map(form,BatchSaveCustomerControlRequest.class);
        saveCustomerControlRequest.setEid(staffInfo.getCurrentEid());
        saveCustomerControlRequest.setId(form.getControlId());
        saveCustomerControlRequest.setOpUserId(staffInfo.getCurrentUserId());
        return Result.success(controlApi.batchSaveCustomer(saveCustomerControlRequest));
    }
}
