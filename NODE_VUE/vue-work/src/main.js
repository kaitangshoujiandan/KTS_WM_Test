import { createApp } from 'vue'
import App from './App.vue'
import router from './router' // 路由
import ElementPlus from 'element-plus' // ElementPlus
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import 'element-plus/dist/index.css' // 样式
import axios from 'axios'
import store from './stores' // Pinia

import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)

// 全局注册图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.config.warnHandler = () => {}
app.use(ElementPlus, {
  locale: zhCn,
  // 自定义分页文案
  pagination: {
    pagesize: '条/页', // 把默认的 /page 改成 条/页
    goto: '前往',
    page: '页',
    total: '共',
    item: '条',
  },
})

app.use(router)
app.use(store) // 使用 Pinia
app.config.globalProperties.$axios = axios // 全局注册 axios
app.config.globalProperties.$baseURL = 'http://localhost:8080' // 全局注册 baseURL
axios.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('jwt_token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error),
)

// 响应拦截器：处理 token 过期或无效
axios.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      // 清除本地存储的 token 和用户信息
      localStorage.removeItem('jwt_token')
      localStorage.removeItem('userInfo')
      // 如果当前不在登录页，跳转到登录页
      if (window.location.pathname !== '/login') {
        window.location.href = '/login'
      }
      // 可选：弹出提示
      import('element-plus').then(({ ElMessage }) => {
        ElMessage.error('登录已过期，请重新登录')
      })
    }
    return Promise.reject(error)
  },
)
app.mount('#app')
