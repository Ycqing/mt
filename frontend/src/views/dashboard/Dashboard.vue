<template>
  <div class="dashboard">
    <el-row :gutter="16" class="stat-row">
      <el-col :span="6">
        <div class="stat-card" @click="goMerchantList">
          <div class="stat-value">{{ stats.total }}</div>
          <div class="stat-label">商家总数</div>
          <div class="stat-trend up">
            <el-icon><TrendCharts /></el-icon> +12 本月
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-value completed">{{ stats.completed }}</div>
          <div class="stat-label">已采集</div>
          <div class="stat-trend up">
            <el-icon><TrendCharts /></el-icon> +8 本月
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-value fetching">{{ stats.fetching }}</div>
          <div class="stat-label">采集中</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-value pending">{{ stats.pending }}</div>
          <div class="stat-label">待采集</div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="chart-row">
      <el-col :span="16">
        <div class="card-container">
          <div class="card-title">近30天采集任务趋势</div>
          <div ref="taskChartRef" style="height: 300px;"></div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="card-container">
          <div class="card-title">商家行业分布</div>
          <div ref="industryChartRef" style="height: 300px;"></div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="chart-row">
      <el-col :span="24">
        <div class="card-container">
          <div class="card-title">最近采集任务</div>
          <el-table :data="recentTasks" stripe style="width: 100%">
            <el-table-column prop="taskName" label="任务名称" min-width="200" />
            <el-table-column prop="taskType" label="类型" width="100">
              <template #default="{ row }">
                <el-tag size="small">{{ getTaskTypeName(row.taskType) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="taskStatus" label="状态" width="100">
              <template #default="{ row }">
                <span :class="['tag-status', row.taskStatus]">
                  {{ getStatusName(row.taskStatus) }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="时间" width="160" />
            <el-table-column prop="costMs" label="耗时" width="100">
              <template #default="{ row }">
                {{ row.costMs ? (row.costMs / 1000).toFixed(1) + 's' : '-' }}
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="action-row">
      <el-col :span="24">
        <div class="quick-actions">
          <span class="quick-title">快捷入口</span>
          <el-button type="primary" @click="$router.push('/merchant/add')">
            <el-icon><Plus /></el-icon> 添加商家
          </el-button>
          <el-button type="success" @click="$router.push('/analysis/compare-new')">
            <el-icon><DataAnalysis /></el-icon> 新建对比
          </el-button>
          <el-button type="warning" @click="$router.push('/report/generate')">
            <el-icon><Document /></el-icon> 生成报告
          </el-button>
          <el-button @click="$router.push('/fetch/task')">
            <el-icon><Connection /></el-icon> 数据采集
          </el-button>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import * as echarts from 'echarts'

const router = useRouter()
const taskChartRef = ref(null)
const industryChartRef = ref(null)
let taskChart = null
let industryChart = null

const stats = ref({
  total: 128,
  completed: 98,
  fetching: 5,
  pending: 25
})

const recentTasks = ref([
  { taskName: '完美日记官网采集', taskType: 'website', taskStatus: 'success', createTime: '2026-06-11 10:00', costMs: 2500 },
  { taskName: '完美日记财报采集', taskType: 'financial', taskStatus: 'running', createTime: '2026-06-11 09:58', costMs: null },
  { taskName: '花西子产品评价', taskType: 'review', taskStatus: 'success', createTime: '2026-06-11 09:55', costMs: 5200 },
  { taskName: '珀莱雅社交账号', taskType: 'social', taskStatus: 'failed', createTime: '2026-06-11 09:50', costMs: 300 },
  { taskName: '薇诺娜官网采集', taskType: 'website', taskStatus: 'pending', createTime: '2026-06-11 09:45', costMs: null }
])

function getTaskTypeName(type) {
  const map = { website: '官网', financial: '财报', review: '评价', social: '社交', product: '产品', operating: '经营' }
  return map[type] || type
}

function getStatusName(status) {
  const map = { pending: '待执行', running: '进行中', success: '成功', failed: '失败', paused: '暂停' }
  return map[status] || status
}

function goMerchantList() {
  router.push('/merchant/list')
}

function initTaskChart() {
  if (!taskChartRef.value) return
  taskChart = echarts.init(taskChartRef.value)
  const option = {
    tooltip: { trigger: 'axis' },
    legend: { data: ['成功', '失败', '总数'] },
    xAxis: {
      type: 'category',
      data: ['06-05', '06-06', '06-07', '06-08', '06-09', '06-10', '06-11']
    },
    yAxis: { type: 'value' },
    series: [
      { name: '成功', type: 'line', smooth: true, data: [12, 15, 18, 14, 20, 22, 25], itemStyle: { color: '#67c23a' } },
      { name: '失败', type: 'line', smooth: true, data: [2, 1, 3, 2, 1, 2, 1], itemStyle: { color: '#f56c6c' } },
      { name: '总数', type: 'line', smooth: true, data: [14, 16, 21, 16, 21, 24, 26], itemStyle: { color: '#0072ef' } }
    ]
  }
  taskChart.setOption(option)
}

function initIndustryChart() {
  if (!industryChartRef.value) return
  industryChart = echarts.init(industryChartRef.value)
  const option = {
    tooltip: { trigger: 'item' },
    series: [{
      type: 'pie',
      radius: ['40%', '70%'],
      data: [
        { value: 35, name: '美妆护肤' },
        { value: 20, name: '服装鞋包' },
        { value: 15, name: '食品饮料' },
        { value: 12, name: '数码家电' },
        { value: 10, name: '家居生活' },
        { value: 8, name: '其他' }
      ]
    }]
  }
  industryChart.setOption(option)
}

onMounted(() => {
  initTaskChart()
  initIndustryChart()
  window.addEventListener('resize', () => {
    taskChart?.resize()
    industryChart?.resize()
  })
})

onUnmounted(() => {
  taskChart?.dispose()
  industryChart?.dispose()
})
</script>

<style scoped>
.stat-row, .chart-row, .action-row {
  margin-bottom: 16px;
}

.stat-card {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #303133;
  margin-bottom: 8px;
}

.stat-value.completed { color: #67c23a; }
.stat-value.fetching { color: #409eff; }
.stat-value.pending { color: #e6a23c; }

.stat-label {
  font-size: 14px;
  color: #909399;
}

.stat-trend {
  font-size: 12px;
  margin-top: 8px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.stat-trend.up { color: #67c23a; }
.stat-trend.down { color: #f56c6c; }

.card-container {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 16px;
}

.quick-actions {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  display: flex;
  align-items: center;
  gap: 12px;
}

.quick-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-right: 12px;
}
</style>
