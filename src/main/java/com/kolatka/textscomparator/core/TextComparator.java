package com.kolatka.textscomparator.core;

import com.kolatka.textscomparator.domain.Synonym;
import com.kolatka.textscomparator.service.DataService;
import com.kolatka.textscomparator.service.dto.WordDTO;
import com.kolatka.textscomparator.util.Logger;
import com.kolatka.textscomparator.util.Statistics;

import java.util.ArrayList;
import java.util.List;

public class TextComparator {

	private final DataService dataService;
	private final Logger logger;
	private Parameters parameters;
	private ArrayList<Text> texts;
	private Boolean isSolved;
	private List<Result> results = new ArrayList<>();
	private Result currentResult;
	private Statistics statistics;

	public TextComparator(Parameters parameters) {
		logger = new Logger("TextComparator");
		statistics = new Statistics();
		dataService = new DataService();
		this.parameters = parameters;
		texts = new ArrayList<>();
	}

	public void loadMainTextFromString(String textString) {
		Text firstText = new Text(0, textString);
		texts.add(0, firstText);
		logger.log("Loaded main text");
	}

	public void loadTextFromString(String textString) {
		Text newText = new Text(texts.size(), textString);
		texts.add(newText);
		logger.log("Loaded secondary text");

	}

	public void loadNewParameters(Parameters parameters) {
		Parameters oldParameters = this.parameters;
		this.parameters = parameters;

		if (oldParameters.getIsSortingWords() && !parameters.getIsSortingWords()) {
			for (int i = 0; i < texts.size(); i++) {
				TextPreparator textPreparator = new TextPreparator(texts.get(i), statistics, parameters);
				textPreparator.reverseSortWords();
				textPreparator.sortTypes();
			}
		}

		if (!oldParameters.getIsSortingWords() && parameters.getIsSortingWords()) {
			for (int i = 0; i < texts.size(); i++) {
				TextPreparator textPreparator = new TextPreparator(texts.get(i), statistics, parameters);
				textPreparator.sortWords();
				textPreparator.sortTypes();
			}
		}
		if (oldParameters.getIsSortingSentences() && !parameters.getIsSortingSentences()) {
			for (int i = 0; i < texts.size(); i++) {
				TextPreparator textPreparator = new TextPreparator(texts.get(i), statistics, parameters);
				textPreparator.reverseSortSentences();
				textPreparator.sortTypes();
			}
		}
		if (!oldParameters.getIsSortingSentences() && parameters.getIsSortingSentences()) {
			for (int i = 0; i < texts.size(); i++) {
				TextPreparator textPreparator = new TextPreparator(texts.get(i), statistics, parameters);
				textPreparator.sortSentences();
				textPreparator.sortTypes();
			}
		}
		logger.log("Loaded new parameters");
	}

	public Boolean solve() {
		results = new ArrayList<>();
		if (statistics.getSpecificStat("solve").length() > 0) statistics = new Statistics();
		Long time = 0L;
		for (int i = 1; i < texts.size(); i++) {
			statistics.startTimer("compareTexts0" + i);
			compareTexts(texts.get(0), texts.get(i));
			statistics.stopTimer();
			logger.log("Comparing texts text0 and text" + i + " completed in " + statistics.getLastStat());
			time += statistics.getLastStatLong();
		}
		statistics.addTime("solve", time);
		logger.log("Comparing text with every text completed in " + statistics.getLastStat());
		return true;
	}

	private Boolean compareTexts(Text firstText, Text secondText) {
		logger.log("Compare text with: " + secondText.getTextId());
		statistics.startTimer("compareTexts");
		if (firstText.getIsPrepared() && secondText.getIsPrepared()) {


			for (Sentence sentence : firstText.getSentences()) {
				currentResult = getResult(sentence.getSentenceId());
				for (int j = 0; j < secondText.getSentences().size(); j++) {
					Sentence secondSentence = secondText.getSentences().get(j);
					currentResult.nextSentence();
					if ((double) sentence.getSentenceLength() /
							(double) secondSentence.getSentenceLength() < 2.0
							|| !parameters.getIsSortingSentences()) {
						compareSentences(sentence, secondSentence);
					} else {
						j = secondText.getSentences().size();
					}
				}
			}
			isSolved = true;

			return true;
		} else return false;
	}

