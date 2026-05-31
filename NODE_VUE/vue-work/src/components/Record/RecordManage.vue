<template>
  <el-main class="layout-main">
    <el-card class="card-container">
      <div style="text-align: center; font-size: 34px; font-weight: 600; margin-bottom: 15px">
        出入库记录管理
      </div>
      <div class="full-line-stretch"></div>
      <div style="font-size: 18px; font-weight: 500; margin-bottom: 15px">
        出入库记录

        <el-button type="danger" style="float: right; margin-left: 5px" @click="resetForm">
          重置
        </el-button>
        <el-button type="primary" style="float: right; margin-left: 5px" @click="loadPost">
          查询
        </el-button>

        <!-- 选择仓库 原样还原 -->
        <el-select
          v-model="searchStorage"
          placeholder="选择仓库"
          clearable
          style="width: 150px; margin: 0 8px; float: right"
        >
          <el-option
            v-for="item in storageList"
            :key="item.id"
            :label="item.name"
            :value="item.name"
          />
        </el-select>

        <!-- 选择分类 原样还原 -->
        <el-select
          v-model="searchGoodsType"
          placeholder="选择分类"
          clearable
          style="width: 150px; margin: 0 8px; float: right"
        >
          <el-option
            v-for="item in typeList"
            :key="item.id"
            :label="item.name"
            :value="item.name"
          />
        </el-select>

        <!-- 搜索物品 -->
        <el-input
          v-model="name"
          class="responsive-input"
          placeholder="物品名称"
          :suffix-icon="Search"
          style="width: 200px; float: right"
          @change="loadPost"
        />
      </div>

      <el-table
        :data="tableData"
        border
        layout="fixed"
        class="full-table table-fixed-height"
        style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="goodsname" label="货品" min-width="120" />
        <el-table-column prop="storagename" label="仓库" min-width="120" />
        <el-table-column prop="goodstypename" label="分类" min-width="120" />
        <el-table-column prop="username" label="操作人" width="120" />
        <el-table-column prop="count" label="数量" width="100" />
        <el-table-column label="操作时间" prop="createtime" width="190">
          <template #default="{ row }">
            {{ formatDateTime(row.createtime) }}
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="200" show-overflow-tooltip />
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="PageNum"
          v-model:page-size="PageSize"
          :page-sizes="[2, 5, 10, 20]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </el-main>
</template>

<script setup>
import { Search } from '@element-plus/icons-vue'
import { getCurrentInstance, onBeforeMount, ref } from 'vue'
import { useCounterStore } from '@/stores/counter'
const { proxy } = getCurrentInstance()
const counterStore = useCounterStore()

const tableData = ref([])
const PageNum = ref(1)
const PageSize = ref(10)
const total = ref(0)
const name = ref('')

// 时间格式化，去除T和毫秒，展示干净时间
const formatDateTime = (time) => {
  if (!time) return '-'
  const d = new Date(time)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hour = String(d.getHours()).padStart(2, '0')
  const minute = String(d.getMinutes()).padStart(2, '0')
  const second = String(d.getSeconds()).padStart(2, '0')
  return `${year}-${month}-${day} ${hour}:${minute}:${second}`
}

// 仓库、分类下拉框数据
const typeList = ref([])
const storageList = ref([])
const searchGoodsType = ref('')
const searchStorage = ref('')

// 加载仓库列表
const loadStorageList = async () => {
  const res = await proxy.$axios.get(proxy.$baseURL + '/systorage/listAll')
  storageList.value = res.data.data
}

// 加载商品分类列表
const loadTypeList = async () => {
  const res = await proxy.$axios.get(proxy.$baseURL + '/goodsType/listAll')
  typeList.value = res.data.data
}

// 加载记录列表（带仓库、分类、备注筛选）
const loadPost = () => {
  const userId = counterStore.userInfo.id
  const roleId = counterStore.userInfo.roleId

  const params = {
    pageNum: PageNum.value,
    pageSize: PageSize.value,
    name: name.value,
    goodsType: searchGoodsType.value,
    storage: searchStorage.value,
  }

  // 不是超级管理员，只能看自己的记录
  if (roleId !== 0) {
    params.userId = userId
  }

  proxy.$axios.post(proxy.$baseURL + '/record/listpage', params).then((res) => {
    tableData.value = res.data.data || res.data.records
    total.value = res.data.total || 0
  })
}

const handleSizeChange = (val) => {
  PageSize.value = val
  loadPost()
}
const handleCurrentChange = (val) => {
  PageNum.value = val
  loadPost()
}

// 重置查询条件
const resetForm = () => {
  name.value = ''
  searchGoodsType.value = ''
  searchStorage.value = ''
  PageNum.value = 1
  loadPost()
}

// 页面加载时获取数据 + 下拉框数据
onBeforeMount(() => {
  loadPost()
  loadTypeList()
  loadStorageList()
})
</script>

<style scoped>
.layout-main {
  background-color: #f0f2f5;
  padding: 10px;
  height: 100vh;
  box-sizing: border-box;
}
.card-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}
.full-table {
  width: 100% !important;
  max-height: calc(100% - 60px);
  overflow-y: auto;
}
.pagination-wrapper {
  margin-top: 8px;
  padding-bottom: 10px;
  border-top: 1px solid #ebeef5;
  padding-top: 8px;
}
.full-line-stretch {
  position: relative;
  left: -20px;
  width: calc(100% + 40px);
  height: 1px;
  background: #ebeef5;
  margin: 15px 0;
}
</style>
