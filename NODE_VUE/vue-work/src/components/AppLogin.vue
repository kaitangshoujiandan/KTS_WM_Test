<template>
  <div class="login-page">
    <div class="login-container">
      <h2>仓库管理系统</h2>
      <p>User Login</p>

      <el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" class="login-form">
        <el-form-item prop="no">
          <el-input
            v-model="loginForm.no"
            placeholder="请输入账号"
            size="large"
            prefix-icon="user"
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            size="large"
            prefix-icon="lock"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" size="large" class="login-btn" @click="handleLogin">
            登 录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import axios from 'axios'
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useCounterStore } from '@/stores/counter'

const router = useRouter()
const counterStore = useCounterStore()

const loginForm = ref({
  no: '',
  password: '',
})

const loginFormRef = ref(null)
const loginRules = {
  no: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
}

const emit = defineEmits(['login-success'])

const handleLogin = () => {
  loginFormRef.value.validate(async (valid) => {
    if (!valid) return
    try {
      const res = await axios.post('http://localhost:8080/auth/login', {
        no: loginForm.value.no,
        password: loginForm.value.password,
      })

      if (res.data.token) {
        ElMessage.success('登录成功！')
        localStorage.setItem('jwt_token', res.data.token)
        //直接保存后端全部用户信息
        const user = res.data.user
        localStorage.setItem('userInfo', JSON.stringify(user))
        counterStore.setUserInfo(user)
        counterStore.setMenu(res.data.menuList)
        router.push('/home')
        emit('login-success')
      } else {
        ElMessage.error(res.data.message || '登录失败')
      }
    } catch (err) {
      ElMessage.error('请求异常，请检查网络或后端')
      console.error(err)
    }
  })
}
</script>

<style scoped>
.login-page {
  width: 100vw;
  height: 100vh;
  background: linear-gradient(135deg, #409eff 10%, #66b1ff 100%);
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-container {
  width: 400px;
  padding: 45px 40px 50px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.login-container h2 {
  margin: 0;
  font-size: 26px;
  color: #333;
}

.login-container p {
  margin: 8px 0 35px;
  font-size: 14px;
  color: #999;
}

.login-form {
  width: 100%;
}

.login-btn {
  width: 100%;
  height: 44px;
  font-size: 16px;
  font-weight: 500;
}
</style>
