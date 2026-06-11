<template>
  <div class="compare-history">
    <div class="page-header">
      <h2 class="page-title">对比历史</h2>
    </div>
    <div class="card-container">
      <el-table :data="list" stripe v-loading="loading">
        <el-table-column prop="id" label="#" width="60" />
        <el-table-column prop="name" label="对比名称" min-width="200" />
        <el-table-column prop="createdBy" label="创建人" width="120" />
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="viewDetail(row)">查看</el-button>
            <el-button type="success" link size="small" @click="reuse(row)">复用</el-button>
            <el-button type="danger" link size="small" @click="remove(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useComparisonStore } from '@/stores/comparison'

const router = useRouter()
const store = useComparisonStore()
const loading = ref(false)
const list = ref([])

function viewDetail(row) {
  ElMessage.info('查看对比详情功能开发中')
}

function reuse(row) {
  router.push('/analysis/compare-new')
}

async function remove(row) {
  await store.removeComparison(row.id)
  ElMessage.success('删除成功')
  loadData()
}

async function loadData() {
  loading.value = true
  try {
    await store.fetchHistory(1, 50)
    list.value = store.comparisonList
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadData()
})
</script>
