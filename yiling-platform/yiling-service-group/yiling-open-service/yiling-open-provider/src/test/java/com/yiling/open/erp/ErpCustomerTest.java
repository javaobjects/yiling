package com.yiling.open.erp;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.fastjson.JSONArray;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.basic.location.dto.RegionFullViewDTO;
import com.yiling.open.BaseTest;
import com.yiling.open.erp.dto.ErpCustomerDTO;
import com.yiling.open.erp.dto.request.ErpCustomerQueryRequest;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.handler.ErpCustomerHandler;
import com.yiling.open.erp.service.ErpCustomerService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;

/**
 * @author: houjie.sun
 * @date: 2021/9/2
 */
public class ErpCustomerTest extends BaseTest {

    @Autowired
    private ErpCustomerHandler  erpCustomerHandler;

    @Autowired
    private ErpCustomerService erpCustomerService;

    @DubboReference
    private LocationApi         locationApi;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final String REGION_REGEX = "(?<province>[^省]+自治区|.*?省|.*?行政区|.*?市)(?<city>[^市]+自治州|.*?地区|.*?行政单位|.+盟|市辖区|.*?市|.*?县)(?<county>[^县]+县|.+区|.+市|.+旗|.+海域|.+岛)?(?<town>[^区]+区|.+镇)?(?<village>.*)";
    private static final String REDIS_KEY = "location_RegionFullView";


    @Test
    public void ErpCustomerTest1() {
        String json = "[{\"address\":\"湖北省武汉市江夏区关山大道888号\",\"city\":\"武汉市\",\"contact\":\"一级商小郑\",\"customer_type\":\"单体药房\",\"dataMd5\":\"84106FA3F54E0CFBA7DA28731A4F5DBC\",\"erpPrimaryKey\":\"1009\",\"failedCount\":0,\"group_name\":\"普通客户\",\"inner_code\":\"1009\",\"license_no\":\"91370602791549QYTD\",\"name\":\"武汉光谷企业天地股份有限公司\",\"opTime\":1644891389912,\"oper_type\":2,\"phone\":\"13554098888\",\"province\":\"湖北省\",\"region\":\"江夏区\",\"sn\":\"1009\",\"su_dept_no\":\"\",\"su_id\":27,\"taskNo\":\"10000001\"}]";
        erpCustomerHandler.handleCustomerMqSync(27L, ErpTopicName.ErpCustomer.getMethod(), JSONArray.parseArray(json, ErpCustomerDTO.class));
    }

    @Test
    public void LocationApiTest() {
        //        String province = "江苏省";
        //        String city = "扬州市";
        //        String region = "江都区";

        String address = "江苏省扬州市江都区扬子江中路186号扬州智谷科技综合体A座11、18层1101、1108-1112、1802房间";
        // 根据地址获取省市区
        Map<String, String> map = getAddressInfo(address);
        String province = map.get("province");
        String city = map.get("city");
        String region = map.get("county");

        // 省市区名称校验
        boolean locationFlag = locationApi.validateName(province, city, region);
        // 根据省市区名称获取省市区编码
        String[] locationArray = locationApi.getCodesByNames(province, city, region);

    }

//    public Map<String, String> getAddressInfo(String address) {
//        String REGION_REGEX1 = "(?<province>[^省]+自治区|.*?省|.*?行政区|.*?市)(?<city>[^市]+自治州|.*?地区|.*?行政单位|.+盟|市辖区|.*?市|.*?县)(?<county>[^县]+县|.+区|.+市|.+旗|.+海域|.+岛)?(?<town>[^区]+区|.+镇)?(?<village>.*)";
//        Matcher m = Pattern.compile(REGION_REGEX1).matcher(address);
//        String province = null, city = null, county = null, town = null, village = null;
//        Map<String, String> row = new HashMap<>();
//        while (m.find()) {
//            row = new LinkedHashMap<String, String>();
//            province = m.group("province");
//            city = m.group("city");
//            county = m.group("county");
//            row.put("province", county == null ? "" : province.trim());
//            row.put("city", city == null ? "" : city.trim());
//            row.put("county", county == null ? "" : county.trim());
//        }
//        return row;
//    }

    @Test
    public void test() {
        ErpCustomerDTO erpCustomerDTO = new ErpCustomerDTO();
        erpCustomerDTO.setProvince("河北省");
        erpCustomerDTO.setProvinceCode("130000");
        erpCustomerDTO.setCity("石家庄市");
        erpCustomerDTO.setCityCode("130100");
        erpCustomerDTO.setRegion("");
        erpCustomerDTO.setRegionCode("");
        erpCustomerDTO.setAddress("正定县");

        String test = testAddress(erpCustomerDTO);

    }

