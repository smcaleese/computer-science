#!/usr/bin/python

import sys
import collections

if (len(sys.argv) != 4):
	sys.stderr.write("Usage: word_pr.py reference.txt system.txt n\n")
	sys.exit(1)

ref_f = sys.argv[1]
system_f = sys.argv[2]
n = int(sys.argv[3])

with open(ref_f) as f1:
	ref = f1.readline()

with open(system_f) as f2:
	system = f2.readline()

# Function to create n-grams from input
# Returns a list of n-grams
def get_ngrams(s,n):
	n_grams = []
	words = s.split()
	for i in range(len(words) - n + 1):
		n_gram = ' '.join([word.lower() for word in words[i:i + n]])
		n_grams.append(n_gram)
	print(n_grams)
	return n_grams
		
# Percentage of ngrams in system.txt that are correct
def ngram_precision(r,s): 	# Calculate n-gram precision
	total = len(s)
	count = 0
	r_counter = collections.Counter(r)

	for ngram in s:
		if ngram in r_counter:
			count += 1
			r_counter[ngram] -= 1
			if r_counter[ngram] == 0:
				del r_counter[ngram]

	p_score = count / total
	return p_score * 100

# Percentage of correct words which occur in output
def ngram_recall(r,s):		#Calculate n-gram recall
	s_ngrams_set = set(s)
	total = len(r)
	count = 0
	for ngram in r:
		if ngram in s:
			count += 1
	r_score = count / total	
	return r_score * 100

ref_ng = get_ngrams(ref, n)
system_ng = get_ngrams(system, n)
p = ngram_precision(ref_ng, system_ng)
r = ngram_recall(ref_ng, system_ng)

print("Precision: " + str(round(p, 2)) + "%")
print("Recall: " + str(round(r, 2)) + "%")
