<template>
  <el-main class="layout-main">
    <el-card class="card-container">
      <div style="text-align: center; font-size: 34px; font-weight: 600; margin-bottom: 15px">
        欢迎来到仓库管理系统
      </div>
      <div class="full-line-stretch"></div>
      <div style="font-size: 18px; font-weight: 500; margin-bottom: 15px">
        仓库管理

        <el-button type="success" style="float: right; margin-left: 5px" @click="Add">
          新增
        </el-button>

        <el-dialog v-model="dialogVisible" title="仓库信息" width="500" :before-close="handleClose">
          <el-form
            ref="formRef"
            :model="form"
            :rules="rules"
            label-width="70px"
            style="width: 340px; margin: 0 auto"
          >
            <el-form-item label="仓库名" prop="name">
              <el-input v-model="form.name" style="width: 100%" />
            </el-form-item>

            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" style="width: 100%" type="textarea" rows="3" />
            </el-form-item>
          </el-form>

          <div style="text-align: center; margin-top: 20px">
            <el-button @click="cancelDialog">取消</el-button>
            <el-button type="primary" @click="save">{{ isEdit() ? '修改' : '创建' }}</el-button>
          </div>
        </el-dialog>

        <el-button type="danger" style="float: right; margin-left: 5px" @click="resetForm">
          重置
        </el-button>
        <el-button type="primary" style="float: right; margin-left: 5px" @click="loadPost">
          查询
        </el-button>

        <el-input
          v-model="name"
          class="responsive-input"
          placeholder="请输入仓库名"
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
        <el-table-column prop="id" label="仓库ID" width="80" />
        <el-table-column prop="name" label="仓库名称" min-width="150" />
        <el-table-column prop="remark" label="备注" min-width="200" show-overflow-tooltip />
        <el-table-column label="操作" width="140">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              style="margin-right: 5px"
              @click="mod(scope.row)"
            >
              编辑
            </el-button>
            <el-popconfirm title="确定删除这个仓库吗？" @confirm="del(scope.row.id)">
              <template #reference>
                <el-button type="danger" size="small">删除</el-button>
              </template>
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
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </el-main>
</template>

<script setup>
import { Search } from '@element-plus/icons-vue'
import { getCurrentInstance, onBeforeMount, ref, nextTick } from 'vue'
const { proxy } = getCurrentInstance()
const tableData = ref([])
const PageNum = ref(1)
const PageSize = ref(10)
const total = ref(0)
const name = ref('')
const dialogVisible = ref(false)

const mod = (row) => {
  resetData()
  dialogVisible.value = true
  nextTick(() => {
    form.value.id = row.id
    form.value.name = row.name
    form.value.remark = row.remark
  })
}

const del = (id) => {
  proxy.$axios.post(proxy.$baseURL + '/systorage/delete', { id }).then((res) => {
    if (res.data.code == 1) {
      proxy.$message.success('删除成功！')
      loadPost()
    } else {
      proxy.$message.error('删除失败！')
    }
  })
}

const initialForm = {
  id: null,
  name: '',
  remark: '',
}
const form = ref({ ...initialForm })
const formRef = ref(null)

const rules = {
  name: [{ required: true, message: '请输入仓库名称', trigger: 'blur' }],
}

const isEdit = () => !!form.value.id

const save = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      let url = form.value.id ? '/systorage/update' : '/systorage/save'
      proxy.$axios.post(proxy.$baseURL + url, form.value).then(async (res) => {
        if (res.data.code == 1) {
          proxy.$message.success('操作成功！')
          dialogVisible.value = false
          await nextTick()
          resetData()
          loadPost()
        } else {
          proxy.$message.error('操作失败！')
        }
      })
    }
  })
}

const loadPost = () => {
  proxy.$axios
    .post(proxy.$baseURL + '/systorage/listpage', {
      pageNum: PageNum.value,
      pageSize: PageSize.value,
      name: name.value,
    })
    .then((res) => {
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

const handleClose = (done) => {
  resetData()
  done()
}

const cancelDialog = () => {
  resetData()
  dialogVisible.value = false
}

const resetData = () => {
  form.value = JSON.parse(JSON.stringify(initialForm))
  nextTick(() => {
    formRef.value?.resetFields()
    formRef.value?.clearValidate()
  })
}

const Add = () => {
  dialogVisible.value = false
  nextTick(() => {
    resetData()
    dialogVisible.value = true
  })
}

const resetForm = () => {
  name.value = ''
  PageNum.value = 1
  loadPost()
}

onBeforeMount(() => loadPost())
</script>

<style scoped>
.layout-main {
  background-color: #f0f2f2;
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
:deep(.el-table__inner-wrapper) {
  width: 100% !important;
}
:deep(.el-table__body-wrapper) {
  overflow-x: hidden !important;
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
