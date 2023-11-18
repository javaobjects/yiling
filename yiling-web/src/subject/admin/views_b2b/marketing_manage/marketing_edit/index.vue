<template>
  <div class="">
    <div class="top-type">
      <el-radio-group v-model="type" @change="typeChange" :disabled="operationType != 3">
        <el-radio-button :label="1">满赠</el-radio-button>
        <el-radio-button :label="2">特价</el-radio-button>
        <el-radio-button :label="3">秒杀</el-radio-button>
        <el-radio-button :label="4">组合包</el-radio-button>
      </el-radio-group>
  </div>
    <!-- 满赠 -->
    <give-edit v-if="giveEditVisible" ref="giveEditRef"></give-edit>
    <!-- 秒杀 -->
    <seckill-edit v-if="seckillEditVisible" :activity-type="type" ref="seckillEditRef"></seckill-edit>
    <!-- 组合包 -->
    <group-edit v-if="groupEditVisible" ref="groupEditRef"></group-edit>
  </div>
</template>

<script>
import GiveEdit from '../components/give_edit';
import SeckillEdit from '../components/seckill_edit';
import GroupEdit from '../components/group_edit';

export default {
  components: {
    GiveEdit,
    SeckillEdit,
    GroupEdit
  },
  computed: {

  },
  data() {
    return {
      // 创建活动类型
      operationType: 3,
      type: 0,
      giveEditVisible: false,
      seckillEditVisible: false,
      groupEditVisible: false
    };
  },
  mounted() {
    this.$log('mounted:',this.$route.params)
    this.operationType = this.$route.params.operationType
    this.type = this.$route.params.type
    this.typeChange(this.type)
  },
  methods: {
    typeChange(type) {
      if (type == 1) {
        this.giveEditVisible = true
        this.seckillEditVisible = false
        this.groupEditVisible = false
        this.$nextTick( () => {
          this.$refs.giveEditRef.init()
        })
      } else if (type == 2 || type == 3) {
        this.giveEditVisible = false
        this.seckillEditVisible = true
        this.groupEditVisible = false
        this.$nextTick( () => {
          this.$refs.seckillEditRef.init()
        })
      } else if (type == 4) {
        this.giveEditVisible = false
        this.seckillEditVisible = false
        this.groupEditVisible = true
        this.$nextTick( () => {
          this.$refs.groupEditRef.init()
        })
      }
    }
  }
};
</script>

<style lang="scss" >
.my-form-box{
  .el-form-item{
    .el-form-item__label{
      color: $font-title-color; 
    }
    label{
      font-weight: 400 !important;
    }
  }
  .my-form-item-right{
    label{
      font-weight: 400 !important;
    }
  }
}
</style>
<style lang="scss" scoped>
@import './index.scss';
</style>
