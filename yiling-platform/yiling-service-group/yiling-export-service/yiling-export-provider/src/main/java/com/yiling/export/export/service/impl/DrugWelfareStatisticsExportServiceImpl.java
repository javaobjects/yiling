package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.bo.ExportDrugWelfareStatisticsBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.welfare.api.DrugWelfareApi;
import com.yiling.hmc.welfare.api.DrugWelfareGroupApi;
import com.yiling.hmc.welfare.api.DrugWelfareGroupCouponApi;
import com.yiling.hmc.welfare.dto.DrugWelfareDTO;
import com.yiling.hmc.welfare.dto.DrugWelfareGroupCouponDTO;
import com.yiling.hmc.welfare.dto.DrugWelfareStatisticsPageDTO;
import com.yiling.hmc.welfare.dto.request.DrugWelfareStatisticsPageRequest;
import com.yiling.hmc.welfare.enums.DrugWelfareCouponStatusEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.api.HmcUserApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.HmcUser;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: hongyang.zhang
 * @data: 2022/09/28
 */
@Slf4j
@Service("drugWelfareStatisticsExportService")
public class DrugWelfareStatisticsExportServiceImpl implements BaseExportQueryDataService<DrugWelfareStatisticsPageRequest> {

    @DubboReference
    DrugWelfareApi drugWelfareApi;

    @DubboReference
    DrugWelfareGroupApi drugWelfareGroupApi;

    @DubboReference
    HmcUserApi hmcUserApi;

    @DubboReference
    UserApi userApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    DrugWelfareGroupCouponApi drugWelfareGroupCouponApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<String, String>() {

        private static final long serialVersionUID = -6207056584544766132L;

        {
            put("userId", "系统用户id");
            put("nickName", "系统用户昵称");
            put("gender", "系统用户性别");
            put("mobile", "系统用户手机号");
            put("drugWelfareName", "用药福利计划名称");
            put("ename", "终端商家");
            put("sellerUserName", "商家销售人员");
            put("medicineUserName", "服药人姓名");
            put("medicineUserPhone", "服药人手机号");
            put("useSchedule", "使用进度");
            put("joinGroupId", "用户入组id");
            put("createTime", "入组时间");
            put("validTime", "有效期");
        }
    };

    @Override
    public QueryExportDataDTO queryData(DrugWelfareStatisticsPageRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<DrugWelfareStatisticsPageDTO> page;
        int current = 1;
        do {
            request.setCurrent(current);
            request.setSize(500);
            //  查询导出的数据填入data
            page = drugWelfareGroupApi.statisticsPage(request);
            List<DrugWelfareStatisticsPageDTO> records = page.getRecords();
            if (CollUtil.isEmpty(records)) {
                continue;
            }

            //用户信息
            List<Long> userIdList = records.stream().map(e -> e.getUserId()).distinct().collect(Collectors.toList());
            List<HmcUser> hmcUserList = hmcUserApi.listByIds(userIdList);
            Map<Long, HmcUser> hmcUserMap = hmcUserList.stream().collect(Collectors.toMap(HmcUser::getUserId, Function.identity()));
            //福利计划
            List<Long> drugWelfareIdList = records.stream().map(e -> e.getDrugWelfareId()).distinct().collect(Collectors.toList());
            List<DrugWelfareDTO> drugWelfareDTOS = drugWelfareApi.getByIdList(drugWelfareIdList);
            Map<Long, DrugWelfareDTO> drugWelfareMap = drugWelfareDTOS.stream().collect(Collectors.toMap(DrugWelfareDTO::getId, Function.identity()));
            //商家信息
            List<Long> eidList = records.stream().map(e -> e.getEid()).distinct().collect(Collectors.toList());
            List<EnterpriseDTO> enterpriseDTOS = enterpriseApi.listByIds(eidList);
            Map<Long, EnterpriseDTO> enterpriseMap = enterpriseDTOS.stream().collect(Collectors.toMap(EnterpriseDTO::getId, Function.identity()));
            //商家销售人员
            List<Long> sellerUserIdList = records.stream().map(e -> e.getSellerUserId()).distinct().collect(Collectors.toList());
            List<UserDTO> userDTOS = userApi.listByIds(sellerUserIdList);
            Map<Long, UserDTO> sellerUserMap = userDTOS.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));

