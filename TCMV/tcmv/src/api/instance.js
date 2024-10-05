import axios from 'axios'

// 定义实例
const apiClient = axios.create({
  // baseURL: 'http://localhost:8080/api',
  baseURL: 'http://43.138.146.197:8081/api',
  timeout: 10000 // 请求超时时间
})

// 可以在请求拦截器中添加请求配置
apiClient.interceptors.request.use(
  (config) => {
    // 在请求发出之前做些什么，比如添加认证 token
    // config.headers['Authorization'] = 'Bearer your-token';
    return config
  },
  (error) => {
    // 对请求错误做些什么
    return Promise.reject(error)
  }
)

// 可以在响应拦截器中处理响应数据
apiClient.interceptors.response.use(
  (response) => {
    // 对响应数据做些什么
    return response
  },
  (error) => {
    // 对响应错误做些什么
    return Promise.reject(error)
  }
)

export default apiClient
