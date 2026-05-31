<template>
  <el-main class="layout-main">
    <el-card class="card-container">
      <div style="text-align: center; font-size: 34px; font-weight: 600; margin-bottom: 15px">
        欢迎来到仓库管理系统
      </div>
      <div class="full-line-stretch"></div>
      <div style="font-size: 18px; font-weight: 500; margin-bottom: 15px">
        商品列表

        <el-button type="danger" style="float: right; margin-left: 5px" @click="GoodOut">
          出库
        </el-button>
        <el-button type="primary" style="float: right; margin-left: 5px" @click="GoodIn">
          入库
        </el-button>
        <el-button
          type="success"
          style="float: right; margin-left: 5px"
          @click="Add"
          v-if="counterStore.userInfo.roleId != 2"
        >
          新增
        </el-button>

        <!-- 新增/编辑弹窗 -->
        <el-dialog v-model="dialogVisible" title="商品信息" width="500" :before-close="handleClose">
          <el-form
            ref="formRef"
            :model="form"
            :rules="rules"
            label-width="70px"
            style="width: 340px; margin: 0 auto"
          >
            <el-form-item label="货名" prop="name">
              <el-input v-model="form.name" style="width: 100%" />
            </el-form-item>
            <el-form-item label="仓库" prop="storage">
              <el-select v-model="form.storage" placeholder="请选择仓库" style="width: 100%">
                <el-option
                  v-for="item in storageList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.name"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="分类" prop="goodsType">
              <el-select v-model="form.goodsType" placeholder="请选择商品分类" style="width: 100%">
                <el-option
                  v-for="item in typeList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.name"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="数量" prop="count">
              <el-input v-model.number="form.count" style="width: 100%" />
            </el-form-item>
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" rows="3" style="width: 100%" />
            </el-form-item>
          </el-form>
          <div style="text-align: center; margin-top: 20px">
            <el-button @click="cancelDialog">取消</el-button>
            <el-button type="primary" @click="save">{{ isEdit() ? '修改' : '创建' }}</el-button>
          </div>
        </el-dialog>

        <!-- 入库弹窗 -->
        <el-dialog
          v-model="inDialogVisible"
          title="商品入库"
          width="500"
          :before-close="handleInClose"
        >
          <el-form
            ref="inFormRef"
            :model="inForm"
            :rules="inRules"
            label-width="90px"
            style="width: 340px; margin: 0 auto"
          >
            <el-form-item label="商品名称"
              ><span style="font-weight: bold">{{ inForm.name }}</span></el-form-item
            >
            <el-form-item label="所属仓库"
              ><span>{{ inForm.storage }}</span></el-form-item
            >
            <el-form-item label="商品分类"
              ><span>{{ inForm.goodsType }}</span></el-form-item
            >
            <el-form-item label="当前库存"
              ><span style="color: #409eff; font-weight: bold">{{
                inForm.count
              }}</span></el-form-item
            >
            <el-form-item label="入库数量" prop="addCount">
              <el-input
                v-model.number="inForm.addCount"
                placeholder="请输入入库数量"
                type="number"
                style="width: 100%"
              />
            </el-form-item>
            <el-form-item label="入库备注">
              <el-input
                v-model="inForm.remark"
                type="textarea"
                rows="2"
                placeholder="选填：本次入库备注"
                style="width: 100%"
              />
            </el-form-item>
          </el-form>
          <div style="text-align: center; margin-top: 20px">
            <el-button @click="cancelInDialog">取消</el-button>
            <el-button type="primary" @click="doGoodIn">确认入库</el-button>
          </div>
        </el-dialog>

        <!-- 出库弹窗 -->
        <el-dialog
          v-model="outDialogVisible"
          title="商品出库"
          width="500"
          :before-close="handleOutClose"
        >
          <el-form
            ref="outFormRef"
            :model="outForm"
            :rules="outRules"
            label-width="90px"
            style="width: 340px; margin: 0 auto"
          >
            <el-form-item label="商品名称"
              ><span style="font-weight: bold">{{ outForm.name }}</span></el-form-item
            >
            <el-form-item label="所属仓库"
              ><span>{{ outForm.storage }}</span></el-form-item
            >
            <el-form-item label="当前库存"
              ><span style="color: #f56c6c; font-weight: bold">{{
                outForm.count
              }}</span></el-form-item
            >
            <el-form-item label="出库数量" prop="outCount">
              <el-input
                v-model.number="outForm.outCount"
                placeholder="请输入出库数量"
                type="number"
                style="width: 100%"
              />
            </el-form-item>
            <el-form-item label="出库备注">
              <el-input
                v-model="outForm.remark"
                type="textarea"
                rows="2"
                placeholder="选填：本次出库备注"
                style="width: 100%"
              />
            </el-form-item>
          </el-form>
          <div style="text-align: center; margin-top: 20px">
            <el-button @click="cancelOutDialog">取消</el-button>
            <el-button type="danger" @click="doGoodOut">确认出库</el-button>
          </div>
        </el-dialog>

        <el-button type="danger" style="float: right; margin-left: 5px" @click="resetForm"
          >重置</el-button
        >
        <el-button type="primary" style="float: right; margin-left: 5px" @click="loadPost"
          >查询</el-button
        >

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
        <el-input
          v-model="name"
          placeholder="请输入货名"
          style="width: 200px; float: right"
          @change="loadPost"
        />
      </div>

      <el-table
        :data="tableData"
        border
        layout="fixed"
        class="full-table"
        @row-click="handleRowClick"
        :row-class-name="tableRowClassName"
      >
        <el-table-column prop="id" label="商品ID" width="80" />
        <el-table-column prop="name" label="货名" min-width="120" />
        <el-table-column prop="storage" label="仓库" width="90" />
        <el-table-column prop="goodsType" label="分类" width="90" />
        <el-table-column prop="count" label="数量" width="90" />
        <el-table-column prop="remark" label="备注" min-width="150" show-overflow-tooltip />
        <el-table-column label="操作" width="140">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              @click="mod(scope.row)"
              v-if="counterStore.userInfo.roleId != 2"
              >编辑</el-button
            >
            <el-popconfirm title="确定删除这个商品吗？" @confirm="del(scope.row.id)">
              <template #reference
                ><el-button type="danger" size="small" v-if="counterStore.userInfo.roleId != 2"
                  >删除</el-button
                ></template
              >
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="PageNum"
          v-model:page-size="PageSize"
          :page-sizes="[2, 5, 10, 20]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
        />
      </div>
    </el-card>
  </el-main>
