import requests
from bs4 import BeautifulSoup

# 要抓取的网页URL
# url = "http://www.qihuanghealthcare.cn/live/detail/740"

input_file = 'wxlinks.txt'
output_file = 'contributee.txt'

results = []

# 读取链接文件
with open(input_file, 'r') as file:
    links = file.readlines()

# 去除每个链接末尾的换行符
links = [link.strip() for link in links]

for idx, url in enumerate(links, start=1):
    try:
        # url = "http://mp.weixin.qq.com/s?__biz=MzI1OTQ4NzMxMA==&mid=2247490556&idx=2&sn=709437da53a3378b71576391b0b92a52&chksm=ea7978dddd0ef1cb067bd5f8f00bba7bb77b5bf4b2e0ddad83499b29ae26c08674358ac3d3c7&scene=27#wechat_redirect"
        # 发送GET请求获取网页内容
        response = requests.get(url)

        print(url)
        # 确保请求成功
        if response.status_code == 200:
            # 获取网页的HTML文本
            html = response.text

            # 使用BeautifulSoup解析HTML
            soup = BeautifulSoup(html, 'html.parser')

            # 获取学会信息
            # 查找data-id为'85639'的 <section> 中的所有 <strong>
            contributee_section = soup.find('section', {'data-id': 85639})
            sacheme_section1 = contributee_section.find_all('strong')
            strong_tags = contributee_section.find_all('strong')

            # 定位最后一个strong标签
            strong_tagend = strong_tags[-1]
            print(strong_tagend)
            # # 获取主题
            # scheme_p = soup.find('div', {'class': 'm-article-detail'}).find_all('p')
            # scheme_text = scheme_p[2].find_all('span')[1].get_text()
            # print(scheme_text)

        else:
            print(f"请求失败，状态码: {response.status_code}")

    except Exception as e:
        # 捕获并记录异常，但继续下一个链接
        print(f"链接 {idx}: {url} - 发生错误: {e}")
        results.append(f"链接 {idx}: {url} - 请求出错: {e}")
        continue  # 跳过当前链接，继续处理下一个链接

# 将结果写入输出文件
with open(output_file, 'w') as file:
    for result in results:
        file.write(result + '\n')

