<template>
  <div class="app-container">
    <!-- 内容区域 -->
    <div class="app-container-content has-bottom-bar">
      <!-- 上部 -->
      <div class="top-bar">
        <div class="header-bar header-renative">
          <div class="sign"></div>待匹配终端
        </div>
        <div class="header-bar-right">
          <yl-button type="primary" plain @click="maintainClick">人工维护入库</yl-button>
        </div>
        <p class="title">{{ data.name }}</p>
        <el-row class="box">
          <el-col :span="12">
            <span>企业类型：</span>
            {{ data.customerType }}
          </el-col>
          <el-col :span="12">
            <span>统一社会信用代码/医疗机构许可证：</span>
            {{ data.licenseNo }}
          </el-col>
        </el-row>
        <el-row class="box">
          <el-col :span="12">
            <span>省市区/县：</span>
            {{ data.province }}{{ data.city }}{{ data.region }}
          </el-col>
          <el-col :span="12">
            <span>详细地址：</span>
            {{ data.address }}
          </el-col>
        </el-row>
        <el-row class="box">
          <el-col :span="12">
            <span>联系人：</span>
            {{ data.contact }}
          </el-col>
          <el-col :span="12">
            <span>联系电话：</span>
            {{ data.phone }}
          </el-col>
        </el-row>
        <el-row class="box">
          <el-col :span="12">
            <span>客户内码：</span>
            {{ data.innerCode }}
          </el-col>
          <el-col :span="12">
            <span>客户编码：</span>
            {{ data.sn }}
          </el-col>
        </el-row>
      </div>
      <!-- 下部 -->
      <div class="app-container-bottom">
        <div class="bottom-top">
          <el-tabs class="my-tabs" v-model="tabs" @tab-click="handleTabClick">
            <el-tab-pane label="平台内相似终端" name="1"></el-tab-pane>
            <el-tab-pane label="天眼查相似终端" name="2"></el-tab-pane>
          </el-tabs>
          <div class="bottom-top-sou">
            <el-input v-model.trim="inputNum" class="bottom-top-sou-input" placeholder="请输入"/>
            <yl-button type="primary" @click="search">搜索</yl-button>
          </div>
        </div>
        <!-- 表格 -->
        <div class="mar-t-8 pad-b-10 order-table-view" v-if="tabs == '1'">
          <yl-table
          :list="dataList"
          :total="query.total"
          :page.sync="query.page"
          :row-class-name="() => 'mar-b-16'"
          :limit.sync="query.limit"
          :loading="loading"
          :cell-no-pad="true"
          @getList="dataListApi">
          <el-table-column>
            <template slot-scope="{ row, $index }">
              <div class="table-view" :key="$index">
                <div class="content flex-row-left">
                   <div class="content-title">{{ row.name }}</div>
                  <div class="content-left">
                    <div class="content-left-title"></div>
                    <div class="item" style="font-size:14px;font-weight: normal"><span class="font-title-color">社会信用统一代码：</span>{{ row.licenseNumber }}</div>
                  </div>
                  <div class="content-center font-size-base font-important-color">
                    <div class="content-left-title"></div>
                    <div class="item"><span class="font-title-color">省市区/县：</span>{{ row.provinceName }}{{ row.cityName }}{{ row.regionName }}</div>
                  </div>
                  <div class="content-center font-size-base font-important-color">
                    <div class="content-left-title"></div>
                    <div class="item">
                      <span class="font-title-color">详细地址：</span>
                      {{ row.address }}
                    </div>
                  </div>
                  <div class="content-right flex-column-center table-button">
                    <yl-button type="text" v-if="data.syncStatus == 3" @click="viewDetailsClick(row)">确认</yl-button>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
          </yl-table>
        </div>
        <!-- 天眼查相似终端 -->
        <div v-if="tabs == '2'" class="mar-t-8 pad-b-10 order-table-view">
          <yl-table
          :list="dataList2"
          :total="query2.total"
          :page.sync="query2.page"
          :row-class-name="() => 'mar-b-16'"
          :limit.sync="query2.limit"
          :loading="loading2"
          :cell-no-pad="true"
          @getList="dataListApi2">
          <el-table-column>
            <template slot-scope="{ row, $index }">
              <div class="table-view" :key="$index">
                <div class="content flex-row-left">
                  <div class="content-title">{{ row.name }}</div>
                  <div class="content-left">
                    <div class="content-left-title"></div>
                    <div class="item" style="font-size:14px;font-weight: normal"><span class="font-title-color">社会信用统一代码：</span>{{ row.creditCode }}</div>
                  </div>
                  <div class="content-center font-size-base font-important-color">
                    <div class="content-left-title"></div>
                    <div class="item"><span class="font-title-color">省市区/县：</span>{{ row.base }}{{ row.city }}{{ row.district }}</div>
                  </div>
                  <div class="content-center font-size-base font-important-color">
                    <div class="content-left-title"></div>
                    <div class="item">
                      <span class="font-title-color">详细地址：</span>
                      {{ row.regLocation }}
                    </div>
                  </div>
                  <div class="content-right flex-column-center table-button">
                    <yl-button type="text" v-if="data.syncStatus == 3" @click="viewDetailsClick2(row)">确认</yl-button>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
          </yl-table>
        </div>
      </div>
    </div>
     <div class="bottom-view flex-row-center">
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
    </div>
    <yl-dialog :title="title" :show-footer="false" width="900px" :visible.sync="showDialog">
      <div class="dialogTc">
        <erp-maintain :dialog-id="dialogId" :dialog-data="dialogData" @cancelShow="cancelShow"></erp-maintain>
      </div>
    </yl-dialog>
  </div>