</template>

<script setup>
import { getCurrentInstance, onBeforeMount, ref, nextTick } from 'vue'
import { useCounterStore } from '@/stores/counter'
const { proxy } = getCurrentInstance()
const counterStore = useCounterStore()

// 核心数据
const tableData = ref([])
const PageNum = ref(1)
const PageSize = ref(10)
const total = ref(0)
const name = ref('')

// 弹窗控制
const dialogVisible = ref(false)
const inDialogVisible = ref(false)
const outDialogVisible = ref(false)

// 下拉列表数据
const typeList = ref([])
const storageList = ref([])
const searchGoodsType = ref('')
const searchStorage = ref('')

// 选中行
const currentSelectRow = ref(null)

// ---------------- 入库表单 ----------------
const inFormRef = ref(null)
const inForm = ref({
  id: null,
  name: '',
  storage: '',
  goodsType: '',
  count: 0,
  addCount: 0,
  remark: '',
})
const inRules = {
  addCount: [
    { required: true, message: '请输入入库数量', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        const num = Number(value)
        isNaN(num) || num <= 0 ? callback(new Error('入库数量必须大于0')) : callback()
      },
      trigger: 'blur',
    },
  ],
}

// ---------------- 出库表单 ----------------
const outFormRef = ref(null)
const outForm = ref({ id: null, name: '', storage: '', count: 0, outCount: 0, remark: '' })
const outRules = {
  outCount: [
    { required: true, message: '请输入出库数量', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        const num = Number(value)
        if (isNaN(num) || num <= 0) {
          callback(new Error('出库数量必须大于0'))
        } else if (num > outForm.value.count) {
          callback(new Error('出库数量不能超过当前库存'))
        } else {
          callback()
        }
      },
      trigger: 'blur',
    },
  ],
}

