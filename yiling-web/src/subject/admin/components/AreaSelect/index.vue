<template>
  <yl-dialog
    :title="title"
    width="980px"
    @confirm="confirm"
    :visible.sync="showDialog">
    <div class="dialog-content">
      <div class="tree-view" v-for="(item, index) in dataList" :key="index">
        <div class="pad-lr-16">
          <div class="flex-row-left tree-item" :class="[item.expand ? 'border-1px-b' : '']" @click.stop="handleExpandIconClick(item, index)">
            <el-checkbox
              v-model="item.checked"
              :indeterminate="item.indeterminate"
              @click.native.stop
              @change="(v) => handleCheckChange(item, index)">
            </el-checkbox>
            <span class="font-size-lg font-important-color mar-l-10">{{ item.name }}</span>
            <span class="flex1 font-size-base font-light-color mar-l-16">{{ item.desc }}</span>
            <span :class="['el-icon-arrow-right', item.expand ? 'arrow-down' : '']"></span>
          </div>
          <el-collapse-transition>
            <div v-show="item.expand">
              <div class="tree-item-pad flex-row-left flex-wrap">
                <div class="flex-row-left tree-item-2" v-for="(child, childIndex) in item.children" :key="childIndex">
                  <el-checkbox
                    v-model="child.checked"
                    @click.native.stop
                    @change="(v) => handleCheckChange(child, index, childIndex)">
                  </el-checkbox>
                  <span class="font-size-base font-important-color mar-l-10">{{ child.name }}</span>
                </div>
              </div>
            </div>
          </el-collapse-transition>
        </div>
      </div>
    </div>
  </yl-dialog>
</template>

<script>
  import { mapGetters } from 'vuex'

  export default {
    name: 'AreaSelect',
    components: {
    },
    props: {
      show: {
        type: Boolean,
        default: true
      },
      title: {
        type: String,
        default: '设置区域'
      }
    },
    computed: {
      showDialog: {
        get() {
          return this.show
        },
        set(val) {
          this.$emit('update:show', val)
        }
      },
      ...mapGetters([
        'areaList'
      ])
    },
    data() {
      return {
        dataList: []
      }
    },
    mounted() {
      if (this.areaList && this.areaList.length) {
        this.dataList = this.areaList
      } else {
        this.getAddress()
      }
    },
    methods: {
      // 获取区域选择器
      async getAddress() {
        this.$common.showLoad()
        let data = await this.$store.dispatch('used/getAreaList')
        this.$common.hideLoad()
        if (data) {
          data = data.map(item => {
            item.desc = ''
            item.level = 1
            item.expand = false
            item.indeterminate = false
            if (Array.isArray(item.children) && item.children.length) {
              item.children = item.children.map(child => {
                child.level = 2
                child.checked = false
                return child
              })
            }
            return item
          })
          this.dataList = data
        }
      },
      // expand按钮点击
      handleExpandIconClick(item, index) {
        let data = this.$common.clone(this.dataList)
        if (data[index]) {
          data[index].expand = !data[index].expand
        }
        this.dataList = data
      },
      setChecked(data, check) {
        if (data && data.length) {
          data = data.map(item => {
            item.checked = check
            return item
          })
        }
        return data
      },
      // checkbox选中
      handleCheckChange(item, index, childIndex) {
        let data = this.$common.clone(this.dataList)
        let levelItem1 = data[index]
        let choose = []
        if (item.level === 1) {
          if (item.checked) {
            levelItem1.children = this.setChecked(levelItem1.children, true)
          } else {
            levelItem1.children = this.setChecked(levelItem1.children, false)
          }
          choose = levelItem1.children.filter(itm => itm.checked === true)
          levelItem1.indeterminate = choose.length > 0 && choose.length !== levelItem1.children.length
        } else if (item.level === 2) {
          choose = levelItem1.children.filter(itm => itm.checked === true)
          if (item.checked) {
            levelItem1.indeterminate = choose.length !== levelItem1.children.length
            levelItem1.checked = choose.length === levelItem1.children.length
          } else {
            levelItem1.indeterminate = choose.length > 0
            levelItem1.checked = !levelItem1.indeterminate
            if (choose.length === 0) {
              levelItem1.checked = false
              levelItem1.indeterminate = false
            }
          }
        }
        if (choose.length) {
          levelItem1.desc = `已选择${choose.length}个市`
        } else {
          levelItem1.desc = ''
        }
        this.dataList = data
      },
      // 获取选中节点
      getChooseNodes() {
        let all = []
        let province = 0
        let city = 0
        this.dataList.map(item => {
          let obj = {
            code: item.code,
            indeterminate: item.indeterminate,
            level: item.level,
            name: item.name,
            parentCode: item.parentCode,
            children: []
          }
          if (Array.isArray(item.children) && item.children.length) {
            item.children.map(child => {
              if (child.checked || child.indeterminate) {
                obj.children.push({
                  code: child.code,
                  indeterminate: child.indeterminate,
                  level: child.level,
                  name: child.name,
                  parentCode: child.parentCode
                })
                city++
              }
            })
          }
          if (item.checked || item.indeterminate) {
            all.push(obj)
            province++
          }
        })
        return {
          nodes: all,
          desc: `已选择${province}个省/直辖区，${city}个市`
        }
      },
      confirm() {
        let data = this.getChooseNodes()
        this.$log(data)
        this.$emit('choose', data)
      }
    }
  }
</script>

<style lang="scss" scoped>
  .tree-view {
    background: #FAFAFA;
    border-radius: 4px;
    margin-bottom: 16px;
    .tree-item {
      cursor: pointer;
      height: 40px;
      &-2 {
        cursor: pointer;
        height: 40px;
        min-width: 120px;
        margin-right: 20px;
      }
      .el-icon-arrow-right {
        transition: all .5s;
      }
      .arrow-down {
        transform: rotate(90deg);
      }
    }
    .tree-item-pad {
      padding-left: 26px;
      padding-right: 16px;
    }
  }
</style>
