import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getFetchTaskList, createFetchTask, deleteFetchTask, batchStartFetchTasks, executeFetchTask } from '@/api/fetchTask'

export const useFetchTaskStore = defineStore('fetchTask', () => {
  const taskList = ref([])
  const loading = ref(false)
  const total = ref(0)

  async function fetchTaskList(params) {
    loading.value = true
    try {
      const res = await getFetchTaskList(params)
      taskList.value = res.records || res
      total.value = res.total || taskList.value.length
    } finally {
      loading.value = false
    }
  }

  async function createTask(data) {
    return await createFetchTask(data)
  }

  async function removeTask(id) {
    await deleteFetchTask(id)
  }

  async function startTasks(ids) {
    await batchStartFetchTasks(ids)
  }

  async function executeTask(id) {
    return await executeFetchTask(id)
  }

  return {
    taskList,
    loading,
    total,
    fetchTaskList,
    createTask,
    removeTask,
    startTasks,
    executeTask
  }
})
