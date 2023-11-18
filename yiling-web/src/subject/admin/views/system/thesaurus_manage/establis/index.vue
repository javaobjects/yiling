<template>
  <div class="app-container">
    <div class="app-container-content has-bottom-bar">
      <div class="c-box">
        <div>
          词语： <el-input v-model.trim="title" :disabled="type==1" maxlength="20" show-word-limit></el-input>
        </div>
        <div class="words">
          <div class="words-tit">
            <p>扩展词：</p>
            <div class="words-div" v-if="type !== 1">
              <div class="words-button" v-if="expansionWord == null || expansionWord.id == undefined">
                <yl-button type="primary" plain @click="addClick('1')">添加为扩展词</yl-button>
              </div>
              <div class="words-button" v-else>
                <yl-button type="primary" plain @click="preservationClick('1')">保存</yl-button>
              </div>
            </div>
            <div class="words-radio" v-if="expansionWord && expansionWord.status != null">
              <el-radio-group v-model="expansionWord.status" :disabled="type == 1">
                <el-radio :label="0">正常</el-radio>
                <el-radio :label="1">停止</el-radio>
              </el-radio-group>
            </div>
          </div>
        </div>
        <div class="words">
          <div class="words-tit">
            <p>停止词：</p>
            <div class="words-div" v-if="type !== 1">
              <div class="words-button" v-if="stopWord == null || stopWord.id == undefined">
                <yl-button type="primary" plain @click="addClick('2')">添加为停止词</yl-button>
              </div>
              <div class="words-button" v-else>
                <yl-button type="primary" plain @click="preservationClick('2')">保存</yl-button>
              </div>
            </div>
            <div class="words-radio" v-if="stopWord && stopWord.status != null">
              <el-radio-group v-model="stopWord.status" :disabled="type == 1">
                <el-radio :label="0">正常</el-radio>
                <el-radio :label="1">停止</el-radio>
              </el-radio-group>
            </div>
          </div>
        </div>
        <div class="words">
          <div class="words-tit">
            <p>单向同义词：</p>
            <div class="words-div" v-if="type !== 1">
              <div class="words-button" v-if="dataListShow">
                <yl-button type="primary" plain @click="preservationClick('3')">保存</yl-button>
              </div>
              <div class="words-button" v-else>
                <yl-button type="primary" plain @click="addClick('3')">添加为单向同义词</yl-button>
              </div>
            </div>
            <div class="words-radio" v-if="oneWaySynonym && oneWaySynonym.status != null">
              <el-radio-group v-model="oneWaySynonym.status" :disabled="type==1">
                <el-radio :label="0">正常</el-radio>
                <el-radio :label="1">停止</el-radio>
              </el-radio-group>
            </div>
          </div>
          <div class="thesaurus-data">
            <yl-table border :show-header="true" :list="dataList" >
              <el-table-column align="center" min-width="60" label="ID" prop="id">
              </el-table-column>
              <el-table-column align="center" min-width="250" label="关联词" prop="word">
                <template slot-scope="{ row }">
                  <div>
                    <el-input v-model.trim="row.word" :disabled="type==1" maxlength="20" show-word-limit></el-input>
                  </div>
                </template>
              </el-table-column>
              <el-table-column fixed="right" align="center" label="操作" min-width="80">
                <template slot-scope="{ row, $index }">
                  <div>
                    <el-switch :disabled="type==1"
                      v-model="row.status"
                      :active-value="0"
                      :inactive-value="1"
                      active-color="#13ce66"
                      inactive-color="#dcdfe6">
                    </el-switch>
                    <yl-button v-if="row.delete" style="margin-left:10px" type="text" @click="deleteClick($index)">删除</yl-button>
                  </div>
                </template>
              </el-table-column>
            </yl-table>
            <div class="thesaurus-data-button" v-if="type !== 1">
               <yl-button type="primary" plain icon="el-icon-plus" @click="addSingle">增加单向同义词</yl-button>
            </div>
          </div>
        </div>
        <div class="words">
          <div class="words-tit">
            <p>双向同义词：</p>
            <div class="words-div" v-if="type !== 1">
              <div class="words-button" v-if="dataListShow2">
                <yl-button type="primary" plain @click="preservationClick('4')">保存</yl-button>
              </div>
              <div class="words-button" v-else>
                <yl-button type="primary" plain @click="addClick('4')">添加为双向同义词</yl-button>
              </div>
            </div>
            <div class="words-radio" v-if="twoWaySynonym && twoWaySynonym.status != null">
              <el-radio-group v-model="twoWaySynonym.status" :disabled="type==1">
                <el-radio :label="0">正常</el-radio>
                <el-radio :label="1">停止</el-radio>
              </el-radio-group>
            </div>
          </div>
          <div class="thesaurus-data">
            <yl-table border :show-header="true" :list="dataList2" >
              <el-table-column align="center" min-width="60" label="ID" prop="id">
              </el-table-column>
              <el-table-column align="center" min-width="250" label="关联词" prop="word">
                <template slot-scope="{ row }">
                  <div>
                    <el-input v-model.trim="row.word" maxlength="20" :disabled="type==1" show-word-limit></el-input>
                  </div>
                </template>
              </el-table-column>
              <el-table-column fixed="right" align="center" label="操作" min-width="80">
                <template slot-scope="{ row, $index }">
                  <div>
                    <el-switch :disabled="type==1"
                      v-model="row.status"
                      :active-value="0"
                      :inactive-value="1"
                      active-color="#13ce66"
                      inactive-color="#dcdfe6">
                    </el-switch>
                    <yl-button v-if="row.delete" style="margin-left:10px" type="text" @click="deleteClick2($index)">删除</yl-button>
                  </div>
                </template>
              </el-table-column>
            </yl-table>
            <div class="thesaurus-data-button" v-if="type !== 1">
              <yl-button type="primary" plain icon="el-icon-plus" @click="addSingle2">增加双向同义词</yl-button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="bottom-view flex-row-center">
      <yl-button type="primary" plain @click="$router.go(-1)">返回</yl-button>
    </div>
  </div>
