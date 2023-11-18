package com.yiling.export.export.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.yiling.dataflow.order.bo.FlowPermissionsBO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2022/2/16
 */
@Slf4j
public class FlowUtil {

    private static final DecimalFormat decimalFormat = new DecimalFormat("##########.######");

    /**
     * 获取用户负责企业列表
     *
     * @param currentUserId
     * @param contactUserMap
     * @param flowPermissionsBO
     * @return
     */
    public static List<Long> getResponsibleEidList(Long currentUserId, Map<Long, List<EnterpriseDTO>> contactUserMap,
                                                   FlowPermissionsBO flowPermissionsBO) {
        // 用户没有负责企业
        if (MapUtil.isEmpty(contactUserMap) || !contactUserMap.keySet().contains(currentUserId)
            || CollUtil.isEmpty(contactUserMap.get(currentUserId))) {
            return ListUtil.empty();
        }

        // 有负责企业
        String ename = flowPermissionsBO.getEname();
        String provinceCode = flowPermissionsBO.getProvinceCode();
        String cityCode = flowPermissionsBO.getCityCode();
        String regionCode = flowPermissionsBO.getRegionCode();
        List<EnterpriseDTO> enterpriseList = contactUserMap.get(currentUserId);

        // 无商业公司相关的搜索条件，返回所有负责的公司id
        if (StrUtil.isBlank(ename) && StrUtil.isBlank(provinceCode) && StrUtil.isBlank(cityCode)
            && StrUtil.isBlank(regionCode)) {
            return enterpriseList.stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
        }

        // 有商业公司相关的搜索条件，进行条件过滤
        if (StrUtil.isNotBlank(ename)) {
            enterpriseList = enterpriseList.stream().filter(e -> ObjectUtil.equal(ename, e.getName())).collect(Collectors.toList());
        }
        if (StrUtil.isNotBlank(provinceCode)) {
            enterpriseList = enterpriseList.stream().filter(e -> ObjectUtil.equal(provinceCode, e.getProvinceCode())).collect(Collectors.toList());
        }
        if (StrUtil.isNotBlank(cityCode)) {
            enterpriseList = enterpriseList.stream().filter(e -> ObjectUtil.equal(cityCode, e.getCityCode())).collect(Collectors.toList());
        }
        if (StrUtil.isNotBlank(regionCode)) {
            enterpriseList = enterpriseList.stream().filter(e -> ObjectUtil.equal(regionCode, e.getRegionCode())).collect(Collectors.toList());
        }
        if (CollUtil.isEmpty(enterpriseList)) {
            return ListUtil.empty();
        } else {
            return enterpriseList.stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
        }
    }

    public static BigDecimal deimalcFormat(BigDecimal deimalc){
        if(ObjectUtil.isNull(deimalc)){
            return BigDecimal.ZERO;
        }
        return new BigDecimal(decimalFormat.format(deimalc));
    }

}
