<template>
  <div class="financial-fetch">
    <div class="page-header">
      <h2 class="page-title">财报数据采集</h2>
    </div>
    <div class="card-container">
      <el-form label-width="120px" style="max-width: 600px;">
        <el-form-item label="选择商家">
          <el-select v-model="merchantId" placeholder="请选择商家" style="width: 100%;">
            <el-option v-for="m in merchants" :key="m.id" :label="m.name" :value="m.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="选择年份">
          <el-date-picker v-model="year" type="year" value-format="YYYY" placeholder="选择年份" style="width: 100%;" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleFetch">
            <el-icon><Search /></el-icon> 开始采集
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getAllMerchants } from '@/api/merchant'
import { fetchFinancial } from '@/api/fetchTask'

const merchants = ref([])
const merchantId = ref(null)
const year = ref(new Date().getFullYear().toString())
const loading = ref(false)

async function handleFetch() {
  if (!merchantId.value) {
    ElMessage.warning('请选择商家')
    return
  }
  loading.value = true
  try {
    await fetchFinancial({ merchantId: merchantId.value, year: parseInt(year.value) })
    ElMessage.success('财报采集任务已创建')
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  const res = await getAllMerchants()
  merchants.value = res || []
})
</script>
