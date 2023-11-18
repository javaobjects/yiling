let ylUpload = () => import(/* webpackChunkName: "ylUpload" */'./Upload/SingleImage')
let ylMultipleUpload = () => import(/* webpackChunkName: "ylUpload" */'./Upload/MultipleImage')
let ylUploadFile = () => import(/* webpackChunkName: "ylUploadFile" */'./Upload/uploadFile')
let ylTable = () => import(/* webpackChunkName: "ylTable" */'./Table')
let ylChooseAddress = () => import(/* webpackChunkName: "ylChooseAddress" */'./ChooseAddress')
let ylBusiSelect = () => import(/* webpackChunkName: "ylBusiSelect" */'./BusiSelect')
let ylSmsCount = () => import(/* webpackChunkName: "ylSmsCount" */'./SmsCount')
let ylTimeLine = () => import(/* webpackChunkName: "ylTimeLine" */'./TimeLine')
const ylUploadImage = () => import(/* webpackChunkName: "ylUpload" */'./Upload/uploadImage.vue')

export {
  ylUpload,
  ylMultipleUpload,
  ylUploadFile,
  ylTable,
  ylChooseAddress,
  ylBusiSelect,
  ylSmsCount,
  ylTimeLine,
  ylUploadImage
}
