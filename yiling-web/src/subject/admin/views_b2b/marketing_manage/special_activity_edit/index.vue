<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <el-form :disabled="operationType == 1" :model="form" :rules="rules" ref="dataForm">
        <div class="top-bar">
          <div class="header-bar header-renative">
            <div class="sign"></div>专场活动基本信息
          </div>
          <div class="content-box">
            <div class="search-box">
              <el-row class="box">
                <el-col :span="7">
                  <div class="title"><span class="red-text">*</span>专场活动名称</div>
                  <el-form-item prop="specialActivityName">
                    <el-input v-model="form.specialActivityName" maxlength="10" show-word-limit placeholder="请输入"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="7">
                  <div class="title"><span class="red-text">*</span>专场活动类型</div>
                  <el-form-item prop="type">
                    <el-radio-group v-model="form.type" @change="typeChange">
                      <el-radio v-for="item in typeArray" :key="item.value" :label="item.value">{{ item.label }}</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
            <div class="search-box">
              <el-row class="box">
                <el-col :span="7">
                  <div class="title"><span class="red-text">*</span>专场起止时间</div>
                  <el-form-item prop="time">
                    <el-date-picker v-model="form.time" type="datetimerange" format="yyyy/MM/dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']">
                    </el-date-picker>
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
          </div>
        </div>
      </el-form>
      <div class="top-bar">
        <div class="header-bar header-renative">
          <div class="sign"></div>参与的营销活动
        </div>
        <div class="content-box">
          <div>
            <yl-button class="mar-r-8 my-buttom" :disabled="operationType == 1" type="primary" @click="addProviderClick">添加</yl-button>
          </div>
          <div class="mar-t-8 pad-b-10 order-table-view">
            <yl-table border show-header :list="enterpriseLimitList" :total="0">
              <el-table-column label="企业名称" min-width="162" align="center">
                <template slot-scope="{ row }">
                  <div class="goods-desc">
                    <div class="font-size-base">{{ row.enterpriseName }}</div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="促销名称" min-width="162" align="center">
                <template slot-scope="{ row }">
                  <div class="goods-desc">
                    <div class="font-size-base">{{ row.promotionActivityName }}</div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="开始时间" min-width="120" align="center">
                <template slot-scope="{ row }">
                  <div>
                    {{ row.startTime | formatDate }}
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="结束时间" min-width="120" align="center">
                <template slot-scope="{ row }">
                  <div>
                    {{ row.endTime | formatDate }}
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="上传专场活动图片" min-width="120" align="center">
                <template slot="header">
                  <div class="upload-remark"><span class="red-text">*</span>上传专场活动图片</div>
                </template>
                <template slot-scope="{ $index, row }">
                  <div v-if="operationType == 1" class="flex-row-center">
                    <img class="item-img" :src="row.pic" />
                  </div>
                  <div v-if="operationType != 1" class="flex-row-center">
                    <yl-upload
                      width="80px"
                      height="80px"
                      :default-url="row.pic"
                      :extral-data="{type: 'specialActivityPicture'}"
                      @onSuccess="(data) => onPicSuccess(data, $index)"
                    />
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center" fixed="right">
                <template slot-scope="{ $index }">
                  <div class="operation-view">
                    <yl-button :disabled="operationType == 1" type="text" @click="providerRemoveClick($index)">删除</yl-button>
                  </div>
                </template>
              </el-table-column>
            </yl-table>
          </div>
        </div>
      </div>
      <div class="flex-row-center bottom-view">
        <yl-button plain type="primary" @click="$router.go(-1)">返回</yl-button>
        <yl-button type="primary" v-if="operationType != 1" @click="saveClick">保存</yl-button>
      </div>
    </div>
    <!-- 添加营销活动 -->
    <yl-dialog title="添加" :visible.sync="addProviderDialog" width="966px" :show-footer="false">
      <div class="dialog-content-box-customer">
        <div class="dialog-content-top">
          <div class="search-box">
            <el-row class="box">
              <el-col :span="8">
                <div class="title">企业名称</div>
                <el-input v-model="providerQuery.ename" placeholder="请输入企业名称" />
              </el-col>
              <el-col :span="8">
                <div class="title">活动时间</div>
                <el-date-picker
                  v-model="providerQuery.time"
                  type="daterange"
                  format="yyyy/MM/dd"
                  value-format="yyyy-MM-dd"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期">
                </el-date-picker>
              </el-col>
            </el-row>
          </div>
          <div class="search-box">
            <el-row class="box">
              <el-col :span="16">
                <yl-search-btn :total="providerTotal" @search="providerHandleSearch" @reset="providerHandleReset" />
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="multi-action">
          <yl-button plain type="primary" v-if="providerList.length" @click="multiAddProviderItem">批量添加</yl-button>
        </div>
        <div class="mar-t-10" style="padding-bottom: 10px;background: #FFFFFF;">
          <yl-table :stripe="true" :show-header="true" :list="providerList" :total="providerTotal" :page.sync="providerQuery.page" :limit.sync="providerQuery.limit" :loading="loading1" @getList="getProviderList">
            <el-table-column label="企业名称" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.enterpriseName }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="促销名称" min-width="162" align="center">
              <template slot-scope="{ row }">
                <div class="goods-desc">
                  <div class="font-size-base">{{ row.promotionActivityName }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="开始时间" min-width="120" align="center">
              <template slot-scope="{ row }">
                <div>
                  {{ row.startTime | formatDate }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="结束时间" min-width="120" align="center">
              <template slot-scope="{ row }">
                <div>
                  {{ row.endTime | formatDate }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="操作" min-width="85" align="center">
              <template slot-scope="{ row }">
                <div>
                  <yl-button class="edit-btn" type="text" v-if="row.isUsed" disabled>已被其他活动添加</yl-button>
                  <yl-button class="view-btn" type="text" v-if="!row.isSelect && !row.isUsed" @click="providerItemAddClick(row)">添加</yl-button>
                  <yl-button class="edit-btn" type="text" v-if="row.isSelect && !row.isUsed" disabled>已添加</yl-button>
                </div>
              </template>
            </el-table-column>
          </yl-table>
        </div>
      </div>
    </yl-dialog>
  </div>
</template>

<script>
import { specialActivityQueryById, specialPageActivityInfo, specialActivitySubmit } from '@/subject/admin/api/b2b_api/marketing_manage'
import { ylUpload } from '@/subject/admin/components'
import { onInputLimit } from '@/common/utils'
import { formatDate } from '@/subject/admin/utils'

export default {
  components: {
    ylUpload
  },
  computed: {
  },
  data() {
    return {
      // 活动类型
      typeArray: [
        {
          label: '满赠',
          value: 1
        },
        {
          label: '特价',
          value: 2
        },
        {
          label: '秒杀',
          value: 3
        },
        {
          label: '组合包',
          value: 4
        }
      ],
      loading: false,
      // 查看 operationType: 1-查看 2-编辑 3-新增
      operationType: 2,
      // 基本信息设置
      form: {
        specialActivityName: '',
        type: 1,
        time: []
      },
      rules: {
        specialActivityName: [{ required: true, message: '请输入专场活动名称', trigger: 'blur' }],
        type: [{ required: true, message: '请选择专场活动类型', trigger: 'change' }],
        time: [{ required: true, message: '请选择日期', trigger: 'change' }]
      },
      // 添加营销活动弹框
      addProviderDialog: false,
      loading1: false,
      providerQuery: {
        page: 1,
        limit: 10,
        time: []
      },
      providerList: [],
      providerTotal: 0,
      // 已选择营销活动
      enterpriseLimitList: []
    };
  },
  mounted() {
    this.$log(this.$route.params)
    this.id = this.$route.params.id
    this.operationType = this.$route.params.operationType
    if (this.id && this.operationType) {
      this.getDetail()
    }
  },
  methods: {
    async getDetail() {
      let data = await specialActivityQueryById(this.id)
      if (data) {
        let form = {
          specialActivityName: data.specialActivityName,
          type: data.type
        }
        if (data.startTime && data.endTime) {
          form.time = [formatDate(data.startTime), formatDate(data.endTime)]
        }
        this.form = form
        this.enterpriseLimitList = data.specialActivityEnterpriseDTOS || []

      }
    },
    // 活动类型切换
    typeChange() {
      this.enterpriseLimitList = []
    },
    // 设置营销活动点击
    addProviderClick() {
      this.addProviderDialog = true
      this.getProviderList()
    },
    // 营销活动搜索
    providerHandleSearch() {
      this.providerQuery.page = 1
      this.getProviderList()
    },
    providerHandleReset() {
      this.providerQuery = {
        page: 1,
        limit: 10,
        time: []
      }
    },
    // 获取营销活动列表
    async getProviderList() {
      this.loading1 = true
      let providerQuery = this.providerQuery
      let data = await specialPageActivityInfo(
        providerQuery.page,
        providerQuery.limit,
        providerQuery.ename,
        1,
        this.form.type,
        providerQuery.time && providerQuery.time.length ? providerQuery.time[0] : undefined,
        providerQuery.time && providerQuery.time.length > 1 ? providerQuery.time[1] : undefined
      );
      this.loading1 = false
      if (data) {

        data.records.forEach(item => {
          let hasIndex = this.enterpriseLimitList.findIndex(obj => {
            return (obj.eid == item.eid && obj.promotionActivityId == item.promotionActivityId)
          });
          if (hasIndex != -1) {
            item.isSelect = true;
          } else {
            item.isSelect = false;
          }
        });

        this.providerList = data.records
        this.providerTotal = data.total
      }
    },
    // 弹框每一项企业点击
    providerItemAddClick(row) {
      let currentArr = this.enterpriseLimitList
      currentArr.push(row)

      this.enterpriseLimitList = currentArr

      let arr = this.providerList;
      arr.forEach(item => {
        let hasIndex = currentArr.findIndex(obj => {
          return (obj.eid == item.eid && obj.promotionActivityId == item.promotionActivityId)
        });
        if (hasIndex != -1) {
          item.isSelect = true;
        } else {
          item.isSelect = false;
        }
      });
      this.providerList = arr;
    },
    // 活动营销活动-批量添加
    multiAddProviderItem() {
      this.providerList.forEach(item => {
        if (!item.isUsed && item.isSelect == false) {
          this.enterpriseLimitList.push(item)
          item.isSelect = true
        }
      })
    },
    providerRemoveClick(index) {
      let currentArr = this.enterpriseLimitList
      currentArr.splice(index, 1)
      this.enterpriseLimitList = currentArr
    },
    // 图片上传成功
    async onPicSuccess(data, index) {
      if (data.key) {
        let currentItem = this.enterpriseLimitList[index]
        currentItem.pic = data.url
        this.$set(this.enterpriseLimitList, index, currentItem);
      }
    },
    // 底部提交
    async saveClick() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {

          let activity = this.form
          activity.startTime = activity.time && activity.time.length ? activity.time[0] : undefined
          activity.endTime = activity.time && activity.time.length > 1 ? activity.time[1] : undefined

          if (this.checkInputData()) {
            let params = {}
            if (this.id) {
              params.id = this.id
            }
            params.promotionSpecialActivitySave = activity
            params.enterpriseSaves = this.enterpriseLimitList

            this.$common.showLoad()
            let data = await specialActivitySubmit(params)
            this.$common.hideLoad()
            if (data) {
              this.$common.n_success('保存成功')
              this.$router.go(-1)
            }

          }

        } else {
          console.log('error submit!!');
          return false;
        }
      })

    },
    //  校验数据
    checkInputData() {
      if (this.enterpriseLimitList.length == 0) {
        this.$common.warn('请先添加参与的营销活动')
        return false
      }

      let picEmpty = false
      for (let i = 0; i < this.enterpriseLimitList.length; i++) {
        let item = this.enterpriseLimitList[i]
        if (!item.pic) {
          picEmpty = true
        }
      }
      if (picEmpty) {
        this.$common.warn('请上传专场活动图片');
        return false
      }
      return true
    },
    // 校验两位小数
    onInput(value, limit) {
      return onInputLimit(value, limit)
    }
  }
};
</script>

<style lang="scss" >
.my-form-box {
  .el-form-item {
    .el-form-item__label {
      color: $font-title-color;
    }
    label {
      font-weight: 400 !important;
    }
  }
  .my-form-item-right {
    label {
      font-weight: 400 !important;
    }
  }
}
</style>
<style lang="scss" scoped>
@import './index.scss';
</style>
