<template>
  <el-header class="layout-header">
    <div class="header-content">
      <!-- 折叠按钮（PINIA 全局控制，动态菜单专用） -->
      <el-icon class="collapse-btn" @click="counterStore.toggleCollapse">
        <Fold v-if="!counterStore.isCollapse" />
        <Expand v-else />
      </el-icon>

      <div class="logo">仓库管理系统</div>

      <div class="user-info">
        <el-dropdown @command="handleCommand">
          <!-- 这里自动显示登录的用户名 -->
          <span class="user-name">{{ username }}</span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="center">个人中心</el-dropdown-item>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </el-header>
</template>

<script setup>
import { Fold, Expand } from '@element-plus/icons-vue'
import { getCurrentInstance, computed } from 'vue'
import { useCounterStore } from '@/stores/counter' // 引入PINIA

const { proxy } = getCurrentInstance()
const counterStore = useCounterStore() // 全局状态

// 自动获取当前登录的用户名（从pinia读取）
const username = computed(() => {
  return counterStore.userInfo?.name || '管理员'
})

// 退出登录（动态版）
const handleCommand = (command) => {
  if (command === 'logout') {
    localStorage.removeItem('userInfo')
    counterStore.setUserInfo({})
    counterStore.setMenu([])
    proxy.$message.success('退出成功！')
    proxy.$router.push('/login')
  }
}
</script>

<style scoped>
.layout-header {
  background-color: #304156;
  color: #fff;
  padding: 0 20px;
  display: flex;
  align-items: center;
}
.header-content {
  width: 100%;
  display: flex;
  align-items: center;
  gap: 16px;
}
.collapse-btn {
  cursor: pointer;
  font-size: 20px;
  color: #fff;
}
.logo {
  font-size: 18px;
  font-weight: bold;
}
.user-info {
  margin-left: auto;
}
.user-name {
  cursor: pointer;
}
</style>
