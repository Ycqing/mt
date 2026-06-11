import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    component: () => import('@/views/Layout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/Dashboard.vue'),
        meta: { title: '首页', icon: 'Odometer' }
      },
      {
        path: 'merchant',
        name: 'Merchant',
        meta: { title: '商家管理', icon: 'Shop' },
        children: [
          {
            path: 'list',
            name: 'MerchantList',
            component: () => import('@/views/merchant/MerchantList.vue'),
            meta: { title: '商家列表', icon: 'List' }
          },
          {
            path: 'add',
            name: 'MerchantAdd',
            component: () => import('@/views/merchant/MerchantForm.vue'),
            meta: { title: '添加商家', icon: 'Plus' }
          },
          {
            path: 'edit/:id',
            name: 'MerchantEdit',
            component: () => import('@/views/merchant/MerchantForm.vue'),
            meta: { title: '编辑商家', icon: 'Edit' }
          },
          {
            path: 'detail/:id',
            name: 'MerchantDetail',
            component: () => import('@/views/merchant/MerchantDetail.vue'),
            meta: { title: '商家详情', icon: 'View' }
          }
        ]
      },
      {
        path: 'fetch',
        name: 'Fetch',
        meta: { title: '数据采集', icon: 'Connection' },
        children: [
          {
            path: 'task',
            name: 'FetchTask',
            component: () => import('@/views/fetch/FetchTask.vue'),
            meta: { title: '采集任务', icon: 'List' }
          },
          {
            path: 'website',
            name: 'WebsiteFetch',
            component: () => import('@/views/fetch/WebsiteFetch.vue'),
            meta: { title: '官网采集', icon: 'Monitor' }
          },
          {
            path: 'financial',
            name: 'FinancialFetch',
            component: () => import('@/views/fetch/FinancialFetch.vue'),
            meta: { title: '财报采集', icon: 'Document' }
          },
          {
            path: 'review',
            name: 'ReviewFetch',
            component: () => import('@/views/fetch/ReviewFetch.vue'),
            meta: { title: '产品评价', icon: 'ChatLineSquare' }
          },
          {
            path: 'third-party',
            name: 'ThirdPartyData',
            component: () => import('@/views/fetch/ThirdPartyData.vue'),
            meta: { title: '第三方数据', icon: 'DataLine' }
          }
        ]
      },
      {
        path: 'analysis',
        name: 'Analysis',
        meta: { title: '对比分析', icon: 'DataAnalysis' },
        children: [
          {
            path: 'compare-new',
            name: 'CompareNew',
            component: () => import('@/views/analysis/CompareNew.vue'),
            meta: { title: '新建对比', icon: 'Plus' }
          },
          {
            path: 'compare-history',
            name: 'CompareHistory',
            component: () => import('@/views/analysis/CompareHistory.vue'),
            meta: { title: '对比历史', icon: 'Clock' }
          }
        ]
      },
      {
        path: 'report',
        name: 'Report',
        meta: { title: '报告中心', icon: 'DocumentCopy' },
        children: [
          {
            path: 'list',
            name: 'ReportList',
            component: () => import('@/views/report/ReportList.vue'),
            meta: { title: '报告列表', icon: 'List' }
          },
          {
            path: 'generate',
            name: 'ReportGenerate',
            component: () => import('@/views/report/ReportGenerate.vue'),
            meta: { title: '生成报告', icon: 'Edit' }
          }
        ]
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = `${to.meta.title} - 抖音商家数据分析系统`
  }
  next()
})

export default router
