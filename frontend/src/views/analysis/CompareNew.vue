<template>
  <div class="compare-new">
    <div class="page-header">
      <h2 class="page-title">新建对比</h2>
    </div>

    <div class="card-container">
      <el-steps :active="stepActive" finish-status="success" align-center>
        <el-step title="选择商家" />
        <el-step title="选择维度" />
        <el-step title="生成报告" />
      </el-steps>

      <!-- Step 1: 选择商家 -->
      <div v-show="stepActive === 0" class="step-content">
        <el-alert title="选择2-5个商家进行对比" type="info" :closable="false" style="margin-bottom: 20px;" />
        <el-row :gutter="16">
          <el-col v-for="m in merchants" :key="m.id" :span="6">
            <div class="merchant-card" :class="{ selected: selectedIds.includes(m.id) }" @click="toggleMerchant(m.id)">
              <div class="merchant-name">{{ m.name }}</div>
              <div class="merchant-industry">{{ m.industryName || '-' }}</div>
              <div class="merchant-check">
                <el-icon v-if="selectedIds.includes(m.id)"><Check /></el-icon>
              </div>
            </div>
          </el-col>
        </el-row>
        <div class="selected-tip">已选择: {{ selectedIds.length }}/5</div>
      </div>

      <!-- Step 2: 选择维度 -->
      <div v-show="stepActive === 1" class="step-content">
        <el-form label-width="120px">
          <el-form-item label="对比维度">
            <el-checkbox-group v-model="dimensions">
              <el-checkbox label="basic">企业基础</el-checkbox>
              <el-checkbox label="scale">企业规模</el-checkbox>
              <el-checkbox label="social">社交账号</el-checkbox>
              <el-checkbox label="financial">财务数据</el-checkbox>
              <el-checkbox label="operating">经营数据</el-checkbox>
              <el-checkbox label="review">产品评价</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-divider content-position="left">详细维度说明</el-divider>
          <div class="dimension-desc">
            <div><b>企业基础:</b> 商家名称、所属行业、成立时间</div>
            <div><b>企业规模:</b> 注册资本、员工人数、企业估值</div>
            <div><b>社交账号:</b> 抖音粉丝、小红书粉丝、微信粉丝</div>
            <div><b>财务数据:</b> 营业收入、净利润、毛利率、ROE</div>
            <div><b>经营数据:</b> GMV、销售额、客单价、转化率、ROI</div>
            <div><b>产品评价:</b> 总评价数、好评率、差评率</div>
          </div>
        </el-form>
      </div>

      <!-- Step 3: 生成报告 -->
      <div v-show="stepActive === 2" class="step-content">
        <el-row :gutter="20">
          <el-col :span="12">
            <div ref="radarChartRef" style="height: 300px;"></div>
          </el-col>
          <el-col :span="12">
            <div class="compare-table">
              <el-table :data="compareData" stripe size="small">
                <el-table-column prop="dimension" label="指标" />
                <el-table-column v-for="id in selectedIds" :key="id" :label="getMerchantName(id)" align="center" />
              </el-table>
            </div>
          </el-col>
        </el-row>
        <el-divider content-position="left">对比结论</el-divider>
        <div class="conclusion">
          <p v-for="(item, i) in conclusions" :key="i">{{ i + 1 }}. {{ item }}</p>
        </div>
        <div class="action-bar" style="margin-top: 20px;">
          <el-button type="primary" @click="saveCompare">
            <el-icon><Save /></el-icon> 保存对比
          </el-button>
          <el-button type="success" @click="exportReport">
            <el-icon><Download /></el-icon> 导出报告
          </el-button>
        </div>
      </div>

      <div class="step-footer">
        <el-button v-if="stepActive > 0" @click="stepActive--">上一步</el-button>
        <el-button v-if="stepActive < 2" type="primary" :disabled="stepActive === 0 && selectedIds.length < 2" @click="nextStep">
          下一步
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'
import { getAllMerchants } from '@/api/merchant'

const stepActive = ref(0)
const merchants = ref([])
const selectedIds = ref([])
const dimensions = ref(['basic', 'social', 'operating'])
const compareData = ref([])
const conclusions = ref(['完美日记在社交媒体运营方面表现最优', '珀莱雅在营收规模上略胜完美日记', '各商家需根据自身短板优化营销策略'])
let radarChart = null
const radarChartRef = ref(null)

function toggleMerchant(id) {
  const idx = selectedIds.value.indexOf(id)
  if (idx >= 0) {
    selectedIds.value.splice(idx, 1)
  } else if (selectedIds.value.length < 5) {
    selectedIds.value.push(id)
  } else {
    ElMessage.warning('最多选择5个商家')
  }
}

function getMerchantName(id) {
  const m = merchants.value.find(m => m.id === id)
  return m ? m.name : '-'
}

function nextStep() {
  if (stepActive.value === 0 && selectedIds.value.length < 2) {
    ElMessage.warning('请至少选择2个商家')
    return
  }
  stepActive.value++
  if (stepActive.value === 2) {
    setTimeout(initRadarChart, 100)
  }
}

function initRadarChart() {
  if (!radarChartRef.value) return
  radarChart = echarts.init(radarChartRef.value)
  const option = {
    tooltip: {},
    legend: { data: ['完美日记', '花西子', '珀莱雅'] },
    radar: {
      indicator: [
        { name: '社交媒体', max: 100 },
        { name: '营收规模', max: 100 },
        { name: 'GMV', max: 100 },
        { name: 'ROI', max: 100 },
        { name: '好评率', max: 100 }
      ]
    },
    series: [{
      type: 'radar',
      data: [
        { value: [95, 80, 92, 88, 92], name: '完美日记' },
        { value: [70, 72, 75, 65, 85], name: '花西子' },
        { value: [75, 85, 80, 78, 88], name: '珀莱雅' }
      ]
    }]
  }
  radarChart.setOption(option)
}

function saveCompare() {
  ElMessage.success('对比已保存')
}

function exportReport() {
  ElMessage.info('导出功能开发中')
}

onMounted(async () => {
  const res = await getAllMerchants()
  merchants.value = res || []
})

onUnmounted(() => {
  radarChart?.dispose()
})
</script>

<style scoped>
.step-content {
  min-height: 400px;
  padding: 20px 0;
}

.merchant-card {
  background: #f9f9f9;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;
  cursor: pointer;
  border: 2px solid transparent;
  position: relative;
  transition: all 0.2s;
}

.merchant-card:hover {
  border-color: #0072ef;
}

.merchant-card.selected {
  border-color: #0072ef;
  background: #ecf5ff;
}

.merchant-name {
  font-weight: 600;
  margin-bottom: 4px;
}

.merchant-industry {
  font-size: 12px;
  color: #909399;
}

.merchant-check {
  position: absolute;
  top: 8px;
  right: 8px;
  width: 20px;
  height: 20px;
  background: #0072ef;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.merchant-card:not(.selected) .merchant-check {
  background: transparent;
  border: 2px solid #dcdfe6;
}

.merchant-card:not(.selected) .merchant-check .el-icon {
  display: none;
}

.selected-tip {
  text-align: center;
  color: #909399;
  margin-top: 16px;
}

.dimension-desc {
  background: #f9f9f9;
  padding: 16px;
  border-radius: 8px;
  line-height: 2;
  color: #606266;
}

.step-footer {
  display: flex;
  justify-content: center;
  gap: 12px;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}
</style>
