<template>
  <div class="app-container-dialog">
    <!-- 内容区 -->
    <div class="app-containers">
      <el-form :model="form" :rules="rules" ref="dataForm" label-position="top">
        <div class="top-bar">
          <div class="header-bar header-renative">
            <div class="sign"></div>信息维护
          </div>
          <el-row class="box">
            <el-col :span="12">
              <el-form-item label="企业名称" prop="name">
                <el-input v-model="form.name" show-word-limit placeholder="请输入" maxlength="50"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="企业类型" prop="customerType">
                <el-input v-model="form.customerType" placeholder="请输入"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row class="box">
            <el-col :span="12">
              <el-form-item label="统一社会信用代码/医疗机构许可证" prop="licenseNo">
                <el-input v-model="form.licenseNo" placeholder="请输入"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="联系人" prop="contact">
                <el-input v-model="form.contact" show-word-limit placeholder="请输入" maxlength="10"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row class="box">
            <el-col :span="12">
              <el-form-item label="联系电话">
                <el-input v-model="form.phone" placeholder="请输入"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="'省市区'+cityData" prop="regionCode">
                <!-- <yl-choose-address :province.sync="form.provinceCode" :city.sync="form.cityCode" :area.sync="form.regionCode"/> -->
                 <yl-choose-address v-if="show"
              :province.sync="form.provinceCode"
              :city.sync="form.cityCode"
              :area.sync="form.regionCode"
            />
                <!-- <el-input class="input-city" v-model="form.province" placeholder="请输入省"></el-input>
                <el-input class="input-city" style="margin:0 12px" v-model="form.city" placeholder="请输入市"></el-input>
                <el-input class="input-city" v-model="form.region" placeholder="请输入区"></el-input> -->
              </el-form-item>
            </el-col>
          </el-row>
          <el-row class="box">
            <el-col :span="12">
              <el-form-item label="详细地址" prop="address">
                <el-input v-model="form.address" placeholder="请输入" ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="ERP内码">
                <el-input :disabled="true" v-model="form.innerCode"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row class="box">
            <el-col :span="12">
              <el-form-item label="ERP编码">
                <el-input :disabled="true" v-model="form.sn"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="">
                <span class="error">{{ syncMsg }}</span>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </el-form>
    </div>
    <div class="app-bottom-view">
      <yl-button type="primary" plain @click="cancelClick">取消</yl-button>
      <yl-button type="primary" @click="saveClick">保存</yl-button>
    </div>
  </div>
</template>

