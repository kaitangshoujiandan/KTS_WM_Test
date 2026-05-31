<template>
  <!-- 登录状态：显示登录页 -->
  <AppLogin v-if="!isLogin" @login-success="handleLoginSuccess" />

  <!-- 登录成功：显示主页 -->
  <el-container class="main-container" v-else>
    <AppHeader :isCollapse="isCollapse" @toggle="isCollapse = !isCollapse" @logout="handleLogout" />

    <el-container class="body-container">
      <AppSidebar :isCollapse="isCollapse" />
      <router-view />
      <!-- 路由展示页面 -->
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import AppHeader from './components/AppHeader.vue'
import AppSidebar from './components/AppSidebar.vue'
import AppLogin from './components/AppLogin.vue'

const isCollapse = ref(false)

const handleLogout = () => {
  isLogin.value = false
  localStorage.removeItem('userInfo')
}

const isLogin = ref(false)

onMounted(() => {
  const user = localStorage.getItem('userInfo')
  if (user) {
    isLogin.value = true
  }
})

const handleLoginSuccess = () => {
  isLogin.value = true
}
</script>

<style scoped>
.main-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
}
.body-container {
  flex: 1;
  display: flex;
  flex-direction: row;
  overflow: hidden;
}
</style>
