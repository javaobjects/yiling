//测评管理
import request from '@/subject/admin/utils/request'
// 新增量表
export function addHealthEvaluate(
  //量表名称
	healthEvaluateName,
  //量表类型 1-健康，2-心理，3-诊疗
	healthEvaluateType,
  //预计答题时间
	evaluateTime,
  //是否医生私有 0-否，1-是
	docPrivate,
  //发布平台
	lineIdList,
  //所属医生id
	docId,
  //量表描述
	healthEvaluateDesc,
  //封面
	coverImage,
  //分享背景图
	backImage,
  //科室
  deptIdList,
  //疾病
  diseaseIdList,
  //答题须知
  answerNotes,
  //是否有结果 0-否，1-是
  resultFlag
){
  return request({
    url: '/cms/api/v1/cms/healthEvaluate/addHealthEvaluate',
    method: 'post',
    data: {
      healthEvaluateName,
      healthEvaluateType,
      evaluateTime,
      docPrivate,
      lineIdList,
      docId,
      healthEvaluateDesc,
      coverImage,
      backImage,
      deptIdList,
      diseaseIdList,
      answerNotes,
      resultFlag
    }
  })
}
// 健康测评列表
export function queryEvaluatePage(
  //量表名称
  healthEvaluateName,
  //量表类型 1-健康，2-心理，3-诊疗
  healthEvaluateType,
  //发布状态 1-已发布，0-未发布
  publishFlag,
  //创建时间-开始	
  createTimeStart,
  //创建时间-截止	
  createTimeEnd,
  //修改时间-开始
  updateTimeStart,
  //修改时间-截止
  updateTimeEnd,
  //第几页，默认：1	
  current,
  //每页记录数，默认：10
  size
) {
  return request({
    url: '/cms/api/v1/cms/healthEvaluate/queryEvaluatePage',
    method: 'get',
    params: {
      healthEvaluateName,
      healthEvaluateType,
      publishFlag,
      createTimeStart,
      createTimeEnd,
      updateTimeStart,
      updateTimeEnd,
      current,
      size
    }
  })
}
// 健康测评详情
export function getDetailById(
  //量表数据id
  id
) {
  return request({
    url: '/cms/api/v1/cms/healthEvaluate/getDetailById',
    method: 'get',
    params: {
      id
    }
  })
}
//编辑健康测评
export function updateHealthEvaluate(
  //量表名称
	healthEvaluateName,
  //量表类型 1-健康，2-心理，3-诊疗
	healthEvaluateType,
  //预计答题时间
	evaluateTime,
  //是否医生私有 0-否，1-是
	docPrivate,
  //发布平台
	lineIdList,
  //所属医生id
	docId,
  //量表描述
	healthEvaluateDesc,
  //封面
	coverImage,
  //分享背景图
	backImage,
  //科室
  deptIdList,
  //疾病
  diseaseIdList,
  //答题须知
  answerNotes,
  //是否有结果 0-否，1-是
  resultFlag,
  //id
  id
){
  return request({
    url: '/cms/api/v1/cms/healthEvaluate/updateHealthEvaluate',
    method: 'post',
    data: {
      healthEvaluateName,
      healthEvaluateType,
      evaluateTime,
      docPrivate,
      lineIdList,
      docId,
      healthEvaluateDesc,
      coverImage,
      backImage,
      deptIdList,
      diseaseIdList,
      answerNotes,
      resultFlag,
      id
    }
  })
}
//添加测评结果
export function addHealthEvaluateResult(
  //结果集
  healthEvaluateResultList,
  //是否有结果 0-否，1-是
  ifResult
) {
  return request({
    url: '/cms/api/v1/cms/healthEvaluateResult/addHealthEvaluateResult',
    method: 'post',
    data: {
      healthEvaluateResultList,
      ifResult
    }
  })
}
// 获取测评结果
export function getResultListByEvaluateId(
  evaluateId
){
  return request({
    url: '/cms/api/v1/cms/healthEvaluateResult/getResultListByEvaluateId',
    method: 'get',
    params: {
      evaluateId
    }
  })
}
// 测评结果编辑页面
export function updateHealthEvaluateResult(
  //测评结果
  evaluateResult,
  //量表id
  healthEvaluateId,
  //健康小贴士	
  healthTip,
  id,
  //结果描述	
  resultDesc,
  //分值区间类型 1-小于，2-小于等于，3-等于，4-大于等于，5-大于
  scoreStartType,
  //开始区间
  scoreStart,
  //分值区间类型 1-小于，2-小于等于，3-等于，4-大于等于，5-大于
  scoreEndType,
  //结束区间
  scoreEnd
) {
  return request({
    url: '/cms/api/v1/cms/healthEvaluateResult/updateHealthEvaluateResult',
    method: 'post',
    data: {
      evaluateResult,
      healthEvaluateId,
      healthTip,
      id,
      resultDesc,
      scoreStartType,
      scoreStart,
      scoreEndType,
      scoreEnd
    }
  })
}
// 删除测评结果
export function delHealthEvaluateResultById(
  id
) {
  return request({
    url: '/cms/api/v1/cms/healthEvaluateResult/delHealthEvaluateResultById',
    method: 'post',
    data: {
      id
    }
  })
}
// 添加测评题目
export function addHealthEvaluateQuestion(
  //测评id	
  healthEvaluateId,
  //题目
  questionList
) {
  return request({
    url: '/cms/api/v1/cms/healthEvaluateQuestion/addHealthEvaluateQuestion',
    method: 'post',
    data: {
      healthEvaluateId,
      questionList
    }
  })
}
// 获取测评题目
export function getQuestionsById(
  //量表id
  id
){
  return request({
    url: '/cms/api/v1/cms/healthEvaluateQuestion/getQuestionsById',
    method: 'get',
    params: {
      id
    }
  })
}
//编辑测评题目
export function editQuestions(
  //测评id
  healthEvaluateId,
  //题目
  question
) {
  return request({
    url: '/cms/api/v1/cms/healthEvaluateQuestion/editQuestions',
    method: 'post',
    data: {
      healthEvaluateId,
      question
    }
  })
}
// 删除测评题目
export function delQuestionsById(
  id	
) {
  return request({
    url: '/cms/api/v1/cms/healthEvaluateQuestion/delQuestionsById',
    method: 'post',
    data: {
      id
    }
  })
}
//获取药品信息
export function getGoodsInfoByStandardId(
  //药品id
  standardId
){
  return request({
    url: '/cms/api/v1/cms/healthEvaluateMarket/getGoodsInfoByStandardId',
    method: 'get',
    params: {
      standardId
    }
  })
}
//营销设置
export function marketSet(
  //改善建议
  adviceList,
  //关联推荐药品
  goodsList,
  //推广服务
  promoteList
) {
  return request({
    url: '/cms/api/v1/cms/healthEvaluateMarket/marketSet',
    method: 'post',
    data: {
      adviceList,
      goodsList,
      promoteList
    }
  })
}
// 获取营销设置
export function getMarketSet(
  // 量表id
  id
) {
  return request({
    url: '/cms/api/v1/cms/healthEvaluateMarket/getMarketSet',
    method: 'post',
    data: {
      id
    }
  })
}
// 删除测评营销
export function delMarket(
  //id
  id,
  //添加类型 1-关联药品，2-改善建议，3-推广服务
  type
) {
  return request({
    url: '/cms/api/v1/cms/healthEvaluateMarket/delMarket',
    method: 'post',
    data: {
      id,
      type
    }
  })
}
// 发布健康测评
export function publishHealthEvaluate(
  //id
  id,
  //发布状态 1-已发布，0-未发布
  publishFlag
) {
  return request({
    url: '/cms/api/v1/cms/healthEvaluate/publishHealthEvaluate',
    method: 'post',
    data: {
      id,
      publishFlag
    }
  })
}