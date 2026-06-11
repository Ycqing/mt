<template>
  <el-container class="layout-container">
    <el-aside width="220px" class="sidebar">
      <div class="logo">
        <el-icon :size="24"><DataLine /></el-icon>
        <span class="logo-text">商家分析</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        router
        class="sidebar-menu"
        background-color="#1a1a2e"
        text-color="#a0a0a0"
        active-text-color="#0072ef"
      >
        <el-menu-item index="/dashboard">
          <el-icon><Odometer /></el-icon>
          <span>首页</span>
        </el-menu-item>

        <el-sub-menu index="/merchant">
          <template #title>
            <el-icon><Shop /></el-icon>
            <span>商家管理</span>
          </template>
          <el-menu-item index="/merchant/list">商家列表</el-menu-item>
          <el-menu-item index="/merchant/add">添加商家</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="/fetch">
          <template #title>
            <el-icon><Connection /></el-icon>
            <span>数据采集</span>
          </template>
          <el-menu-item index="/fetch/task">采集任务</el-menu-item>
          <el-menu-item index="/fetch/website">官网采集</el-menu-item>
          <el-menu-item index="/fetch/financial">财报采集</el-menu-item>
          <el-menu-item index="/fetch/review">产品评价</el-menu-item>
          <el-menu-item index="/fetch/third-party">第三方数据</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="/analysis">
          <template #title>
            <el-icon><DataAnalysis /></el-icon>
            <span>对比分析</span>
          </template>
          <el-menu-item index="/analysis/compare-new">新建对比</el-menu-item>
          <el-menu-item index="/analysis/compare-history">对比历史</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="/report">
          <template #title>
            <el-icon><DocumentCopy /></el-icon>
            <span>报告中心</span>
          </template>
          <el-menu-item index="/report/list">报告列表</el-menu-item>
          <el-menu-item index="/report/generate">生成报告</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="header">
        <div class="header-left">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item v-for="item in breadcrumbs" :key="item.path" :to="item.path">
              {{ item.title }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-icon class="header-icon"><Bell /></el-icon>
          <el-icon class="header-icon"><Setting /></el-icon>
        </div>
      </el-header>

      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

const activeMenu = computed(() => {
  const path = route.path
  if (path.includes('/merchant/detail')) return '/merchant/list'
  if (path.includes('/merchant/edit')) return '/merchant/list'
  return path
})

const breadcrumbs = computed(() => {
  const matched = route.matched.filter(item => item.meta && item.meta.title)
  return matched.map(item => ({
    title: item.meta.title,
    path: item.path
  }))
})
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.sidebar {
  background: #1a1a2e;
  overflow-x: hidden;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  border-bottom: 1px solid #2a2a3e;
}

.logo-text {
  color: #fff;
}

.sidebar-menu {
  border-right: none;
  background: #1a1a2e;
}

.sidebar-menu:not(.el-menu--collapse) {
  width: 220px;
}

.header {
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
  height: 60px;
}

.header-left {
  display: flex;
  align-items: center;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.header-icon {
  font-size: 18px;
  color: #606266;
  cursor: pointer;
  padding: 4px;
  border-radius: 4px;
  transition: all 0.2s;
}

.header-icon:hover {
  background: #f5f7fa;
  color: #0072ef;
}

.main-content {
  background: #f0f2f5;
  padding: 20px;
  overflow-y: auto;
}
</style>
