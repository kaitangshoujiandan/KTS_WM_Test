import { createApp } from 'vue'
import App from './App.vue'
import router from './router' // 路由
import ElementPlus from 'element-plus' // ElementPlus
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import 'element-plus/dist/index.css' // 样式
import axios from 'axios'
import store from './stores' // Pinia

// ✅ 我只加了这一行！修复图标未定义报错
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)

// 全局注册图标（现在正常了）
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
app.mount('#app')
