import { createRouter, createWebHistory } from 'vue-router'
import IndexPage from '@/IndexPage.vue'
import AppHome from '@/components/AppHome.vue'
import AppLogin from '@/components/AppLogin.vue'
import UserManage from '@/components/User/UserManage.vue' // 你的用户管理页面
import AdminManage from '@/components/Admin/AdminManage.vue' // 你的管理员管理页面
import StorageManage from '@/components/storage/StorageManage.vue'
import goodsManage from '@/components/Goods/GoodsManage.vue'
import goodsTypeManage from '@/components/GoodsType/GoodsTypeManage.vue'
import RecordManage from '@/components/Record/RecordManage.vue'

const routes = [
  {
    path: '/login',
    component: AppLogin,
  },
  {
    path: '/',
    component: IndexPage,
    redirect: '/home',
    children: [
      { path: '/home', component: AppHome }, // 首页 → 个人中心
      { path: '/user', component: UserManage }, // 用户管理 → UserManage.vue
      { path: '/admin', component: AdminManage }, // 管理员管理 → AdminManage.vue
      { path: '/Storage', component: StorageManage },
      { path: '/Goodstype', component: goodsTypeManage },
      { path: '/Goods', component: goodsManage },
      { path: '/Record', component: RecordManage },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
