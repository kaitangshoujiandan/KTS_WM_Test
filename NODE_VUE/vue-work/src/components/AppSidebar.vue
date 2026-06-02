<template>
  <el-aside :width="isCollapse ? '64px' : '200px'" class="layout-aside">
    <el-scrollbar>
      <el-menu
        :default-openeds="[]"
        :collapse="isCollapse"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        router
      >
        <!-- 首页 -->
        <el-menu-item index="/home">
          <el-icon :size="18"><House /></el-icon>
          <span>首页</span>
        </el-menu-item>

        <!-- 动态菜单 -->
        <template v-for="menu in menuList" :key="menu.id">
          <el-menu-item :index="`/${menu.menuClick}`">
            <el-icon :size="18">
              <!-- 数据库写啥图标名，这里直接渲染啥 -->
              <component :is="menu.menuIcon || 'House'" />
            </el-icon>
            <span>{{ menu.menuName }}</span>
          </el-menu-item>
        </template>
      </el-menu>
    </el-scrollbar>
  </el-aside>
</template>

<script setup>
import { House,} from '@element-plus/icons-vue'
import { useCounterStore } from '@/stores/counter'
import { onMounted, computed } from 'vue'

const counterStore = useCounterStore()
const isCollapse = computed(() => counterStore.isCollapse)
const menuList = computed(() => counterStore.menuList || [])
// 新增：组件挂载优先从本地缓存补全Pinia用户数据
onMounted(() => {
  const userStr = localStorage.getItem('userInfo')
  if (userStr) {
    const user = JSON.parse(userStr)
    // 强制写入pinia，保证当前组件能拿到roleId
    counterStore.setUserInfo(user)
    // 后续：用user.roleId去请求后端菜单接口 setMenuList
  }
})
</script>

<style scoped>
.layout-aside {
  background-color: #304156;
  transition: width 0.3s;
}
</style>
