import sys
from collections import defaultdict

def get_data(src_lines, target_lines):
    src_vocab, target_vocab, tef = defaultdict(int), defaultdict(int), defaultdict(int)

    for i in range(len(src_lines)):
        src_sentence = src_lines[i].strip().split()
        target_sentence = target_lines[i].strip().split()

        for word in src_sentence:
            src_vocab[word] = 1

        for word in target_sentence:
            target_vocab[word] = 1

        for src_word in src_sentence:
            for target_word in target_sentence:
                tef[(target_word, src_word)] = 0

    return src_vocab, target_vocab, tef

def initialize_tef(src_vocab, tef):
    src_vocab_size = len(src_vocab)
    init_value = 1.0 / src_vocab_size 

    for target_word, src_word in tef:
        tef[(target_word, src_word)] = init_value

    return tef

def get_em(src_vocab, target_vocab, src_lines, target_lines, tef):
    for i in range(1):
        count, total = defaultdict(float), defaultdict(float)

        for j in range(len(src_lines)):
            print('sentence: ', j)
            src_sentence = src_lines[j].strip().split()       
            target_sentence = target_lines[j].strip().split()
            s_total = defaultdict(float)

            # compute normalization for length of target sentence
            for target_word in target_sentence:
                for src_word in src_sentence:
                    s_total[target_word] += tef[(target_word, src_word)]

            # print('s_total:', s_total)
            
            # collect counts
            for target_word in target_sentence:
                for src_word in src_sentence:
                    # if s_total[target_word] == 0: s_total[target_word] = 0.01
                    count[(target_word, src_word)] += (tef[(target_word, src_word)] / s_total[target_word])
                    total[src_word] += (tef[(target_word, src_word)] / s_total[target_word])
                
            # print('count:', count)
            # print('total:', total)
            # print()

        # estimate probabilities
        # number of target-source alignment pairs divided by the number of times the source word
        # is encountered
        print('calculate new probabilities')
        for i, src_word in enumerate(src_vocab):
            for target_word in target_vocab:
                if total[src_word] == 0: total[src_word] = 0.01
                tef[(target_word, src_word)] = count[(target_word, src_word)] / total[src_word]
            print(i, len(src_vocab))

        print('done')

        # print('count:', count)
        # print('total:', total)
        # print('tef:', tef)
        # print()
        
    return tef

def translate(french, french_to_eng):
    english = []
    for french_word in french.split():
        eng_word = ''
        if french_word in french_to_eng:
            eng_translations = french_to_eng[french_word]
            eng_word = eng_translations[0][0]
            i = 0
            while eng_word in english:
                if i == len(eng_translations) - 1:
                    break
                eng_word = eng_translations[i][0] 
                i += 1
        english.append(eng_word)
    return ' '.join(english)

def main():
    if len(sys.argv) != 3:
        print('Usage: python3 solution.py <source> <target>')
        print('eg. python3 solution.py train.en train.fr')
        sys.exit(1)

    # Given the source and target pairs, calculate the probabilities of the word pairs
    src, target = sys.argv[1], sys.argv[2]

    with open(src) as src_file, open(target) as target_file:
        src_lines = src_file.readlines()[:100]
        target_lines = target_file.readlines()[:100]

    # 1. get all vocab words
    src_vocab, target_vocab, tef = get_data(src_lines, target_lines)
    # 2. initialize values for tef
    tef = initialize_tef(src_vocab, tef)
    # 3. main algorithm
    tef = get_em(src_vocab, target_vocab, src_lines, target_lines, tef)
    # for k, v in sorted(tef.items(), key=lambda x: x[1], reverse=True)[:20]:
    #     print(k, v)

    french_to_eng = {}
    # create a dictionary where each key is each french word in the src_vocab and each value is the best english word:
    for k, prob in tef.items():
        fr, eng = k
        if fr not in french_to_eng:
            french_to_eng[fr] = []
        french_to_eng[fr].append((eng, prob))
    
    # sort all the keys
    for k, v in french_to_eng.items():
        french_to_eng[k] = sorted(v, key=lambda x: x[1], reverse=True)

    for k, v in french_to_eng.items():
        print(k, v[:5])
    
    french = input('Please enter a french sentence to translate. Press q to quit.\n')
    while french != 'q':
        if french == 'q':
            break
        english = translate(french, french_to_eng)
        print('english translation: ', english, end='\n\n')
        french = input('Please enter a french sentence to translate. Press q to quit.\n')

if __name__ == '__main__':
    main()
