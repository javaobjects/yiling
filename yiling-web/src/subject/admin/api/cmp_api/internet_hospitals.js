import request from '@/subject/admin/utils/request'

//终端药店下拉
export function pharmacyList() {
  return request({
    url: '/hmc/api/v1/pharmacy/pharmacyList',
    method: 'get',
    params: {}
  });
}
//终端药店列表
export function pharmacyPageList(
  //终端商家id
  eid,
  //状态：1-启用，2-停用
  status,
  current,
  size
) {
  return request({
    url: '/hmc/api/v1/pharmacy/pharmacyPageList',
    method: 'post',
    data: {
      eid,
      status,
      current,
      size
    }
  });
}
//保存终端药店
export function savePharmacy(
  //终端商家id
  eid,
  //终端商家名称
  ename
) {
  return request({
    url: '/hmc/api/v1/pharmacy/savePharmacy',
    method: 'post',
    data: {
      eid,
      ename
    }
  });
}
//商家问诊码
export function pharmacyQrCode(
  id
) {
  return request({
    url: '/hmc/api/v1/pharmacy/pharmacyQrCode',
    method: 'post',
    data: {
      id
    }
  });
}
//停启用
export function updatePharmacyStatus(
  id,
  //状态：1-启用，2-停用
  status
) {
  return request({
    url: '/hmc/api/v1/pharmacy/updatePharmacyStatus',
    method: 'post',
    data: {
      id,
      status
    }
  });
}