            for (DrugWelfareStatisticsPageDTO dto : records) {
                ExportDrugWelfareStatisticsBO bo = new ExportDrugWelfareStatisticsBO();
                bo.setUserId(dto.getUserId());
                if (Objects.nonNull(hmcUserMap.get(dto.getUserId())) && StringUtils.isNotBlank(hmcUserMap.get(dto.getUserId()).getNickName())){
                    bo.setNickName(hmcUserMap.get(dto.getUserId()).getNickName());
                }
                if (Objects.nonNull(hmcUserMap.get(dto.getUserId())) && Objects.nonNull(hmcUserMap.get(dto.getUserId()).getNickName())){
                    Integer gender = hmcUserMap.get(dto.getUserId()).getGender();
                    if (gender == 0) {
                        bo.setGender("未知");
                    } else if (gender == 1) {
                        bo.setGender("男性");
                    } else {
                        bo.setGender("女性");
                    }
                }
                if (Objects.nonNull(hmcUserMap.get(dto.getUserId())) && StringUtils.isNotBlank(hmcUserMap.get(dto.getUserId()).getMobile())){
                    bo.setMobile(hmcUserMap.get(dto.getUserId()).getMobile());
                }
                bo.setDrugWelfareName(drugWelfareMap.get(dto.getDrugWelfareId()).getName());
                bo.setEname(enterpriseMap.get(dto.getEid()).getName());
                if (Objects.nonNull(sellerUserMap.get(dto.getSellerUserId()))) {
                    bo.setSellerUserName(sellerUserMap.get(dto.getSellerUserId()).getName());
                } else {
                    bo.setSellerUserName("无");
                }
                bo.setMedicineUserName(dto.getMedicineUserName());
                bo.setMedicineUserPhone(dto.getMedicineUserPhone());
                List<DrugWelfareGroupCouponDTO> groupCouponList = drugWelfareGroupCouponApi.getWelfareGroupCouponByGroupId(dto.getId());
                Long usedCount = groupCouponList.stream().filter(t -> t.getCouponStatus().equals(DrugWelfareCouponStatusEnum.USED.getCode())).count();
                StringBuffer useSchedule = new StringBuffer();
                useSchedule.append(usedCount);
                useSchedule.append("/");
                useSchedule.append(groupCouponList.size());
                bo.setUseSchedule(useSchedule.toString());
                bo.setJoinGroupId(dto.getJoinGroupId());
                bo.setCreateTime(DateUtil.format(dto.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                StringBuffer validTime = new StringBuffer();
                validTime.append(DateUtil.format(drugWelfareMap.get(dto.getDrugWelfareId()).getBeginTime(), "yyyy-MM-dd HH:mm:ss"));
                validTime.append(" 至 ");
                validTime.append(DateUtil.format(drugWelfareMap.get(dto.getDrugWelfareId()).getEndTime(), "yyyy-MM-dd HH:mm:ss"));
                bo.setValidTime(validTime.toString());

                Map<String, Object> dataPojo = BeanUtil.beanToMap(bo);
                data.add(dataPojo);
            }
            current = current + 1;
        } while (CollectionUtils.isNotEmpty(page.getRecords()));

        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("用药福利统计导出");
        // 页签字段
        exportDataDTO.setFieldMap(FIELD);
        // 页签数据
        exportDataDTO.setData(data);

        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportDataDTO);
        result.setSheets(sheets);
        return result;
    }

    @Override
    public DrugWelfareStatisticsPageRequest getParam(Map<String, Object> map) {
        return PojoUtils.map(map, DrugWelfareStatisticsPageRequest.class);
    }
}
