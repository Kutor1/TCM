import numpy as np
import matplotlib.pyplot as plt

def birthday_paradox_probability(n):
    probability = 1.0
    for i in range(n):
        probability *= (365 - i) / 365
    return probability

# 生成 n 从 1 到 365 的概率数据，并打印每个n对应的概率
n_values = np.arange(1, 366)
probabilities = []

for n in n_values:
    prob = birthday_paradox_probability(n)
    probabilities.append(prob)
    print(f"n = {n}, Probability of no shared birthday = {prob:.6f}")

# 创建图形
plt.figure(figsize=(10, 6))
plt.plot(n_values, probabilities, label="Probability of no shared birthday", color="blue", linewidth=2)
plt.xlabel("Number of People (n)")
plt.ylabel("Probability")
plt.title("Probability of No Shared Birthday (n from 1 to 365)")
plt.grid(True)
plt.axhline(y=0.5, color='r', linestyle='--', label="50% probability")
plt.legend()

# 显示图形
plt.show()

