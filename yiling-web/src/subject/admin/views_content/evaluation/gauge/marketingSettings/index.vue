<template>
   <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <el-form
        :model="form"
        :rules="rules"
        class="demo-ruleForm"
        label-width="80px"
        ref="dataForm">
        <div class="c-box">
          <div class="title">当前量表：{{ title }}</div>
          <div class="c-box-content">
            <div class="c-box-title"><span></span>关联推荐药品: </div>
            <div class="c-box-item" v-for="(item, index) in form.goodsList" :key="'goodsList.' + index">
              <!-- 关联推荐药品 -->
              <div class="result-for">
                <el-row>
                  <el-col :span="12">
                    <el-form-item 
                      label="标准库ID"
                      :prop="'goodsList.' + index + '.standardId'"
                      :rules="{ required: true, message: '标准库ID不能为空', trigger: 'blur'}">
                      <el-input class="select-width" v-model.trim="item.standardId"></el-input>
                      <yl-button class="button-margin" type="primary" plain @click="matchingClick(item, index)">匹配</yl-button>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="通用名">
                      <el-input v-model.trim="item.name" :disabled="true" class="item-input"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="24">
                    <el-form-item label="适应症" >
                      <el-input v-model.trim="item.indications" class="item-input"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="24">
                    <el-form-item label="跳转链接">
                      <el-input v-model.trim="item.jumpUrl" class="item-input"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
              </div>
              <span class="item-details" @click="detailsClick(1, item, index)"><i class="el-icon-delete icon_style"></i></span>
            </div>
            <div class="add-button">
              <yl-button type="primary" @click="addClick(1)" plain>添加</yl-button>
            </div>
            <!-- 改善建议 -->
            <div class="c-box-title"><span></span>改善建议: </div>
            <el-form-item v-if="form.adviceList && form.adviceList.length > 0" class="item-more" label="更多跳转链接" prop="moreJumpUrl">
              <el-input style="width:600px" v-model.trim="form.moreJumpUrl"></el-input>
            </el-form-item>
            <div class="c-box-item" v-for="(item, index) in form.adviceList" :key="'adviceList' + index" @mouseover="mouseover(index)">
              <div class="result-for">
                <el-row>
                  <el-col :span="12">
                    <el-form-item 
                      label="标题"
                      :prop="'adviceList.' + index + '.title'"
                      :rules="{ required: true, message: '标题不能为空', trigger: 'blur'}">
                      <el-input v-model.trim="item.title" class="item-input"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12" >
                    <el-form-item 
                      label="来源"
                      :prop="'adviceList.' + index + '.sourceDesc'"
                      :rules="{ required: true, message: '来源不能为空', trigger: 'blur'}">
                      <el-input v-model.trim="item.sourceDesc" class="item-input"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="24">
                    <el-form-item 
                      label="跳转链接"
                      :prop="'adviceList.' + index + '.jumpUrl'"
                      :rules="{ required: true, message: '跳转链接不能为空', trigger: 'blur'}">
                      <el-input v-model.trim="item.jumpUrl" class="item-input"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="24">
                    <el-form-item label="图片">
                      <yl-upload
                        :default-url="item.picUrl"
                        :extral-data="{type: 'healthEvaluate'}"
                        :oss-key="'healthEvaluate'"
                        @onSuccess="onSuccess"/>
                      <p class="explain">建议尺寸：214 x 146像素，支持.jpg 、.png格式</p>
                    </el-form-item>
                  </el-col>
                </el-row>
              </div>
              <span class="item-details" @click="detailsClick(2, item, index)"><i class="el-icon-delete icon_style"></i></span>
            </div>
            <div class="add-button">
              <yl-button type="primary" @click="addClick(2)" plain>添加</yl-button>
            </div>
            <!-- 推广服务 -->
            <div class="c-box-title"><span></span>推广服务: </div>
            <div class="c-box-item" v-for="(item, index) in form.promoteList" :key="'promoteList' + index" @mouseover="promoteMouseover(index)">
              <div class="result-for">
                <el-row>
                  <el-col :span="24">
                    <el-form-item 
                      label="跳转链接" 
                      :prop="'promoteList.' + index + '.jumpUrl'"
                      :rules="{ required: true, message: '跳转链接不能为空', trigger: 'blur'}">
                      <el-input v-model.trim="item.jumpUrl" class="item-input"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row> 
                <el-row>
                  <el-col :span="24">
                    <el-form-item 
                      label="图片"
                      :prop="'promoteList.' + index + '.picUrl'"
                      :rules="{ required: true, message: '图片不能为空', trigger: 'blur'}"
                      >
                      <yl-upload
                        :default-url="item.picUrl"
                        :extral-data="{type: 'healthEvaluate'}"
                        :oss-key="'healthEvaluate'"
                        @onSuccess="promoteOnSuccess"/>
                      <p class="explain">建议尺寸：650 x 118像素，支持.jpg 、.png格式</p>
                    </el-form-item>
                  </el-col>
                </el-row>
              </div>
              <span class="item-details" @click="detailsClick(3, item, index)"><i class="el-icon-delete icon_style"></i></span>
            </div>
            <div class="add-button">
              <yl-button type="primary" @click="addClick(3)" plain>添加</yl-button>
            </div>
          </div>
        </div>
      </el-form>
    </div>
    <div class="flex-row-center bottom-view">
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
      <yl-button type="primary" @click="preservationClick">保存</yl-button>
    </div>
  </div>
</template>

