def longest_common_subsequence(S1, S2):
    m = len(S1)
    n = len(S2)
    L = [[0 for x in range(n+1)] for x in range(m+1)]
    #print(L)

    # Building the mtrix in bottom-up way
    for i in range(m+1):
        for j in range(n+1):
            if i == 0 or j == 0:
                L[i][j] = 0
            # compare two indices in the words
            elif S1[i-1] == S2[j-1]:
                L[i][j] = L[i-1][j-1] + 1
            else:
                L[i][j] = max(L[i-1][j], L[i][j-1])

    index = L[m][n]

    lcs_algo = [""] * (index+1)
    lcs_algo[index] = ""

    i = m
    j = n
    while i > 0 and j > 0:
        # does the top left index have equal letters?
        if S1[i-1] == S2[j-1]:
            lcs_algo[index-1] = S1[i-1]
            i -= 1
            j -= 1
            index -= 1
        # if top greater than left -> go up
        elif L[i-1][j] > L[i][j-1]:
            i -= 1
        else:
            # go left
            j -= 1

    # Printing the sub sequences
    #print("S1 : " + S1 + "\nS2 : " + S2)
    #print("LCS: " + "".join(lcs_algo))
    lcs = "".join(lcs_algo)
    return lcs

def get_lcss_r(s, t):
    if len(s) == 0 or len(t) == 0:
        return 0
    else:
        move_both = get_lcss_r(s[1:], t[1:]) + 1 if s[0] == t[0] else 0
        move_s = get_lcss_r(s[1:], t)
        move_t = get_lcss_r(s, t[1:])

    return max([move_both, move_s, move_t])


def main():
    S2 = "VJTXWDLQ"
    S1 = "STZWULQ"
    answer = longest_common_subsequence(S1, S2)
    print(answer)

if __name__ == '__main__':
    main()
