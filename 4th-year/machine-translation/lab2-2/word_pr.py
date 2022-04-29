#!/usr/bin/python

import sys
import collections

if (len(sys.argv) != 3):
	sys.stderr.write("Usage: word_pr.py reference.txt system.txt\n")
	sys.exit(1)

ref_f = sys.argv[1]
system_f = sys.argv[2]

# Read reference sentence from file
with open(ref_f) as f1:
	ref = f1.readline()

# Read system translated sentence from file
with open(system_f) as f2:
	system = f2.readline()

# Function that calculates the word PRECISION and returns it as a percentage
# Precision: percentage of words in output that are correct
def w_precision(r,s):
	s_count = 0
	s_total = len(s.split())
	r_words_lower = [word.lower() for word in r.split()]
	r_counter = collections.Counter(r_words_lower)

	for word in s.split():
		word = word.lower()
		if word in r_counter:
			s_count += 1
			r_counter[word] -= 1
			if r_counter[word] == 0:
				del r_counter[word]

	p_score = s_count / s_total 
	return p_score * 100

# Function that calculates the word RECALL and returns it as a percentage
# Percentage of correct words (words in ref.txt) which occur in system.txt
def w_recall(r,s):
	s_count = 0		
	r_total = len(r.split())
	s_set = set([word.lower() for word in s.split()])

	for word in r.split():
		word = word.lower()
		if word in s_set:
			s_count += 1

	r_score = s_count / r_total
	return r_score * 100

p = w_precision(ref, system)
r = w_recall(ref, system)

print("Precision: " + str(round(p, 2)) + "%")
print("Recall: " + str(round(r, 2)) + "%")
