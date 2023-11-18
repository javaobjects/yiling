import request from '@/subject/pop/utils/request';

// 获取cmp员工分页列表
export function getStaffList(current, size, mobile, name) {
  return request({
    url: '/admin/hmc/api/v1/employee/pageList',
    method: 'get',
    params: {
      current,
      size,
      mobile,
      name
    }
  });
}

//获取二维码;
export function getQrcode(id) {
  return request({
    url: '/admin/hmc/api/v1/employee/getQrcode',
    method: 'get',
    params: {
      id
    }
  });
}
// 药品福利计划二维码
export function getDrugWelfareQrcode(id) {
  return request({
    url: '/admin/hmc/api/v1/employee/getDrugWelfareQrcode',
    method: 'get',
    params: {
      id
    }
  });
}
