import { UserLayout, TabLayout, RouteView, BlankLayout, PageView } from '@/components/layouts'

/**
 * 走菜单，走权限控制
 * @type {[null,null]}
 */
export const asyncRouterMap = [

  {
    path: '/',
    name: 'defaultDocSearch',
    component: TabLayout,
    meta: { title: '首页' },
    redirect: '/defaultDocSearch',
    // redirect: '/dashboard/analysis',
    children: [
    ]
  },
  {
    path: '*', redirect: '/404', hidden: true
  }
]

/**
 * 基础路由
 * @type { *[] }
 */
export const constantRouterMap = [
  {
    path: '/user',
    component: UserLayout,
    redirect: '/user/login',
    hidden: true,
    children: [
      {
        path: 'login',
        name: 'login',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/Login')
      },
    ]
  },
  {
    path: '/404',
    component: () => import(/* webpackChunkName: "fail" */ '@/views/exception/404')
  },
  {
    path: '/defaultDocSearch',
    name:'defaultDocSearch',
    meta: { title: '首页' ,keepAlive: true },
    component: () => import('@/views/km/search/DefaultDocSearch')
  },
  {
    path:'/docSearch',
    name:'docSearch',
    meta: { title: '检索结果' },
    component: () => import('@/views/km/search/DocSearch')
  },
  {
    path:'/login',
    component: () => import('@/views/km/search/Login')
  }

]