</template>
<script>
import { saveWord, wordDetail } from '@/subject/admin/api/role'
export default {
  data() {
    return {
      title: '',
      dataList: [],
      dataList2: [],
      type: 0, // 0 新增 1 查看详情 2 修改
      expansionWord: {}, // 扩展词
      stopWord: {}, // 停止词
      oneWaySynonym: {}, // 单向同义词
      twoWaySynonym: {}, // 双向同义词
      dataListShow: false,
      dataListShow2: false,
      name: ''
    }
  },
  mounted() {
    let query = this.$route.params;
    this.type = query.type;
    this.name = query.word == '0' ? '' : query.word;
    if (query.type !== 0) {
      this.getList()
    }
  },
  methods: {
    async getList() {
      let data = await wordDetail(this.name)
      if (data !== undefined) {
        this.expansionWord = data.expansionWord;
        this.stopWord = data.stopWord;
        this.oneWaySynonym = data.oneWaySynonym;
        this.twoWaySynonym = data.twoWaySynonym;
        this.title = data.mainWord;
        if (data.oneWaySynonym !== null ) {
          this.dataList = data.oneWaySynonym.refWordList;
          this.dataListShow = true;
        } else {
          this.dataList = [];
          this.dataListShow = false;
        }
        if (data.twoWaySynonym !== null) {
          this.dataList2 = data.twoWaySynonym.refWordList;
           this.dataListShow2 = true;
        } else {
          this.dataList2 = [];
          this.dataListShow2 = false;
        }
      }
    },
    // 新增
    addClick(type) {
      if (this.title == '') {
        this.$common.warn('请输入您所设置的词语！')
      } else {
        this.$confirm('是否更新词库文件！', '提示', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        })
        .then( async() => {
          if (type == '1' || type == '2') {
            this.$common.showLoad();
            let data1 = await saveWord({
              type: type,
              word: this.title,
              uploadFlag: true
            })
            this.$common.hideLoad();
            if (data1 !== undefined) {
              this.$common.n_success('操作成功')
              this.$router.go(-1)
            }
          } else if (type == '3' || type == '4') {
            this.$common.showLoad();
            let data3 = await saveWord({
              type: type,
              word: this.title,
              uploadFlag: true,
              refWordList: type == '3' ? this.dataList : this.dataList2
            })
            this.$common.hideLoad();
            if (data3 !== undefined) {
              this.$common.n_success('操作成功')
              this.$router.go(-1)
              // this.getList()
              // if (type == '3') {
              //   this.dataList = []
              // } else if (type == '4') {
              //   this.dataList2 = []
              // }
            }
          }
        })
        .catch( async() => {
          if (type == '1' || type == '2' ) {
            this.$common.showLoad();
            let datas1 = await saveWord({
              type: type,
              word: this.title,
              uploadFlag: false
            })
            this.$common.hideLoad();
            if (datas1 !== undefined) {
              this.$common.n_success('操作成功')
            }
          } else if (type == '3' || type == '4') {
            this.$common.showLoad();
            let data3 = await saveWord({
              type: type,
              word: this.title,
              uploadFlag: false,
              refWordList: type == '3' ? this.dataList : this.dataList2
            })
            this.$common.hideLoad();
             if (data3 !== undefined) {
              this.$common.n_success('操作成功')
              this.$router.go(-1)
              // this.getList()
            }
          }
        });
      }
    },
    // 保存
    async preservationClick(type) {
      if (this.title == '') {
        this.$common.warn('请输入您所设置的词语！')
      } else {
        this.$confirm('是否更新词库文件！', '提示', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        })
        .then( async() => {
          if (type == '1') {
            this.$common.showLoad();
            let data1 = await saveWord({
              id: this.expansionWord.id,
              refId: this.expansionWord.refId,
              status: this.expansionWord.status,
              type: this.expansionWord.type,
              word: this.title,
              uploadFlag: true
            })
            this.$common.hideLoad();
            if (data1 !== undefined) {
              this.$common.n_success('操作成功')
              this.$router.go(-1)
            }
          } else if (type == '2'){
            this.$common.showLoad();
            let data2 = await saveWord({
              id: this.stopWord.id,
              refId: this.stopWord.refId,
              status: this.stopWord.status,
              type: this.stopWord.type,
              word: this.title,
              uploadFlag: true
            })
            this.$common.hideLoad();
            if (data2 !== undefined) {
              this.$common.n_success('操作成功')
              this.$router.go(-1)
            }
          } else if (type == '3') {
            this.$common.showLoad();
            let data3 = await saveWord({
              id: this.oneWaySynonym.id,
              refId: this.oneWaySynonym.refId,
              refWordList: this.dataList,
              status: this.oneWaySynonym.status,
              type: this.oneWaySynonym.type,
              word: this.title,
              uploadFlag: true
            })
            this.$common.hideLoad();
            if (data3 !== undefined) {
              this.$common.n_success('操作成功')
              this.$router.go(-1)
            }
          } else if (type == '4') {
            this.$common.showLoad();
            let data4 = await saveWord({
              id: this.twoWaySynonym.id,
              refId: this.twoWaySynonym.refId,
              refWordList: this.dataList2,
              status: this.twoWaySynonym.status,
              type: this.twoWaySynonym.type,
              word: this.title,
              uploadFlag: true
            })
            this.$common.hideLoad();
            if (data4 !== undefined) {
              this.$common.n_success('操作成功')
              this.$router.go(-1)
            }
          }
        })
        .catch( async() => {
          if (type == '1') {
            this.$common.showLoad();
            let data1 = await saveWord({
              id: this.expansionWord.id,
              refId: this.expansionWord.refId,
              status: this.expansionWord.status,
              type: this.expansionWord.type,
              word: this.title,
              uploadFlag: false
            })
            this.$common.hideLoad();
            if (data1 !== undefined) {
              this.$common.n_success('操作成功')
              this.$router.go(-1)
            }
          } else if (type == '2'){
            this.$common.showLoad();
            let data2 = await saveWord({
              id: this.stopWord.id,
              refId: this.stopWord.refId,
              status: this.stopWord.status,
              type: this.stopWord.type,
              word: this.title,
              uploadFlag: false
            })
            this.$common.hideLoad();
            if (data2 !== undefined) {
              this.$common.n_success('操作成功')
              this.$router.go(-1)
            }
          } else if (type == '3') {
            this.$common.showLoad();
            let data3 = await saveWord({
              id: this.oneWaySynonym.id,
              refId: this.oneWaySynonym.refId,
              refWordList: this.dataList,
              status: this.oneWaySynonym.status,
              type: this.oneWaySynonym.type,
              word: this.title,
              uploadFlag: false
            })
            this.$common.hideLoad();
            if (data3 !== undefined) {
              this.$common.n_success('操作成功')
              this.$router.go(-1)
            }
          } else if (type == '4') {
            this.$common.showLoad();
            let data4 = await saveWord({
              id: this.twoWaySynonym.id,
              refId: this.twoWaySynonym.refId,
              refWordList: this.dataList2,
              status: this.twoWaySynonym.status,
              type: this.twoWaySynonym.type,
              word: this.title,
              uploadFlag: false
            })
            this.$common.hideLoad();
            if (data4 !== undefined) {
              this.$common.n_success('操作成功')
              this.$router.go(-1)
            }
          }
        })
      }
    },
    // 增加单向同义词
    addSingle() {
      this.dataList.push({
        id: '',
        status: 0,
        word: '',
        delete: true
      })
    },
    // 增加双向 同义词
    addSingle2() {
      this.dataList2.push({
        id: '',
        status: 0,
        word: '',
        delete: true
      })
    },
    // 删除单向同义词
    deleteClick(val) {
      this.dataList.splice(val,1)
    },
    // 删除双向同义词
    deleteClick2(val) {
      this.dataList2.splice(val,1)
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "./index.scss";
</style>