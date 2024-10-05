import apiClient from './instance'

// 点击事件
export const clickApi = async (id) => {
  try {
    const response = await apiClient.get(`/click/${id}`)
    return response
  } catch (error) {
    console.log(error)
  }
}
