import apiClient from './instance'

// 获取所有节点
export const getAllNodes = async () => {
  try {
    const response = await apiClient.get('/nodes')
    return response
  } catch (error) {
    console.log(error)
  }
}

// 获取所有links
export const getAllLinks = async () => {
  try {
    const response = await apiClient.get('/links')
    return response
  } catch (error) {
    console.log(error)
  }
}

// 获取所有herb节点
export const getAllHerbs = async () => {
  try {
    const response = await apiClient.get('/herbs')
    return response
  } catch (error) {
    console.log(error)
  }
}

// 获取所有source节点
export const getAllSources = async () => {
  try {
    const response = await apiClient.get('/sources')
    return response
  } catch (error) {
    console.log(error)
  }
}

// 获取所有method节点
export const getAllMethods = async () => {
  try {
    const response = await apiClient.get('/methods')
    return response
  } catch (error) {
    console.log(error)
  }
}

// 获取所有quality节点
export const getAllQualitys = async () => {
  try {
    const response = await apiClient.get('/qualitys')
    return response
  } catch (error) {
    console.log(error)
  }
}

// 获取所有storage节点
export const getAllStorages = async () => {
  try {
    const response = await apiClient.get('/storages')
    return response
  } catch (error) {
    console.log(error)
  }
}

// 获取所有effect节点
export const getAllEffects = async () => {
  try {
    const response = await apiClient.get('/effects')
    return response
  } catch (error) {
    console.log(error)
  }
}

// 获取所有research节点
export const getAllResearchs = async () => {
  try {
    const response = await apiClient.get('/researchs')
    return response
  } catch (error) {
    console.log(error)
  }
}
