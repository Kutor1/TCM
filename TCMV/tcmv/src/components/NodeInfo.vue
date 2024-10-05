<template>
  <div class="node-info-panel">
    <el-card>
      <h3>Node Information</h3>
      <div>
        <p><strong>Label:</strong> {{ groupLabel }}</p>
        <p><strong>ID:</strong> {{ props.id }}</p>
        <p><strong>Content:</strong> {{ nodeData.name }}</p>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { getNodeInfo } from '@/api/node'
import { ElMessage } from 'element-plus'
import { ref, watch, computed } from 'vue'

// Props
const props = defineProps({
  id: {
    type: String,
    required: true
  },
  group: {
    type: String,
    required: true
  }
})

// Data
const nodeData = ref('')

// 计算样式
const groupLabel = computed(() => {
  if (!nodeData.value) return 'Unknown Group'

  const labels = {
    herb: '处方用名',
    quality: '质量要求',
    research: '炮制研究',
    effect: '炮制作用',
    source: '来源',
    storage: '贮存',
    method: '炮制方法'
  }
  return labels[nodeData.value.group] || 'Unknown Group'
})

// 监测id变化
watch(
  () => props.id,
  (newId) => {
    if (newId) {
      fetchNodeData(newId)
    }
  }
)

async function fetchNodeData(id) {
  try {
    const response = await getNodeInfo(id)
    if (response && response.data.length > 0) {
      nodeData.value = response.data[0] // 取数组的第一个元素
      //   console.log(nodeData.value)
    } else {
      nodeData.value = null
      ElMessage.warning('No data available for the given ID.')
    }
  } catch (error) {
    nodeData.value = null
    ElMessage.error('Error fetching node data: ' + error.message)
  }
}
</script>

<style scoped>
.node-info-panel {
  margin: 20px;
}

h3 {
  margin-bottom: 10px;
}

p {
  margin: 0 0 10px 0;
}
</style>