// ---------------- 新增/编辑表单 ----------------
const initialForm = { id: null, name: '', storage: '', goodsType: '', count: null, remark: '' }
const form = ref({ ...initialForm })
const formRef = ref(null)
const rules = {
  name: [{ required: true, message: '请输入货名', trigger: 'blur' }],
  storage: [{ required: true, message: '请选择仓库', trigger: 'change' }],
  goodsType: [{ required: true, message: '请选择分类', trigger: 'change' }],
  count: [
    { required: true, message: '请输入数量', trigger: 'blur' },
    { type: 'number', message: '数量必须为数字' },
  ],
}

// 加载下拉列表
const loadTypeList = async () => {
  const res = await proxy.$axios.get(proxy.$baseURL + '/goodsType/listAll')
  typeList.value = res.data.data
}
const loadStorageList = async () => {
  const res = await proxy.$axios.get(proxy.$baseURL + '/systorage/listAll')
  storageList.value = res.data.data
}

// 行选中逻辑
const handleRowClick = (row) => {
  currentSelectRow.value = currentSelectRow.value?.id === row.id ? null : row
}
const tableRowClassName = ({ row }) => {
  return currentSelectRow.value?.id === row.id ? 'selected-row' : ''
}

// 保存操作记录
const saveRecord = (goodsId, operation, detail, count = 0, remark = '') => {
  proxy.$axios.post(proxy.$baseURL + '/record/save', {
    userId: counterStore.userInfo.id,
    adminId: counterStore.userInfo.id,
    goods: goodsId,
    operation: operation,
    detail: detail,
    count: Number(count),
    remark: remark || '',
  })
}

// ==================== 入库功能 ====================
const GoodIn = () => {
  if (!currentSelectRow.value) return proxy.$message.warning('请先选择一行商品！')
  const goods = currentSelectRow.value
  inForm.value = {
    id: goods.id,
    name: goods.name,
    storage: goods.storage,
    goodsType: goods.goodsType,
    count: goods.count,
    addCount: 0,
    remark: '',
  }
  inDialogVisible.value = true
}
const doGoodIn = () => {
  inFormRef.value.validate((valid) => {
    if (!valid) return
    const newCount = inForm.value.count + inForm.value.addCount
    proxy.$axios
      .post(proxy.$baseURL + '/goods/update', {
        id: inForm.value.id,
        count: newCount,
        remark: inForm.value.remark,
      })
      .then((res) => {
        if (res.data.code === 1) {
          proxy.$message.success('入库成功！')
          saveRecord(
            inForm.value.id,
            '商品入库',
            `入库数量：${inForm.value.addCount}`,
            inForm.value.addCount,
            inForm.value.remark,
          )
          inDialogVisible.value = false
          loadPost()
          currentSelectRow.value = null
        } else {
          proxy.$message.error('入库失败！')
        }
      })
  })
}
const handleInClose = (done) => {
  inForm.value.addCount = 0
  inForm.value.remark = ''
  done()
}
const cancelInDialog = () => {
  inDialogVisible.value = false
  inForm.value.addCount = 0
  inForm.value.remark = ''
}

