<template>
  <div class="third-party-data">
    <div class="page-header">
      <h2 class="page-title">第三方数据</h2>
    </div>

    <el-row :gutter="16" class="platform-cards">
      <el-col v-for="p in platforms" :key="p.code" :span="8">
        <div class="platform-card" :class="{ active: p.status }">
          <div class="platform-icon">
            <el-icon :size="40"><Bell /></el-icon>
          </div>
          <div class="platform-name">{{ p.name }}</div>
          <div class="platform-status">
            <el-tag :type="p.status ? 'success' : 'info'" size="small">{{ p.status ? '已连接' : '未配置' }}</el-tag>
          </div>
          <div v-if="p.status" class="platform-quota">
            配额: {{ p.used }}/{{ p.quota }}
          </div>
          <el-button v-if="!p.status" type="primary" size="small" @click="goConfig">去配置</el-button>
          <el-button v-else type="primary" size="small" @click="refreshData(p)">刷新数据</el-button>
        </div>
      </el-col>
    </el-row>

    <div class="card-container" style="margin-top: 20px;">
      <el-form inline>
        <el-form-item label="数据类型">
          <el-select v-model="dataType" style="width: 150px;">
            <el-option label="商品排行榜" value="product" />
            <el-option label="品牌排行榜" value="brand" />
            <el-option label="带货榜单" value="sales" />
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-select v-model="timeRange" style="width: 120px;">
            <el-option label="近7天" value="7d" />
            <el-option label="近30天" value="30d" />
            <el-option label="近90天" value="90d" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">查询</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" stripe style="margin-top: 16px;">
        <el-table-column prop="rank" label="排名" width="80" />
        <el-table-column prop="name" label="商品/品牌" min-width="180" />
        <el-table-column prop="sales" label="销售额(万)" width="120" />
        <el-table-column prop="orders" label="订单数" width="120" />
        <el-table-column prop="conversionRate" label="转化率" width="100" />
        <el-table-column label="关联商家" width="120">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="linkMerchant(row)">关联</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const platforms = ref([
  { code: 'feigua', name: '飞瓜数据', status: false, used: 0, quota: 10000 },
  { code: 'chanmama', name: '蝉妈妈', status: false, used: 0, quota: 10000 },
  { code: 'faxian', name: '发现报告', status: false, used: 0, quota: 1000 }
])

const dataType = ref('product')
const timeRange = ref('30d')
const tableData = ref([])

function goConfig() {
  ElMessage.info('第三方API配置功能开发中')
}

function refreshData(p) {
  ElMessage.success(`${p.name}数据刷新中...`)
}

function loadData() {
  tableData.value = []
  ElMessage.info('数据加载功能开发中')
}

function linkMerchant(row) {
  ElMessage.info('商家关联功能开发中')
}
</script>

<style scoped>
.platform-cards {
  margin-bottom: 16px;
}

.platform-card {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  text-align: center;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  border: 2px solid transparent;
  transition: all 0.3s;
}

.platform-card.active {
  border-color: #0072ef;
}

.platform-icon {
  margin-bottom: 12px;
  color: #909399;
}

.platform-card.active .platform-icon {
  color: #0072ef;
}

.platform-name {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 8px;
}

.platform-status {
  margin-bottom: 8px;
}

.platform-quota {
  font-size: 12px;
  color: #909399;
  margin-bottom: 12px;
}
</style>
