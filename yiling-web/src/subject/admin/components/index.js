const ylUpload = () => import(/* webpackChunkName: "ylUpload" */'./Upload/SingleImage')
const ylUploadFile = () => import(/* webpackChunkName: "ylUploadFile" */'./Upload/uploadFile')
const ylTable = () => import(/* webpackChunkName: "ylTable" */'./Table')
const ylChooseAddress = () => import(/* webpackChunkName: "ylChooseAddress" */'./ChooseAddress')
const ylSmsCount = () => import(/* webpackChunkName: "ylSmsCount" */'./SmsCount')
const ylStatus = () => import(/* webpackChunkName: "ylStatus" */'./Status')
const ylUploadImage = () => import(/* webpackChunkName: "ylUpload" */'./Upload/uploadImage.vue')
const wangEditor = () => import(/* webpackChunkName: "ylUpload" */'./WangEditor')
const ylAreaSelect = () => import(/* webpackChunkName: "ylAreaSelect" */'./AreaSelect')
export {
  ylUpload,
  ylUploadFile,
  ylTable,
  ylChooseAddress,
  ylSmsCount,
  ylStatus,
  ylUploadImage,
  wangEditor,
  ylAreaSelect
}
