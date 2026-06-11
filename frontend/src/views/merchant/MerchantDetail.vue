<template>
  <div class="merchant-detail">
    <div class="page-header">
      <div class="header-left">
        <el-button @click="$router.push('/merchant/list')">
          <el-icon><ArrowLeft /></el-icon> 返回
        </el-button>
        <h2 class="page-title">{{ merchant.name }}</h2>
        <el-tag v-if="merchant.isListed === 1" type="success" size="small">已上市</el-tag>
        <el-tag v-else type="info" size="small">未上市</el-tag>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="$router.push(`/merchant/edit/${merchant.id}`)">
          <el-icon><Edit /></el-icon> 编辑
        </el-button>
        <el-dropdown @command="handleFetchCommand">
          <el-button type="success">
            <el-icon><Connection /></el-icon> 数据采集 <el-icon><ArrowDown /></el-icon>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="website">官网采集</el-dropdown-item>
              <el-dropdown-item command="financial">财报采集</el-dropdown-item>
              <el-dropdown-item command="review">评价采集</el-dropdown-item>
              <el-dropdown-item command="social">社交账号</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        <el-dropdown @command="handleReportCommand">
          <el-button type="warning">
            <el-icon><Document /></el-icon> 生成报告 <el-icon><ArrowDown /></el-icon>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="full">完整报告</el-dropdown-item>
              <el-dropdown-item command="marketing">营销报告</el-dropdown-item>
              <el-dropdown-item command="financial">财务报告</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <div v-loading="loading">
      <el-tabs v-model="activeTab" class="detail-tabs">
        <el-tab-pane label="基本信息" name="basic">
          <div class="card-container">
            <el-descriptions :column="2" border>
              <el-descriptions-item label="商家名称">{{ merchant.name }}</el-descriptions-item>
              <el-descriptions-item label="官网">
                <el-link v-if="merchant.website" :href="merchant.website" target="_blank" type="primary">
                  {{ merchant.website }}
                </el-link>
                <span v-else>-</span>
              </el-descriptions-item>
              <el-descriptions-item label="所属行业">{{ merchant.industryName }}</el-descriptions-item>
              <el-descriptions-item label="企业规模">{{ merchant.companyScale || '-' }}</el-descriptions-item>
              <el-descriptions-item label="员工人数">{{ merchant.employeeCount ? merchant.employeeCount + '人' : '-' }}</el-descriptions-item>
              <el-descriptions-item label="成立日期">{{ merchant.establishedDate || '-' }}</el-descriptions-item>
              <el-descriptions-item label="总部地址">{{ merchant.headquarters || '-' }}</el-descriptions-item>
              <el-descriptions-item label="数据状态">
                <span :class="['tag-status', getStatusClass(merchant.dataStatus)]">
                  {{ getStatusName(merchant.dataStatus) }}
                </span>
              </el-descriptions-item>
            </el-descriptions>

            <el-divider content-position="left">商家介绍</el-divider>
            <div class="description-text">{{ merchant.businessDescription || '暂无介绍' }}</div>

            <el-divider content-position="left">核心优势</el-divider>
            <div class="description-text">{{ merchant.advantages || '暂无数据' }}</div>

            <el-divider content-position="left">产品信息</el-divider>
            <div class="description-text">{{ merchant.productsInfo || '暂无数据' }}</div>
          </div>
        </el-tab-pane>

        <el-tab-pane label="社交账号" name="social">
          <div class="card-container">
            <el-row :gutter="16">
              <el-col v-for="account in socialAccounts" :key="account.id" :span="12">
                <div class="account-card">
                  <div class="account-header">
                    <el-icon size="24"><Bell /></el-icon>
                    <span class="platform-name">{{ getPlatformName(account.platform) }}</span>
                    <el-tag v-if="account.verificationStatus === 1" type="success" size="small">已认证</el-tag>
                  </div>
                  <div class="account-info">
                    <div class="account-name">{{ account.accountName }}</div>
                    <div class="account-stats">
                      <span>粉丝: {{ formatNumber(account.followersCount) }}</span>
                      <span>帖子: {{ account.postsCount }}</span>
                    </div>
                  </div>
                </div>
              </el-col>
            </el-row>
            <el-empty v-if="!socialAccounts.length" description="暂无社交账号数据" />
          </div>
        </el-tab-pane>

        <el-tab-pane label="产品信息" name="product">
          <div class="card-container">
            <el-table :data="products" stripe>
              <el-table-column prop="productName" label="产品名称" />
              <el-table-column prop="category" label="类别" width="120" />
              <el-table-column prop="price" label="价格" width="100">
                <template #default="{ row }">¥{{ row.price }}</template>
              </el-table-column>
              <el-table-column prop="salesCount" label="销量" width="120">
                <template #default="{ row }">{{ formatNumber(row.salesCount) }}</template>
              </el-table-column>
              <el-table-column prop="rating" label="评分" width="80">
                <template #default="{ row }">{{ row.rating || '-' }}</template>
              </el-table-column>
            </el-table>
            <el-empty v-if="!products.length" description="暂无产品数据" />
          </div>
        </el-tab-pane>

        <el-tab-pane label="财务数据" name="financial">
          <div class="card-container">
            <el-table :data="financials" stripe>
              <el-table-column prop="reportYear" label="年份" width="80" />
              <el-table-column prop="reportPeriod" label="报告期" width="100" />
              <el-table-column prop="revenue" label="营业收入(万)" width="140">
                <template #default="{ row }">{{ row.revenue?.toLocaleString() }}</template>
              </el-table-column>
              <el-table-column prop="netProfit" label="净利润(万)" width="120">
                <template #default="{ row }">{{ row.netProfit?.toLocaleString() }}</template>
              </el-table-column>
              <el-table-column prop="grossMargin" label="毛利率(%)" width="100" />
              <el-table-column prop="roe" label="ROE(%)" width="100" />
              <el-table-column prop="source" label="数据来源" />
            </el-table>
            <el-empty v-if="!financials.length" description="暂无财务数据" />
          </div>
        </el-tab-pane>

        <el-tab-pane label="经营数据" name="operating">
          <div class="card-container">
            <el-row :gutter="16" class="operating-stats">
              <el-col :span="4"><div class="stat-item"><div class="stat-val">{{ summaryData.gmv }}</div><div class="stat-label">GMV(万)</div></div></el-col>
              <el-col :span="4"><div class="stat-item"><div class="stat-val">{{ summaryData.sales }}</div><div class="stat-label">销售额(万)</div></div></el-col>
              <el-col :span="4"><div class="stat-item"><div class="stat-val">{{ summaryData.orders }}</div><div class="stat-label">订单数</div></div></el-col>
              <el-col :span="4"><div class="stat-item"><div class="stat-val">{{ summaryData.avgPrice }}</div><div class="stat-label">客单价(元)</div></div></el-col>
              <el-col :span="4"><div class="stat-item"><div class="stat-val">{{ summaryData.conversion }}</div><div class="stat-label">转化率(%)</div></div></el-col>
              <el-col :span="4"><div class="stat-item"><div class="stat-val">{{ summaryData.roi }}</div><div class="stat-label">ROI</div></div></el-col>
            </el-row>
          </div>
        </el-tab-pane>

        <el-tab-pane label="产品评价" name="review">
          <div class="card-container">
            <div class="review-summary">
              <el-tag type="success" size="large">正面 {{ reviewStats.positive }}%</el-tag>
              <el-tag type="info" size="large">中性 {{ reviewStats.neutral }}%</el-tag>
              <el-tag type="danger" size="large">负面 {{ reviewStats.negative }}%</el-tag>
            </div>
            <el-table :data="reviews" stripe>
              <el-table-column prop="platform" label="平台" width="100">
                <template #default="{ row }">{{ getPlatformName(row.platform) }}</template>
              </el-table-column>
              <el-table-column prop="rating" label="评分" width="80">
                <template #default="{ row }">
                  <el-rate v-model="row.rating" disabled text-color="#ff9900" />
                </template>
              </el-table-column>
              <el-table-column prop="content" label="评价内容" min-width="300" show-overflow-tooltip />
              <el-table-column prop="sentiment" label="情感" width="80">
                <template #default="{ row }">
                  <el-tag size="small" :type="getSentimentType(row.sentiment)">{{ row.sentiment }}</el-tag>
                </template>
              </el-table-column>
            </el-table>
            <el-empty v-if="!reviews.length" description="暂无评价数据" />
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useMerchantStore } from '@/stores/merchant'
import { getSocialAccounts } from '@/api/socialAccount'

