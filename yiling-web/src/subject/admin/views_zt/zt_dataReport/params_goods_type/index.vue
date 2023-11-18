<!--
 * @Description: 
 * @Author: xuxingwang
 * @Date: 2022-02-23 16:46:25
 * @LastEditTime: 2022-02-28 18:05:55
 * @LastEditors: xuxingwang
-->
<template>
  <div class="app-container">
    <div class="app-container-content">
      <div class="down-box">
        <div></div>
        <div class="btn">
          <ylButton type="primary" plain @click="add">添加类型</ylButton>
        </div>
      </div>
      <div class="mar-t-8 pad-b-100">
        <yl-table border :show-header="true" :list="dataList" :total="query.total" :page.sync="query.page" :limit.sync="query.limit" :loading="loading" @getList="getList">
          <el-table-column min-width="100" align="center" label="商品类型id" prop="id"></el-table-column>
          <el-table-column min-width="100" align="center" label="商品类型" prop="name"></el-table-column>
          <el-table-column min-width="100" align="center" label="操作时间" prop="updateTime">
            <template slot-scope="{ row }">
              <div>{{ row.updateTime | formatDate }}</div>
            </template>
          </el-table-column>
          <el-table-column min-width="100" align="center" label="操作人" prop="updateUserName"></el-table-column>
          <el-table-column align="center" label="操作">
            <template slot-scope="{ row }">
              <yl-button type="text" @click="see(row)">查看</yl-button>
              <yl-button type="text" @click="edit(row)">修改</yl-button>
              <yl-button type="text" @click="addGoods(row)">添加商品</yl-button>
            </template>
          </el-table-column>
        </yl-table>
      </div>
      <div class="bottom-view flex-row-center">
        <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      </div>
      <!-- 添加商品 -->
      <yl-dialog :visible.sync="addShow" width="1152px" :title="addGoodsTitle" @confirm="confirm" :show-footer="false">
        <div class="dialog-content">
          <el-form class="pad-t-8" ref="form" :rules="formRules" :model="form" label-width="100px">
            <el-row>
              <el-col :span="12" :offset="0">
                <el-form-item label="商品名称：" prop="goodsName">
                  <!-- <el-input v-model="form.goodsName"></el-input> -->
                  <el-select v-model="form.goodsName" filterable remote reserve-keyword placeholder="请输入关键词" :remote-method="remoteMethod" :loading="goodsloading" @change="selectGoods">
                    <el-option v-for="item in options" :key="item.id" :label="item.name" :value="item">
                      <span>{{ item.name }}---{{ item.sellSpecifications }}</span>
                      <!-- <span style="float: right; color: #8492a6; font-size: 13px">{{ item.specifications }}</span> -->
                    </el-option>
                  </el-select>
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
            <el-row>
              <el-col :span="12" :offset="0" class="time-form">
                <el-form-item label="执行时间：" prop="startTime">
                  <!--  -->
                  <el-date-picker v-model="form.startTime" type="date" format="yyyy 年 MM 月 dd 日" value-format="yyyy-MM-dd" range-separator="至" placeholder="开始时间">
                  </el-date-picker>
                </el-form-item>
                <div class="line">-</div>
                <el-form-item label="" label-width="0" prop="endTime">
                  <el-date-picker v-model="form.endTime" type="date" format="yyyy 年 MM 月 dd 日" value-format="yyyy-MM-dd" range-separator="至" placeholder="结束时间" :picker-options="pickerOptions1">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>

            <el-form-item>
              <yl-button type="primary" @click="onSubmit('form')">立即创建</yl-button>
              <yl-button @click="addShow = false">取消</yl-button>
            </el-form-item>
          </el-form>
        </div>
      </yl-dialog>
      <!-- 添加类型 -->
      <yl-dialog :visible.sync="show" :title="typeTitle" @confirm="confirm" :show-footer="false">
        <div class="dialog-content">
          <el-form class="pad-t-8" ref="formType" :model="formType" :rules="typeRules" label-width="100px">
            <el-row>
              <el-col :span="12" :offset="0">
                <el-form-item label="商品名称：" prop="goodsTypeName">
                  <el-input v-model="formType.goodsTypeName"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item>
              <yl-button type="primary" @click="onSubmitType('formType')">保存</yl-button>
              <yl-button @click="show = false">取消</yl-button>
            </el-form-item>
          </el-form>
        </div>
      </yl-dialog>
      <!-- 查看 -->
      <yl-dialog :visible.sync="showTypeList" width="70%" :title="seeTypeName" :show-footer="false">
        <div class="dialog-content">
          <ParamsGoodsTypeDetail :id="typeId"></ParamsGoodsTypeDetail>
        </div>
      </yl-dialog>
    </div>
  </div>
</template>

