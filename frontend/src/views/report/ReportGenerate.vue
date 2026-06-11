<template>
  <div class="report-generate">
    <div class="page-header">
      <h2 class="page-title">生成报告</h2>
    </div>

    <div class="card-container">
      <el-steps :active="stepActive" finish-status="success" align-center>
        <el-step title="选择商家" />
        <el-step title="配置内容" />
        <el-step title="生成报告" />
      </el-steps>

      <!-- Step 1 -->
      <div v-show="stepActive === 0" class="step-content">
        <el-form label-width="120px" style="max-width: 600px;">
          <el-form-item label="选择商家">
            <el-select v-model="form.merchantId" placeholder="请选择商家" style="width: 100%;" filterable>
              <el-option v-for="m in merchants" :key="m.id" :label="m.name" :value="m.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="报告类型">
            <el-radio-group v-model="form.reportType">
              <el-radio label="full">完整报告</el-radio>
              <el-radio label="marketing">营销报告</el-radio>
              <el-radio label="financial">财务报告</el-radio>
              <el-radio label="comparison">对比报告</el-radio>
              <el-radio label="product">产品报告</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
      </div>

      <!-- Step 2 -->
      <div v-show="stepActive === 1" class="step-content">
        <el-form label-width="120px" style="max-width: 600px;">
          <el-form-item label="报告名称">
            <el-input v-model="form.reportName" placeholder="请输入报告名称" />
          </el-form-item>
          <el-form-item label="报告周期">
            <el-date-picker v-model="form.reportPeriod" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="YYYY-MM" style="width: 100%;" />
          </el-form-item>
          <el-form-item label="包含模块">
            <el-checkbox-group v-model="form.modules">
              <el-checkbox label="summary">执行摘要</el-checkbox>
              <el-checkbox label="basic">基础信息</el-checkbox>
              <el-checkbox label="social">社交媒体分析</el-checkbox>
              <el-checkbox label="operating">经营数据分析</el-checkbox>
              <el-checkbox label="marketing">营销效果评估</el-checkbox>
              <el-checkbox label="comparison">竞品对比</el-checkbox>
              <el-checkbox label="review">产品评价分析</el-checkbox>
              <el-checkbox label="financial">财务数据</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="输出格式">
            <el-radio-group v-model="form.outputFormat">
              <el-radio label="pdf">PDF</el-radio>
              <el-radio label="word">Word</el-radio>
              <el-radio label="excel">Excel</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
      </div>

      <!-- Step 3 -->
      <div v-show="stepActive === 2" class="step-content">
        <div v-if="generating" class="generating">
          <el-progress :percentage="progress" :stroke-width="20" />
          <p>{{ currentStep }}</p>
        </div>
        <div v-else class="result">
          <el-result
            :icon="reportStatus === 1 ? 'success' : 'error'"
            :title="reportStatus === 1 ? '报告生成成功' : '报告生成失败'"
          >
            <template #extra>
              <el-button v-if="reportStatus === 1" type="primary" @click="downloadReport">
                <el-icon><Download /></el-icon> 下载报告
              </el-button>
              <el-button @click="previewReport">
                <el-icon><View /></el-icon> 预览报告
              </el-button>
            </template>
          </el-result>
        </div>
      </div>

      <div class="step-footer">
        <el-button v-if="stepActive > 0 && stepActive < 2" @click="stepActive--">上一步</el-button>
        <el-button v-if="stepActive < 1" type="primary" :disabled="!form.merchantId" @click="stepActive++">
          下一步
        </el-button>
        <el-button v-if="stepActive === 1" type="primary" :loading="generating" @click="startGenerate">
          开始生成
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getAllMerchants } from '@/api/merchant'
import { createReport } from '@/api/report'

const route = useRoute()
const stepActive = ref(0)
const merchants = ref([])
const generating = ref(false)
const progress = ref(0)
const currentStep = ref('')
const reportStatus = ref(0)

const form = reactive({
  merchantId: null,
  reportType: 'marketing',
  reportName: '',
  reportPeriod: '',
  modules: ['summary', 'basic', 'social', 'operating'],
  outputFormat: 'pdf'
})

async function startGenerate() {
  if (!form.reportName) {
    ElMessage.warning('请输入报告名称')
    return
  }
  generating.value = true
  progress.value = 0
  currentStep.value = '正在初始化...'

  const steps = ['正在采集商家基础信息...', '正在汇总社交媒体数据...', '正在分析经营数据...', '正在生成图表...', '正在生成报告...']
  for (let i = 0; i < steps.length; i++) {
    currentStep.value = steps[i]
    progress.value = (i / steps.length) * 100
    await new Promise(r => setTimeout(r, 800))
  }

  try {
    await createReport(form)
    reportStatus.value = 1
    progress.value = 100
    ElMessage.success('报告生成成功')
  } catch {
    reportStatus.value = 2
  } finally {
    generating.value = false
  }
}

function downloadReport() {
  ElMessage.info('下载报告功能开发中')
}

function previewReport() {
  ElMessage.info('预览报告功能开发中')
}

onMounted(async () => {
  const res = await getAllMerchants()
  merchants.value = res || []

  if (route.query.merchantId) {
    form.merchantId = parseInt(route.query.merchantId)
  }
  if (route.query.type) {
    form.reportType = route.query.type
  }
  if (route.query.merchantId && merchants.value.length) {
    const m = merchants.value.find(m => m.id === parseInt(route.query.merchantId))
    if (m) {
      form.reportName = `${m.name}${new Date().getFullYear()}Q${Math.ceil((new Date().getMonth() + 1) / 3)}营销报告`
    }
  }
})
</script>

<style scoped>
.step-content {
  min-height: 300px;
  padding: 20px 0;
}

.step-footer {
  display: flex;
  justify-content: center;
  gap: 12px;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.generating {
  text-align: center;
  padding: 40px;
}

.generating p {
  margin-top: 20px;
  color: #606266;
}
</style>
