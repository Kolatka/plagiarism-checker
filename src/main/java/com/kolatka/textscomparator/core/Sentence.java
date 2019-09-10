package com.kolatka.textscomparator.core;

import com.kolatka.textscomparator.service.dto.WordDTO;

import java.util.ArrayList;
import java.util.List;

public class Sentence implements Comparable<Sentence> {

	private int sentenceId;
	private String rawSentence;

	private List<WordDTO> words = new ArrayList<>();
	private List<WordDTO> wordsSorted = new ArrayList<>();
	private List<WordDTO> verbs = new ArrayList<>();
	private List<WordDTO> nouns = new ArrayList<>();
	private List<WordDTO> adjectives = new ArrayList<>();
	private List<WordDTO> adverbs = new ArrayList<>();
	private List<WordDTO> numerals = new ArrayList<>();
	private List<WordDTO> otherWords = new ArrayList<>();

	public Sentence(int sentenceId) {
		this.sentenceId = sentenceId;
	}

	public int getSentenceId() {
		return sentenceId;
	}

	public void setSentenceId(int sentenceId) {
		this.sentenceId = sentenceId;
	}

	public String getRawSentence() {
		return rawSentence;
	}

	public void setRawSentence(String rawSentence) {
		this.rawSentence = rawSentence;
	}

	public List<WordDTO> getWords() {
		return words;
	}

	public void setWords(List<WordDTO> words) {
		this.words = words;
	}

	public List<WordDTO> getWordsSorted() {
		return wordsSorted;
	}

	public void setWordsSorted(List<WordDTO> wordsSorted) {
		this.wordsSorted = wordsSorted;
	}

	public List<WordDTO> getVerbs() {
		return verbs;
	}

	public void setVerbs(List<WordDTO> verbs) {
		this.verbs = verbs;
	}

	public List<WordDTO> getNouns() {
		return nouns;
	}

	public void setNouns(List<WordDTO> nouns) {
		this.nouns = nouns;
	}

	public List<WordDTO> getAdjectives() {
		return adjectives;
	}

	public void setAdjectives(List<WordDTO> adjectives) {
		this.adjectives = adjectives;
	}

	public List<WordDTO> getAdverbs() {
		return adverbs;
	}

	public void setAdverbs(List<WordDTO> adverbs) {
		this.adverbs = adverbs;
	}

	public List<WordDTO> getNumerals() {
		return numerals;
	}

	public void setNumerals(List<WordDTO> numerals) {
		this.numerals = numerals;
	}

	public List<WordDTO> getOtherWords() {
		return otherWords;
	}

	public void setOtherWords(List<WordDTO> otherWords) {
		this.otherWords = otherWords;
	}

	public int getSentenceLength() {
		return words.size();
	}

	public void printWords() {
		for (int i = 0; i < words.size(); i++) {
			System.out.println(words.get(i).getWord() + " " + words.get(i).getWordId() + " " + words.get(i).getType());
		}

	}

	@Override
	public String toString() {
		return rawSentence;
	}

	@Override
	public int compareTo(Sentence other) {
		if (this.getSentenceLength() > other.getSentenceLength()) return 1;
		else if (this.getSentenceLength() == other.getSentenceLength()) return 0;
		else return -1;
	}


}