<script>
import { ylChooseAddress } from '@/subject/admin/components'
// import { isTel } from '@/subject/admin/utils/rules';
import { findById, maintain } from '@/subject/admin/api/zt_api/erpAdministration';
export default {
  name: 'ErpMaintain',
  components: {
    ylChooseAddress
  },
  props: {
    dialogId: {
      type: String,
      default: () => ''
    },
    dialogData: {
      type: Object,
      default: () => {}
    }
  },
  data() {
    return {
      form: {
        name: '',
        customerType: '',
        licenseNo: '',
        contact: '',
        phone: '',
        // province: '',
        provinceCode: '',
        cityCode: '',
        // city: '',
        // region: '',
        regionCode: '',
        address: '',
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
      rules: {
        name: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
        customerType: [{ required: true, message: '请输入企业类型', trigger: 'blur' }],
        licenseNo: [{ required: true, message: '请输入统一社会信用代码/医疗机构许可证', trigger: 'blur' }],
        contact: [{ required: true, message: '请输入联系人', trigger: 'blur' }],
        phone: [
          { required: true, message: '请输入正确的联系方式', trigger: 'blur' },
          { pattern: /^(1[3|4|5|6|7|8|9])\d{9}$|^0\d{2,3}-?\d{7,8}$|^400[016789]\d{6}$|^400-[016789]\d{2}-\d{4}$/, message: '请输入正确的联系方式' }
        ],
        address: [{ required: true, message: '请输入详细地址', trigger: 'blur' }],
        regionCode: [{ required: true, message: '请选择省市区', trigger: 'change' }]
      },
      cityData: '',
      show: false,
      syncMsg: ''
    }
  },
  watch: {
    dialogId: {
      handler(newVal, oldVal) {
        if (JSON.stringify(this.dialogData) == '{}'){
          this.getData(newVal)
        }
      },
      deep: true,
      immediate: true
    },
    dialogData: {
      handler(newVal, oldVal) {
        if (JSON.stringify(this.dialogData) !== '{}'){
          this.getData2(newVal)
        }
      },
      deep: true,
      immediate: true
    }
    // 'form.regionCode': {
    //   handler(newVal, oldVal) {
    //     console.log(newVal,887887)
    //   },
    //   deep: true,
    // }
  },
  mounted() {
    // let queryId = this.$route.params;
    // if (queryId) {
    //   this.getData(queryId.id)
    // }
  },
  methods: {
    async getData(id) {
      let data = await findById(id)
      if (data !== undefined) {
        this.form = {
          name: data.name,
          customerType: data.customerType,
          licenseNo: data.licenseNo,
          contact: data.contact,
          phone: data.phone,
          provinceCode: data.provinceCode,
          // province: data.province,
          cityCode: data.cityCode,
          // city: data.city,
          regionCode: data.regionCode,
          // region: data.region,
          address: data.address,
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
        };
        this.cityData =' (' + data.province + data.city + data.region+')';
        this.syncMsg = data.syncMsg;
        if (this.form.provinceCode == '' || this.form.provinceCode === null) {
          this.form.provinceCode = '';
          this.form.cityCode = '';
          this.form.regionCode = '';
        } else if (this.form.cityCode == '' || this.form.cityCode === null) {
            this.form.provinceCode = '';
          this.form.cityCode = '';
          this.form.regionCode = '';
        } else if (this.form.regionCode == '' || this.form.regionCode === null) {
          this.form.provinceCode = '';
          this.form.cityCode = '';
          this.form.regionCode = '';
        }
        this.show = true;
      }
      
    },
    getData2(data) {
      this.form = {
        name: data.name,
        customerType: data.customerType,
        licenseNo: data.licenseNo,
        contact: data.contact,
        phone: data.phone,
        // province: data.province,
        provinceCode: data.provinceCode,
        // city: data.city,
        cityCode: data.cityCode,
        // region: data.region,
        regionCode: data.regionCode,
        address: data.address,
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
      };
      this.cityData =' (' + data.province + data.city + data.region+')';
      this.syncMsg = data.syncMsg;
      if (this.form.provinceCode == '' || this.form.provinceCode === null) {
        this.form.provinceCode = '';
        this.form.cityCode = '';
        this.form.regionCode = '';
      } else if (this.form.cityCode == '' || this.form.cityCode === null) {
          this.form.provinceCode = '';
        this.form.cityCode = '';
        this.form.regionCode = '';
      } else if (this.form.regionCode == '' || this.form.regionCode === null) {
        this.form.provinceCode = '';
        this.form.cityCode = '';
        this.form.regionCode = '';
      }
      this.show = true;
    },
    // 保存
    saveClick() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          let query = this.form;
          this.$common.showLoad();
          let data = await maintain({
            ...query
          })
          this.$common.hideLoad();
          if (data.bindResult) {
            this.$common.n_success('维护成功！')
            this.$emit('cancelShow',false);
            this.$common.alert(data.failMsg, r => {
              this.$router.go(-1)
            })
          } else {
            this.$common.error(data.failMsg);
            // this.$emit('cancelShow',false)
            // this.$common.alert(data.failMsg, r => {
            //   this.$router.go(-1)
            // })
          }
        }
      })
    },
    // 取消
    cancelClick() {
      this.$emit('cancelShow',false)
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "./index.scss";
</style>