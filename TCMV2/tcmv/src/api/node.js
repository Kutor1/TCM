import apiClient from './instance'

// 获取节点信息
export const getNodeInfo = async (id) => {
  try {
    const response = await apiClient.get(`/info/${id}`)
    return response
  } catch (error) {
    console.log(error)
  }
}
