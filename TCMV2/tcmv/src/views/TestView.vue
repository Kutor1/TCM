<template>
  <el-card style="height: 98vh">
    <el-row gutter="20" class="controls-row">
      <!-- Checkbox Group -->
      <el-col :span="16">
        <el-checkbox-group
          v-model="checkboxGroup"
          size="large"
          @change="handleCheckedChange"
          :disabled="dbFlag"
        >
          <el-checkbox-button
            v-for="list in lists"
            :key="list"
            :value="list"
            style="margin-bottom: 10px"
          >
            {{ list }}
          </el-checkbox-button>
        </el-checkbox-group>
      </el-col>

      <!-- Button -->
      <el-col :span="8">
        <el-button type="primary" @click="rechange"> 重置 </el-button>
        <el-button type="primary" @click="drawNodeName()">添加name</el-button>
        <el-button type="primary" @click="removeNodeName()">移除name</el-button>
        <el-button type="primary" @click="table = true" disabled
          >上传文件</el-button
        >
      </el-col>
    </el-row>

    <el-drawer v-model="table" title="文件上传" direction="rtl" size="50%">
      <el-upload
        class="upload-demo"
        drag
        action="https://run.mocky.io/v3/9d059bf9-4660-45f2-925d-ce80ad6c4d15"
        :auto-upload="false"
        :file-list="fileList"
        :on-change="onFileChange"
        :on-remove="handleRemove"
        :limit="1"
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip>
          <div class="el-upload__tip">请上传.doc/docx文件</div>
        </template>
      </el-upload>

      <el-button
        type="primary"
        @click="uploadFile"
        :disabled="!selectedFile"
        style="margin-top: 20px"
      >
        上传
      </el-button>
    </el-drawer>

    <el-row gutter="20" class="search-row" style="margin-top: 20px">
      <!-- Search Input -->
      <el-col :span="16">
        <el-input
          clearable
          placeholder="请输入搜索内容"
          v-model="search"
          size="large"
        ></el-input>
      </el-col>

      <!-- Search Button -->
      <el-col :span="8">
        <el-button type="primary" @click="searchNode" block> 搜索 </el-button>
      </el-col>
    </el-row>

    <!-- Node Info Component -->
    <el-row style="margin-top: 20px">
      <el-col :span="24">
        <NodeInfo :id="selectedNodeId" :group="selectedNodeGroup" />
      </el-col>
    </el-row>

    <!-- D3 Container -->
    <el-row style="margin-top: 20px">
      <el-col :span="24">
        <el-card>
          <div class="container"></div>
        </el-card>
      </el-col>
    </el-row>
  </el-card>
</template>

<script setup>
import { nextTick, onMounted, ref } from 'vue'
import * as d3 from 'd3'
import NodeInfo from '@/components/NodeInfo.vue'
import {
  getAllHerbs,
  getAllEffects,
  getAllStorages,
  getAllMethods,
  getAllResearchs,
  getAllQualitys,
  getAllSources
} from '@/api/nodes'
import { clickApi } from '@/api/click'
import { searchApi } from '@/api/search'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const table = ref(false)
const nameFlag = ref(false)
const dbFlag = ref(false)
const selectedNodeId = ref('')
const selectedNodeGroup = ref('')
const search = ref('')
const checkboxGroup = ref([''])
const lists = [
  '处方用名',
  '来源',
  '炮制方法',
  '炮制作用',
  '炮制研究',
  '贮存',
  '质量要求'
]

const handleCheckedChange = async (value) => {
  // 定义包含所有分组的映射关系
  const groupMapping = {
    处方用名: 'herb',
    来源: 'source',
    炮制作用: 'effect',
    炮制方法: 'method',
    炮制研究: 'research',
    贮存: 'storage',
    质量要求: 'quality'
  }

  // 根据映射关系，删除未选中的节点
  for (const [key, group] of Object.entries(groupMapping)) {
    if (!value.includes(key)) {
      data.value.nodes = data.value.nodes.filter((node) => node.group !== group)
    }
  }

  await nextTick()
  draw()

  await value.forEach((selectedValue) => {
    switch (selectedValue) {
      case '处方用名':
        catchHerbs()
        break
      case '来源':
        catchSource()
        break
      case '质量要求':
        catchQualitys()
        break
      case '炮制方法':
        catchMethods()
        break
      case '炮制研究':
        catchResearchs()
        break
      case '炮制作用':
        catchEffects()
        break
      case '贮存':
        catchStorage()
        break
    }
  })
}

