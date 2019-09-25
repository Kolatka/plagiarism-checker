# About application
This app is an implementation of algorithm designed for my Master's thesis on text data comparison. 

After analyzing existing solutions I decided to rely on Rabin-Karp algorithm and extend it 

according to my own needs. This algorithm is about finding a pattern in text using hashes. For 

longer texts it's ideal.

In current version this app can find similarities with very nice accuracy thanks to very big 

databases of words. First of all it's able to recognize the change in the order of words and 

sentences. But the most important thing is that that it's able to recognize different flexions. In 

polish words can by declined in many ways without changing meaning. And second important feature is 

recognizing of using synonyms. With this 4 aspects this app works perfectly as anti plagiarism.But the biggest challenge was reduction of the number of comparisons. App accomplishes this in many ways like comparing only words with same part of speech.

# How to run
To run the application you need a database containing all existing flexions and collections of 

synonymous. For the first I used polish dictionary - sgjp.pl and for the second I used polish 

thesaurus project. Generally right now its hard to share this app so this is why I am working on 

web version. Desktop version was just developed for tests and to prove this concept.

# To Do
* Making web version of this app
