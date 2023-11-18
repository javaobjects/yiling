package com.yiling.admin.erp.enterprisecustomer.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.erp.enterprisecustomer.form.BindCustomerForm;
import com.yiling.admin.erp.enterprisecustomer.form.QueryCustomerPageListForm;
import com.yiling.admin.erp.enterprisecustomer.form.QueryEnterprisePageListForm;
import com.yiling.admin.erp.enterprisecustomer.form.TycEnterpriseInfoForm;
import com.yiling.admin.erp.enterprisecustomer.vo.BindErpCustomerVO;
import com.yiling.admin.erp.enterprisecustomer.vo.EnterpriseVO;
import com.yiling.admin.erp.enterprisecustomer.vo.ErpCustomerVO;
import com.yiling.admin.erp.enterprisecustomer.vo.TycEnterpriseInfoVO;
import com.yiling.basic.dict.api.DictApi;
import com.yiling.basic.dict.bo.DictBO;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.api.ErpCustomerApi;
import com.yiling.open.erp.dto.ErpCustomerDTO;
import com.yiling.open.erp.dto.request.ErpCustomerQueryRequest;
import com.yiling.open.erp.dto.request.SaveEnterpriseCustomerRequest;
import com.yiling.open.erp.enums.SyncStatus;
import com.yiling.basic.tianyancha.api.TycEnterpriseApi;
import com.yiling.basic.tianyancha.dto.TycEnterpriseInfoDTO;
import com.yiling.basic.tianyancha.dto.TycResultDTO;
import com.yiling.basic.tianyancha.dto.request.TycEnterpriseQueryRequest;
import com.yiling.basic.tianyancha.enums.TycErrorCode;
import com.yiling.basic.tianyancha.enums.TycProvinceEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseByNameRequest;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.enterprise.enums.ErpSyncLevelEnum;
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 CustomerController
 * @描述
 * @创建时间 2022/1/12
 * @修改人 shichen
 * @修改时间 2022/1/12
 **/
@RestController
@Api(tags = "erp客户管理")
@RequestMapping("/erp/customer")
@Slf4j
public class CustomerController extends BaseController {

    @DubboReference(timeout = 1000*10)
    private ErpCustomerApi          erpCustomerApi;

    @DubboReference(timeout = 1000*10)
    private TycEnterpriseApi        tycEnterpriseApi;

    @DubboReference
    private EnterpriseApi           enterpriseApi;

    @DubboReference
    private DictApi                 dictApi;

    @DubboReference
    private LocationApi             locationApi;

    @Resource
    private StringRedisTemplate     stringRedisTemplate;



    @Value("${tyc.expiration}")
    private Long tycExpiration;

    private static final String ERP_CUSTOMER_ERROR = "erp_customer_error";

    private static final String TYC_REDIS_PREFIX = "tyc_";