// ==================== 出库功能 ====================
const GoodOut = () => {
  if (!currentSelectRow.value) return proxy.$message.warning('请先选择一行商品！')
  const goods = currentSelectRow.value
  outForm.value = {
    id: goods.id,
    name: goods.name,
    storage: goods.storage,
    count: goods.count,
    outCount: 0,
    remark: '',
  }
  outDialogVisible.value = true
}
const doGoodOut = () => {
  outFormRef.value.validate((valid) => {
    if (!valid) return
    const newCount = outForm.value.count - outForm.value.outCount
    proxy.$axios
      .post(proxy.$baseURL + '/goods/update', {
        id: outForm.value.id,
        count: newCount,
        remark: outForm.value.remark,
      })
      .then((res) => {
        if (res.data.code === 1) {
          proxy.$message.success('出库成功！')
          saveRecord(
            outForm.value.id,
            '商品出库',
            `出库数量：${outForm.value.outCount}`,
            -outForm.value.outCount,
            outForm.value.remark,
          )
          outDialogVisible.value = false
          loadPost()
          currentSelectRow.value = null
        } else {
          proxy.$message.error('出库失败！')
        }
      })
  })
}
const handleOutClose = (done) => {
  outForm.value.outCount = 0
  outForm.value.remark = ''
  done()
}
const cancelOutDialog = () => {
  outDialogVisible.value = false
  outForm.value.outCount = 0
  outForm.value.remark = ''
}

// ==================== 基础增删改查 ====================
const mod = (row) => {
  resetData()
  dialogVisible.value = true
  nextTick(() => {
    form.value = { ...row }
  })
}
const del = (id) => {
  proxy.$axios.post(proxy.$baseURL + '/goods/delete', { id }).then((res) => {
    if (res.data.code === 1) {
      proxy.$message.success('删除成功！')
      saveRecord(id, '删除商品', `删除商品ID：${id}`, 0, '')
      loadPost()
    } else {
      proxy.$message.error('删除失败！')
    }
  })
}
const isEdit = () => !!form.value.id
const save = () => {
  formRef.value.validate((valid) => {
    if (!valid) return
    const url = isEdit() ? '/goods/update' : '/goods/save'
    proxy.$axios.post(proxy.$baseURL + url, form.value).then(async (res) => {
      if (res.data.code === 1) {
        proxy.$message.success('操作成功！')
        // 记录操作日志
        if (isEdit()) {
          saveRecord(
            form.value.id,
            '编辑商品',
            `修改商品：${form.value.name}`,
            form.value.count,
            form.value.remark,
          )
        } else {
          saveRecord(
            res.data.id,
            '新增商品',
            `新增商品：${form.value.name}`,
            form.value.count,
            form.value.remark,
          )
        }
        dialogVisible.value = false
        resetData()
        loadPost()
      } else {
        proxy.$message.error('操作失败！')
      }
    })
  })
}
const loadPost = () => {
  proxy.$axios
    .post(proxy.$baseURL + '/goods/listpage', {
      pageNum: PageNum.value,
      pageSize: PageSize.value,
      name: name.value,
      goodsType: searchGoodsType.value,
      storage: searchStorage.value,
    })
    .then((res) => {
      tableData.value = res.data.records || res.data.data
      total.value = res.data.total || 0
    })
}
const handleClose = (done) => {
  resetData()
  done()
}
const cancelDialog = () => {
  dialogVisible.value = false
  resetData()
}
const resetData = () => {
  form.value = { ...initialForm }
  nextTick(() => {
    formRef.value?.resetFields()
    formRef.value?.clearValidate()
  })
}
const Add = () => {
  resetData()
  loadTypeList()
  loadStorageList()
  dialogVisible.value = true
}
const resetForm = () => {
  name.value = ''
  searchGoodsType.value = ''
  searchStorage.value = ''
  PageNum.value = 1
  loadPost()
}

// 初始化加载
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

/* 选中行高亮 */
:deep(.el-table__row.selected-row > td) {
  background-color: #e6f1ff !important;
}
:deep(.el-table__row.selected-row:hover > td) {
  background-color: #e6f1ff !important;
}
</style>