// 重置
const rechange = () => {
  // 清空data数据
  data.value.links = []
  data.value.nodes = []

  // 设置多选框状态
  dbFlag.value = false

  // 清除多选框状态
  checkboxGroup.value = []

  // 重新绘图
  draw()
}

// 定义文件信息的对象
const selectedFile = ref(null)
const fileList = ref([])
const files = ref([])
// 文件选择事件处理
const onFileChange = (file) => {
  selectedFile.value = file.raw
  fileList.value = [file]
}

// 从上传处移除文件
const handleRemove = () => {
  selectedFile.value = null
  fileList.value = []
}

// 文件上传
const uploadFile = async () => {
  if (!selectedFile.value) {
    ElMessage.error('请选择一个文件')
    return
  }

  const formData = new FormData()
  formData.append('file', selectedFile.value)

  try {
    // const response = await axios.post('http://localhost:8081/files', formData, {
    const response = await axios.post(
      'http://129.204.131.237:8081/files',
      formData,
      {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }
    )
    if (response.data.code === 200) {
      ElMessage.success('文件上传成功')
      await fetchFiles() // 上传成功后刷新文件列表
      handleRemove() // 清除已上传的文件
    } else if (response.data.code === 500) {
      ElMessage.error(response.data.message)
    }
  } catch (error) {
    console.error('上传失败:', error)
    ElMessage.error('文件上传失败')
  }
}

// 获取文件列表
const fetchFiles = async () => {
  try {
    // const response = await axios.get('http://localhost:8081/files')
    const response = await axios.get('http://129.204.131.237:8081/files')
    files.value = response.data.data
    ElMessage.success('获取文件列表成功')
  } catch (error) {
    console.error('获取文件列表失败:', error)
    ElMessage.error('获取文件列表失败')
  }
}

// 搜索内容
const searchNode = async () => {
  // 重置已有的data数据
  data.value.links = []
  data.value.nodes = []

  try {
    const response = await searchApi(search.value)
    // 将node数据添加到data前，检测是否已经存在相同id的节点数据
    response.data.forEach((newNode) => {
      if (!data.value.nodes.some((node) => node.id === newNode.id)) {
        data.value.nodes.push(newNode)
      }
    })
    // 确保数据更新后再绘制图像
    await nextTick()
    draw()
  } catch (error) {
    console.log(error)
  }
  // 清空输入框
  search.value = ''
}

onMounted(async () => {
  draw()
})

// 定义data
const data = ref({
  nodes: [],
  links: []
})

var updateNodeLink = null
var updateNode = null
var drawNodeName = null
var removeNodeName = null
const node = ref([])
const link = ref([])
const simulation = ref(null)
const nodeName = ref([])
const linkName = ref([])

const catchHerbs = async () => {
  try {
    const response = await getAllHerbs()
    // 将node数据添加到data前，检测是否已经存在相同id的节点数据
    // response.data.forEach((newNode) => {
    //   if (!data.value.nodes.some((node) => node.id === newNode.id)) {
    //     newNode.group = 'herb' // 为节点设置分组标识
    //     data.value.nodes.push(newNode)
    //   }
    // })
    // // 确保数据更新后再绘制图像
    // await nextTick()
    // draw()
    updateNode(response)
  } catch (error) {
    console.log(error)
  }
}

// 获取effect单节点
const catchEffects = async () => {
  try {
    const response = await getAllEffects()
    updateNode(response)
  } catch (error) {
    console.log(error)
  }
}

// 获取storage单节点
const catchStorage = async () => {
  try {
    const response = await getAllStorages()
    updateNode(response)
  } catch (error) {
    console.log(error)
  }
}

// 获取method单节点
const catchMethods = async () => {
  try {
    const response = await getAllMethods()
    updateNode(response)
  } catch (error) {
    console.log(error)
  }
}