    @Test
    public void test1(){
        ErpCustomerQueryRequest request = new ErpCustomerQueryRequest();
        request.setSyncStatus(3);
        request.setQueryLimit(3000);
        List<ErpCustomerDTO> list = erpCustomerService.getErpCustomerListBySyncStatus(request);
        System.out.println(list);
    }

    public String testAddress(ErpCustomerDTO erpCustomerDTO){
        if (isRebuildRegion(erpCustomerDTO)) {
            // 根据地址获取省市区
//            Map<String, String> map = getAddressInfo(erpCustomerDO.getAddress());
//            String province = map.get("province");
//            String city = map.get("city");
//            String region = map.get("county");
//            if (StringUtils.isBlank(erpCustomerDO.getProvince())) {
//                if (StringUtils.isBlank(province)) {
//                    return "省不能为空";
//                }
//                erpCustomerDO.setProvince(province);
//            }
//            if (StringUtils.isBlank(erpCustomerDO.getCity())) {
//                if (StringUtils.isBlank(city)) {
//                    return "市不能为空";
//                }
//                erpCustomerDO.setCity(city);
//            }
//            if (StringUtils.isBlank(erpCustomerDO.getRegion())) {
//                if (StringUtils.isBlank(region)) {
//                    return "区不能为空";
//                }
//                erpCustomerDO.setRegion(region);
//            }

            RegionFullViewDTO provinceCityTown = findProvinceCityTown(erpCustomerDTO.getProvince(), erpCustomerDTO.getCity(), erpCustomerDTO.getRegion(), erpCustomerDTO.getAddress());
            if(ObjectUtil.isNull(provinceCityTown)){
                return "省市区不能为空";
            }
            if(StrUtil.isBlank(provinceCityTown.getProvinceName())){
                return "省不能为空";
            }
            erpCustomerDTO.setProvince(provinceCityTown.getProvinceName());
            erpCustomerDTO.setProvinceCode(provinceCityTown.getProvinceCode());
            if(StrUtil.isBlank(provinceCityTown.getCityName())){
                return "市不能为空";
            }
            erpCustomerDTO.setCity(provinceCityTown.getCityName());
            erpCustomerDTO.setCityCode(provinceCityTown.getCityCode());
            if(StrUtil.isBlank(provinceCityTown.getRegionName())){
                return "区不能为空";
            }
            erpCustomerDTO.setRegion(provinceCityTown.getRegionName());
            erpCustomerDTO.setRegionCode(provinceCityTown.getRegionCode());

        }

        // 省市区名称校验
//        boolean locationFlag = locationApi.validateName(erpCustomerDO.getProvince(), erpCustomerDO.getCity(), erpCustomerDO.getRegion());
//        if (!locationFlag) {
//            return "省市区名称校验不通过";
//        }
//
//        // 根据省市区名称获取省市区编码
//        String[] locationArray = locationApi.getCodesByNames(erpCustomerDO.getProvince(), erpCustomerDO.getCity(), erpCustomerDO.getRegion());
//        if (ArrayUtils.isEmpty(locationArray)) {
//            return "根据省市区名称获取省市区编码为空";
//        }
//        String provinceCode = locationArray[0];
//        String cityCode = locationArray[1];
//        String regionCode = locationArray[2];
//        if (StringUtils.isBlank(provinceCode)) {
//            return "获取省份编码为空";
//        }
//        erpCustomerDO.setProvinceCode(provinceCode);
//        if (StringUtils.isBlank(cityCode)) {
//            return "获取市编码为空";
//        }
//        erpCustomerDO.setCityCode(cityCode);
//        if (StringUtils.isBlank(regionCode)) {
//            return "获取区域编码为空";
//        }
//        erpCustomerDO.setRegionCode(regionCode);
        return null;
    }

    public boolean isRebuildRegion(ErpCustomerDTO erpCustomerDTO){
        if(ObjectUtil.isNull(erpCustomerDTO)){
            return false;
        }
        // 1.省市区，三个字段有空的
        if (StringUtils.isBlank(erpCustomerDTO.getProvince()) || StringUtils.isBlank(erpCustomerDTO.getCity())
                || StringUtils.isBlank(erpCustomerDTO.getRegion())) {
            return true;
        }
        // 2.省市区，三个字段不标准的、不配套的
        // 省市区名称校验
//        boolean locationFlag = locationApi.validateName(erpCustomerDO.getProvince(), erpCustomerDO.getCity(), erpCustomerDO.getRegion());
//        if (!locationFlag) {
//            return true;
//        }
        // 根据省市区名称获取省市区编码
        String[] locationArray = locationApi.getCodesByNames(erpCustomerDTO.getProvince(), erpCustomerDTO.getCity(), erpCustomerDTO.getRegion());
        if (ArrayUtils.isEmpty(locationArray)) {
            return true;
        }
        String provinceCode = locationArray[0];
        String cityCode = locationArray[1];
        String regionCode = locationArray[2];
        if (StringUtils.isBlank(provinceCode)) {
            return true;
        }
        if (StringUtils.isBlank(cityCode)) {
            return true;
        }
        if (StringUtils.isBlank(regionCode)) {
            return true;
        }
        return false;
    }