	private void compareSentences(Sentence firstSentence, Sentence secondSentence) {
		if (!parameters.getIsUsingWordTypes()) {
			compareWordsLists(firstSentence.getWords(), secondSentence.getWords());
		} else {
			compareWordsLists(firstSentence.getVerbs(), secondSentence.getVerbs());
			compareWordsLists(firstSentence.getNouns(), secondSentence.getNouns());
			compareWordsLists(firstSentence.getAdjectives(), secondSentence.getAdjectives());
			compareWordsLists(firstSentence.getAdverbs(), secondSentence.getAdverbs());
			compareWordsLists(firstSentence.getNumerals(), secondSentence.getNumerals());
			compareWordsLists(firstSentence.getOtherWords(), secondSentence.getOtherWords());
		}
	}

	private void compareWordsLists(List<WordDTO> firstList, List<WordDTO> secondList) {
		for (WordDTO firstWord : firstList) {
			for (int j = 0; j < secondList.size(); j++) {
				WordDTO secondWord = secondList.get(j);
				if ((double) firstWord.getWord().length() / (double) secondWord.getWord().length() <= 1.0
						|| !parameters.getIsSortingWords() || parameters.getIsCheckingSynonyms()) {
					statistics.nextComparison();
					if (compareWords(firstWord, secondWord)) {
						if (!currentResult.checkIfAlreadyFound(secondWord.getWordId())) {
							currentResult.addToResults(secondWord);
							j = secondList.size();
						}
					}
				} else {
					j = secondList.size();
				}
			}
		}
	}

	private Boolean compareWords(WordDTO firstWord, WordDTO secondWord) {
		if (firstWord.getHash().intValue() == secondWord.getHash().intValue()) {
			if (firstWord.getWord().equals(secondWord.getWord())) {
				return true;
			}
		}
		if (parameters.getIsCheckingSynonyms()
				&& firstWord.getSynonyms() != null && secondWord.getSynonyms() != null) {
			return compareSynonyms(firstWord, secondWord);
		}
		return false;
	}

	private Boolean compareSynonyms(WordDTO firstWord, WordDTO secondWord) {
		List<Synonym> firstSynonymsList = new ArrayList<>(firstWord.getSynonyms());
		List<Synonym> secondSynonymsList = new ArrayList<>(secondWord.getSynonyms());
		for (int i = 0; i < firstSynonymsList.size(); i++) {
			Synonym firstSynonym = firstSynonymsList.get(i);
			for (int j = 0; j < secondSynonymsList.size(); j++) {
				Synonym secondSynonym = secondSynonymsList.get(j);
				if (firstSynonym.getSynonymGroupId().intValue() ==
						secondSynonym.getSynonymGroupId().intValue()) {
					return true;
				}
			}
		}
		return false;
	}

	public Boolean prepareText(int textId) {
		Double time = 0.0;
		logger.log("Preparing text");
		TextPreparator textPreparator = new TextPreparator(texts.get(textId),
				statistics, parameters);
		if (textPreparator.prepareText())
			texts.get(textId).setIsPrepared(true);

		return true;
	}

	private Result getResult(int sentenceId) {
		Result result;
		int id = checkIfResultExists(sentenceId);
		if (id == -1) {
			result = new Result(sentenceId);
			results.add(result);
		} else {
			result = results.get(id);
		}
		return result;
	}

	private int checkIfResultExists(int sentenceId) {
		for (int i = 0; i < results.size(); i++) {
			if (results.get(i).getSentenceId() == sentenceId) return i;
		}
		return -1;
	}

	public void printResults(Double min, Double max) {
		System.out.println(getSimilarSentences(min, max));
		System.out.println(getStatistics());
		System.out.println(getSentencesStats(min, max));
	}

