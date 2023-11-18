package com.yiling.basic.location.util;

import java.util.List;

import com.yiling.basic.location.dto.LocationSelectTreeDTO;
import com.yiling.basic.location.dto.LocationTreeDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrFormatter;

/**
 * @author: xuan.zhou
 * @date: 2022/1/18
 */
public class LocationTreeUtils {

    public static final String TEMPLATE_1 = "{}个省";
    public static final String TEMPLATE_2 = "{}个省{}个市";
    public static final String TEMPLATE_3 = "{}个省{}个市{}个区域";

    /**
     * 获取区域树节点编码
     *
     * @param locationTree 区域树
     * @return java.util.List<java.lang.String>
     * @author xuan.zhou
     * @date 2022/3/9
     **/
    public static List<String> listTreeCode(List<LocationTreeDTO> locationTree) {
        if (CollUtil.isEmpty(locationTree)) {
            return ListUtil.empty();
        }

        List<String> codeList = CollUtil.newArrayList();
        locationTree.forEach(e -> {
            codeList.add(e.getCode());
            codeList.addAll(listTreeCode(e.getChildren()));
        });

        return codeList;
    }

    /**
     * 获取区域选择树
     *
     * @param locationTree 选择树
     * @param selectedLocationCode 已选中区域编码列表
     * @return java.util.List<com.yiling.basic.location.dto.LocationSelectTreeDTO>
     * @author xuan.zhou
     * @date 2022/3/9
     **/
    public static List<LocationSelectTreeDTO> getLocationSelectTree(List<LocationTreeDTO> locationTree, List<String> selectedLocationCode) {
        if (CollUtil.isEmpty(locationTree)) {
            return ListUtil.empty();
        }

        List<LocationSelectTreeDTO> list = CollUtil.newArrayList();
        locationTree.forEach(e -> {
            LocationSelectTreeDTO location = new LocationSelectTreeDTO();
            location.setCode(e.getCode());
            location.setName(e.getName());
            // 是否选中
            if (selectedLocationCode.contains(e.getCode())) {
                location.setSelectedFlag(true);
                selectedLocationCode.remove(e.getCode());
            }
            // 子节点
            location.setChildren(getLocationSelectTree(e.getChildren(), selectedLocationCode));

            list.add(location);
        });

        return list;
    }

    /**
     * 获取区域全选树
     *
     * @param locationTree 选择树
     * @return java.util.List<com.yiling.basic.location.dto.LocationSelectTreeDTO>
     * @author xuan.zhou
     * @date 2022/3/16
     **/
    public static List<LocationSelectTreeDTO> getLocationSelectedAllTree(List<LocationTreeDTO> locationTree) {
        if (CollUtil.isEmpty(locationTree)) {
            return ListUtil.empty();
        }

        List<LocationSelectTreeDTO> list = CollUtil.newArrayList();
        locationTree.forEach(e -> {
            LocationSelectTreeDTO location = new LocationSelectTreeDTO();
            location.setCode(e.getCode());
            location.setName(e.getName());
            location.setSelectedFlag(true);
            // 子节点
            location.setChildren(getLocationSelectedAllTree(e.getChildren()));

            list.add(location);
        });

        return list;
    }

    /**
     * 获取区域树描述
     *
     * @param locationTree 区域树列表
     * @param level 区域树深度层级：1-省 2-市 3-区
     * @return
     */
    public static String getLocationTreeDesc(List<LocationTreeDTO> locationTree, int level) {
        Assert.isTrue(level >= 1 && level <= 3);

        Integer provinceSize = 0, citySize = 0, regionSize = 0;

        if (level >= 1) {
            provinceSize = locationTree.size();
        }
        if (level >= 2) {
            citySize = getChildrenSize(locationTree);
        }
        if (level >= 3) {
            regionSize = locationTree.stream().map(e -> {
                return getChildrenSize(e.getChildren());
            }).reduce(0, Integer::sum);
        }

        if (level == 1) {
            return StrFormatter.format(TEMPLATE_1, provinceSize);
        } else if (level == 2) {
            return StrFormatter.format(TEMPLATE_2, provinceSize, citySize);
        } else if (level == 3) {
            return StrFormatter.format(TEMPLATE_3, provinceSize, citySize, regionSize);
        }

        return "";
    }

    private static Integer getChildrenSize(List<LocationTreeDTO> locationTree) {
        if (CollUtil.isEmpty(locationTree)) {
            return 0;
        }

        Integer size = locationTree.stream().map(e -> {
            if (CollUtil.isNotEmpty(e.getChildren())) {
                return e.getChildren().size();
            }
            return 0;
        }).reduce(0, Integer::sum);

        return size;
    }
}