// 获取research单节点
const catchResearchs = async () => {
  try {
    const response = await getAllResearchs()
    updateNode(response)
  } catch (error) {
    console.log(error)
  }
}

// 获取quality单节点
const catchQualitys = async () => {
  try {
    const response = await getAllQualitys()
    updateNode(response)
  } catch (error) {
    console.log(error)
  }
}

// 获取source单节点
const catchSource = async () => {
  try {
    const response = await getAllSources()
    updateNode(response)
  } catch (error) {
    console.log(error)
  }
}

// 获取所有节点
// const catchAllNodes = async () => {
//   try {
//     // 获取所有节点和连接
//     const responsenodes = await getAllNodes()
//     const responselinks = await getAllLinks()
//     if (responsenodes && responselinks) {
//       // 将后台获取的数据格式化为 nodes 的结构
//       data.value.nodes = responsenodes.data.map((herb) => ({
//         id: herb.id, // 直接使用后台返回的 'id'
//         name: herb.name, // 直接使用后台返回的 'name'
//         group: null // 暂时设置 group 为 null，之后可以根据需要修改
//       }))
//       data.value.links = responselinks.data.map((link) => ({
//         target: link.target,
//         source: link.source
//       }))
//     }
//   } catch (error) {
//     console.error('Error fetching herbs data:', error)
//   }
// }