const route = useRoute()
const router = useRouter()
const merchantStore = useMerchantStore()

const loading = ref(false)
const activeTab = ref('basic')
const merchant = ref({})
const socialAccounts = ref([])
const products = ref([])
const financials = ref([])
const reviews = ref([])

const summaryData = ref({ gmv: '-', sales: '-', orders: '-', avgPrice: '-', conversion: '-', roi: '-' })
const reviewStats = ref({ positive: 0, neutral: 0, negative: 0 })

function getStatusName(status) {
  const map = { 0: '待采集', 1: '采集中', 2: '已完成', 3: '失败' }
  return map[status] ?? '-'
}

function getStatusClass(status) {
  const map = { 0: 'pending', 1: 'running', 2: 'success', 3: 'failed' }
  return map[status] ?? ''
}

function getPlatformName(code) {
  const map = { douyin: '抖音', xiaohongshu: '小红书', weibo: '微博', weixin: '微信', taobao: '淘宝', jd: '京东' }
  return map[code] || code
}

function getSentimentType(sentiment) {
  const map = { positive: 'success', neutral: 'info', negative: 'danger' }
  return map[sentiment] || 'info'
}

function formatNumber(num) {
  if (!num) return '-'
  if (num >= 10000) return (num / 10000).toFixed(1) + 'w'
  return num.toLocaleString()
}

function handleFetchCommand(command) {
  ElMessage.success(`已创建${command}采集任务`)
}

function handleReportCommand(command) {
  router.push({ path: '/report/generate', query: { merchantId: merchant.value.id, type: command } })
}

async function loadData() {
  loading.value = true
  try {
    await merchantStore.fetchMerchantDetail(route.params.id)
    merchant.value = merchantStore.currentMerchant || {}
    socialAccounts.value = merchant.value.socialAccounts || []
    products.value = merchant.value.products || []
    financials.value = merchant.value.financials || []
    reviews.value = merchant.value.reviews || []
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.merchant-detail {
  height: 100%;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.header-actions {
  display: flex;
  gap: 8px;
}

.detail-tabs {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
}

.account-card {
  background: #f9f9f9;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;
}

.account-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
}

.platform-name {
  font-weight: 600;
  flex: 1;
}

.account-name {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 8px;
}

.account-stats {
  display: flex;
  gap: 16px;
  color: #909399;
  font-size: 13px;
}

.description-text {
  line-height: 1.8;
  color: #606266;
  white-space: pre-wrap;
}

.operating-stats {
  margin-top: 16px;
}

.stat-item {
  text-align: center;
  padding: 16px;
  background: #f9f9f9;
  border-radius: 8px;
}

.stat-val {
  font-size: 20px;
  font-weight: 700;
  color: #0072ef;
}

.stat-label {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}

.review-summary {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
}
</style>
