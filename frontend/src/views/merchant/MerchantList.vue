<template>
  <div class="merchant-list">
    <div class="page-header">
      <h2 class="page-title">商家列表</h2>
      <div>
        <el-button type="primary" @click="$router.push('/merchant/add')">
          <el-icon><Plus /></el-icon> 添加商家
        </el-button>
        <el-button @click="handleExport">
          <el-icon><Download /></el-icon> 导出
        </el-button>
      </div>
    </div>

    <div class="filter-bar">
      <div class="filter-item">
        <span class="label">商家名称</span>
        <el-input v-model="queryParams.name" placeholder="请输入商家名称" style="width: 180px;" clearable @keyup.enter="handleSearch" />
      </div>
      <div class="filter-item">
        <span class="label">行业</span>
        <el-select v-model="queryParams.industryId" placeholder="请选择行业" style="width: 150px;" clearable>
          <el-option v-for="cat in industryList" :key="cat.id" :label="cat.name" :value="cat.id" />
        </el-select>
      </div>
      <div class="filter-item">
        <span class="label">上市状态</span>
        <el-select v-model="queryParams.isListed" placeholder="请选择" style="width: 120px;" clearable>
          <el-option label="已上市" :value="1" />
          <el-option label="未上市" :value="0" />
        </el-select>
      </div>
      <div class="filter-item">
        <span class="label">数据状态</span>
        <el-select v-model="queryParams.dataStatus" placeholder="请选择" style="width: 120px;" clearable>
          <el-option label="待采集" :value="0" />
          <el-option label="采集中" :value="1" />
          <el-option label="已完成" :value="2" />
          <el-option label="失败" :value="3" />
        </el-select>
      </div>
      <el-button type="primary" @click="handleSearch">
        <el-icon><Search /></el-icon> 搜索
      </el-button>
      <el-button @click="handleReset">
        <el-icon><Refresh /></el-icon> 重置
      </el-button>
    </div>

    <div class="table-container">
      <el-table :data="tableData" stripe v-loading="loading" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" />
        <el-table-column prop="id" label="#" width="60" />
        <el-table-column prop="name" label="商家名称" min-width="160">
          <template #default="{ row }">
            <el-link type="primary" @click="$router.push(`/merchant/detail/${row.id}`)">{{ row.name }}</el-link>
          </template>
        </el-table-column>
        <el-table-column prop="website" label="官网" min-width="180">
          <template #default="{ row }">
            <el-link v-if="row.website" :href="row.website" target="_blank" type="primary">
              {{ row.website }}
            </el-link>
            <span v-else class="text-muted">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="industryName" label="行业" width="100" />
        <el-table-column prop="isListed" label="上市" width="80">
          <template #default="{ row }">
            <el-tag v-if="row.isListed === 1" type="success" size="small">是</el-tag>
            <el-tag v-else type="info" size="small">否</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="dataStatus" label="数据状态" width="100">
          <template #default="{ row }">
            <span :class="['tag-status', getStatusClass(row.dataStatus)]">
              {{ getStatusName(row.dataStatus) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="$router.push(`/merchant/detail/${row.id}`)">查看</el-button>
            <el-button type="primary" link size="small" @click="handleFetch(row)">采集</el-button>
            <el-button type="primary" link size="small" @click="$router.push(`/merchant/edit/${row.id}`)">编辑</el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="queryParams.pageNum"
          v-model:page-size="queryParams.pageSize"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadData"
          @current-change="loadData"
        />
      </div>
    </div>

    <el-dialog v-model="fetchDialogVisible" title="数据采集" width="500px">
      <el-form label-width="100px">
        <el-form-item label="商家名称">
          <el-input :value="currentMerchant?.name" disabled />
        </el-form-item>
        <el-form-item label="采集类型">
          <el-checkbox-group v-model="fetchTypes">
            <el-checkbox label="website">官网数据</el-checkbox>
            <el-checkbox label="financial">财报数据</el-checkbox>
            <el-checkbox label="review">产品评价</el-checkbox>
            <el-checkbox label="social">社交账号</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="fetchDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmFetch">确认采集</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useMerchantStore } from '@/stores/merchant'
import { createFetchTask } from '@/api/fetchTask'

const router = useRouter()
const merchantStore = useMerchantStore()

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: '',
  industryId: null,
  isListed: null,
  dataStatus: null
})

const industryList = ref([
  { id: 1, name: '美妆护肤' },
  { id: 2, name: '服装鞋包' },
  { id: 3, name: '食品饮料' },
  { id: 4, name: '数码家电' },
  { id: 5, name: '家居生活' }
])

const fetchDialogVisible = ref(false)
const currentMerchant = ref(null)
const fetchTypes = ref(['website'])

function getStatusName(status) {
  const map = { 0: '待采集', 1: '采集中', 2: '已完成', 3: '失败' }
  return map[status] ?? '-'
}

function getStatusClass(status) {
  const map = { 0: 'pending', 1: 'running', 2: 'success', 3: 'failed' }
  return map[status] ?? ''
}

async function loadData() {
  loading.value = true
  try {
    await merchantStore.fetchMerchantList(queryParams)
    tableData.value = merchantStore.merchantList
    total.value = merchantStore.total
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  queryParams.pageNum = 1
  loadData()
}

function handleReset() {
  Object.assign(queryParams, {
    pageNum: 1,
    pageSize: 10,
    name: '',
    industryId: null,
    isListed: null,
    dataStatus: null
  })
  loadData()
}

function handleSelectionChange() {}

function handleFetch(row) {
  currentMerchant.value = row
  fetchDialogVisible.value = true
}

async function confirmFetch() {
  for (const type of fetchTypes.value) {
    await createFetchTask({
      taskName: `${currentMerchant.value.name} - ${type}`,
      merchantId: currentMerchant.value.id,
      taskType: type
    })
  }
  ElMessage.success('采集任务已创建')
  fetchDialogVisible.value = false
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(`确定删除商家"${row.name}"吗？`, '提示', { type: 'warning' })
    await merchantStore.removeMerchant(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch {}
}

function handleExport() {
  ElMessage.info('导出功能开发中')
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.merchant-list {
  height: 100%;
}

.text-muted {
  color: #909399;
}
</style>