    public RegionFullViewDTO findProvinceCityTown(String province, String city, String town, String address) {
        List<RegionFullViewDTO> regionFullViewDTOList = null;
        String key = REDIS_KEY + SecureUtil.md5(REDIS_KEY);
        String regionJson = (String)stringRedisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(regionJson)) {
            regionFullViewDTOList = locationApi.getAllProvinceCityRegionList();
            stringRedisTemplate.opsForValue().set(key, JSONArray.toJSONString(regionFullViewDTOList), 12, TimeUnit.HOURS);
        } else {
            regionFullViewDTOList = JSONArray.parseArray(regionJson, RegionFullViewDTO.class);
        }

        if (CollUtil.isEmpty(regionFullViewDTOList)) {
            return null;
        }
        //首先判断towm是否为空
        if (StrUtil.isBlank(town)) {
            Map<String, String> addressInfo = getAddressInfo(address);
            if (addressInfo != null && addressInfo.get("county") != null) {
                town = addressInfo.get("county");
            }
        }
        //在通过address匹配第3级
        List<RegionFullViewDTO> fullViewDTOList = new ArrayList<>();
        if (town != null) {//从town信息获取第3级
            for (RegionFullViewDTO regionFullView : regionFullViewDTOList) {
                if (regionFullView.getRegionName().contains(town)) {
                    fullViewDTOList.add(regionFullView);
                }
            }
        }

        if (CollUtil.isEmpty(fullViewDTOList)) {
            for (RegionFullViewDTO regionFullView : regionFullViewDTOList) {
                if (address.contains(regionFullView.getRegionName())) {
                    fullViewDTOList.add(regionFullView);
                }
            }
        }

        if (CollUtil.isEmpty(fullViewDTOList)) {
            return null;
        }

        if (fullViewDTOList.size() == 1) {//如果只有一条匹配
            return fullViewDTOList.get(0);
        }

        List<RegionFullViewDTO> cityViewDTOList = new ArrayList<>();
        //如果已经有确定的市
        if (StringUtils.isNotEmpty(city)) {
            for (RegionFullViewDTO regionFullView : fullViewDTOList) {
                if (regionFullView.getCityName().contains(city)) {//匹配第2个数据
                    cityViewDTOList.add(regionFullView);
                }
            }
        }

        if (CollUtil.isEmpty(cityViewDTOList)) {
            for (RegionFullViewDTO regionFullView : fullViewDTOList) {
                if (address.contains(regionFullView.getCityName())) {//匹配第2个数据
                    cityViewDTOList.add(regionFullView);
                }
            }
        }

        if (CollUtil.isEmpty(cityViewDTOList)) {
            return null;
        }

        if (cityViewDTOList.size() == 1) {//如果只有一条匹配
            return cityViewDTOList.get(0);
        }

        List<RegionFullViewDTO> provinceViewDTOList = new ArrayList<>();
        //如果已经有确定的省
        if (StringUtils.isNotEmpty(province)) {
            for (RegionFullViewDTO regionFullView : fullViewDTOList) {
                if (regionFullView.getProvinceName().contains(province)) {//匹配第2个数据
                    provinceViewDTOList.add(regionFullView);
                }
            }
        }

        if (CollUtil.isEmpty(provinceViewDTOList)) {
            for (RegionFullViewDTO regionFullView : cityViewDTOList) {
                if (address.contains(regionFullView.getProvinceName())) {//匹配第2个数据
                    provinceViewDTOList.add(regionFullView);
                }
            }
        }

        if (CollUtil.isEmpty(provinceViewDTOList)) {
            return null;
        }

        if (provinceViewDTOList.size() == 1) {//如果只有一条匹配
            return cityViewDTOList.get(0);
        }
        return null;
    }


    public Map<String, String> getAddressInfo(String address) {
        String regex = "(?<province>[^省]+自治区|.*?省|.*?行政区|.*?市)(?<city>[^市]+自治州|.*?地区|.*?行政单位|.+盟|市辖区|.*?市|.*?县)(?<county>[^县]+县|.+区|.+市|.+旗|.+海域|.+岛)?(?<town>[^区]+区|.+镇)?(?<village>.*)";
        Matcher m = Pattern.compile(regex).matcher(address);
        String province = null, city = null, county = null, town = null, village = null;
        Map<String, String> row = null;
        while (m.find()) {
            row = new LinkedHashMap<String, String>();
            county = m.group("county");
            row.put("county", county == null ? "" : county.trim());
        }
        return row;
    }


}