	public String getStatistics() {
		String message = "";
		int otherTextsWordsCount = 0, otherTextsSentencesCount = 0, firstTextWordsCount = 0, firstTextSentencesCount = 0, textsCount = 0;
		if (texts.size() > 0) textsCount = texts.size() - 1;
		for (int i = 0; i < texts.size(); i++) {
			Text text = texts.get(i);
			if (i > 0) otherTextsSentencesCount += text.getSentences().size();
			else firstTextSentencesCount = texts.get(0).getSentences().size();
			for (int j = 0; j < text.getSentences().size(); j++) {
				Sentence sentence = text.getSentences().get(j);
				if (i > 0) otherTextsWordsCount += sentence.getWords().size();
				else firstTextWordsCount += sentence.getWords().size();
			}
		}

		message += "--------------------------------------------------------------" + "\n";
		message += "First text words count: " + (firstTextWordsCount + 1) + "\n";
		message += "First text sentences count: " + firstTextSentencesCount + "\n";
		message += "--------------------------------------------------------------" + "\n";
		message += "Total texts count: " + textsCount + "\n";
		message += "Other texts words count: " + otherTextsWordsCount + "\n";
		message += "Other texts sentences count: " + otherTextsSentencesCount + "\n";
		message += "--------------------------------------------------------------" + "\n";
		message += "Total comparisons: " + statistics.getComparison() + "\n";
		message += "--------------------------------------------------------------" + "\n";
		message += "Total preparing time: " + statistics.getSpecificStat("prepareText0") + "\n";
		message += "Total comparing time: " + statistics.getSpecificStat("solve") + "\n";
		message += "--------------------------------------------------------------" + "\n";

		return message;
	}

	public String getSentencesStats(double min, double max) {
		int similarWordsCount = 0, similarSentencesCount = 0, firstTextWordsCount = 0, firstTextSentencesCount;
		String message = "";
		firstTextSentencesCount = texts.get(0).getSentences().size();
		for (int j = 0; j < texts.get(0).getSentences().size(); j++) {
			Sentence sentence = texts.get(0).getSentences().get(j);
			firstTextWordsCount += sentence.getWords().size();
		}
		if (results.size() > 0) {
			for (int i = 0; i < results.size(); i++) {
				Result result = results.get(i);
				if (result.getHighestSimilarityList() != null && result.getHighestSimilarityList().size() > 0) {
					if ((double) result.getHighestSimilarityList().size() / (double) texts.get(0).getSentences().get(i).getWords().size() > min &&
							(double) result.getHighestSimilarityList().size() / (double) texts.get(0).getSentences().get(i).getWords().size() <= max) {
						similarWordsCount += result.getHighestSimilarityList().size();
						similarSentencesCount++;
					}
				}
			}
		}
		message += "Probability from " + (min + 0.01) * 100 + "% to " + max * 100 + "%" + "\n";
		message += "Found similar words: " + similarWordsCount + "/" + firstTextWordsCount + "\n";
		message += "Found similar sentences:: " + similarSentencesCount + "/" + firstTextSentencesCount + "\n";
		message += "--------------------------------------------------------------" + "\n";
		return message;
	}


	public String getSimilarSentences(double min, double max) {
		StringBuilder message = new StringBuilder();
		if (results.size() > 0) {
			for (int i = 0; i < results.size(); i++) {
				Result result = results.get(i);
				if (result.getHighestSimilarityList() != null && result.getHighestSimilarityList().size() > 0) {
					int secondTextId = result.getHighestSimilarityList().get(0).getTextId();
					int secondTextSentenceId = result.getHighestSimilarityList().get(0).getSentenceId();
					if ((double) result.getHighestSimilarityList().size() / (double) texts.get(0).getSentences().get(i).getWords().size() > min &&
							(double) result.getHighestSimilarityList().size() / (double) texts.get(0).getSentences().get(i).getWords().size() <= max) {
						message.append("Sentence: ").append(result.getSentenceId()).append(" - Podobienstwo: ").append(result.getHighestSimilarityList().size()).append("/").append(texts.get(0).getSentences().get(i).getWords().size()).append(" (Wzorzec: ").append(secondTextId).append(", Zdanie: ").append(secondTextSentenceId).append(")\n");
						message.append("Examined text: ").append(texts.get(0).getSentenceById(result.getSentenceId())).append("\n");
						message.append("Second text: ").append(texts.get(secondTextId).getSentenceById(secondTextSentenceId)).append("\n");
						message.append("Similar words: ").append(result.getSimilarWordsList(result.getHighestSimilarityId())).append("\n");
						message.append("--------------------------------------------------------------" + "\n");
					}
				}
			}
		}
		return message.toString();
	}


	public ArrayList<Text> getTexts() {
		return texts;
	}

	public void setTexts(ArrayList<Text> texts) {
		this.texts = texts;
	}

}
	