// nodes 数据中，group非必选，但会根据group赋予颜色
// links 数据中，value非必选，但会根据value赋予link宽度
const draw = () => {
  // 清空之前的图像，避免重复绘制
  d3.select('.container').selectAll('*').remove()

  // 图像大小，宽度与高度定义
  // Specify the dimensions of the chart.
  const width = 1200
  const height = 1000

  // Specify the color scale.
  const color = d3.scaleOrdinal(d3.schemeCategory10)

  // The force simulation mutates links and nodes, so create a copy
  // so that re-evaluating this cell produces the same result.

  const nodes = data.value.nodes.map((d) => ({ ...d }))
  const links = data.value.links.map((d) => ({ ...d }))

  // Create a simulation with several forces.
  simulation.value = d3
    .forceSimulation(nodes) // 仿真模拟节点
    .force(
      'link',
      d3
        .forceLink(links)
        .id((d) => d.id)
        .distance(115)
    ) // 仿真模拟关系
    .force(
      'collide',
      d3.forceCollide().radius(() => 70) // 碰撞半径
    ) // 定义碰撞关系
    .force('charge', d3.forceManyBody())
    .force('center', d3.forceCenter(width / 2, height / 2))
    .on('tick', ticked)

  // 创建svg容器
  // Create the SVG container.
  //   const svg = d3
  //     .create('svg')
  //     .attr('width', width)
  //     .attr('height', height)
  //     .attr('viewBox', [0, 0, width, height])
  //     .attr('style', 'max-width: 100%; height: auto;')
  // 绑定容器
  const svg = d3
    .select('.container')
    .append('svg')
    .attr('width', width)
    .attr('height', height)
    .attr('viewBox', [0, 0, width, height])
    .attr('style', 'max-width: 100%; height: auto;')

  const positiveMarker = svg
    .append('marker')
    .attr('id', 'positiveMarker')
    .attr('refX', 35) // xy偏移
    .attr('refY', 0)
    .attr('orient', 'auto')
    .attr('stroke-width', 2) // 箭头粗细
    .attr('markerUnits', 'userSpaceOnUse')
    .attr('markerUnits', 'strokeWidth')
    .attr('viewBox', '0 -5 10 10')
    .attr('markerWidth', 12)
    .attr('markerHeight', 12)
    .append('path')
    .attr('d', 'M 0 -5 L 10 0 L 0 5')
    .attr('fill', '#999')
    .attr('stroke-opacity', 0.6)

  const negativeMarker = svg
    .append('marker')
    .attr('id', 'negativeMarker')
    .attr('refX', -25) // xy偏移
    .attr('refY', 0)
    .attr('orient', 'auto')
    .attr('stroke-width', 2) // 箭头粗细
    .attr('markerUnits', 'userSpaceOnUse')
    .attr('markerUnits', 'strokeWidth')
    .attr('viewBox', '0 -5 10 10')
    .attr('markerWidth', 12)
    .attr('markerHeight', 12)
    .append('path')
    .attr('d', 'M 10 -5 L 0 0 L 10 5')
    .attr('fill', '#999')
    .attr('stroke-opacity', 0.6)

  // 定义使用g标签包裹后的svg图像的鼠标缩放方法，并限制缩放范围
  svg.call(
    d3
      .zoom()
      .scaleExtent([0.7, 5]) // 限制缩放范围为 0.7 到 5 倍
      .on('zoom', (event) => {
        g.attr('transform', event.transform)
      })
  )

  const g = svg.append('g') // 定义g标签，并用其包裹nodes和links

  // Add a line for each link, and a circle for each node.
  link.value = g
    .append('g')
    .attr('stroke', '#999') // link颜色
    .attr('stroke-opacity', 0.6) // link透明度
    //.attr('marker-end', 'url(#direction)') // 添加绑定箭头方向
    .selectAll('path')
    .data(links)
    .enter()
    .append('path')
    .attr('stroke-width', (d) => Math.sqrt(d.value)) // link轮廓宽度
    .attr('class', 'link')
    .attr('id', (d) => d.source + ' ' + d.relationship + ' ' + d.target)

  linkName.value = g
    .append('g')
    .selectAll('text')
    .data(links)
    .join('text')
    .attr('x', 100)
    .attr('y', 80)
    .style('fill', 'black') // 字体颜色
    .style('font-size', '10px')
    .style('font-weight', 'bold')
    .append('textPath')
    .attr('href', (d) => '#' + d.source + ' ' + d.relationship + ' ' + d.target)
    .text((d) => d.relationship)

  // 节点属性设置
  node.value = g
    .append('g')
    .attr('stroke', '#fff')
    .attr('stroke-width', 1.5)
    .selectAll()
    .data(nodes) // 绑定数据
    .join('circle') // 节点样式
    .attr('r', 30) // 节点半径大小
    .attr('fill', (d) => color(d.group)) // 根据group来填充节点颜色
    .on('dblclick', (event, d) => {
      ondbclickNode(d) // 双击触发事件
    }) // 绑定点击事件, 获得当前点击节点的属性，后续可以与api绑定获取相应数据
    .on('click', (event, d) => {
      onClickNode(d)
    })
    .on('mouseenter', function () {
      // 鼠标悬停时，增加高亮效果
      d3.select(this)
        .attr('stroke', 'black') // 更改节点边框颜色
        .attr('stroke-width', 4) // 增加边框宽度
    })
    .on('mouseleave', function () {
      // 鼠标移开时，移除高亮效果
      d3.select(this)
        .attr('stroke', '#fff') // 恢复原始边框颜色
        .attr('stroke-width', 1.5) // 恢复原始边框宽度
    })

  node.value.append('title').text((d) => d.id) // 为节点赋予title属性

  nodeName.value = g
    .append('g')
    .selectAll('text')
    .data(nodes)
    .join('text')
    .text((d) => {
      if (nameFlag.value) {
        return d.name
      }
    })
    .attr('dx', function () {
      return -this.getBBox().width / 2 // 使用getBBox()来获取文本的宽度，计算偏移量
    })
    .attr('dy', 50)
    .attr('class', 'node-name') // 为标题赋予类名
    .attr('fill', 'black') // 文本颜色设置

  // Add a drag behavior.
  // 节点拖拽事件 dragstarted拖动开始 dragged拖动 dragend拖动结束
  node.value.call(
    d3.drag().on('start', dragstarted).on('drag', dragged).on('end', dragended)
  )

  // const onClickNode = async (d) => {
  //   try {
  //     const response = await clickApi(d.id)
  //     const newNodes = response.data

  //     newNodes.forEach((newNode) => {
  //       const existingNode = data.value.nodes.find(
  //         (node) => node.id === newNode.id
  //       )

  //       if (!existingNode) {
  //         data.value.nodes.push(newNode)
  //       }
  //       const existingLink = data.value.links.find(
  //         (link) =>
  //           (link.source === d.id && link.target === newNode.id) ||
  //           (link.source === newNode.id && link.target === d.id)
  //       )
  const onClickNode = async (d) => {
    selectedNodeGroup.value = d.group
    selectedNodeId.value = d.id
  }
  const ondbclickNode = async (d) => {
    dbFlag.value = true
    const response = await clickApi(d.id)
    updateNodeLink(response, d)
  }

  // 绘制节点与链接方法
  updateNodeLink = async (response, d) => {
    try {
      nameFlag.value = true // 反向设置nameFlag(是的，true就是反向。吗？)
      // const response = await clickApi(d.id)
      const newNodes = response.data

      // 过滤出还未存在的节点
      const uniqueNewNodes = newNodes.filter((newNode) => {
        return !data.value.nodes.some((node) => node.id === newNode.id)
      })

      // 将新节点合并到现有的节点数据中
      data.value.nodes.push(...uniqueNewNodes)

      // 为每个新节点添加到新的链接，假设链接的目标是当前点击的节点d
      // 增设判断条件，如果target是当前id则反向link
      newNodes.forEach((newNode) => {
        if (newNode.target === d.id) {
          data.value.links.push({
            source: newNode.id,
            target: newNode.target,
            relationship: newNode.relationship
          })
        } else {
          data.value.links.push({
            source: d.id,
            target: newNode.target,
            relationship: newNode.relationship
          })
        }
      })

      // 更新链接绘制，包含新添加的链接
      link.value = link.value
        .data(data.value.links, (d) => `${d.source.id}-${d.target.id}`)
        .join('path')
        .attr('stroke', '#999')
        .attr('stroke-opacity', 0.6)
        .attr('stroke-width', (d) => Math.sqrt(d.value || 1))
        .attr('class', 'link')
        .attr('marker-end', 'url(#direction)')
        // .attr('id', (d) => `link-${d.source.id}-${d.target.id}`)
        .attr('id', function (d) {
          if (typeof d.source === 'object') {
            return d.source.id + ' ' + d.relationship + ' ' + d.target.id
          } else {
            return d.source + ' ' + d.relationship + ' ' + d.target
          }
        })

      // 更新链接名称的绘制
      // linkName.value = linkName.value
      //   .data(data.value.links, (d) => `${d.source.id}-${d.target.id}`)
      //   .join(
      //     (enter) => {
      //       const text = enter
      //         .append('text')
      //         .attr('class', 'link-name')
      //         .attr('dy', -5)
      //         .style('fill', 'black')
      //         .style('font-size', '10px')
      //         .style('font-weight', 'bold')
      //       text
      //         .append('textPath')
      //         .attr('startOffset', '50%')
      //         .attr('href', function (d) {
      //           // 处理 source 和 target 可能是对象或字符串的情况
      //           if (typeof d.source === 'object') {
      //             return `#${d.source.id} ${d.relationship} ${d.target.id}`
      //           } else {
      //             return `#${d.source} ${d.relationship} ${d.target}`
      //           }
      //         })
      //         .text((d) => d.relationship)
      //       return text
      //     },
      //     (update) => {
      //       update
      //         .select('textPath')
      //         .attr('href', function (d) {
      //           // 同样处理 update 时 source 和 target 的类型
      //           if (typeof d.source === 'object') {
      //             return `#${d.source.id} ${d.relationship} ${d.target.id}`
      //           } else {
      //             return `#${d.source} ${d.relationship} ${d.target}`
      //           }
      //         })
      //         .text((d) => d.relationship)
      //       return update
      //     },
      //     (exit) => exit.remove()
      //   )
      linkName.value = linkName.value
        .data(data.value.links, (d) => `${d.source.id}-${d.target.id}`)
        .join(
          (enter) => {
            const text = enter
              .append('text')
              .attr('class', 'link-name')
              .attr('dy', -5)
              .style('fill', 'black')
              .style('font-size', '10px')
              .style('font-weight', 'bold')
              .style('text-anchor', 'middle')

            text
              .append('textPath')
              .attr('startOffset', '50%')
              .attr('href', function (d) {
                // 处理 source 和 target 可能是对象或字符串的情况
                if (typeof d.source === 'object') {
                  return `#${d.source.id} ${d.relationship} ${d.target.id}`
                } else {
                  return `#${d.source} ${d.relationship} ${d.target}`
                }
              })
              .text((d) => d.relationship)

            return text
          },
          (update) => {
            update
              .select('textPath')
              .attr('href', function (d) {
                // 同样处理 update 时 source 和 target 的类型
                if (typeof d.source === 'object') {
                  return `#${d.source.id} ${d.relationship} ${d.target.id}`
                } else {
                  return `#${d.source} ${d.relationship} ${d.target}`
                }
              })
              .text((d) => d.relationship)

            return update
          },
          (exit) => exit.remove()
        )

      // 只对新节点进行绘制
      node.value = node.value
        .data(data.value.nodes, (d) => d.id)
        .join('circle')
        .attr('r', 30)
        .attr('fill', (d) => color(d.group))
        .attr('stroke', '#fff')
        .attr('stroke-width', 1.5)
        .call(
          d3
            .drag()
            .on('start', dragstarted)
            .on('drag', dragged)
            .on('end', dragended)
        )
        .on('dblclick', (event, d) => ondbclickNode(d)) // 双击触发事件
        .on('click', (event, d) => onClickNode(d)) // 单击触发事件
        .on('mouseenter', function (event, d) {
          onClickNode(d)
          d3.select(this).attr('stroke', 'black').attr('stroke-width', 4)
        })
        .on('mouseleave', function () {
          d3.select(this).attr('stroke', '#fff').attr('stroke-width', 1.5)
        })

      // 为新节点添加名称
      // nodeName.value = nodeName.value
      //   .data(data.value.nodes, (d) => d.id)
      //   .join('text')
      //   .text((d) => d.name)
      //   .attr('dx', function () {
      //     return -this.getBBox().width / 2
      //   })
      //   .attr('dy', 50)
      //   .attr('class', 'node-name')
      //   .attr('fill', 'black')

      // 更新仿真以包含新的节点和链接
      simulation.value.nodes(data.value.nodes)
      simulation.value.force('link').links(data.value.links)
      // simulation.value.alpha(1).restart()
      simulation.value.alpha(0.5).restart()
    } catch (error) {
      console.log('Error fetching node data:', error)
    }
  }

  // 绘制节点方法
  updateNode = async (response) => {
    try {
      nameFlag.value = false // 设置是否添加nodeName
      // const response = await clickApi(d.id)
      const newNodes = response.data

      // 过滤出还未存在的节点
      const uniqueNewNodes = newNodes.filter((newNode) => {
        return !data.value.nodes.some((node) => node.id === newNode.id)
      })

      // 将新节点合并到现有的节点数据中
      data.value.nodes.push(...uniqueNewNodes)

      // 只对新节点进行绘制
      node.value = node.value
        .data(data.value.nodes, (d) => d.id)
        .join('circle')
        .attr('r', 30)
        .attr('fill', (d) => color(d.group))
        .attr('stroke', '#fff')
        .attr('stroke-width', 1.5)
        .call(
          d3
            .drag()
            .on('start', dragstarted)
            .on('drag', dragged)
            .on('end', dragended)
        )
        .on('dblclick', (event, d) => ondbclickNode(d)) // 绑定双击触发事件
        .on('click', (event, d) => onClickNode(d)) // 绑定单击触发事件
        .on('mouseenter', function (event, d) {
          onClickNode(d)
          d3.select(this).attr('stroke', 'black').attr('stroke-width', 4)
        })
        .on('mouseleave', function () {
          d3.select(this).attr('stroke', '#fff').attr('stroke-width', 1.5)
        })

      // // 为新节点添加名称
      // nodeName.value = nodeName.value
      //   .data(data.value.nodes, (d) => d.id)
      //   .join('text')
      //   .text((d) => d.name)
      //   .attr('dx', function () {
      //     return -this.getBBox().width / 2
      //   })
      //   .attr('dy', 50)
      //   .attr('class', 'node-name')
      //   .attr('fill', 'black')

      // 更新仿真以包含新的节点和链接
      await nextTick()
      simulation.value.nodes(data.value.nodes)
      simulation.value.alpha(1).restart()
    } catch (error) {
      console.log('Error fetching node data:', error)
    }
  }

  // 绘制节点标题方法
  drawNodeName = async () => {
    // 为新节点添加名称
    nodeName.value = nodeName.value
      .data(data.value.nodes, (d) => d.id)
      .join('text')
      .text((d) => d.name)
      .attr('dx', function () {
        return -this.getBBox().width / 2
      })
      .attr('dy', 50)
      .attr('class', 'node-name')
      .attr('fill', 'black')
    await nextTick()
    simulation.value.nodes(data.value.nodes)
    simulation.value.alpha(0.01).restart()
  }

  // 移除节点标题方法
  removeNodeName = async () => {
    nodeName.value = nodeName.value
      .data(data.value.nodes, (d) => d.id)
      .join('text')
      .text(() => '')
      .attr('dx', function () {
        return -this.getBBox().width / 2
      })
      .attr('dy', 50)
      .attr('class', 'node-name')
      .attr('fill', 'black')
    await nextTick()
    simulation.value.nodes(data.value.nodes)
    simulation.value.alpha(0.01).restart()
  }

  // 每次仿真模拟时，设置链接和节点的属性
  // Set the position attributes of links and nodes each time the simulation ticks.
  function ticked() {
    // linkName.value.attr('transform', (d) => {
    //   const midX = (d.source.x + d.target.x) / 2
    //   const midY = (d.source.y + d.target.y) / 2
    //   return `translate(${midX}, ${midY})`
    // })
    link.value
      .attr('d', (d) => {
        const source = d.source.x < d.target.x ? d.source : d.target
        const target = d.source.x < d.target.x ? d.target : d.source
        return `M${source.x},${source.y}L${target.x},${target.y}`
      })
      .attr('marker-end', (d) =>
        d.source.x < d.target.x ? 'url(#positiveMarker)' : null
      )
      .attr('marker-start', (d) =>
        d.source.x >= d.target.x ? 'url(#negativeMarker)' : null
      )

    node.value.attr('cx', (d) => d.x).attr('cy', (d) => d.y)

    nodeName.value.attr('x', (d) => d.x).attr('y', (d) => d.y) // 为节点设置标题属性
  }

  // 拖动开始方法
  // Reheat the simulation when drag starts, and fix the subject position.
  function dragstarted(event, d) {
    // if (!event.active) simulation.value.alphaTarget(0.3).restart()
    if (!event.active) simulation.value.alphaTarget(0.01).restart() // alphaTarget 控制节点逸散程度
    // event.subject.fx = event.subject.x
    // event.subject.fy = event.subject.y
    // 确保在拖动开始时设置固定位置
    if (d.type === 'child') {
      // 子节点在拖动开始时，不需要特别处理
    } else {
      d.fx = d.x
      d.fy = d.y
    }
  }

  // 拖动节点方法
  // Update the subject (dragged node) position during drag.
  function dragged(event, d) {
    if (d.type === 'child') {
      // 对于子节点，更新 fx 和 fy
      d.fx = event.x
      d.fy = event.y
    } else {
      // 对于主节点，保持 fx 和 fy 不变
      d.fx = event.x
      d.fy = event.y
    }
  }

  // 拖动结束方法
  // Restore the target alpha so the simulation cools after dragging ends.
  // Unfix the subject position now that it’s no longer being dragged.
  function dragended(event, d) {
    if (!event.active) simulation.value.alphaTarget(0)
    // event.subject.fx = null
    // event.subject.fy = null
    // 拖动结束时，保持节点的 fx 和 fy 不变
    d.fx = d.x
    d.fy = d.y
  }

  // When this cell is re-run, stop the previous simulation. (This doesn’t
  // really matter since the target alpha is zero and the simulation will
  // stop naturally, but it’s a good practice.)
  //   invalidation.then(() => simulation.stop());

  //   return svg.node();
}
</script>

<style scoped>
.container {
  width: 100%;
  height: 100vh;
}

.el-card__body {
  padding: 0px;
}
</style>
