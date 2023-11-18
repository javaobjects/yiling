import request from '@/subject/admin/utils/request'

// 导航列表
export function getNavList(current, size, state, name, startCreateTime, endCreateTime) {
  return request({
    url: '/pop/api/v1/navigation/info/get',
    method: 'post',
    data: {
      current,
      size,
      name,
      startCreateTime,
      endCreateTime,
      state: state === 0 ? undefined : state
    }
  })
}

// 保存导航
export function saveNav(link, name, sort, state) {
  return request({
    url: '/pop/api/v1/navigation/info/save',
    method: 'post',
    data: {
      link,
      name,
      sort,
      state
    }
  })
}

// 更新导航
export function updateNav(id, link, name, sort, state) {
  return request({
    url: '/pop/api/v1/navigation/info/update',
    method: 'post',
    data: {
      link,
      name,
      sort,
      state,
      id
    }
  })
}

// banner列表
export function getBannerList(current, size, status, title, createTimeStart, createTimeEnd, startTime, endTime) {
  return request({
    url: '/pop/api/v1/banner/pageList',
    method: 'post',
    data: {
      current,
      size,
      title,
      createTimeStart,
      createTimeEnd,
      startTime,
      endTime,
      status
    }
  })
}

// 启用停用banner
export function updateBannerStatus(id, status) {
  return request({
    url: '/pop/api/v1/banner/update/status',
    method: 'get',
    params: {
      id,
      status
    }
  })
}

// 运营后台获取banner详细信息
export function getBannerDetail(id) {
  return request({
    url: '/pop/api/v1/banner/get',
    method: 'get',
    params: {
      id
    }
  })
}

// 保存banner
export function saveBanner(status, title, startTime, endTime, sort, pic, linkType, linkUrl) {
  return request({
    url: '/pop/api/v1/banner/save',
    method: 'post',
    data: {
      status,
      title,
      startTime,
      endTime,
      sort,
      pic,
      linkType,
      linkUrl
    }
  })
}

// 更新banner
export function updateBanner(id, status, title, startTime, endTime, sort, pic, linkType, linkUrl) {
  return request({
    url: '/pop/api/v1/banner/update',
    method: 'post',
    data: {
      id,
      status,
      title,
      startTime,
      endTime,
      sort,
      pic,
      linkType,
      linkUrl
    }
  })
}

// 推荐位列表
export function getRecommendList(current, size, status, title) {
  return request({
    url: '/pop/api/v1/recommend/pageList',
    method: 'post',
    data: {
      current,
      size,
      title,
      status: status === 0 ? undefined : status
    }
  })
}

// 推荐位商品列表
export function getRecommendProductList(recommendId, current, size) {
  return request({
    url: '/pop/api/v1/recommend/goods/pageList',
    method: 'post',
    data: {
      current,
      size,
      recommendId
    }
  })
}

// 可选择商品列表
export function getChooseList(recommendId, categoryId, name, licenseNo, current, size) {
  return request({
    url: '/pop/api/v1/admin/goods/popList',
    method: 'post',
    data: {
      categoryId,
      current,
      size,
      recommendId,
      name,
      licenseNo
    }
  })
}

// 保存推荐位
export function saveRecommend(title, status, goodsList) {
  return request({
    url: '/pop/api/v1/recommend/save',
    method: 'post',
    data: {
      title,
      status,
      goodsList
    }
  })
}

// 更新推荐位
export function updateRecommend(id, title, status, goodsList) {
  return request({
    url: '/pop/api/v1/recommend/update',
    method: 'post',
    data: {
      title,
      status,
      goodsList,
      id
    }
  })
}

// 更新推荐位状态
export function updateRecommendStatus(id, status) {
  return request({
    url: '/pop/api/v1/recommend/update/status',
    method: 'post',
    data: {
      status,
      id
    }
  })
}

// -----------热词管理------------------
//管理后台获取热词详情列表
export function getHotWordList(current, size, state, name, starCreateTime, endCreateTime, startTime, endTime) {
  return request({
    url: '/pop/api/v1/goods/hot/words/get',
    method: 'post',
    data: {
      current,
      size,
      name,
      starCreateTime,
      endCreateTime,
      startTime,
      endTime,
      state
    }
  })
}

// 管理后台获取热词详细信息
export function getHotWordDetail(id) {
  return request({
    url: '/pop/api/v1/goods/hot/words/get/details',
    method: 'get',
    params: {
      id
    }
  })
}

// 管理后台保存热词信息
export function saveHotWords(name, state, startTime, endTime, sort, goodsId) {
  return request({
    url: '/pop/api/v1/goods/hot/words/save',
    method: 'post',
    data: {
      name,
      state,
      startTime,
      endTime,
      sort,
      goodsId
    }
  })
}

// 管理后台修改热词信息
export function updateHotWords(id, name, state, startTime, endTime, sort, goodsId) {
  return request({
    url: '/pop/api/v1/goods/hot/words/update',
    method: 'post',
    data: {
      id,
      name,
      state,
      startTime,
      endTime,
      sort,
      goodsId
    }
  })
}

// -----------分类商品管理------------------
// 运营后台获取商品分类列表
export function getCategoryList(current, size, status, name) {
  return request({
    url: '/pop/api/v1/category/pageList',
    method: 'post',
    data: {
      current,
      size,
      name,
      status
    }
  })
}

// 运营后台获取分类商品详细信息
export function getCategoryDetail(id) {
  return request({
    url: '/pop/api/v1/category/get',
    method: 'get',
    params: {
      id
    }
  })
}

// 运营后台编辑商品分类信息
export function updateCategory(id, name, status, sort, goodsList) {
  return request({
    url: '/pop/api/v1/category/update',
    method: 'post',
    data: {
      id,
      name,
      status,
      sort,
      goodsList
    }
  })
}

// 运营后台编辑商品分类信息
export function saveCategory(name, status, sort, goodsList) {
  return request({
    url: '/pop/api/v1/category/save',
    method: 'post',
    data: {
      name,
      status,
      sort,
      goodsList
    }
  })
}

// -------------公告列表-------------------
// 获取公告列表
export function getNoticeList(current, size, title, state, startCreateTime, endCreateTime) {
  return request({
    url: '/pop/api/v1/notice/info/get',
    method: 'post',
    data: {
      current,
      size,
      title,
      state,
      startCreateTime,
      endCreateTime
    }
  })
}

// 获取公告详情
export function getNoticeDetail(id) {
  return request({
    url: '/pop/api/v1/notice/info/getDetails',
    method: 'get',
    params: {
      id
    }
  })
}

// 保存公告
export function saveNotice(id, title, content, state, startTime, endTime) {
  return request({
    url: '/pop/api/v1/notice/info/save',
    method: 'post',
    data: {
      id,
      title,
      state,
      content,
      startTime,
      endTime
    }
  })
}

