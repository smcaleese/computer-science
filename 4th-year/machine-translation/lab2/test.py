def normalize(s):
    chars = [c if c.isalpha() else ' ' for c in s.lower()]
    words = ''.join(chars).split()
    return ' '.join(words) 

if __name__ == '__main__':
    with open('input.txt') as f:
        lines = f.readlines()

    english, french = [], []
    for line in lines:
        line = line.split('\t')
        if len(line) == 2:
            fr = normalize(line[0])
            eng = normalize(line[1])
            # print('before:', line[1], 'eng:', eng)
            french.append(fr)
            english.append(eng)
    
    with open('train.en', 'w') as f:
        for line in english:
            f.write(line + '\n')

    with open('train.fr', 'w') as f:
        for line in french:
            f.write(line + '\n')