</template>
<script>
import { findById, enterpriseList, queryByKeyword, bind, bindTyc } from '@/subject/admin/api/zt_api/erpAdministration';
import erpMaintain from '../erp_maintain'
export default {
  components: {
    erpMaintain
  },
  data() {
    return {
      tabs: '1',
      query: {
        total: 0,
        page: 1,
        limit: 10
      },
      loading: false,
      dataList: [],
      inputNum: '', 
      data: {
        name: '',
        customerType: '',
        licenseNo: '',
        province: '',
        city: '',
        region: '',
        address: '',
        contact: '',
        phone: '',
        innerCode: '',
        sn: '',
        id: '',
        groupName: '',
        suDeptNo: '',
        suId: '',
        suName: '',
        syncMsg: '',
        syncStatus: '',
        syncTime: ''
      },
      // 天眼查相似终端参数
      query2: {
        total: 0,
        page: 1,
        limit: 10
      },
      loading2: false,
      dataList2: [],
      dataList2Show: true, //是否 查询天眼查数据
      title: '人工维护',
      showDialog: false,
      dialogId: '',
      dialogData: {}
    }
  },
  mounted() {
    let queryId = this.$route.params;
    if (queryId.id) {
      this.getData(queryId.id)
    }
  },
  methods: {
    // 查询匹配终端
    async getData(id) {
      let data = await findById(id)
        if (data !== undefined) {
          this.data = {
            name: data.name,
            customerType: data.customerType,
            licenseNo: data.licenseNo,
            province: data.province,
            city: data.city,
            region: data.region,
            address: data.address,
            contact: data.contact,
            phone: data.phone,
            innerCode: data.innerCode,
            sn: data.sn,
            id: data.id,
            groupName: data.groupName,
            suDeptNo: data.suDeptNo,
            suId: data.suId,
            suName: data.suName,
            syncMsg: data.syncMsg,
            syncStatus: data.syncStatus,
            syncTime: data.syncTime
          }
          this.dataListApi(); //获取平台内相似终端
        }
    },
    async dataListApi() {
      this.loading = true;
      let query = this.query;
      let data = await enterpriseList(
        query.page,
        this.inputNum == '' ? this.data.name : this.inputNum,
        query.limit
      )
      if (data !== undefined) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
      this.loading = false;
    },
    // 点击搜索
    search() {
      if (this.tabs == '1') {
        this.dataListApi();
      } else {
        this.dataListApi2();
      }
    },
    // 保存
    saveClick() {

    },
    //点击 人工维护入库 
    maintainClick() {
      this.dialogId = this.data.id.toString();
      this.showDialog = true;
      this.dialogData = {};
    },
   
    // 点击切换 
    handleTabClick(row) {
      this.inputNum = '';
      if (row.name == '2') {
        if (this.dataList2Show) {
          this.dataListApi2(); //获取天眼查相似终端
        }
      } else {
        this.dataListApi(); //获取平台内相似终端
      }
    },
    //获取天眼查相似终端
    async dataListApi2() {
      this.loading2 = true;
      // this.dataList2 =[];
      let data = await queryByKeyword(
        this.inputNum == '' ? this.data.name : this.inputNum
      )
      if (data !== undefined) {
        this.dataList2 = [data];
        // this.dataList2= {
        //   name: data.name,
        //   creditCode: data.creditCode,
        //   base: data.base,
        //   city: data.city,
        //   district: data.district,
        //   regLocation: data.regLocation
        // }
        // console.log(this.dataList2,"999")
      }
      this.dataList2Show = false;
      this.loading2 = false;
    },
    //平台内相似终端 确认 
    viewDetailsClick(row) {
      this.$confirm(`${row.name} 确认为平台内相似终端`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then( async() => {
        this.$common.showLoad();
        let data = await bind(
          row.id,
          this.data
        )
        this.$common.hideLoad();
        if (data !== undefined) {
          this.$common.n_success('操作成功！');
          // this.dataListApi();
          this.$common.alert(data.failMsg, r => {
            this.$router.go(-1)
          })
        }
      })
      .catch(() => {
        //取消
      });
    },
    // 天眼查相似终端 
    async viewDetailsClick2(row) {
      this.$common.showLoad();
      let data = await bindTyc(
        this.data,
        row
      )
      this.$common.hideLoad();
      if (data !== undefined) {
         if (data.bindResult) {
            this.$common.n_success(`${row.name} 确认为天眼查相似终端`);
         } else {
            this.$confirm(`${data.failMsg} 需要走人工维护`, '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            })
            .then(()=>{
              this.showDialog = true;
              this.dialogData = {
                ...data.erpCustomer
              };

            })
            .catch(() => {
              //取消
            });
         }
      }
      // this.$confirm(`${row.name} 确认为天眼查相似终端`, "提示", {
      //   confirmButtonText: "确定",
      //   cancelButtonText: "取消",
      //   type: "warning"
      // })
      // .then( async() => {
      //   let data = await bindTyc(
      //     this.data,
      //     row
      //   )
      //   if (data !== undefined) {
      //     if (data.bindResult) {
      //       this.$common.n_success('操作成功！');
            // this.$common.alert(data.failMsg, r => {
            //   this.$router.go(-1)
            // })
      //     } else {
      //       this.$common.error(data.failMsg);
      //       this.maintainClick();
      //     }
      //   }
      // })
      // .catch(() => {
      //   //取消
      // });
    },
    // 关闭弹窗
    cancelShow(val) {
      this.showDialog = val;
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "./index.scss";
  .my-tabs ::v-deep .el-tabs__nav-wrap:after{
    background-color: rgba(255,255,255,0);
  }
  .box ::v-deep .el-col{
    color: #333;
  }
</style>