import apiClient from './instance'

export const searchApi = async (context) => {
  try {
    const response = await apiClient.get(`/search/${context}`)
    return response
  } catch (error) {
    console.log(error)
  }
}
