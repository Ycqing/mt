<template>
  <div class="website-fetch">
    <div class="page-header">
      <h2 class="page-title">官网数据采集</h2>
    </div>
    <div class="card-container">
      <el-form label-width="120px" style="max-width: 600px;">
        <el-form-item label="选择商家">
          <el-select v-model="merchantId" placeholder="请选择商家" style="width: 100%;" filterable>
            <el-option v-for="m in merchants" :key="m.id" :label="m.name" :value="m.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="官网地址">
          <el-input v-model="url" placeholder="请输入官网地址" />
        </el-form-item>
        <el-form-item label="采集内容">
          <el-checkbox-group v-model="contentTypes">
            <el-checkbox label="basic">基本信息</el-checkbox>
            <el-checkbox label="product">产品信息</el-checkbox>
            <el-checkbox label="about">关于我们</el-checkbox>
            <el-checkbox label="contact">联系方式</el-checkbox>
          </el-checkbox-group>
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
import { fetchWebsite } from '@/api/fetchTask'

const merchants = ref([])
const merchantId = ref(null)
const url = ref('')
const contentTypes = ref(['basic', 'product'])
const loading = ref(false)

async function handleFetch() {
  if (!merchantId.value) {
    ElMessage.warning('请选择商家')
    return
  }
  loading.value = true
  try {
    await fetchWebsite({ merchantId: merchantId.value, url: url.value })
    ElMessage.success('采集任务已创建')
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  const res = await getAllMerchants()
  merchants.value = res || []
})
</script>
