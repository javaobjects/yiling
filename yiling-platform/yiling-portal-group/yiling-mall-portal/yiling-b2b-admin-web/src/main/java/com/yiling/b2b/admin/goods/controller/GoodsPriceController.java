package com.yiling.b2b.admin.goods.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.yiling.export.excel.handler.ImportDataHandler;
import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.util.ExeclImportUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.b2b.admin.goods.form.ImportGoodsCustomerPriceForm;
import com.yiling.b2b.admin.goods.form.ImportGoodsGroupPriceForm;
import com.yiling.b2b.admin.goods.form.QueryGoodsPriceCustomerForm;
import com.yiling.b2b.admin.goods.form.QueryGoodsPricePageListBaseForm;
import com.yiling.b2b.admin.goods.form.QueryGoodsPricePageListForm;
import com.yiling.b2b.admin.goods.form.SaveGoodsPriceCustomerForm;
import com.yiling.b2b.admin.goods.form.SaveGoodsPriceCustomerGroupForm;
import com.yiling.b2b.admin.goods.handler.ImportGoodsCustomerPriceDataHandler;
import com.yiling.b2b.admin.goods.handler.ImportGoodsCustomerPriceVerifyHandler;
import com.yiling.b2b.admin.goods.handler.ImportGoodsGroupPriceDataHandler;
import com.yiling.b2b.admin.goods.handler.ImportGoodsGroupPriceVerifyHandler;
import com.yiling.b2b.admin.goods.vo.GoodsPriceCustomerGroupVO;
import com.yiling.b2b.admin.goods.vo.GoodsPriceCustomerVO;
import com.yiling.b2b.admin.goods.vo.GoodsPriceVO;
import com.yiling.common.web.goods.utils.PictureUrlUtils;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.framework.common.web.rest.IdObject;
import com.yiling.framework.oss.vo.ImportResultVO;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.enums.GoodsLineEnum;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.pricing.goods.api.GoodsPriceCustomerApi;
import com.yiling.pricing.goods.api.GoodsPriceCustomerGroupApi;
import com.yiling.pricing.goods.bo.CountGoodsPriceBO;
import com.yiling.pricing.goods.dto.GoodsPriceCustomerDTO;
import com.yiling.pricing.goods.dto.GoodsPriceCustomerGroupDTO;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceCustomerGroupPageListRequest;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceCustomerPageListRequest;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceCustomerRequest;
import com.yiling.pricing.goods.dto.request.SaveOrUpdateGoodsPriceCustomerGroupRequest;
import com.yiling.pricing.goods.dto.request.SaveOrUpdateGoodsPriceCustomerRequest;
import com.yiling.user.enterprise.api.ChannelCustomerApi;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.CustomerGroupApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseCustomerGroupDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 商品定价 Controller
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-22
 */
@RestController
@RequestMapping("price")
@Api(tags = "商品定价模块接口")
@Slf4j
public class GoodsPriceController extends BaseController {

    @DubboReference
    GoodsPriceCustomerGroupApi    goodsPriceCustomerGroupApi;
    @DubboReference
    GoodsPriceCustomerApi         goodsPriceCustomerApi;
    @DubboReference
    GoodsApi                      goodsApi;
    @DubboReference
    ChannelCustomerApi            channelCustomerApi;
    @DubboReference
    CustomerGroupApi                      customerGroupApi;
    @DubboReference
    EnterpriseApi                         enterpriseApi;
    @DubboReference
    CustomerApi                           customerApi;
    @Autowired
    PictureUrlUtils                       pictureUrlUtils;
    @Autowired
    ImportGoodsCustomerPriceVerifyHandler importGoodsCustomerPriceVerifyHandler;
    @Autowired
    ImportGoodsCustomerPriceDataHandler   importGoodsCustomerPriceDataHandler;
    @Autowired
    ImportGoodsGroupPriceVerifyHandler    importGoodsGroupPriceVerifyHandler;
    @Autowired
    ImportGoodsGroupPriceDataHandler      importGoodsGroupPriceDataHandler;

