<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="c-box">
        <el-form 
          :model="form" 
          :rules="rules" 
          ref="dataForm" 
          label-width="140px" 
          class="demo-ruleForm">
          <h4>基本信息：</h4>
          <el-form-item label="规则名称：" prop="name">
            <el-input 
              v-model.trim="form.name" 
              maxlength="20" 
              show-word-limit 
              placeholder="请输入规则名称"
              :disabled="type == 2"></el-input>
          </el-form-item>
          <el-form-item label="生效时间：" prop="time">
            <el-date-picker
              v-model="form.time"
              type="datetimerange"
              format="yyyy/MM/dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              :default-time="['00:00:00', '23:59:59']"
              :disabled="type == 2"
              :picker-options="pickerOptions">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="规则说明：" prop="description">
            <el-input
              type="textarea"
              maxlength="50"  
              show-word-limit
              style="max-width: 90%"
              :autosize="{ minRows: 2, maxRows: 2}"
              placeholder="请输入规则说明"
              v-model.trim="form.description"
              :disabled="type == 2">
            </el-input>
          </el-form-item>
          <el-form-item label="选择行为：" prop="behaviorId">
            <div>
              <el-tag
                v-model="form.behaviorId"
                :closable="type == 2 ? false : true"
                type="info" 
                class="item-el-tag" 
                v-for="(item, index) in dataList" 
                :key="index" 
                @close="behaviorClose(index)"
                > 
                {{ item.name }} 
              </el-tag>
            </div>
            <span class="add-span" @click="addClick" v-if="type != 2">{{ dataList && dataList.length > 0 ? '编辑' : '添加' }}</span>
          </el-form-item>
          <div class="basicInfo">
            <yl-button type="primary" v-if="type != 2" @click="basicInfoClick">基本信息保存</yl-button>
          </div>
        </el-form>
        <div v-if="form.id != ''">
          <el-form 
            :model="luckDrawForm" 
            :rules="luckDrawRules" 
            ref="luckDrawRef" 
            label-width="140px" 
            class="demo-ruleForm">
            <h4>参与抽奖：</h4>
            <el-form-item label="选择抽奖活动：" prop="lotteryActivityId">
              <div>
                <el-tag
                  v-model="luckDrawForm.lotteryActivityId"
                  :closable="type == 2 ? false : true"
                  type="info" 
                  class="item-el-tag" 
                  v-for="(item, index) in luckdrawDataList" 
                  :key="index" 
                  @close="luckDrawClose(index)"> 
                  {{ item.activityName }} 
                </el-tag>
              </div>
              <span class="add-span" v-if="type != 2" @click="luckdrawClick">{{ luckdrawDataList && luckdrawDataList.length > 0 ? '编辑' : '添加' }}</span>
            </el-form-item>
            <el-form-item label="消耗积分值：" prop="useIntegralValue">
              <el-input 
                v-model.trim="luckDrawForm.useIntegralValue" 
                maxlength="8" 
                placeholder="请输入"
                :disabled="type == 2"
                @input="e => (luckDrawForm.useIntegralValue = checkInput2(e))">
              </el-input> 积分/次
            </el-form-item>
            <el-form-item label="消耗总次数限制：" prop="useSumTimes">
              <el-input 
                v-model.trim="luckDrawForm.useSumTimes" 
                maxlength="10"  
                placeholder="请输入, 0代表不限制"
                :disabled="type == 2"
                @input="e => (luckDrawForm.useSumTimes = checkInput(e))">
              </el-input> 次
            </el-form-item>
            <el-form-item label="单用户每天消耗总次数限制：" prop="everyDayTimes">
              <el-input 
                v-model.trim="luckDrawForm.everyDayTimes" 
                maxlength="10" 
                placeholder="请输入, 0代表不限制"
                :disabled="type == 2"
                @input="e => (luckDrawForm.everyDayTimes = checkInput(e))">
              </el-input> 次
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
    <div class="bottom-view flex-row-center">
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      <yl-button type="primary" v-if="form.id != '' && type != 2" @click="preservationClick">保存参与抽奖</yl-button>
    </div>
    <!-- 选择行为 -->
    <behavior-pop-up 
      :show="show" 
      :data-list="dataList" 
      @close="close" 
      @addDoctor="addDoctor"
      :data-type="dataType"
      v-if="show">
    </behavior-pop-up>
    <!-- 选择抽奖活动 -->
    <luckdraw-pop-up 
      v-if="luckdrawShow"
      :show="luckdrawShow" 
      :data-list="luckdrawDataList" 
      @close="close" 
      @addLuckDraw="addLuckDraw">
    </luckdraw-pop-up>
  </div>    
</template>

