<template>
  <div class="home-page">
    <div class="welcome-banner">
      <h1>欢迎使用仓库管理系统</h1>
      <p class="time">{{ currentTime }}</p>
    </div>

    <div class="info-card">
      <h3 class="card-title">个人信息</h3>
      <div class="info-list">
        <div class="info-item">
          <span class="label">用户名：</span>
          <span class="value">{{ userInfo?.name || '未填写' }}</span>
        </div>
        <div class="info-item">
          <span class="label">账号：</span>
          <span class="value">{{ userInfo?.no || '未填写' }}</span>
        </div>
        <div class="info-item">
          <span class="label">性别：</span>
          <span class="value">{{
            userInfo?.sex === 1 ? '男' : userInfo?.sex === 0 ? '女' : '未填写'
          }}</span>
        </div>
        <div class="info-item">
          <span class="label">电话：</span>
          <span class="value">{{ userInfo?.phone || '未填写' }}</span>
        </div>
        <div class="info-item">
          <span class="label">角色：</span>
          <span class="value role-tag">{{ getRoleName(userInfo?.roleId) }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const userInfo = ref({})
const currentTime = ref('')
let timer = null

const initUserInfo = () => {
  try {
    const storageInfo = localStorage.getItem('userInfo')
    if (storageInfo) {
      // 现在直接解析出来就是 user 对象
      userInfo.value = JSON.parse(storageInfo)
    }
  } catch (err) {
    console.error('读取用户信息失败：', err)
    userInfo.value = {}
  }
}

const formatTime = () => {
  const now = new Date()
  const year = now.getFullYear()
  const month = String(now.getMonth() + 1).padStart(2, '0')
  const day = String(now.getDate()).padStart(2, '0')
  const hour = String(now.getHours()).padStart(2, '0')
  const minute = String(now.getMinutes()).padStart(2, '0')
  const second = String(now.getSeconds()).padStart(2, '0')
  return `${year}/${month}/${day} ${hour}:${minute}:${second}`
}

const getRoleName = (roleId) => {
  const roleMap = {
    0: '超级管理员',
    1: '管理员',
    2: '普通用户',
  }
  return roleMap[roleId] || '未知角色'
}

onMounted(() => {
  initUserInfo()
  currentTime.value = formatTime()
  timer = setInterval(() => {
    currentTime.value = formatTime()
  }, 1000)
})

onUnmounted(() => {
  clearInterval(timer)
})
</script>

<style scoped>
.home-page {
  padding: 20px;
  background-color: #f5f5f5;
  width: 100%;
  height: 100%;
  box-sizing: border-box;
}

.welcome-banner {
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  color: #fff;
  padding: 30px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(64, 158, 255, 0.2);
}

.welcome-banner h1 {
  margin: 0 0 10px;
  font-size: 24px;
  font-weight: 600;
}

.time {
  margin: 0;
  font-size: 14px;
  opacity: 0.9;
}

.info-card {
  background: #fff;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.card-title {
  margin: 0 0 20px;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.info-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.info-item {
  display: flex;
  align-items: center;
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.info-item:last-child {
  border-bottom: none;
  padding-bottom: 0;
}

.label {
  width: 80px;
  font-size: 16px;
  color: #606266;
  font-weight: 500;
}

.value {
  font-size: 16px;
  color: #303133;
}

.role-tag {
  color: #67c23a;
  font-weight: 600;
}
</style>