    @ApiOperation(value = "b2b管理后台商品定价信息导入", httpMethod = "POST")
    @PostMapping(value = "import", headers = "content-type=multipart/form-data")
    @Log(title = "b2b管理后台商品定价信息导入",businessType = BusinessTypeEnum.IMPORT)
    public Result<ImportResultVO> goodsImport(@RequestParam(value = "file", required = true) MultipartFile file, @CurrentUser CurrentStaffInfo staffInfo) {
        try {
            Workbook workBook = getWorkBook(file);
            List<Class<?>> classList = new ArrayList<>();
            List<ImportParams> paramsList = new ArrayList<>();
            List<ImportDataHandler> importDataHandlerList = new ArrayList<>();
            List<String> sheetNameList = new ArrayList<>();
            for (int num = 0; num < workBook.getNumberOfSheets(); num++) {
                Sheet sheet = workBook.getSheetAt(num);
                ImportParams params = new ImportParams();
                params.setNeedVerify(true);
                params.setStartSheetIndex(num);
                params.setKeyIndex(0);
                if ("批量设置客户定价".equals(sheet.getSheetName())) {
                    params.setVerifyHandler(importGoodsCustomerPriceVerifyHandler);
                    classList.add(ImportGoodsCustomerPriceForm.class);
                    paramsList.add(params);
                    importDataHandlerList.add(importGoodsCustomerPriceDataHandler);
                    sheetNameList.add(sheet.getSheetName());
                    continue;
                } else if ("批量设置客户分组定价".equals(sheet.getSheetName())) {
                    params.setVerifyHandler(importGoodsGroupPriceVerifyHandler);
                    classList.add(ImportGoodsGroupPriceForm.class);
                    paramsList.add(params);
                    importDataHandlerList.add(importGoodsGroupPriceDataHandler);
                    sheetNameList.add(sheet.getSheetName());
                    continue;
                }
            }
            Map<String,Object> paramMap=new HashMap<>();
            paramMap.put(MyMetaHandler.FIELD_OP_USER_ID,staffInfo.getCurrentUserId());
            paramMap.put("eid",staffInfo.getCurrentEid());
            ImportResultModel<ImportResultVO> objectImportResultModel = ExeclImportUtils.importExcelMore(file, classList, paramsList, importDataHandlerList, sheetNameList, paramMap);
            return Result.success(PojoUtils.map(objectImportResultModel, ImportResultVO.class));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.EXCEL_PARSING_ERROR);
        }
    }

    @ApiOperation(value = "b2b后台商品定价分页查询", httpMethod = "POST")
    @PostMapping("/pageList")
    public Result<Page<GoodsPriceVO>> pageList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody QueryGoodsPricePageListForm form) {
        // 先查询商品列表
        QueryGoodsPageListRequest request = PojoUtils.map(form, QueryGoodsPageListRequest.class);
        if (staffInfo.getCurrentEid() == null || staffInfo.getCurrentEid() == 0) {
            return Result.success(request.getPage());
        }
        // 判断当前用户是否子企业
        request.setEidList(Arrays.asList(staffInfo.getCurrentEid()));
        List<Long> eidList = enterpriseApi.listSubEids(staffInfo.getCurrentEid());
        if (CollUtil.isNotEmpty(eidList)) {
            eidList.add(staffInfo.getCurrentEid());
            request.setEidList(eidList);
        }
        request.setAuditStatus(GoodsStatusEnum.AUDIT_PASS.getCode());
        request.setGoodsLine(GoodsLineEnum.B2B.getCode());
        Page<GoodsListItemBO> goodsPage = goodsApi.queryPageListGoods(request);
        // 拼接商品定价信息
        if (CollUtil.isEmpty(goodsPage.getRecords())) {
            return Result.success(request.getPage());
        }
        Page<GoodsPriceVO> page = PojoUtils.map(goodsPage, GoodsPriceVO.class);
        // 统计企业客户数量
        Map<Long, Long> customerNumMap = channelCustomerApi.countCustomersByEids(Arrays.asList(staffInfo.getCurrentEid()));
        // 统计企业下客户分组数量
        Map<Long, Long> customerGroupNumMap = customerGroupApi.countCustomerGroupNumByEids(Arrays.asList(staffInfo.getCurrentEid()));

        // 批量处理客户定价、客户分组定价
        List<Long> goodsIds = goodsPage.getRecords().stream().map(GoodsListItemBO::getId).collect(Collectors.toList());
        // 统计企业下某商品参与客户定价个数
        List<CountGoodsPriceBO> customerGoodsPriceList = goodsPriceCustomerApi.countGoodsCustomerPrice(goodsIds, GoodsLineEnum.B2B.getCode());
        Map<Long, CountGoodsPriceBO> customerGoodsPriceMap = customerGoodsPriceList.stream().collect(Collectors.toMap(CountGoodsPriceBO::getGoodsId, Function.identity(), (key1, key2) -> key2));
        // 统计企业下某商品参与客户分组定价个数
        List<CountGoodsPriceBO> customerGroupGoodsGPriceList = goodsPriceCustomerGroupApi.countGoodsCustomerGroupPrice(goodsIds, GoodsLineEnum.B2B.getCode());
        Map<Long, CountGoodsPriceBO> customerGroupGoodsGPriceMap = customerGroupGoodsGPriceList.stream().collect(Collectors.toMap(CountGoodsPriceBO::getGoodsId, Function.identity(), (key1, key2) -> key2));
        List<GoodsPriceVO> list = new ArrayList<>();
        goodsPage.getRecords().forEach(e -> {
            GoodsPriceVO vo = PojoUtils.map(e, GoodsPriceVO.class);
            if (customerGoodsPriceMap.containsKey(e.getId())) {
                CountGoodsPriceBO customerGoodsPriceBO = customerGoodsPriceMap.get(e.getId());
                vo.setCustomerPriceNum(customerGoodsPriceBO.getItemNum() + "/".concat(customerNumMap.get(staffInfo.getCurrentEid()).toString()));
                vo.setCustomerPrice(customerGoodsPriceBO.getMinPrice() + "~".concat(customerGoodsPriceBO.getMaxPrice().toString()));
            }
            if (customerGroupGoodsGPriceMap.containsKey(e.getId())) {
                CountGoodsPriceBO customerGroupGoodsGPriceBO = customerGroupGoodsGPriceMap.get(e.getId());
                vo.setCustomerGroupPriceNum(customerGroupGoodsGPriceBO.getItemNum() + "/".concat(customerGroupNumMap.get(staffInfo.getCurrentEid()).toString()));
                vo.setCustomerGroupPrice(customerGroupGoodsGPriceBO.getMinPrice() + "~".concat(customerGroupGoodsGPriceBO.getMaxPrice().toString()));
            }
            vo.setPic(pictureUrlUtils.getGoodsPicUrl(vo.getPic()));
            list.add(vo);
        });
        page.setRecords(list);
        return Result.success(page);
    }

    @ApiOperation(value = "b2b后台商品定价-客户定价分页查询跳转", httpMethod = "POST")
    @PostMapping("/customer/pageList")
    public Result<Page<GoodsPriceCustomerVO>> customerPricePageList(@RequestBody QueryGoodsPricePageListBaseForm form) {
        QueryGoodsPriceCustomerPageListRequest request = PojoUtils.map(form, QueryGoodsPriceCustomerPageListRequest.class);
        request.setGoodsLine(GoodsLineEnum.B2B.getCode());
        if (form.getGoodsId() == null || form.getGoodsId() == 0) {
            return Result.success(request.getPage());
        }
        request.setGoodsIds(Arrays.asList(form.getGoodsId()));
        Page<GoodsPriceCustomerDTO> page = goodsPriceCustomerApi.pageList(request);
        if (CollUtil.isEmpty(page.getRecords())) {
            return Result.success(request.getPage());
        }
        List<GoodsPriceCustomerVO> list = new ArrayList<>();
        List<Long> customerEids = page.getRecords().stream().map(GoodsPriceCustomerDTO::getCustomerEid).collect(Collectors.toList());
        // 查询客户企业信息
        Map<Long, EnterpriseDTO> customerEnterpriseMap = MapUtil.newHashMap();
        if (CollUtil.isNotEmpty(customerEids)) {
            List<EnterpriseDTO> customerEnterpriseList = enterpriseApi.listByIds(customerEids);
            customerEnterpriseMap = customerEnterpriseList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, Function.identity(), (key1, key2) -> key2));
        }
        Map<Long, EnterpriseDTO> finalCustomerEnterpriseMap = customerEnterpriseMap;
        page.getRecords().forEach(e -> {
            GoodsPriceCustomerVO vo = PojoUtils.map(e, GoodsPriceCustomerVO.class);
            if (finalCustomerEnterpriseMap.containsKey(e.getCustomerEid())) {
                EnterpriseDTO dto = finalCustomerEnterpriseMap.get(e.getCustomerEid());
                vo.setCustomerName(dto.getName());
                vo.setContactor(dto.getContactor());
                vo.setContactorPhone(dto.getContactorPhone());
                vo.setAddress(new StringJoiner(" ")
                        .add(dto.getProvinceName())
                        .add(dto.getCityName())
                        .add(dto.getRegionName())
                        .add(dto.getAddress())
                        .toString());
            }
            list.add(vo);
        });
        Page<GoodsPriceCustomerVO> goodsPriceCustomerVOPage = PojoUtils.map(page, GoodsPriceCustomerVO.class);
        goodsPriceCustomerVOPage.setRecords(list);
        return Result.success(goodsPriceCustomerVOPage);
    }

    @ApiOperation(value = "b2b后台商品定价-客户分组定价分页查询跳转", httpMethod = "POST")
    @PostMapping("/customerGroup/pageList")
    public Result<Page<GoodsPriceCustomerGroupVO>> customerGroupPricePageList(@RequestBody QueryGoodsPricePageListBaseForm form) {
        QueryGoodsPriceCustomerGroupPageListRequest request = PojoUtils.map(form, QueryGoodsPriceCustomerGroupPageListRequest.class);
        if (form.getGoodsId() == null || form.getGoodsId() == 0) {
            return Result.success(request.getPage());
        }
        request.setGoodsIds(Arrays.asList(form.getGoodsId()));
        request.setGoodsLine(GoodsLineEnum.B2B.getCode());
        Page<GoodsPriceCustomerGroupDTO> page = goodsPriceCustomerGroupApi.pageList(request);
        if (CollUtil.isEmpty(page.getRecords())) {
            return Result.success(request.getPage());
        }
        // 查询客户分组下客户数
        List<Long> groupIds = page.getRecords().stream().map(GoodsPriceCustomerGroupDTO::getCustomerGroupId).distinct().collect(Collectors.toList());
        Map<Long, Long> countCustomerMap = customerApi.countGroupCustomers(groupIds);
        List<GoodsPriceCustomerGroupVO> list = new ArrayList<>();
        // 查询客户分组列表
        Map<Long, EnterpriseCustomerGroupDTO> enterpriseCustomerGroupMap = MapUtil.newHashMap();
        List<Long> customerGroupIds = page.getRecords().stream().map(GoodsPriceCustomerGroupDTO::getCustomerGroupId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(customerGroupIds)) {
            List<EnterpriseCustomerGroupDTO> enterpriseCustomerGroupDTOList = customerGroupApi.listByIds(customerGroupIds);
            enterpriseCustomerGroupMap = enterpriseCustomerGroupDTOList.stream().collect(Collectors.toMap(EnterpriseCustomerGroupDTO::getId, Function.identity(), (key1, key2) -> key2));
        }
        Map<Long, EnterpriseCustomerGroupDTO> finalEnterpriseCustomerGroupMap = enterpriseCustomerGroupMap;
        page.getRecords().forEach(e -> {
            GoodsPriceCustomerGroupVO vo = PojoUtils.map(e, GoodsPriceCustomerGroupVO.class);
            if (finalEnterpriseCustomerGroupMap.containsKey(e.getCustomerGroupId())) {
                vo.setCustomerGroupName(finalEnterpriseCustomerGroupMap.get(e.getCustomerGroupId()).getName());
            }
            if (MapUtil.isNotEmpty(countCustomerMap) && countCustomerMap.containsKey(e.getCustomerGroupId())) {
                vo.setCustomerNum(Convert.toInt(countCustomerMap.get(e.getCustomerGroupId())));
            }
            list.add(vo);
        });
        Page<GoodsPriceCustomerGroupVO> goodsPriceCustomerGroupVOPage = PojoUtils.map(page, GoodsPriceCustomerGroupVO.class);
        goodsPriceCustomerGroupVOPage.setRecords(list);
        return Result.success(goodsPriceCustomerGroupVOPage);
    }

    @ApiOperation(value = "b2b后台商品定价-保存客户定价")
    @PostMapping("/customer/save")
    @Log(title = "保存客户定价",businessType = BusinessTypeEnum.INSERT)
    public Result<IdObject> saveOrUpdateCustomerPrice(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid SaveGoodsPriceCustomerForm form) {
        SaveOrUpdateGoodsPriceCustomerRequest request = PojoUtils.map(form, SaveOrUpdateGoodsPriceCustomerRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setGoodsLine(GoodsLineEnum.B2B.getCode());
        return Result.success(new IdObject(goodsPriceCustomerApi.saveOrUpdate(request)));
    }

    @ApiOperation(value = "b2b后台商品定价-移除客户定价")
    @GetMapping("/customer/remove")
    @Log(title = "移除客户定价",businessType = BusinessTypeEnum.DELETE)
    public Result<BoolObject> removeCustomerPrice(@CurrentUser CurrentStaffInfo staffInfo,
                                                  @RequestParam("departmentId") @ApiParam(value = "客户定价ID", required = true) Long customerPriceId) {
        if (staffInfo.getCurrentEid() == null || staffInfo.getCurrentEid() == 0) {
            return Result.failed("企业信息为空，用户登录异常！");
        }
        return Result.success(new BoolObject(goodsPriceCustomerApi.removeById(customerPriceId)));
    }

    @ApiOperation(value = "b2b后台商品定价-查询客户定价")
    @PostMapping("/customer/get")
    public Result<GoodsPriceCustomerVO> get(@RequestBody QueryGoodsPriceCustomerForm goodsPriceCustomerForm) {
        QueryGoodsPriceCustomerRequest request = PojoUtils.map(goodsPriceCustomerForm, QueryGoodsPriceCustomerRequest.class);
        request.setGoodsLine(GoodsLineEnum.B2B.getCode());
        GoodsPriceCustomerDTO goodsPriceCustomerDTO = goodsPriceCustomerApi.get(request);
        return Result.success(PojoUtils.map(goodsPriceCustomerDTO, GoodsPriceCustomerVO.class));
    }

    @ApiOperation(value = "b2b后台商品定价-保存客户分组定价")
    @PostMapping("/customerGroup/save")
    @Log(title = "保存客户分组定价",businessType = BusinessTypeEnum.INSERT)
    public Result<IdObject> saveOrUpdateCustomerGroupPrice(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid SaveGoodsPriceCustomerGroupForm form) {
        SaveOrUpdateGoodsPriceCustomerGroupRequest request = PojoUtils.map(form, SaveOrUpdateGoodsPriceCustomerGroupRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setGoodsLine(GoodsLineEnum.B2B.getCode());
        return Result.success(new IdObject(goodsPriceCustomerGroupApi.saveOrUpdate(request)));
    }

    @ApiOperation(value = "b2b后台商品定价-移除客户分组定价")
    @GetMapping("/customerGroup/remove")
    @Log(title = "保存客户分组定价",businessType = BusinessTypeEnum.DELETE)
    public Result<BoolObject> removeCustomerGroupPrice(@CurrentUser CurrentStaffInfo staffInfo,
                                                       @RequestParam("departmentId") @ApiParam(value = "客户分组定价ID", required = true) Long customerGroupPriceId) {
        if (staffInfo.getCurrentEid() == null || staffInfo.getCurrentEid() == 0) {
            return Result.failed("企业信息为空，用户登录异常！");
        }
        return Result.success(new BoolObject(goodsPriceCustomerGroupApi.removeById(customerGroupPriceId)));
    }

    public static Workbook getWorkBook(MultipartFile file) throws IOException {

        Workbook hssfWorkbook = null;
        try {
            hssfWorkbook = new HSSFWorkbook(file.getInputStream());
        } catch (Exception ex) {
            hssfWorkbook = new XSSFWorkbook(file.getInputStream());
        }
        return hssfWorkbook;
    }

}
