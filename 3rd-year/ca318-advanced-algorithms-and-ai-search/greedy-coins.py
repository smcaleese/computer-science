def make_sum(total, coins):
    coins_used = []
    balance = total
    i = 0
    while balance > 0:
        if coins[i] <= balance:
            balance -= coins[i]
            coins_used.append(coins[i])
        else:
            i += 1
    answer = [0] * len(coins)
    for c in coins_used:
        answer[coins.index(c)] += 1
    return answer

print(make_sum(21, [25,12,10,1]))
