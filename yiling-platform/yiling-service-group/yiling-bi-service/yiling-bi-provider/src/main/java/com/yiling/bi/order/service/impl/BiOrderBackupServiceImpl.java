package com.yiling.bi.order.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.bi.order.dao.BiOrderBackupMapper;
import com.yiling.bi.order.entity.BiOrderBackupDO;
import com.yiling.bi.order.entity.BiOrderBackupTaskDO;
import com.yiling.bi.order.entity.OdsOrderDetailDO;
import com.yiling.bi.order.enums.BiTaskEnums;
import com.yiling.bi.order.service.BiOrderBackupService;
import com.yiling.bi.order.service.BiOrderBackupTaskService;
import com.yiling.bi.order.service.OdsOrderDetailService;
import com.yiling.framework.common.base.BaseServiceImpl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2022/9/19
 */
@Service
@Slf4j
public class BiOrderBackupServiceImpl extends BaseServiceImpl<BiOrderBackupMapper, BiOrderBackupDO> implements BiOrderBackupService {

    @Autowired
    private BiOrderBackupTaskService biOrderBackupTaskService;

    @Autowired
    private OdsOrderDetailService odsOrderDetailService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void backupBiOrderData() {
        //  将ods_order_detail数据按月份备份至bi_order_backup中
        String monthStr = getPreMonthStr();

        // 获取biOrderBackup最后抽取时间
        String lastExtractTime = baseMapper.getMaxExtractTime();
        if (StringUtils.isEmpty(lastExtractTime)) {
            // 获取ods_order_detail最早的抽取时间
            lastExtractTime = odsOrderDetailService.getMinExtractTime();
        }

        log.info("开始备份ods_order_detail [" + monthStr + "]数据...");

        //  创建bi_order_backup_task任务
        BiOrderBackupTaskDO biOrderBackupTaskDO = new BiOrderBackupTaskDO();
        biOrderBackupTaskDO.setTaskCode(BiTaskEnums.BI_ORDER_BACKUP.getCode());
        biOrderBackupTaskDO.setBackupMonth(monthStr);
        biOrderBackupTaskDO.setBackupStatus(1);
        biOrderBackupTaskDO.setStartTime(DateUtil.parse(lastExtractTime, "yyyy-MM-dd HH:mm:ss"));
        biOrderBackupTaskService.save(biOrderBackupTaskDO);

        //  从ods_order_detail读取当月数据，并备份至bi_order_backup中
        try {
            backupOrderData(lastExtractTime, biOrderBackupTaskDO.getId());

            String endTime = baseMapper.getMaxExtractTime();
            //  更新创建bi_order_backup_task任务状态
            biOrderBackupTaskService.updateBackupStatus(biOrderBackupTaskDO.getId(), endTime, 2);
            log.info("备份[" +monthStr  + "]ods_order_detail至bi_order_backup完成！");
        } catch (Exception e) {
            log.error("备份[" + monthStr +  "] ods_order_detail至bi_order_backup失败！");
            e.printStackTrace();
            //  修改任务状态为失败
            biOrderBackupTaskService.updateBackupStatus(biOrderBackupTaskDO.getId(), lastExtractTime, 3);
        }

    }

    @Override
    public List<BiOrderBackupDO> getNoMatchedBiOrderBackup(Date endDate, List<Long> b2bGoodsIdList) {
        List<BiOrderBackupDO> result = new ArrayList<>();
        //  分页获取
        LambdaQueryWrapper<BiOrderBackupDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BiOrderBackupDO::getMatchFlag, 0);
        wrapper.in(BiOrderBackupDO::getGoodsId, b2bGoodsIdList);
        wrapper.lt(BiOrderBackupDO::getOrderTime, endDate);
        wrapper.orderByDesc(BiOrderBackupDO::getOrderTime);   // 倒序
        Integer count = baseMapper.selectCount(wrapper);
        if (count == 0) {
            return result;
        }

