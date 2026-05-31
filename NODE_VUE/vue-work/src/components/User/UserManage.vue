<template>
  <el-main class="layout-main">
    <el-card class="card-container">
      <div style="text-align: center; font-size: 34px; font-weight: 600; margin-bottom: 15px">
        欢迎来到仓库管理系统
      </div>
      <div class="full-line-stretch"></div>
      <div style="font-size: 18px; font-weight: 500; margin-bottom: 15px">
        用户列表

        <el-button type="success" style="float: right; margin-left: 5px" @click="Add">
          新增
        </el-button>

        <el-dialog v-model="dialogVisible" title="提示" width="500" :before-close="handleClose">
          <el-form
            ref="formRef"
            :model="form"
            :rules="rules"
            label-width="70px"
            style="width: 340px; margin: 0 auto"
          >
            <el-form-item label="账号" prop="no">
              <el-input v-model="form.no" style="width: 100%" :disabled="isEdit()" />
              <div v-if="isEdit()" style="color: #909399; font-size: 12px; margin-top: 3px">
                账号不可修改
              </div>
            </el-form-item>

            <el-form-item label="密码" prop="password" v-if="!isEdit()">
              <el-input v-model="form.password" style="width: 100%" />
            </el-form-item>

            <el-form-item label="名字" prop="name">
              <el-input v-model="form.name" style="width: 100%" />
            </el-form-item>

            <el-form-item label="年龄" prop="age">
              <el-input v-model.number="form.age" style="width: 100%" />
            </el-form-item>

            <el-form-item label="性别" prop="sex">
              <el-radio-group v-model="form.sex">
                <el-radio value="1">男</el-radio>
                <el-radio value="0">女</el-radio>
              </el-radio-group>
            </el-form-item>

            <el-form-item label="电话" prop="phone">
              <el-input v-model.number="form.phone" style="width: 100%" />
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

        <el-select v-model="sex" placeholder="性别" style="width: 115px; float: right">
          <el-option label="男" value="1" />
          <el-option label="女" value="0" />
        </el-select>

        <el-input
          v-model="name"
          class="responsive-input"
          placeholder="请输入名字"
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
        <el-table-column prop="id" label="用户ID" width="80" />
        <el-table-column prop="no" label="用户编号" width="100" />
        <el-table-column prop="name" label="姓名" min-width="100" />
        <el-table-column label="性别" width="70">
          <template #default="scope">
            <el-tag :type="scope.row.sex == 1 ? 'primary' : 'success'">
              {{ scope.row.sex == 1 ? '男' : '女' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="角色" min-width="120">
          <template #default="scope">
            <el-tag
              :type="scope.row.roleId == 0 ? 'danger' : scope.row.roleId == 1 ? 'primary' : 'info'"
            >
              {{ scope.row.roleId == 0 ? '超级管理员' : scope.row.roleId == 1 ? '管理员' : '用户' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="电话" min-width="120" show-overflow-tooltip />
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
            <el-popconfirm title="确定删除吗？" @confirm="del(scope.row.id)">
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
const sex = ref('')
const dialogVisible = ref(false)

const mod = (row) => {
  resetData()
  dialogVisible.value = true
  nextTick(() => {
    form.value.id = row.id
    form.value.no = row.no
    form.value.name = row.name
    form.value.password = null
    form.value.age = row.age
    form.value.phone = row.phone
    form.value.sex = row.sex + ''
    form.value.roleId = row.roleId
  })
}

const del = (id) => {
  proxy.$axios.post(proxy.$baseURL + '/delete', { id }).then((res) => {
    if (res.data.code === 1) {
      proxy.$message.success('删除成功！')
      loadPost()
    } else {
      proxy.$message.error('删除失败！')
    }
  })
}

const checkDuplicate = async (rule, value, callback) => {
  if (form.value.id) {
    return callback()
  }
  try {
    const res = await proxy.$axios.get(`${proxy.$baseURL}/user/findByNo?no=${value}`)
    if (res.data.code === 1) {
      callback()
    } else {
      callback(new Error('账号已经存在'))
    }
  } catch (err) {
    callback(new Error('校验失败，请稍后重试'))
  }
}

const initialForm = {
  id: null,
  no: '',
  name: '',
  password: '',
  age: null,
  phone: null,
  sex: '0',
  roleId: '2',
}
const form = ref({ ...initialForm })
const formRef = ref(null)

const validator = (rule, value, callback) => {
  if (value > 150 || value < 1) {
    callback(new Error('年龄必须在 1 ~ 149 岁之间'))
  } else {
    callback()
  }
}

const rules = {
  no: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { validator: checkDuplicate, trigger: 'blur' },
  ],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  name: [{ required: true, message: '请输入名字', trigger: 'blur' }],
  age: [
    { required: true, message: '请输入年龄', trigger: 'blur' },
    { type: 'number', message: '年龄必须为数字' },
    { validator: validator, trigger: 'blur' },
  ],
  sex: [{ required: true, message: '请选择性别', trigger: 'change' }],
  phone: [
    { required: true, message: '请输入电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号' },
  ],
}

const isEdit = () => !!form.value.id

const save = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      proxy.$axios.post(proxy.$baseURL + '/saveOrUpdate', form.value).then(async (res) => {
        if (res.data.code === 1) {
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
    .post(proxy.$baseURL + '/listpage', {
      pageNum: PageNum.value,
      pageSize: PageSize.value,
      name: name.value,
      sex: sex.value,
      roleId: 2,
    })
    .then((res) => {
      tableData.value = res.data.records || res.data.data
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
  sex.value = ''
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
