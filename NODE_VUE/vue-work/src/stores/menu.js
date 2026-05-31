// src/stores/menu.js
import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useMenuStore = defineStore(
  'menu',
  () => {
    // 菜单数据（对应 Vuex 的 state）
    const menu = ref([])

    // 设置菜单（对应 Vuex 的 setMenu）
    const setMenu = (menuList) => {
      menu.value = menuList
    }

    // 获取菜单（对应 Vuex 的 getters）
    const getMenu = computed(() => menu.value)

    return {
      menu,
      setMenu,
      getMenu,
    }

    // 👇 只加了这一段持久化配置
  },
  {
    persist: true,
  },
)