<script>
import {
  getParamsTypeList,
  addGoodsAndmMemberType,
  getGoodsList,
  addGoods,
  editGoodsAndMemberType
} from '@/subject/admin/api/zt_api/dataReport';
import ParamsGoodsTypeDetail from '../components/params_goods_type_detail/index'
export default {
  name: 'ZtParamsEditType',
  components: { ParamsGoodsTypeDetail },
  data() {
    const validateStart = (rule, value, callback) => {
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
      typeID: '',
      dataList: [],
      query: {
        total: 0,
        page: 1,
        limit: 10
      },
      loading: false,
      addShow: false,
      addGoodsTitle: '商品类型--',
      form: {
        goodsName: '',
        goodsUnit: '',
        goodsId: '',
        startTime: '',
        endTime: ''
      },
      formRules: {
        goodsName: [
          { required: true, message: '请输入商品名称', trigger: 'trigger' }
        ],
        startTime: [
          { required: true, validator: validateStart, trigger: 'trigger' }
        ],
        endTime: [
          { required: true, validator: validateEnd, trigger: 'trigger' }
        ]
      },
      //  添加修改商品类型参数
      typeTitle: '增加商品类型',
      typeStatus: 'add',
      show: false,
      formType: {
        goodsTypeName: '',
        goodsTypeId: ''
      },
      typeRules: {
        goodsTypeName: [
          { required: true, message: '请输入类型', trigger: 'trigger' }
        ]
      },
      // 远程搜索参数
      goodsloading: false,
      options: [],
      pickerOptions1: {
        disabledDate(time) {
          return time.getTime() < Date.now() - 8.64e7;
        }
      },
      //  查看商品类型下的商品
      showTypeList: false,
      typeId: '',// 查看商品列表传参
      seeTypeName: ''// 点击查看商品列表传参
    };
  },
  mounted() {
    if (this.$route.params.id) {
      this.typeID = this.$route.params.id
    }
    this.getList();
  },
  methods: {
    async getList() {
      // 1-商品类型 2-促销活动 3-阶梯规则 4-会员返利 
      let data = await getParamsTypeList(this.query.page, this.query.limit, 1, this.typeID);
      console.log(data);
      if (data !== undefined) {
        this.dataList = data.records;
        this.query.total = data.total;
      }
    },
    // 增加商品类型
    add() {
      this.formType.goodsTypeName = '';
      this.formType.goodsTypeId = '';
      this.typeTitle = '增加商品类型';
      this.typeStatus = 'add';
      this.show = true;
    },
    // 修改商品类型
    edit(row) {
      this.typeTitle = '修改商品类型';
      this.typeStatus = 'edit';
      this.show = true;
      this.formType.goodsTypeName = row.name;
      this.formType.goodsTypeId = row.id;
    },
    // 查看商品
    see(row) {
      this.typeId = row.id
      this.seeTypeName = '商品类型--' + row.name
      this.showTypeList = true
    },
    //  添加商品
    addGoods(row) {
      this.form.goodsName = '';
      this.form.goodsUnit = '';
      this.form.goodsId = '';
      this.form.startTime = '';
      this.form.endTime = '';
      this.addShow = true;
      this.form.id = row.id; // 商品类型id
      this.addGoodsTitle = '商品类型--' + row.name;
      this.options = []
    },
    confirm() { },
    // 添加商品
    onSubmit(formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          this.$common.showLoad();
          let data = await addGoods(
            this.form.id,
            this.form.goodsName,
            this.form.goodsId,
            this.form.goodsUnit,
            this.form.startTime,
            this.form.endTime
          );
          this.$common.hideLoad();
          if (data !== undefined) {
            this.addShow = false;
            this.$common.success('保存成功');
          }
        } else {
          console.log('error submit!!');
          // this.$common.error('请检查输入内容是否符合规范')
          return false;
        }
      });
    },
    // 保存商品类型
    onSubmitType(formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          this.$common.showLoad();
          //  新增/修改商品类型
          if (this.typeStatus === 'add') {
            let data = await addGoodsAndmMemberType(this.typeID, 1, this.formType.goodsTypeName);
            this.$common.hideLoad();
            if (data !== undefined) {
              this.$common.success('添加成功');
              this.getList();
              this.show = false;
            }
          } else if (this.typeStatus === 'edit') {
            let data = await editGoodsAndMemberType(
              this.typeID,
              1,
              this.formType.goodsTypeName,
              this.formType.goodsTypeId
            );
            this.$common.hideLoad();
            if (data !== undefined) {
              this.$common.success('修改成功');
              this.getList();
              this.show = false;
            }
          }
        } else {
          console.log('error submit!!');
          // this.$common.error('请检查输入内容是否符合规范')
          return false;
        }
      });
    },
    // 远程搜索商品
    async remoteMethod(query) {
      if (query !== '') {
        this.goodsloading = true;
        let data = await getGoodsList(query);
        console.log(data);
        this.goodsloading = false;
        if (data !== undefined) {
          this.options = data.list;
        }
      } else {
        this.options = [];
      }
    },
    // 选择商品
    selectGoods(e) {
      console.log(e);
      this.form.goodsName = e.name;
      this.form.goodsId = e.id;
      this.form.goodsUnit = e.sellSpecifications;
    }
  }
};
</script>
<style lang="scss" scoped>
@import './index.scss';
</style>
