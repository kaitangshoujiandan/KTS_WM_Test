import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

export default defineConfig({
  plugins: [vue(), vueDevTools()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  // ✅ 代理配置（必须和下面这段完全一致，不能少任何一项）
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // 后端端口，必须和你SpringBoot启动端口一致
        changeOrigin: true, // 必须加，否则后端拿不到真实请求头
        rewrite: (path) => path.replace(/^\/api/, ''), // 把 /api 前缀去掉，后端直接接 /login
      },
    },
  },
})
