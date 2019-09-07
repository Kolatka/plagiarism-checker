/**
 * 
 */
package service.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import core.Sentence;
import domain.Synonym;

public class WordDTO implements Comparable<WordDTO>{
	
	private int wordId;
	private int sentenceId;
	private int textId;
	private String word;
	private String originalWord;
	private String type;
	private Integer hash;
	private Set<Synonym> synonyms;

	public WordDTO(int wordId, int sentenceId, int textId, String originalWord, String word, String type, Integer hash, Set<Synonym> synonyms) {
		this.wordId = wordId;
		this.sentenceId = sentenceId;
		this.textId = textId;
		this.originalWord = originalWord;
		this.word = word;
		this.type = type;
		this.hash = hash;
		this.synonyms = synonyms;
	}
	
	public int getWordId() {
		return wordId;
	}

	public int getSentenceId() {
		return sentenceId;
	}

	public void setSentenceId(int sentenceId) {
		this.sentenceId = sentenceId;
	}

	public int getTextId() {
		return textId;
	}

	public void setTextId(int textId) {
		this.textId = textId;
	}

	public void setWordId(int wordId) {
		this.wordId = wordId;
	}

	public String getWord() {
		return word;
	}
	
	public String getOriginalWord() {
		return originalWord;
	}

	public void setOriginalWord(String originalWord) {
		this.originalWord = originalWord;
	}

	public void setWord(String word) {
		this.word = word;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public Integer getHash() {
		return hash;
	}
	
	public void setHash(Integer hash) {
		this.hash = hash;
	}
	
	public Set<Synonym> getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(Set<Synonym> synonyms) {
		this.synonyms = synonyms;
	}

	@Override
	public String toString() {
		String message = "Word: " + this.originalWord +", Id: " + this.wordId + ", Type: " + this.type + ", SynonymsGroups: ";
		if(this.getSynonyms()!=null && this.getSynonyms().size()>0) {
			List<Synonym> synonymsList = new ArrayList<Synonym>(this.getSynonyms());
			for(int i=0;i<synonyms.size();i++) {
				message += synonymsList.get(i) + " ";
			}
		}
		return message;
	}
	
	
	
	@Override
	 public int compareTo(WordDTO other) {
        if (this.word.length() > other.getWord().length()) return 1;
        else if (this.word.length() == other.getWord().length()) return 0;
        else return -1;
    }
	
	
	
}