<script>
import { 
  saveBasic,
  getIntegralUseRule,
  saveLotteryConfig
} from '@/subject/admin/api/b2b_api/consume_manage'
import behaviorPopUp from '../../components/behaviorPopUp'
import luckdrawPopUp from '../../components/luckdrawPopUp'
import { formatDate } from '@/subject/admin/utils'
export default {
  name: 'ConsumeManageEstablish',
  components: {
    behaviorPopUp,
    luckdrawPopUp
  },
  data() {
    return {
      form: {
        //基本信息
        id: '',
        name: '',
        time: [],
        description: '',
        behaviorId: ''
      },
      luckDrawForm: {
        //抽奖活动ID
        lotteryActivityId: '',
        //消耗积分值
        useIntegralValue: '',
        //消耗总次数限制	
        useSumTimes: '',
        //单用户每天消耗总次数限制	
        everyDayTimes: ''
      },
      rules: {
        name: [{ required: true, message: '请输入规则名称', trigger: 'blur' }],
        time: [{ required: true, message: '请选择生效时间', trigger: 'change' }],
        description: [{ required: true, message: '请输入规则说明', trigger: 'blur' }],
        behaviorId: [{ required: true, message: '请选择行为', trigger: 'change' }]
      },
      luckDrawRules: {
        lotteryActivityId: [{ required: true, message: '请选择抽奖活动', trigger: 'change' }],
        useIntegralValue: [{ required: true, message: '请输入消耗积分值', trigger: 'blur' }],
        useSumTimes: [{ required: true, message: '请输入消耗总次数限制', trigger: 'blur' }],
        everyDayTimes: [{ required: true, message: '请输入单用户每天消耗总次数限制', trigger: 'blur' }]
      },
      show: false,
      //抽奖活动
      luckdrawShow: false,
      // 选择后的抽奖活动
      luckdrawDataList: [],
      //传递给子组件的数据
      dataList: [],
      //选择行为传递给子组件的数据
      dataType: {
        //平台：1-B2B
        platform: 1,
        //行为类型：1-发放 2-消耗
        type: 2
      },
      // 选择当前日期之后的时间
      pickerOptions: {
        disabledDate(time) {
          //如果没有后面的-8.64e7就是不可以选择今天的
          return time.getTime() < Date.now() - 8.64e7;
        }
      },
      //1 创建 2查看 3编辑
      type: 1,
      //基本信息是否保存成功过
      basicInform: false
    }
  },
  mounted() {
    let query = this.$route.params;
    // query.type  //1 创建 2查看 3编辑 4复制
    if (query.type && query.type != 1) {
      this.type = query.type
      this.getData(query.id)
      if (query.type == 3) {
        this.basicInform = true
      }
    }
  },
  methods: {
    async getData(id) {
      let data = await getIntegralUseRule(id)
      if (data) {
        this.form = {
          ...data
        }
        this.$nextTick(() => {
          if (data.startTime && data.endTime && data.startTime != -28800000) {
            this.$set(this.form, 'time', [formatDate(data.startTime, 'yyyy-MM-dd hh:mm:ss'), formatDate(data.endTime, 'yyyy-MM-dd hh:mm:ss')]);
          } else {
            this.$set(this.form, 'time', [])
          }
          this.dataList = [{
            id: data.behaviorId,
            name: data.behaviorName
          }]
        });
        if (data.integralLotteryConfig) {
          this.luckDrawForm = {
            ...data.integralLotteryConfig
          }
          this.luckdrawDataList = [{
            activityName: data.integralLotteryConfig.lotteryActivityName,
            id: data.integralLotteryConfig.lotteryActivityId
          }]
        }
      }
    },
    //点击基本信息保存
    basicInfoClick() {
      this.$refs['dataForm'].validate( async(valid) => {
        if (valid) {
          let form = this.form;
          this.$common.showLoad();
          let data = await saveBasic(
            form.name,
            form.time && form.time.length > 0 ? form.time[0] : '',
            form.time && form.time.length > 1 ? form.time[1] : '',
            form.description,
            form.behaviorId,
            form.id
          )
          this.$common.hideLoad();
          if (data !== undefined) {
            if (this.type == 1) {
              this.$common.n_success('基本信息保存成功');
            }
            this.basicInform = true;
            this.form.id = data.id;
          }
        }
      })
    },
    // 关闭弹窗
    close() {
      this.show = false;
      this.luckdrawShow = false
    },
    //选择行为 传递过来的数据
    addDoctor(row) {
      this.dataList = []
      this.dataList.push({ ...row})
      this.form.behaviorId = row.id
      this.close();
    },
    //选择活动 确定返回
    addLuckDraw(row) {
      console.log(row, 99)
      this.luckDrawForm.lotteryActivityId = row.id
      this.luckdrawDataList = [{ ...row }]
      this.close(); 
    },
    //点击选择行为 添加
    addClick() {
      this.show = true
    },
    //点击抽奖活动的添加
    luckdrawClick() {
      this.luckdrawShow = true
    },
    //点击底部保存
    preservationClick() {
      if (this.basicInform) {
        this.$refs['luckDrawRef'].validate( async(valid) => {
          if (valid) {
            let luckDraw = this.luckDrawForm;
            this.$common.showLoad();
            let data = await saveLotteryConfig(
              this.form.id,
              luckDraw.lotteryActivityId,
              luckDraw.useIntegralValue,
              luckDraw.useSumTimes,
              luckDraw.everyDayTimes
            )
            this.$common.hideLoad();
            if (data !== undefined) {
              this.$common.alert('保存成功', r => {
                this.$router.go(-1)
              })
            }
          }
        })
      } else {
        this.$common.warn('请先保存基本信息')
      }
    },
    //删除选择行为
    behaviorClose() {
      this.form.behaviorId = '';
      this.dataList = []
    },
    //删除抽奖活动
    luckDrawClose() {
      this.luckDrawForm.lotteryActivityId = '';
      this.luckdrawDataList = []
    },
    checkInput(val) {
      val = val.replace(/[^0-9]/gi, '')
      if (val < 0) {
        val = ''
      }
      return val
    },
    checkInput2(val) {
      val = val.replace(/[^0-9]/gi, '')
      if (val < 1) {
        val = ''
      }
      return val
    }
  }
}
</script>

<style lang="scss" scoped>
  @import './index.scss';
</style>