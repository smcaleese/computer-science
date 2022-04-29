import Data.Map
pmap = Prelude.map

twoSum :: Int->[Int]->[(Int, Int)]
twoSum n x = [(i, j) | let indexed = zip x [0..],
                       let indexedmap = fromList indexed,
                       (m,i) <- indexed,
                       member (n - m) indexedmap,
                       let j = indexedmap ! (n - m),
                       i < j]