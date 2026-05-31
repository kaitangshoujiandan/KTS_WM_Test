import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

// 👇 核心：加了 persist: true 开启持久化
export const useCounterStore = defineStore(
  'counter',
  () => {
    // 计数（你原来的，不动）
    const count = ref(0)
    const doubleCount = computed(() => count.value * 2)
    function increment() {
      count.value++
    }

    // ====================
    // 1. 侧边栏折叠状态
    // ====================
    const isCollapse = ref(false)
    function toggleCollapse() {
      isCollapse.value = !isCollapse.value
    }

    // ====================
    // 2. 动态菜单
    // ====================
    const menuList = ref([])
    function setMenu(menulist) {
      menuList.value = menulist
    }

    // ====================
    // 3. 用户信息
    // ====================
    const userInfo = ref({})
    function setUserInfo(info) {
      userInfo.value = info
    }

    // ====================
    // 4. 统一登录方法（最实用）
    // ====================
    function setLoginData(user, menu) {
      userInfo.value = user
      menuList.value = menu
    }

    // ====================
    // 5. 角色权限判断
    // ====================
    const isSuperAdmin = computed(() => userInfo.value?.roleId === 0) // 超管
    const isAdmin = computed(() => userInfo.value?.roleId === 1) // 管理员
    const isUser = computed(() => userInfo.value?.roleId === 2) // 普通用户

    return {
      // 原有
      count,
      doubleCount,
      increment,

      // 侧边栏
      isCollapse,
      toggleCollapse,

      // 菜单
      menuList,
      setMenu,

      // 用户
      userInfo,
      setUserInfo,
      setLoginData,

      // 角色
      isSuperAdmin,
      isAdmin,
      isUser,
    }
  },
  {
    // ✅ 持久化配置（刷新不会丢！）
    persist: true,
  },
)
