<template>
  <div class="report-list">
    <div class="page-header">
      <h2 class="page-title">报告列表</h2>
      <el-button type="primary" @click="$router.push('/report/generate')">
        <el-icon><Plus /></el-icon> 生成报告
      </el-button>
    </div>

    <div class="filter-bar">
      <div class="filter-item">
        <span class="label">报告类型</span>
        <el-select v-model="queryParams.type" placeholder="全部" style="width: 150px;" clearable>
          <el-option label="完整报告" value="full" />
          <el-option label="营销报告" value="marketing" />
          <el-option label="财务报告" value="financial" />
          <el-option label="对比报告" value="comparison" />
        </el-select>
      </div>
      <el-button type="primary" @click="loadData">
        <el-icon><Search /></el-icon> 搜索
      </el-button>
    </div>

    <div class="table-container">
      <el-table :data="list" stripe v-loading="loading">
        <el-table-column prop="id" label="#" width="60" />
        <el-table-column prop="reportName" label="报告名称" min-width="200" />
        <el-table-column prop="reportType" label="类型" width="100">
          <template #default="{ row }">
            <el-tag size="small">{{ getTypeName(row.reportType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reportPeriod" label="报告周期" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <span :class="['tag-status', getStatusClass(row.status)]">
              {{ getStatusName(row.status) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="generateTime" label="生成时间" width="160" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button v-if="row.status === 1" type="primary" link size="small" @click="download(row)">下载</el-button>
            <el-button type="primary" link size="small" @click="preview(row)">预览</el-button>
            <el-button type="danger" link size="small" @click="remove(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useReportStore } from '@/stores/report'

const store = useReportStore()
const loading = ref(false)
const list = ref([])
const queryParams = reactive({ type: '' })

function getTypeName(type) {
  const map = { full: '完整报告', marketing: '营销报告', financial: '财务报告', comparison: '对比报告', product: '产品报告' }
  return map[type] || type
}

function getStatusName(status) {
  const map = { 0: '生成中', 1: '已完成', 2: '失败' }
  return map[status] || '-'
}

function getStatusClass(status) {
  const map = { 0: 'pending', 1: 'success', 2: 'failed' }
  return map[status] || ''
}

async function loadData() {
  loading.value = true
  try {
    await store.fetchReportList(queryParams)
    list.value = store.reportList
  } finally {
    loading.value = false
  }
}

function download(row) {
  ElMessage.info('报告下载功能开发中')
}

function preview(row) {
  ElMessage.info('报告预览功能开发中')
}

async function remove(row) {
  await store.removeReport(row.id)
  ElMessage.success('删除成功')
  loadData()
}

onMounted(() => {
  loadData()
})
</script>
