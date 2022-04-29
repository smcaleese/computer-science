## Example

### Source
the house
house

### Target
la maison
maison

## Notes
### Initialization
t(e|f) is initially initialized to 1 / target_vocab_size = 1 / 2 = 0.5

### Expectation maximization
1. Normalization:
For the sentence above, the normalization is counted by iterating through every src word for each target word and adding t(e|f) to a total. This is done so that differences in length between the source sentences do not affect the process.

2. Collect counts:
We then loop through the target and source words again and count the number of times a target word is matched with a source word and count the total number of source words encountered.

We then loop through the source and target words and calculate the new values of t(e|f) by dividing each source target pair count by the source counts.

count(target, src) is a probabilistic sum that is proportional to the number of matches between target and source words and is normalized by the length of the target sentence.

total(src) is a probabilistic sum that is proportional to the number of times a source word is associated with any target word.

3. Calculate probabilities:
Dividing count by total yields the new probabilities t(e|f).