<script>
import { ylUpload } from '@/subject/admin/components'
import { getGoodsInfoByStandardId, marketSet, getMarketSet, delMarket } from '@/subject/admin/api/content_api/evaluation'
export default {
  name: 'MarketingSettings',
  components: {
    ylUpload
  },
  data() {
    return {
      form: {
        goodsList: [
          
        ],
        adviceList: [
          
        ],
        promoteList: [
          
        ],
        //更多跳转链接
        moreJumpUrl: ''
      },
      rules: {
        moreJumpUrl: [{ required: true, message: '请输入更多跳转链接', trigger: 'blur' }]
      },
      fzData: [],
      title: '',
      healthEvaluateId: '',
      //改善建议鼠标移入下标
      adviceIndex: null,
      //推广服务鼠标移入下标
      promoteIndex: null
    }
  },
  mounted() {
    let dataTitle = this.$route.params;
    if (dataTitle.title) {
      this.title = dataTitle.title;
      this.healthEvaluateId = parseInt(dataTitle.id);
      this.getData()
    }
  },
  methods: {
    // 获取数据信息
    async getData() {
      let data = await getMarketSet(this.healthEvaluateId)
      if (data) {
        this.form = {
          ...data,
          moreJumpUrl: ''
        };
        if (data.adviceList && data.adviceList.length > 0) {
          this.form.moreJumpUrl = JSON.parse(JSON.stringify(data.adviceList[0].moreJumpUrl))
        } else {
          this.form.moreJumpUrl = ''
        }
      }
    },
    //点击匹配
    async matchingClick(item, index) {
      if (item.standardId) {
        this.$common.showLoad();
        let data = await getGoodsInfoByStandardId(item.standardId)
        this.$common.hideLoad();
        if (data !== undefined) {
          this.form.goodsList[index].name = data.name;
          this.form.goodsList[index].indications = data.indications;
        }
      } else {
        this.$common.warn('请输入药品标准库id')
      }
      
    },
    //添加
    addClick(val) {
      if (val == 1) {
        this.form.goodsList.push({
          healthEvaluateId: this.healthEvaluateId,
          standardId: '',
          indications: '',
          jumpUrl: '',
          name: ''
        })
      } else if (val == 2) {
        this.form.adviceList.push({
          healthEvaluateId: this.healthEvaluateId,
          title: '',
          sourceDesc: '',
          jumpUrl: '',
          imgUrl: '',
          pic: '',
          picUrl: '',
          moreJumpUrl: ''
        })
      } else if (val == 3) {
        this.form.promoteList.push({
          healthEvaluateId: this.healthEvaluateId,
          jumpUrl: '',
          pic: '',
          picUrl: ''
        })
      }
    },
    // 删除
    detailsClick(val, item, index) {
      if (val == 1) {
        if (item.id) {
          this.$confirm(`确认删除标准库ID为 ${ item.standardId } 的药品 ！`, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          })
          .then( async() => {
            //确定
            this.$common.showLoad();
            let data = await delMarket(
              item.id,
              1
            )
            this.$common.hideLoad();
            if (data !== undefined) {
              this.$common.n_success('删除成功!');
              this.getData()
            }
          })
          .catch(() => {
            //取消
          });
        } else {
          this.form.goodsList.splice(index, 1)
        }
      } else if (val == 2) {
        if (item.id) {
          this.$confirm(`确认删除标题为 ${ item.title } 的改善建议 ！`, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          })
          .then( async() => {
            //确定
            this.$common.showLoad();
            let data = await delMarket(
              item.id,
              2
            )
            this.$common.hideLoad();
            if (data !== undefined) {
              this.$common.n_success('删除成功!');
              this.getData()
            }
          })
          .catch(() => {
            //取消
          });
        } else {
          this.form.adviceList.splice(index, 1)
        }
      } else {
        if (item.id) {
          this.$confirm(`确认删除跳转链接为 ${ item.jumpUrl } 的推广服务 ！`, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          })
          .then( async() => {
            //确定
            this.$common.showLoad();
            let data = await delMarket(
              item.id,
              3
            )
            this.$common.hideLoad();
            if (data !== undefined) {
              this.$common.n_success('删除成功!');
              this.getData()
            }
          })
          .catch(() => {
            //取消
          });
        } else {
          this.form.promoteList.splice(index, 1)
        }
      }
    },
    //点击保存
    preservationClick() {
      this.$refs['dataForm'].validate( async(valid) => {
        if (valid) {
          let form = this.form;
          if (form.moreJumpUrl) {
            form.adviceList[0].moreJumpUrl = form.moreJumpUrl
          }
          this.$common.showLoad();
          let data = await marketSet(
            form.adviceList,
            form.goodsList,
            form.promoteList
          )
          this.$common.hideLoad();
          if (data !== undefined) {
            this.$common.alert('保存成功', r => {
              this.$router.go(-1)
            })
          }
        }
      })
    },
    // 图片上传后的返回
    contentCover() {

    },
    //改善建议 鼠标移入的下标
    mouseover(index) {
      this.adviceIndex = index;
    },
    onSuccess(data) {
      this.form.adviceList[this.adviceIndex].pic = data.key;
      this.form.adviceList[this.adviceIndex].picUrl = data.url;
    },
    // 推广服务 鼠标移入的下标
    promoteMouseover(index) {
      this.promoteIndex = index;
    },
    promoteOnSuccess(data) {
      this.form.promoteList[this.promoteIndex].pic = data.key;
      this.form.promoteList[this.promoteIndex].picUrl = data.url;
    }
  }
}
</script>

<style lang="scss" scoped>
@import './index.scss';
</style>