    @ApiOperation(value = "异常erp客户列表", httpMethod = "POST")
    @PostMapping("/abnormalCustomerList")
    public Result<Page<ErpCustomerVO>> queryAbnormalCustomerPageList(@CurrentUser CurrentAdminInfo adminInfo,
                                                                     @RequestBody @Valid QueryCustomerPageListForm form){
        ErpCustomerQueryRequest request = PojoUtils.map(form, ErpCustomerQueryRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        request.setOpTime(new Date());
        if(form.getErrorCode()!=null&&form.getErrorCode()!=0){
            DictBO dictBO = dictApi.getDictByName(ERP_CUSTOMER_ERROR);
            String errorMsg = dictBO.getDataList().stream().filter(dictData -> form.getErrorCode().toString().equals(dictData.getValue())).findFirst().get().getLabel();
            request.setSyncMsg(errorMsg);
        }
        request.setSyncStatus(SyncStatus.FAIL.getCode());
        Page<ErpCustomerVO> page = PojoUtils.map(erpCustomerApi.QueryErpCustomerPageBySyncStatus(request), ErpCustomerVO.class);
        List<ErpCustomerVO> voList = page.getRecords();
        if(CollectionUtils.isNotEmpty(voList)){
            List<Long> suIdList = voList.stream().map(ErpCustomerVO::getSuId).collect(Collectors.toList());
            Map<Long, EnterpriseDTO> map = enterpriseApi.getMapByIds(suIdList);
            voList.forEach(vo->{
                EnterpriseDTO enterpriseDTO = map.get(vo.getSuId());
                vo.setSuName(ObjectUtil.isNotNull(enterpriseDTO)?enterpriseDTO.getName():"");
            });
        }
        return Result.success(page);
    }

    @ApiOperation(value = "天眼查查询企业", httpMethod = "GET")
    @GetMapping("/tyc/queryByKeyword")
    public Result<TycEnterpriseInfoVO> tycQueryByKeyword(@CurrentUser CurrentAdminInfo adminInfo,
                                                         @RequestParam("keyword")String keyword){
        //搜索关键字去空格
        keyword = keyword.replaceAll(" ","");
        TycEnterpriseQueryRequest request = new TycEnterpriseQueryRequest();
        request.setKeyword(keyword);
        request.setOpUserId(adminInfo.getCurrentUserId());
        request.setOpTime(new Date());
        TycResultDTO<TycEnterpriseInfoDTO> resultDTO;
        //先从redis获取数据
        String tycJson = stringRedisTemplate.opsForValue().get(TYC_REDIS_PREFIX + keyword);
        if(StringUtils.isNotBlank(tycJson)){
            resultDTO=JSONObject.parseObject(tycJson,new TypeReference<TycResultDTO<TycEnterpriseInfoDTO>>(){});
        }else {
            resultDTO = tycEnterpriseApi.findEnterpriseByKeyword(request);
            //当为成功状态或者无数据状态加入redis
            if(TycErrorCode.OK.getCode().equals(resultDTO.getError_code()) || TycErrorCode.NO_DATA.getCode().equals(resultDTO.getError_code())){
                stringRedisTemplate.opsForValue().set(TYC_REDIS_PREFIX + keyword, JSONObject.toJSONString(resultDTO),tycExpiration, TimeUnit.SECONDS);
            }
        }
        if(TycErrorCode.OK.getCode().equals(resultDTO.getError_code())){
            TycProvinceEnum tycProvinceEnum = TycProvinceEnum.getByCode(resultDTO.getResult().getBase());
            if(null!=tycProvinceEnum){
                resultDTO.getResult().setBase(tycProvinceEnum.getProvinceName());
            }
            return Result.success(PojoUtils.map(resultDTO.getResult(),TycEnterpriseInfoVO.class));
        }else {
            return Result.failed(TycErrorCode.getByCode(resultDTO.getError_code()));
        }
    }

    @ApiOperation(value = "id查询erp客户", httpMethod = "GET")
    @GetMapping("/findById")
    public Result<ErpCustomerVO> findById(@CurrentUser CurrentAdminInfo adminInfo,
                                                         @RequestParam("erpCustomerId")Long id){

        ErpCustomerDTO customerDTO = erpCustomerApi.findById(id);
        return Result.success(PojoUtils.map(customerDTO,ErpCustomerVO.class));
    }

    @ApiOperation(value = "名字模糊查询接通erp的供应商", httpMethod = "GET")
    @GetMapping("/erpSupplierList")
    public Result<List<EnterpriseVO>> queryByKeyword(@CurrentUser CurrentAdminInfo adminInfo,
                                                     @RequestParam("name")String name){
        QueryEnterpriseByNameRequest request = new QueryEnterpriseByNameRequest();
        request.setName(name);
        request.setErpSyncLevelList(Arrays.asList(ErpSyncLevelEnum.BASED_DOCKING.getCode(),
                ErpSyncLevelEnum.ORDER_DOCKING.getCode(),ErpSyncLevelEnum.DELIVERY_DOCKING.getCode()));
        request.setType(EnterpriseTypeEnum.BUSINESS.getCode());
        List<EnterpriseVO> list = PojoUtils.map(enterpriseApi.getEnterpriseListByName(request), EnterpriseVO.class);
        return Result.success(list);
    }

    @ApiOperation(value = "名字分页模糊查询企业", httpMethod = "POST")
    @PostMapping("/enterpriseList")
    public Result<Page<EnterpriseVO>> enterpriseList(@CurrentUser CurrentAdminInfo adminInfo,
                                                      @RequestBody @Valid QueryEnterprisePageListForm form){
        QueryEnterprisePageListRequest request = PojoUtils.map(form,QueryEnterprisePageListRequest.class);
        Page<EnterpriseVO> page;
        if(StringUtils.isBlank(form.getName())){
            page = new Page<>(form.getCurrent(),form.getSize());
            return Result.success(page);
        }
        request.setCurrent(form.getCurrent());
        request.setSize(form.getSize());
        page = PojoUtils.map(enterpriseApi.pageList(request), EnterpriseVO.class);
        return Result.success(page);
    }

    @ApiOperation(value = "erp客户绑定平台企业", httpMethod = "POST")
    @PostMapping("/bind")
    @Log(title = "erp客户绑定平台企业", businessType = BusinessTypeEnum.UPDATE)
    public Result<BindErpCustomerVO> bind(@CurrentUser CurrentAdminInfo adminInfo,@RequestBody BindCustomerForm form){
        SaveEnterpriseCustomerRequest request = new SaveEnterpriseCustomerRequest();
        request.setOpUserId(adminInfo.getCurrentUserId());
        request.setOpTime(new Date());
        if(null==form.getCustomerEid() || 0 == form.getCustomerEid()){
            return Result.failed("缺少绑定企业");
        }
        request.setCustomerEid(form.getCustomerEid());
        if(null==form.getErpCustomer()){
            return Result.failed("缺少erp客户");
        }
        request.setErpCustomer(PojoUtils.map(form.getErpCustomer(),ErpCustomerDTO.class));
        log.info("erp客户绑定平台企业参数：{}",request);
        Boolean flag = erpCustomerApi.relationCustomer(request);
        BindErpCustomerVO bindVO = new BindErpCustomerVO();
        if(flag){
            bindVO.setBindResult(true);
            bindVO.setFailMsg("绑定成功");
        }else {
            bindVO.setBindResult(false);
            ErpCustomerDTO erpCustomerDTO = erpCustomerApi.findById(form.getErpCustomer().getId());
            bindVO.setErpCustomer(PojoUtils.map(erpCustomerDTO,ErpCustomerVO.class));
            bindVO.setFailMsg(erpCustomerDTO.getSyncMsg());
        }
        return Result.success(bindVO);
    }

    @ApiOperation(value = "维护erp客户", httpMethod = "POST")
    @PostMapping("/maintain")
    @Log(title = "维护erp客户", businessType = BusinessTypeEnum.UPDATE)
    public Result<BindErpCustomerVO> maintain(@CurrentUser CurrentAdminInfo adminInfo,@RequestBody BindCustomerForm form){
        SaveEnterpriseCustomerRequest request = new SaveEnterpriseCustomerRequest();
        request.setOpUserId(adminInfo.getCurrentUserId());
        request.setOpTime(new Date());
        if(null==form.getErpCustomer()){
            return Result.failed("缺少erp客户维护信息");
        }
        request.setErpCustomer(PojoUtils.map(form.getErpCustomer(),ErpCustomerDTO.class));
        log.info("维护erp客户参数：{}",request);
        String provinceCode = request.getErpCustomer().getProvinceCode();
        String cityCode = request.getErpCustomer().getCityCode();
        String regionCode = request.getErpCustomer().getRegionCode();
        //当省市区code都不为空，省市区名称以code获取为主
        if(StringUtils.isNoneBlank(provinceCode,cityCode,regionCode)){
            String[] names = locationApi.getNamesByCodes(provinceCode, cityCode, regionCode);
            request.getErpCustomer().setProvince(names[0]);
            request.getErpCustomer().setCity(names[1]);
            request.getErpCustomer().setRegion(names[2]);
        }
        Boolean flag = erpCustomerApi.maintain(request);
        BindErpCustomerVO bindVO = new BindErpCustomerVO();
        if(flag){
            bindVO.setBindResult(true);
            bindVO.setFailMsg("维护绑定成功");
        }else {
            bindVO.setBindResult(false);
            ErpCustomerDTO erpCustomerDTO = erpCustomerApi.findById(form.getErpCustomer().getId());
            bindVO.setErpCustomer(PojoUtils.map(erpCustomerDTO,ErpCustomerVO.class));
            bindVO.setFailMsg(erpCustomerDTO.getSyncMsg());
        }
        return Result.success(bindVO);
    }

    @ApiOperation(value = "erp客户绑定天眼查企业", httpMethod = "POST")
    @PostMapping("/bindTyc")
    @Log(title = "erp客户绑定天眼查企业", businessType = BusinessTypeEnum.UPDATE)
    public Result<BindErpCustomerVO> bindTyc(@CurrentUser CurrentAdminInfo adminInfo,@RequestBody BindCustomerForm form){
        SaveEnterpriseCustomerRequest request = new SaveEnterpriseCustomerRequest();
        request.setOpUserId(adminInfo.getCurrentUserId());
        request.setOpTime(new Date());
        TycEnterpriseInfoForm tycEnterprise = form.getTycEnterprise();
        if(null==tycEnterprise){
            return Result.failed("缺少天眼查企业信息");
        }
        request.setTycEnterprise(PojoUtils.map(form.getTycEnterprise(),TycEnterpriseInfoDTO.class));
        if(null==form.getErpCustomer()){
            return Result.failed("缺少erp客户");
        }
        request.setErpCustomer(PojoUtils.map(form.getErpCustomer(),ErpCustomerDTO.class));
        log.info("erp客户绑定天眼查参数：{}",request);
        if(StringUtils.isBlank(form.getTycEnterprise().getCreditCode())){
            return Result.failed("该天眼查企业缺少唯一社会信用代码无法绑定");
        }
        EnterpriseDTO enterpriseDTO = enterpriseApi.getByLicenseNumber(form.getTycEnterprise().getCreditCode());
        if(null!=enterpriseDTO){
            return Result.failed("该天眼查企业唯一社会信用代码已存在");
        }
        Boolean flag = erpCustomerApi.relationCustomerByTyc(request);
        BindErpCustomerVO bindVO = new BindErpCustomerVO();
        if(flag){
            bindVO.setBindResult(true);
            bindVO.setFailMsg("天眼查绑定成功");
        }else {
            bindVO.setBindResult(false);
            ErpCustomerDTO erpCustomerDTO = erpCustomerApi.findById(form.getErpCustomer().getId());
            erpCustomerDTO.setName(tycEnterprise.getName());
            erpCustomerDTO.setLicenseNo(tycEnterprise.getCreditCode());
            erpCustomerDTO.setProvince(tycEnterprise.getBase());
            erpCustomerDTO.setCity(tycEnterprise.getCity());
            erpCustomerDTO.setRegion(tycEnterprise.getDistrict());
            erpCustomerDTO.setAddress(tycEnterprise.getRegLocation());
            bindVO.setErpCustomer(PojoUtils.map(erpCustomerDTO,ErpCustomerVO.class));
            bindVO.setFailMsg(erpCustomerDTO.getSyncMsg());
        }
        return Result.success(bindVO);
    }

}
