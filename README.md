# About application
This app is an implementation of algorithm designed for my Master's thesis on data text comparison. After analyzing existing solutions I decided to rely on Rabin-Karp algorithm and extend it according to my own needs. This algorithm finds a pattern in text using hashes so for longer texts it will reduce execution time significantly. In current version this app can find similarities with very nice accuracy thanks to very big databases of words. First of all it's able to recognize the change in the order of words and sentences. But the most important feature is this that app is able to recognize different flexions. In polish, words can by declined in many ways without changing meaning. And second important feature is recognizing of using synonyms. With this 4 aspects this app works perfectly as anti plagiarism. But the biggest challenge was reduction of the number of comparisons. App accomplishes this in many ways like comparing only words with same part of speech.

# How to run
To run this application you need a database of all existing flexions and also collections of synonyms. For the first I used polish dictionary - sgjp.pl and for the second I used polish thesaurus project. Generally right now its hard to share this app so this is why I am working on a web version. Desktop version was only developed for tests and to prove this concept.

# To Do
* Implement web version of this app
* Create a better README.md 
