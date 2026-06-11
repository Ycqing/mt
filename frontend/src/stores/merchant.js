import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getMerchantList, getMerchantDetail, getAllMerchants, addMerchant, updateMerchant, deleteMerchant, batchDeleteMerchants } from '@/api/merchant'

export const useMerchantStore = defineStore('merchant', () => {
  const merchantList = ref([])
  const currentMerchant = ref(null)
  const loading = ref(false)
  const total = ref(0)

  async function fetchMerchantList(params) {
    loading.value = true
    try {
      const res = await getMerchantList(params)
      merchantList.value = res.records || res
      total.value = res.total || merchantList.value.length
    } finally {
      loading.value = false
    }
  }

  async function fetchMerchantDetail(id) {
    loading.value = true
    try {
      currentMerchant.value = await getMerchantDetail(id)
    } finally {
      loading.value = false
    }
  }

  async function fetchAllMerchants() {
    const res = await getAllMerchants()
    merchantList.value = res
    return res
  }

  async function createMerchant(data) {
    return await addMerchant(data)
  }

  async function editMerchant(id, data) {
    return await updateMerchant(id, data)
  }

  async function removeMerchant(id) {
    await deleteMerchant(id)
  }

  async function removeMerchants(ids) {
    await batchDeleteMerchants(ids)
  }

  return {
    merchantList,
    currentMerchant,
    loading,
    total,
    fetchMerchantList,
    fetchMerchantDetail,
    fetchAllMerchants,
    createMerchant,
    editMerchant,
    removeMerchant,
    removeMerchants
  }
})
