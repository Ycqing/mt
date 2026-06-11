<template>
  <div class="fetch-task">
    <div class="page-header">
      <h2 class="page-title">采集任务</h2>
      <el-button type="primary" @click="dialogVisible = true">
        <el-icon><Plus /></el-icon> 新建任务
      </el-button>
    </div>

    <div class="filter-bar">
      <div class="filter-item">
        <span class="label">任务类型</span>
        <el-select v-model="queryParams.taskType" placeholder="全部" style="width: 120px;" clearable>
          <el-option label="官网采集" value="website" />
          <el-option label="财报采集" value="financial" />
          <el-option label="评价采集" value="review" />
          <el-option label="社交账号" value="social" />
          <el-option label="产品采集" value="product" />
        </el-select>
      </div>
      <div class="filter-item">
        <span class="label">任务状态</span>
        <el-select v-model="queryParams.taskStatus" placeholder="全部" style="width: 120px;" clearable>
          <el-option label="待执行" value="pending" />
          <el-option label="执行中" value="running" />
          <el-option label="成功" value="success" />
          <el-option label="失败" value="failed" />
        </el-select>
      </div>
      <el-button type="primary" @click="loadData">
        <el-icon><Search /></el-icon> 搜索
      </el-button>
    </div>

    <div class="table-container">
      <div style="margin-bottom: 12px;">
        <el-button @click="batchStart" :disabled="selectedIds.length === 0">
          <el-icon><VideoPlay /></el-icon> 批量执行
        </el-button>
        <el-button @click="batchPause" :disabled="selectedIds.length === 0">
          <el-icon><VideoPause /></el-icon> 批量暂停
        </el-button>
        <el-button type="danger" @click="batchDelete" :disabled="selectedIds.length === 0">
          <el-icon><Delete /></el-icon> 批量删除
        </el-button>
      </div>

      <el-table :data="tableData" stripe v-loading="loading" @selection-change="handleSelection">
        <el-table-column type="selection" width="50" />
        <el-table-column prop="id" label="#" width="60" />
        <el-table-column prop="taskName" label="任务名称" min-width="200" />
        <el-table-column prop="taskType" label="类型" width="100">
          <template #default="{ row }">
            <el-tag size="small">{{ getTypeName(row.taskType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="taskStatus" label="状态" width="100">
          <template #default="{ row }">
            <span :class="['tag-status', row.taskStatus]">{{ getStatusName(row.taskStatus) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="80">
          <template #default="{ row }">
            <el-tag size="small" :type="getPriorityType(row.priority)">{{ row.priority }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column prop="costMs" label="耗时" width="100">
          <template #default="{ row }">
            {{ row.costMs ? (row.costMs / 1000).toFixed(1) + 's' : '-' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button v-if="row.taskStatus === 'pending' || row.taskStatus === 'paused'" type="primary" link size="small" @click="startTask(row)">执行</el-button>
            <el-button v-if="row.taskStatus === 'running'" type="warning" link size="small" @click="pauseTask(row)">暂停</el-button>
            <el-button type="primary" link size="small" @click="viewLog(row)">日志</el-button>
            <el-button type="danger" link size="small" @click="deleteTask(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="queryParams.pageNum"
          v-model:page-size="queryParams.pageSize"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          @change="loadData"
        />
      </div>
    </div>

    <el-dialog v-model="dialogVisible" title="新建采集任务" width="500px">
      <el-form ref="formRef" :model="form" label-width="100px">
        <el-form-item label="任务名称" prop="taskName" :rules="[{ required: true, message: '请输入任务名称', trigger: 'blur' }]">
          <el-input v-model="form.taskName" placeholder="请输入任务名称" />
        </el-form-item>
        <el-form-item label="商家选择" prop="merchantId" :rules="[{ required: true, message: '请选择商家', trigger: 'change' }]">
          <el-select v-model="form.merchantId" placeholder="请选择商家" style="width: 100%;">
            <el-option v-for="m in merchantOptions" :key="m.id" :label="m.name" :value="m.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="采集类型" prop="taskType" :rules="[{ required: true, message: '请选择采集类型', trigger: 'change' }]">
          <el-radio-group v-model="form.taskType">
            <el-radio label="website">官网采集</el-radio>
            <el-radio label="financial">财报采集</el-radio>
            <el-radio label="review">评价采集</el-radio>
            <el-radio label="social">社交账号</el-radio>
            <el-radio label="product">产品采集</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="采集源URL">
          <el-input v-model="form.sourceUrl" placeholder="可输入URL或关键词" />
        </el-form-item>
        <el-form-item label="优先级">
          <el-rate v-model="form.priority" :max="5" show-text :texts="['紧急', '高', '较高', '普通', '低']" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确认创建</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useFetchTaskStore } from '@/stores/fetchTask'
import { getAllMerchants } from '@/api/merchant'
import { startFetchTask, pauseFetchTask, deleteFetchTask, batchStartFetchTasks } from '@/api/fetchTask'

const store = useFetchTaskStore()
const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const selectedIds = ref([])
const dialogVisible = ref(false)
const merchantOptions = ref([])
const formRef = ref(null)

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  taskType: '',
  taskStatus: ''
})

const form = reactive({
  taskName: '',
  merchantId: null,
  taskType: 'website',
  sourceUrl: '',
  priority: 3
})

function getTypeName(type) {
  const map = { website: '官网', financial: '财报', review: '评价', social: '社交', product: '产品' }
  return map[type] || type
}

function getStatusName(status) {
  const map = { pending: '待执行', running: '进行中', success: '成功', failed: '失败', paused: '暂停' }
  return map[status] || status
}

function getPriorityType(p) {
  if (p >= 4) return 'danger'
  if (p >= 3) return 'warning'
  return 'info'
}

async function loadData() {
  loading.value = true
  try {
    await store.fetchTaskList(queryParams)
    tableData.value = store.taskList
    total.value = store.total
  } finally {
    loading.value = false
  }
}

function handleSelection(val) {
  selectedIds.value = val.map(v => v.id)
}

async function startTask(row) {
  await startFetchTask(row.id)
  ElMessage.success('任务已开始')
  loadData()
}

async function pauseTask(row) {
  await pauseFetchTask(row.id)
  ElMessage.success('任务已暂停')
  loadData()
}

async function deleteTask(row) {
  try {
    await ElMessageBox.confirm(`确定删除任务"${row.taskName}"吗？`, '提示', { type: 'warning' })
    await deleteFetchTask(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch {}
}

async function batchStart() {
  await batchStartFetchTasks(selectedIds.value)
  ElMessage.success('任务已开始')
  loadData()
}

async function batchPause() {
  ElMessage.info('批量暂停功能开发中')
}

async function batchDelete() {
  ElMessage.info('批量删除功能开发中')
}

function viewLog(row) {
  ElMessage.info('日志查看功能开发中')
}

async function submitForm() {
  await store.createTask(form)
  ElMessage.success('任务创建成功')
  dialogVisible.value = false
  Object.assign(form, { taskName: '', merchantId: null, taskType: 'website', sourceUrl: '', priority: 3 })
  loadData()
}

onMounted(async () => {
  loadData()
  const res = await getAllMerchants()
  merchantOptions.value = res || []
})
</script>
