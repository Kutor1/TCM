import pandas as pd
import os
import warnings
from docx import Document
from transformers import BertTokenizer, BertForTokenClassification, pipeline

# 禁用 symlink 缓存警告
os.environ['HF_HUB_DISABLE_SYMLINKS_WARNING'] = '1'

# 屏蔽 FutureWarning
warnings.filterwarnings("ignore", category=FutureWarning)

# 更改缓存路径
os.environ['TRANSFORMERS_CACHE'] = 'D:/Tcm/cache'

# 加载 BERT 模型和 tokenizer
tokenizer = BertTokenizer.from_pretrained('bert-base-chinese')
model = BertForTokenClassification.from_pretrained('bert-base-chinese')

# 创建一个命名实体识别 (NER) pipeline
nlp_ner = pipeline("ner", model=model, tokenizer=tokenizer)


def read_docx(file_path):
    doc = Document(file_path)
    text = []
    for para in doc.paragraphs:
        text.append(para.text)
    return '\n'.join(text)


def split_text_with_ner(text, keywords):
    # 使用 NER 模型对文本进行处理
    ner_results = nlp_ner(text)

    # 处理 NER 结果
    sentences = text.split('。')  # 中文句子通常以“。”结束
    relevant_sentences = []
    for sentence in sentences:
        if any(keyword in sentence for keyword in keywords):
            relevant_sentences.append(sentence.strip())

    # 对切割后的文本进行特殊符号和换行符号的剔除
    cleaned_sentences = [s.replace('【', '').replace('】', '').replace('\n', '').strip() for s in relevant_sentences]

    return cleaned_sentences


def extract_information(sentences, keywords):
    records = []
    current_prescription = None
    record = {key: None for key in keywords}  # 初始化每个关键词对应的字段

    for sentence in sentences:
        # 检查每个关键词
        for keyword in keywords:
            if keyword in sentence:
                content = sentence.split(keyword)[-1].strip()  # 提取关键词后的内容
                if keyword == "处方用名":
                    # 将记录保存到列表中，并创建一个新的记录
                    if current_prescription and any(val is not None for val in record.values()):
                        record["处方用名"] = current_prescription
                        records.append(record)
                    # 初始化新的记录
                    record = {key: None for key in keywords}
                    current_prescription = content
                    record[keyword] = content
                else:
                    record[keyword] = content

    # 保存最后一个记录
    if current_prescription and any(val is not None for val in record.values()):
        record["处方用名"] = current_prescription
        records.append(record)

    return records


def process_docx(file_path, keywords):
    text = read_docx(file_path)

    # 基于关键词的文本切割
    cut_text = split_text_with_ner(text, keywords)

    # 提取信息并构造数据结构
    records = extract_information(cut_text, keywords)

    # 将提取的信息保存为 CSV 文件，格式为 Neo4j 节点和关系
    df = pd.DataFrame(records)
    df.to_csv('neo4j_data.csv', index=False, encoding='utf-8')


# 使用示例
docx_file_path = 'from.docx'  # 替换为你的 .docx 文件路径
keywords = ["处方用名", "来源", "炮制方法", "质量要求", "炮制作用", "炮制研究", "贮存"]  # 替换为你要提取的关键词列表

process_docx(docx_file_path, keywords)