        long current = 1L;
        long size = 1000L;
        while ((current - 1) * size < count) {
            Page<BiOrderBackupDO> page = new Page<>(current, size);
            Page<BiOrderBackupDO> pageResult = baseMapper.selectPage(page, wrapper);
            result.addAll(pageResult.getRecords());
            current++;
        }

        return result;
    }

    private String getPreMonthStr() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        Date preMonthDate = calendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        return sdf.format(preMonthDate);
    }

    private void backupOrderData(String extractTime, Long taskId) {
        Integer count = odsOrderDetailService.getCountByGeExtractTime(extractTime);

        if (count == 0) {
            log.warn("备份 ods_order_detail 数据为空！");
            return;
        }

        //  分页备份
        long current = 1L;
        long size = 1000L;
        while ((current - 1) * size < count) {
            Page<OdsOrderDetailDO> page = new Page<>(current, size);
            //            List<OdsOrderDetailDO> odsOrderDetailDOList = odsOrderDetailService.getByOrderMonth(monthStr, page);
            List<OdsOrderDetailDO> odsOrderDetailDOList = odsOrderDetailService.getGeExtractTime(extractTime, page);
            List<BiOrderBackupDO> biOrderBackupDOList = new ArrayList<>();
            for (OdsOrderDetailDO odsOrderDetailDO : odsOrderDetailDOList) {

                BiOrderBackupDO biOrderBackupDO = new BiOrderBackupDO();
                biOrderBackupDO.setTaskId(taskId);
                biOrderBackupDO.setOdsDetailId(odsOrderDetailDO.getDatailid());
                biOrderBackupDO.setOrderNo(odsOrderDetailDO.getOrderNo());
                biOrderBackupDO.setBuyerEid(odsOrderDetailDO.getBuyerEid());
                biOrderBackupDO.setBuyerEname(odsOrderDetailDO.getBuyerEname() != null ? odsOrderDetailDO.getBuyerEname().trim() : "");
                biOrderBackupDO.setSellerEid(odsOrderDetailDO.getSellerEid());
                biOrderBackupDO.setSellerEname(odsOrderDetailDO.getSellerEname() != null ? odsOrderDetailDO.getSellerEname().trim() : "");
                biOrderBackupDO.setOrderTime(odsOrderDetailDO.getCreateTime());
                biOrderBackupDO.setOrderStatus(odsOrderDetailDO.getOrderStatus());
                biOrderBackupDO.setPaymentMethod(odsOrderDetailDO.getPaymentMethod());
                biOrderBackupDO.setGoodsId(odsOrderDetailDO.getGoodsId());
                biOrderBackupDO.setGoodsName(odsOrderDetailDO.getGoodsName());
                biOrderBackupDO.setDeliveryQuantity(odsOrderDetailDO.getDeliveryQuantity());
                biOrderBackupDO.setReturnQuantity(odsOrderDetailDO.getReturnQuantity());
                biOrderBackupDO.setExtractTime(odsOrderDetailDO.getExtractTime());

                biOrderBackupDOList.add(biOrderBackupDO);
            }
            if (CollUtil.isNotEmpty(biOrderBackupDOList)) {
                // 若唯一索引冲突，则跳过不插入
                baseMapper.batchIgnoreInsert(biOrderBackupDOList);
            }
            current++;
        }
    }

    //    private List<Long> getAlreadyBackupDataByMonth(String monthStr) {
    //        List<BiOrderBackupDO> resultList = new ArrayList<>();
    //
    //        Integer count = baseMapper.getCountByOrderExtractMonth(monthStr);
    //        if (count == 0) {
    //            return new ArrayList<>();
    //        }
    //
    //        long current = 1L;
    //        long size = 1000L;
    //
    //        while ((current - 1) * size < count) {
    //            Page<BiOrderBackupDO> page = new Page<>(current, size);
    //            List<BiOrderBackupDO> list = baseMapper.getByOrderExtractMonth(monthStr, page);
    //            resultList.addAll(list);
    //            current++;
    //        }
    //        return resultList.stream().map(BiOrderBackupDO::getOdsDetailId).collect(Collectors.toList());
    //    }
}
