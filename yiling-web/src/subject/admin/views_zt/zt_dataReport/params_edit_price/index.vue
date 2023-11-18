<!--
 * @Description:
 * @Author: xuxingwang
 * @Date: 2022-02-22 18:07:11
 * @LastEditTime: 2022-03-01 10:40:32
 * @LastEditors: xuxingwang
-->
<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="common-box">
        <div class="search-box">
          <el-row class="box">
            <el-col :span="8">
              <div class="title">商品名称</div>
              <el-input v-model="query.name" placeholder="请输入" @keyup.enter.native="handleSearch"></el-input>
            </el-col>
            <el-col :span="8">
              <div class="title">操作时间</div>
              <el-date-picker
                v-model="query.time"
                type="daterange"
                format="yyyy 年 MM 月 dd 日"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始时间"
                end-placeholder="结束时间"
              >
              </el-date-picker>
            </el-col>
            <el-col :span="8">
              <div class="title">操作人</div>
              <el-input v-model="query.updateUser" placeholder="请输入" @keyup.enter.native="handleSearch"></el-input>
            </el-col>
          </el-row>
        </div>
        <div class="search-box mar-t-16">
          <el-row class="box">
            <el-col :span="24">
              <yl-search-btn :total="query.total" @search="handleSearch" @reset="handleReset" />
            </el-col>
          </el-row>
        </div>
      </div>
      <div class="down-box">
        <div></div>
        <div class="btn">
          <yl-button type="primary" plain @click="add">添加</yl-button>
        </div>
      </div>
      <div class="mar-t-8 pad-b-100">
        <yl-table
          border
          :show-header="true"
          :list="dataList"
          :total="query.total"
          :page.sync="query.page"
          :limit.sync="query.limit"
          :loading="loading"
          @getList="getList"
        >
          <el-table-column min-width="100" align="center" label="以岭品ID" prop="goodsId"></el-table-column>
          <el-table-column min-width="100" align="center" label="商品名称" prop="goodsName"></el-table-column>
          <el-table-column min-width="100" align="center" label="商品规格" prop="goodsSpecification"></el-table-column>
          <el-table-column min-width="100" align="center" :label="priceNameLabel" prop="price"></el-table-column>
          <el-table-column min-width="100" align="center" label="标准库规格ID" prop="specificationId" v-if="$route.params.id == 3"></el-table-column>
          <el-table-column min-width="100" align="center" label="开始时间" prop="startTime">
            <template slot-scope="{ row }">
              <div>{{ row.startTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="结束时间" prop="endTime">
            <template slot-scope="{ row }">
              <div>{{ row.endTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="操作时间" prop="updateTime">
            <template slot-scope="{ row }">
              <div>{{ row.updateTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="操作人" prop="updateUserName"></el-table-column>
          <el-table-column align="center" label="操作">
            <template slot-scope="{ row }">
              <yl-button type="text" @click="edit(row)">修改</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <div class="bottom-view flex-row-center">
        <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      </div>
      <!-- 商品添加 修改弹窗 -->
      <yl-dialog :visible.sync="show" width="1152px" :title="title" @confirm="confirm" :show-footer="false">
        <div class="dialog-content">
          <el-form class="pad-t-8" ref="form" :rules="formRules" :model="form" label-width="100px">
            <el-row>
              <el-col :span="12" :offset="0">
                <el-form-item label="商品名称">
                  <el-select
                    v-if="$route.params.id != 3"
                    v-model="form.goodsId"
                    filterable
                    remote
                    reserve-keyword
                    placeholder="请输入关键词"
                    :remote-method="remoteMethod"
                    :loading="goodsloading"
                    @change="selectGoods"
                    :disabled="flag"
                  >
                    <el-option v-for="(item, index) in options" :key="index" :label="item.name" :value="item.id">
                      <span>{{ item.name }}---{{ item.sellSpecifications }}---{{ item.manufacturer }}</span>
                    </el-option>
                  </el-select>
                  <el-select
                    v-if="$route.params.id == 3"
                    v-model="form.specificationId"
                    filterable
                    remote
                    reserve-keyword
                    placeholder="请输入关键词"
                    :remote-method="remoteMethod"
                    :loading="goodsloading"
                    @change="selectGoods"
                    :disabled="flag"
                  >
                    <el-option v-for="(item, index) in options" :key="index" :label="item.name" :value="item.sellSpecificationsId">
                      <span>{{ item.name }}---{{ item.sellSpecifications }}---{{ item.manufacturer }}</span>
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="商品规格">
                  <el-input v-model="form.goodsUnit" disabled></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12" :offset="0">
                <el-form-item :label="priceNameLabel" prop="price">
                  <el-input v-model="form.price" :disabled="flag"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="以岭品ID">
                  <el-input v-model="form.goodsId" disabled></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row v-if="$route.params.id == 3">
              <el-col :span="24" :offset="0">
                <el-form-item label="标准库规格ID">
                  <el-input v-model="form.specificationId" disabled></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12" :offset="0" class="time-form">
                <el-form-item label="执行时间" prop="startTime">
                  <el-date-picker
                    v-model="form.startTime"
                    type="date"
                    format="yyyy 年 MM 月 dd 日"
                    value-format="yyyy-MM-dd"
                    range-separator="至"
                    placeholder="开始时间"
                    :disabled="flag"
                  >
                  </el-date-picker>
                </el-form-item>
                <div class="line">-</div>
                <el-form-item label="" label-width="0" prop="endTime">
                  <el-date-picker
                    v-model="form.endTime"
                    type="date"
                    format="yyyy 年 MM 月 dd 日"
                    value-format="yyyy-MM-dd"
                    range-separator="至"
                    placeholder="结束时间"
                    :picker-options="pickerOptions1"
                  >
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item>
              <yl-button type="primary" @click="onSubmit('form')">保存</yl-button>
              <yl-button @click="show = false">取消</yl-button>
            </el-form-item>
          </el-form>
        </div>
      </yl-dialog>
    </div>
  </div>
</template>

<script>
import { formatDate } from '@/common/utils/index';
import {
  getParamsPriceList,
  getGoodsList,
  addParamsPrice,
  editParamsPrice,
  queryGoodsSpecification,
  queryYlGoods
} from '@/subject/admin/api/zt_api/dataReport';
export default {
  name: 'ZtParamsEditPrice',
  components: {},
  data() {
    const validateStart = (rule, value, callback) => {
      if (value) {
        let startTime = new Date(this.form.startTime).getTime();
        let endTime = new Date(this.form.endTime).getTime();
        if (startTime > endTime) {
          callback(new Error('结束日期必须大于开始日期，请重新选择！'));
        } else {
          callback();
        }
      } else {
        callback(new Error('请选择'));
      }
    };
    const validateEnd = (rule, value, callback) => {
      if (value) {
        let startTime = new Date(this.form.startTime).getTime();
        let endTime = new Date(this.form.endTime).getTime();
        if (startTime > endTime) {
          callback(new Error('结束日期必须大于开始日期，请重新选择！'));
        } else {
          callback();
        }
      } else {
        callback(new Error('请选择'));
      }
    };
    const validatePrice = (rule, value, callback) => {
      let reg = /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/
      if (!value) {
        callback(new Error('价格不能为空'))
      } else if (!reg.test(value)) {
        callback(new Error('请输入正确的格式，最多保留2位小数'))
      } else if (value.length > 10) {
        callback(new Error('最多可输入10个字符'))
      } else {
        callback();
      }
    }
    return {
      priceNameLabel: '--',
      query: {
        total: 0,
        page: 1,
        limit: 10,
        goodsName: '',
        updateUser: '',
        time: []
      },
      loading: false,
      dataList: [],
      show: false,
      title: '商品供货商添加',
      flag: false,
      form: {
        id: '',
        goodsName: '',
        goodsUnit: '',
        goodsId: '',
        price: '',
        startTime: '',
        endTime: '',
        // 标准库规格ID
        specificationId: ''
      },
      formRules: {
        goodsName: [
          { required: true, message: '请输入类型', trigger: 'trigger' }
        ],
        startTime: [
          { required: true, validator: validateStart, trigger: 'trigger' }
        ],
        endTime: [
          { required: true, validator: validateEnd, trigger: 'trigger' }
        ],
        price: [
          { required: true, validator: validatePrice, trigger: 'trigger' }
        ]
      },
      goodsloading: false,
      options: [],
      pickerOptions1: {
        disabledDate(time) {
          return time.getTime() < Date.now() - 8.64e7;
        }
      }
    };
  },
  mounted() {
    this.priceNameLabel = this.$route.params.type ? this.$route.params.type : '--';
    this.getList();
  },
  methods: {
    async getList() {
      let query = this.query;
      this.loading = true;
      let data = await getParamsPriceList(
        this.$route.params.id,
        query.page,
        query.limit,
        query.name,
        query.updateUser,
        query.time && query.time.length > 0 ? query.time[0] : null,
        query.time && query.time.length > 1 ? query.time[1] : null
      );
      this.loading = false;
      if (data) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
    },
    handleSearch() {
      this.query.page = 1;
      this.getList();
    },
    handleReset() {
      this.query = {
        page: 1,
        limit: 10,
        goodsName: '',
        updateUser: '',
        time: []
      };
    },
    // 添加
    add() {
      this.form.goodsName = '';
      this.form.goodsId = '';
      this.form.goodsUnit = '';
      this.form.price = '';
      this.form.startTime = '';
      this.form.endTime = '';
      this.form.specificationId = '';
      this.form.id = ''
      this.title = '商品' + this.$route.params.type + '添加';
      this.show = true;
      this.flag = false;
      this.options = []
    },
    // 编辑
    async edit(row) {
      await this.remoteMethod(row.goodsName)
      this.title = '商品' + this.$route.params.type + '编辑';
      this.show = true;
      this.flag = true;
      this.form.id = row.id;
      this.form.goodsName = row.goodsName;
      this.form.goodsId = row.goodsId;
      this.form.goodsUnit = row.goodsSpecification;
      this.form.price = row.price;
      this.form.startTime = formatDate(row.startTime, 'yyyy-MM-dd');
      this.form.endTime = formatDate(row.endTime, 'yyyy-MM-dd');
      this.form.specificationId = row.specificationId
    },
    confirm() {
      this.show = false;
    },
    // 添加商品
    onSubmit(formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          this.$common.showLoad();
          let data = '';
          if (this.flag === false) {
            data = await addParamsPrice(
              this.$route.params.id,
              this.form.goodsId,
              this.form.goodsName,
              this.form.goodsUnit,
              this.form.price,
              this.form.startTime,
              this.form.endTime,
              this.form.specificationId
            );
          } else {
            data = await editParamsPrice(this.form.id, this.form.endTime);
          }
          this.$common.hideLoad();
          if (data !== undefined) {
            if (this.flag) {
              this.$common.success('编辑成功');
            } else {
              this.$common.success('添加成功');
            }
            this.show = false;
            this.getList();
          }
        } else {
          return false;
        }
      });
    },
    // 远程搜索商品
    async remoteMethod(query) {
      if (query !== '') {
        this.goodsloading = true;
        // 商销价页面先搜索规格名称, 根据选择的规格id查询接口得到具体商品
        if (this.$route.params.id == 3) {
          let data = await queryGoodsSpecification(query);
          this.goodsloading = false;
          if (data) {
            this.options = data.list;
          }
        } else {
          let data = await getGoodsList(query);
          this.goodsloading = false;
          if (data) {
            this.options = data.list;
          }
        }
      } else {
        this.options = [];
      }
    },
    // 选择商品
    async selectGoods(e) {
      // 商销价需要根据选择的规格id查询对应的商品
      if (this.$route.params.id == 3) {
        let data = await queryYlGoods(e)
        if (data) {
          this.form.goodsName = data.name;
          this.form.goodsId = data.id;
          this.form.goodsUnit = data.sellSpecifications;
          this.form.specificationId = data.sellSpecificationsId
        } else {
          this.$common.warn('通过该规格ID查询无对应的商品')
          this.form.goodsName = ''
        }
      } else {
        this.form.goodsName = this.options.find(item => item.id === e).name
        this.form.goodsUnit = this.options.find(item => item.id === e).sellSpecifications
        this.form.specificationId = this.options.find(item => item.id === e).sellSpecificationsId
      }
    }
  }
};
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>
