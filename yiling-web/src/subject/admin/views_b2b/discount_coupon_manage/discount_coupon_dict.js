
// 活动状态
export function activityStatusArray() {
  return [
    {
      label: '未开始',
      value: 1
    },
    {
      label: '进行中',
      value: 2
    },
    {
      label: '已结束',
      value: 3
    }
  ];
}

// 优惠券状态
export function statusArray() {
  return [
    {
      label: '启用',
      value: 1
    },
    {
      label: '停用',
      value: 2
    },
    {
      label: '废弃',
      value: 3
    }
  ];
}

// 优惠券状态
export function couponTypeArray() {
  return [
    {
      label: '满减券',
      value: 1
    },
    {
      label: '满折券',
      value: 2
    }
  ];
}

// 发放方式
export function getTypeArray() {
  return [
    {
      label: '运营发放',
      value: 1
    },
    {
      label: '自动发放',
      value: 2
    },
    {
      label: '自主领取',
      value: 3
    },
    {
      label: '促销活动赠送',
      value: 4
    }
  ];
}

