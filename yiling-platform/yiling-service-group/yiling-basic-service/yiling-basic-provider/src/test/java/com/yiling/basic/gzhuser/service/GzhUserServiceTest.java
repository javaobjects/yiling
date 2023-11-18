package com.yiling.basic.gzhuser.service;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.BaseTest;
import com.yiling.basic.dict.entity.DictDataDO;
import com.yiling.basic.dict.service.DictDataService;
import com.yiling.basic.gzh.service.GzhUserService;
import com.yiling.basic.wx.dto.GzhUserDTO;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author: xuan.zhou
 * @date: 2021/6/3
 */
@Slf4j
public class GzhUserServiceTest extends BaseTest {

    @Autowired
    GzhUserService gzhUserService;

    @Test
    public void pageList() {
        QueryPageListRequest request = new QueryPageListRequest();
        Page<GzhUserDTO> gzhUserDTOPage = gzhUserService.pageList(request);
        System.out.println(JSONUtil.toJsonStr(gzhUserDTOPage));
    }


}
