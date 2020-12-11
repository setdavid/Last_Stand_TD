Last Stand Tower Defense
David Setiawan

Changes from proposal:


Collections:
LinkedList to model path to allow for traversal step by step and order matters
TreeSet to model the target of towers. Towers will always target the enemy that has made the most progress (aka the one that is closer to homebase). Hence, each tower will need to look at the enemies within its range and choose the one that is closest to homebase. Used a TreeSet and implemented Comparable to Enemy class to allow the TreeSet to put first the enemy that is closer to the exit. 
HashSet because in Javadocs, it is supposedly the fastest. Whenever I wanted to use a normal set where order didn't matter, I used the HashSet. 