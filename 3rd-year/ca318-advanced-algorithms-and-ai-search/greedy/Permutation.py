class Permutation:
    def permute(self, s):
        result = []
        visited = set()
        self.dfs(result, visited, [], list(s))
        return result

    def dfs(self, result, visited, subset, a):
        if len(subset) == len(a):
            result.append(subset)
        for i in range(len(a)):
            if a[i] not in visited:
                visited.add(a[i])
                self.dfs(result, visited, subset + [a[i]], a)
                visited.remove(a[i])

if __name__ == '__main__':
    p = Permutation()
    answer = p.permute("12345678 ")
    print(answer)