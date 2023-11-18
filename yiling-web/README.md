# 查看打包文件大小
npm run preview -- --report

# eslint校验
npm run lint

#调试命令
npm run dev-pop
npm run dev-admin

#本地调试打包dist内js
npm run test

#代码规范
变量命名：变量均按驼峰命名，不允许下划线类命名
例：let dataList = []

函数命名：函数均按驼峰命名，不允许下划线类命名
例：handleClick = () => {}

枚举：均按大写字符命名，分隔以下划线隔开
例：YL_USER_INFO

请求接口入参传值：必须一个一个传入，不得直接传入对象类，通用简单接口酌情例外
例：export function getCustomerGroupList(current, size, name, type, status) {
    return request({
      url: '/customer/group/pageList',
      method: 'post',
      data: {
        // 页码
        current,
        // 个数
        size,
        // 名字
        name,
        // 类别
        type,
        // 状态
        status
      }
    })
  }
  
#报错弹框规范
操作类接口成功用：this.$common.n_success()方法

#打印
可用全局打印函数 this.$log() 此函数会再生产环境中自动禁用
  
#创建目录及文件规范
components 通用组件目录开头均大写
views 页面目录中，创建新目录，均以模块判定，一个模块一个目录，模块中页面创建新目录新文件均在该目录中

#常用全局过滤器
toThousand: 10000 => "10,000"
formatDate: 转换时间 格式： yyyy-MM-dd h:m:s  yyyy-MM-dd hh:mm:ss
dictLabel: 根据数组取键值的label
dictValue: 根据数组取键值的value

#常用全局指令
v-role-btn="['6']": 按钮级别权限

#常用方法
downloadByUrl 下载文件
paramObj2 转换对象为get参数
clone 克隆对象，深拷贝
add 加
sub 减
mul 乘
div 除
interceptNumber 舍尾法截取数字（非四舍五入）

#常用页面
importFile/importFile_index 导入页面 


#提交代码前注意事项
先检查eslint有无报错，无错再提交

#禁止事项
禁止行内样式中包含宽度、高度值

#eslint类规范
{{ }} 内前后必须加空格 
例 {{ row.name }}

#路由配置说明
/**
 * 路由配置项
 *
 // 当设置 true 的时候该路由不会在侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
 hidden: true // (默认 false)
 //当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
 redirect: 'noRedirect'
 // 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
 // 只有一个时，会将那个子路由当做根路由显示在侧边栏--如引导页面
 // 若你想不管路由下面的 children 声明的个数都显示你的根路由
 // 你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由
 alwaysShow: true
 name: 'router-name' // 设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题
 meta: {
  title: 'title' // 设置该路由在侧边栏和面包屑中展示的名字
  icon: 'svg-name' // 设置该路由的图标，支持 svg-class
  noCache: true // 如果设置为true，则不会被 <keep-alive> 缓存(默认 false)
  // 当路由设置了该属性，则会高亮相对应的侧边栏。
  // 这在某些场景非常有用，比如：一个文章的列表页路由为：/article/list
  // 点击文章进入文章详情页，这时候路由为/article/1，但你想在侧边栏高亮文章列表的路由，就可以进行如下设置，支持多个
  // 也支持所有，示例：activeMenu: ['*']
  activeMenu: ['/article/list']
  // 页面标识，上线后将根据配置展示此标识页面
  identify: "zt:zt_enterprise:zt_enterprise_information"
}
