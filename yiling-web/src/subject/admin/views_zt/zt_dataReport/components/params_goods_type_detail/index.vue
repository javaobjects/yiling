<!--
 * @Description: 
 * @Author: xuxingwang
 * @Date: 2022-02-23 16:46:25
 * @LastEditTime: 2022-02-28 17:53:29
 * @LastEditors: xuxingwang
-->
<template>
  <!-- <div class="app-container"> -->
  <!-- <div class="app-container-content"> -->
  <div>
    <div>
      <div class="mar-t-8">
        <yl-table border :show-header="true" :list="dataList" :total="query.total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList">
          <el-table-column min-width="100" align="center" label="商品ID" prop="ylGoodsId"></el-table-column>
          <el-table-column min-width="100" align="center" label="商品名称" prop="goodsName"></el-table-column>
          <el-table-column min-width="100" align="center" label="商品规格" prop="goodsSpecification"></el-table-column>
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
      <!-- 添加商品 -->
      <yl-dialog :append-to-body="true" :visible.sync="addShow" width="1152px" title="修改商品" @confirm="confirm" :show-footer="false">
        <div class="dialog-content">
          <el-form class="pad-t-8" ref="form" :rules="formRules" :model="form" label-width="100px">
            <el-row>
              <el-col :span="12" :offset="0">
                <el-form-item label="商品名称：" prop="goodsName">
                  <el-input v-model="form.goodsName" disabled></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" :offset="0">
                <el-form-item label="商品规格：">
                  <el-input v-model="form.goodsUnit" disabled></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12" :offset="0">
                <el-form-item label="商品ID：">
                  <el-input v-model="form.goodsId" disabled></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row class="time-form">
              <el-form-item label="执行时间：">
                <el-date-picker v-model="form.startTime" type="date" format="yyyy 年 MM 月 dd 日" value-format="yyyy-MM-dd" range-separator="至" placeholder="开始时间" disabled>
                </el-date-picker>
              </el-form-item>
              <div class="line">-</div>
              <el-form-item label="" label-width="0" prop="endTime">
                <el-date-picker v-model="form.endTime" type="date" format="yyyy 年 MM 月 dd 日" value-format="yyyy-MM-dd" range-separator="至" placeholder="结束时间" :picker-options="pickerOptions1">
                </el-date-picker>
              </el-form-item>
              <!-- <el-col :span="12" :offset="0" > -->
              <!-- </el-col> -->
            </el-row>
            <el-form-item>
              <yl-button type="primary" @click="onSubmit('form')">确定</yl-button>
              <yl-button @click="addShow = false">取消</yl-button>
            </el-form-item>
          </el-form>
        </div>
      </yl-dialog>
    </div>
  </div>
</template>

<script>
import {
  getGoodsListOfType,
  editGoodsEndTime
} from '@/subject/admin/api/zt_api/dataReport';
import { formatDate } from '@/common/utils/index';
export default {
  name: 'ParamsGoodsTypeDetail',
  props: {
    // 商品类型id，用来查找商品类型下的商品列表
    id: {
      type: [String, Number],
      default: '',
      required: true
    }
  },
  data() {
    var validateEnd = (rule, value, callback) => {
      console.log(value);
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
    return {
      headeTitle: '',
      dataList: [],
      query: {
        total: 0,
        page: 1,
        limit: 10
      },
      loading: false,
      addShow: false,
      form: {
        id: '',
        goodsName: '',
        goodsUnit: '',
        goodsId: '',
        startTime: '',
        endTime: ''
      },
      formRules: {
        goodsName: [
          { required: true, message: '请输入类型', trigger: 'trigger' }
        ],
        endTime: [
          { required: true, validator: validateEnd, trigger: 'trigger' }
        ]
      },
      pickerOptions1: {
        disabledDate(time) {
          return time.getTime() < Date.now() - 8.64e7;
        }
      }
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    async getList() {
      let data = await getGoodsListOfType(
        // this.$route.params.id,
        this.id,
        this.query.page,
        this.query.limit
      );
      console.log(data);
      if (data !== undefined) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
    },
    // 修改商品类型
    edit(row) {
      this.typeTitle = '修改商品类型';
      this.addShow = true;
      this.form.id = row.id;
      this.form.goodsName = row.goodsName;
      this.form.goodsId = row.ylGoodsId;
      this.form.goodsUnit = row.goodsSpecification;
      this.form.price = row.price;
      this.form.startTime = formatDate(row.startTime, 'yyyy-MM-dd');
      this.form.endTime = formatDate(row.endTime, 'yyyy-MM-dd');
    },
    confirm() { },
    onSubmit(formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          this.$common.showLoad();
          let data = await editGoodsEndTime(this.form.id, this.form.endTime);
          this.$common.hideLoad();
          if (data !== undefined) {
            this.addShow = false;
            this.$common.success('修改成功');
            this.getList();
          }
        } else {
          console.log('error submit!!');
          // this.$common.error('请检查输入内容是否符合规范')
          return false;
        }
      });
    }
  }
};
</script>
<style lang="scss" scoped>
// @import './index.scss';
.time-form {
  display: flex;
  .line {
    width: 50px;
    height: 36px;
    line-height: 36px;
    margin: 0 10px;
  }
}
</style>
