package core;

import java.util.ArrayList;
import java.util.List;

import domain.Synonym;
import service.DataService;
import service.dto.WordDTO;
import util.Logger;
import util.Statistics;

public class TextComparator {

	private Parameters parameters;
	private DataService dataService;
	private ArrayList<Text> texts;
	private Boolean isSolved;
	private List<Result> results = new ArrayList<Result>();
	private Result currentResult;
	private Logger logger;
	private Statistics statistics;
	
	public TextComparator(Parameters parameters) {
		logger = new Logger("TextComparator");
		statistics = new Statistics();
		dataService = new DataService();
		this.parameters = parameters;
		texts = new ArrayList<Text>();
	}
	
	public void loadMainTextFromString(String textString) {
		Text firstText = new Text(0, textString);
		texts.add(0, firstText);
		logger.log("Loaded main text");
	}
	
	public void loadTextFromString(String textString){
		Text newText = new Text(texts.size(), textString);
		texts.add(newText);
		logger.log("Loaded secondary text");

	}

	public void loadNewParameters(Parameters parameters){
		Parameters oldParameters = this.parameters;
		this.parameters = parameters;
		
		if(oldParameters.getIsSortingWords()==true && parameters.getIsSortingWords() == false) {
			for(int i=0;i<texts.size();i++) {
				TextPreparator textPreparator = new TextPreparator(texts.get(i), statistics, parameters);
				textPreparator.reverseSortWords();
				textPreparator.sortTypes();
			}
		}

		if(oldParameters.getIsSortingWords()==false && parameters.getIsSortingWords() == true) {
			for(int i=0;i<texts.size();i++) {
				TextPreparator textPreparator = new TextPreparator(texts.get(i), statistics, parameters);
				textPreparator.sortWords();
				textPreparator.sortTypes();
			}
		}
		if(oldParameters.getIsSortingSentences()==true && parameters.getIsSortingSentences() == false) {
			for(int i=0;i<texts.size();i++) {
				TextPreparator textPreparator = new TextPreparator(texts.get(i), statistics, parameters);
				textPreparator.reverseSortSentences();
				textPreparator.sortTypes();
			}
		}
		if(oldParameters.getIsSortingSentences()==false && parameters.getIsSortingSentences() == true) {
			for(int i=0;i<texts.size();i++) {
				TextPreparator textPreparator = new TextPreparator(texts.get(i), statistics, parameters);
				textPreparator.sortSentences();
				textPreparator.sortTypes();
			}
		}
		logger.log("Loaded new parameters");
	}
	
	public Boolean solve() {
		results = new ArrayList<Result>();
		if(statistics.getSpecificStat("solve").length()>0)statistics = new Statistics();
		Long time = 0L;
		for(int i=1;i<texts.size();i++) {
			statistics.startTimer("compareTexts0" + i);
			compareTexts(texts.get(0), texts.get(i));
			statistics.stopTimer();
			logger.log("Comparing texts text0 and text" + i + " completed in " +  statistics.getLastStat());
			time += statistics.getLastStatLong();
		}
		statistics.addTime("solve", time);
		logger.log("Comparing text with every text completed in " + statistics.getLastStat());
		return true;
	}
	
	public Boolean compareTexts(Text firstText, Text secondText){
		logger.log("Compare text with: " + secondText.getTextId());
		statistics.startTimer("compareTexts");
		if(firstText.getIsPrepared() && secondText.getIsPrepared()) {
			
			
			for(Sentence sentence : firstText.getSentences()) {
				currentResult = getResult(sentence.getSentenceId());
				for(int j=0; j<secondText.getSentences().size();j++) {
					Sentence secondSentence = secondText.getSentences().get(j);
					currentResult.nextSentence();
					if(Double.valueOf(sentence.getSentenceLength())/
							Double.valueOf(secondSentence.getSentenceLength())<2.0
							|| !parameters.getIsSortingSentences()){
						compareSentences(sentence, secondSentence);
					}else {
						j = secondText.getSentences().size();
					}
				}		
			}
			isSolved = true;
			
			return true;
		}else return false;	
	}
	
	private void compareSentences(Sentence firstSentence, Sentence secondSentence) {
		if(!parameters.getIsUsingWordTypes()) {
			compareWordsLists(firstSentence.getWords(), secondSentence.getWords());
		}else {
			compareWordsLists(firstSentence.getVerbs(), secondSentence.getVerbs());
			compareWordsLists(firstSentence.getNouns(), secondSentence.getNouns());
			compareWordsLists(firstSentence.getAdjectives(), secondSentence.getAdjectives());
			compareWordsLists(firstSentence.getAdverbs(), secondSentence.getAdverbs());
			compareWordsLists(firstSentence.getNumerals(), secondSentence.getNumerals());
			compareWordsLists(firstSentence.getOtherWords(), secondSentence.getOtherWords());
		}
	}
	
	private void compareWordsLists(List<WordDTO> firstList, List<WordDTO> secondList) {
		for(WordDTO firstWord : firstList) {
			for(int j = 0; j<secondList.size();j++) {
				WordDTO secondWord = secondList.get(j);
				if(Double.valueOf(firstWord.getWord().length())/Double.valueOf(secondWord.getWord().length())<=1.0 
						|| !parameters.getIsSortingWords() || parameters.getIsCheckingSynonyms()){
					statistics.nextComparison();
					if(compareWords(firstWord,secondWord)) {
						if(!currentResult.checkIfAlreadyFound(secondWord.getWordId())) {
							currentResult.addToResults(secondWord);
							j = secondList.size();
						}
					}	
				}else {
					j = secondList.size();
				}
			}
		}
	}
	
	private Boolean compareWords(WordDTO firstWord, WordDTO secondWord){
		if(firstWord.getHash().intValue() == secondWord.getHash().intValue()) {
			if(firstWord.getWord().equals(secondWord.getWord())) {
				return true;
			}
		}
		if(parameters.getIsCheckingSynonyms() 
				&& firstWord.getSynonyms()!=null && secondWord.getSynonyms()!=null ) {
			if(compareSynonyms(firstWord,secondWord)) {
				return true;
			}
		}
		return false;
	}
	
	private Boolean compareSynonyms(WordDTO firstWord, WordDTO secondWord) {
		List<Synonym> firstSynonymsList = new ArrayList<Synonym>(firstWord.getSynonyms());
		List<Synonym> secondSynonymsList = new ArrayList<Synonym>(secondWord.getSynonyms());
		for(int i=0;i<firstSynonymsList.size();i++) {
			Synonym firstSynonym = firstSynonymsList.get(i);
			for(int j=0;j<secondSynonymsList.size();j++) {
				Synonym secondSynonym = secondSynonymsList.get(j);
				if(firstSynonym.getSynonymGroupId().intValue() == 
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
		if(textPreparator.prepareText()) 
			texts.get(textId).setIsPrepared(true);	
		
		return true;
	}

	private Result getResult(int sentenceId) {
		Result result;
		int id = checkIfResultExists(sentenceId);
		if(id==-1) {
			result = new Result(sentenceId);
			results.add(result);
		}else {
			result = results.get(id);
		}
		return result;
	}
	
	private int checkIfResultExists(int sentenceId) {
		for(int i=0;i<results.size();i++) {
			if(results.get(i).getSentenceId()==sentenceId) return i;
		}
		return -1;
	}
	
	public void printResults(Double min, Double max) {
		System.out.println(getSimilarSentences(min,max));
		System.out.println(getStatistics());
		System.out.println(getSentencesStats(min,max));
	}
	
	public String getStatistics() {
		String message = "";
		int otherTextsWordsCount = 0, otherTextsSentencesCount = 0, firstTextWordsCount = 0, firstTextSentencesCount = 0, textsCount = 0;
		if(texts.size()>0) textsCount = texts.size()-1;
		for(int i=0;i<texts.size();i++) {
			Text text = texts.get(i);
			if(i>0)otherTextsSentencesCount += text.getSentences().size();
			else firstTextSentencesCount = texts.get(0).getSentences().size();
			for(int j=0;j<text.getSentences().size();j++) {
				Sentence sentence = text.getSentences().get(j);
				if(i>0)otherTextsWordsCount += sentence.getWords().size();
				else firstTextWordsCount += sentence.getWords().size();
			}
		}

		message += "--------------------------------------------------------------"+ "\n";
		message +="Liczba słów badanego tekstu: " + (firstTextWordsCount+1) + "\n";
		message +="Liczba zdań badanego tekstu: " + firstTextSentencesCount + "\n";
		message += "--------------------------------------------------------------"+ "\n";
		message +="Liczba tekstów: " + textsCount + "\n";
		message +="Liczba słów pozostałych tekstów: " + otherTextsWordsCount + "\n";
		message +="Liczba zdań pozostałych tekstów: " + otherTextsSentencesCount + "\n";
		message += "--------------------------------------------------------------"+ "\n";
		message +="Liczba porównań: " + statistics.getComparison()+ "\n";
		message +="--------------------------------------------------------------"+ "\n";
		message +="Czas trwania przygotowywania badanego tekstu: " + statistics.getSpecificStat("prepareText0")+ "\n";
		message +="Czas badania podobieństwa tekstów " + statistics.getSpecificStat("solve")+ "\n";
		message +="--------------------------------------------------------------"+ "\n";
		
		return message;
	}
	
	public String getSentencesStats(double min, double max) {
		int similarWordsCount = 0, similarSentencesCount = 0,firstTextWordsCount = 0, firstTextSentencesCount = 0;
		String message = "";
		firstTextSentencesCount = texts.get(0).getSentences().size();
		for(int j=0;j<texts.get(0).getSentences().size();j++) {
			Sentence sentence = texts.get(0).getSentences().get(j);
			firstTextWordsCount += sentence.getWords().size();
		}
		if(results.size()>0) {
			for(int i=0;i<results.size();i++) {
				Result result = results.get(i);
				if(result.getHighestSimilarityList()!=null && result.getHighestSimilarityList().size()>0) {
					if(Double.valueOf(result.getHighestSimilarityList().size())/Double.valueOf(texts.get(0).getSentences().get(i).getWords().size())>min &&
							Double.valueOf(result.getHighestSimilarityList().size())/Double.valueOf(texts.get(0).getSentences().get(i).getWords().size())<=max) {
						similarWordsCount +=  result.getHighestSimilarityList().size();
						similarSentencesCount++;
					}
				}
			}
		}
		message +="Akceptowalne podobieństwo: od " + (min+0.01)*100 +"% do " + max*100 + "%" + "\n";
		message +="Znalezieno podobnych słów: " + similarWordsCount + "/" + firstTextWordsCount + "\n";
		message +="Znalezieno podobnych zdań: " + similarSentencesCount + "/" + firstTextSentencesCount + "\n";
		message += "--------------------------------------------------------------"+ "\n";
		return message;
	}
	
	
	
	public String getSimilarSentences(double min, double max) {
		String message = "";
		if(results.size()>0) {
			for(int i=0;i<results.size();i++) {
				Result result = results.get(i);
				if(result.getHighestSimilarityList()!=null && result.getHighestSimilarityList().size()>0) {
					int secondTextId = result.getHighestSimilarityList().get(0).getTextId();
					int secondTextSentenceId = result.getHighestSimilarityList().get(0).getSentenceId();
					if(Double.valueOf(result.getHighestSimilarityList().size())/Double.valueOf(texts.get(0).getSentences().get(i).getWords().size())>min &&
							Double.valueOf(result.getHighestSimilarityList().size())/Double.valueOf(texts.get(0).getSentences().get(i).getWords().size())<=max) {
						message += "Zdanie: " + result.getSentenceId() +  " - Podobienstwo: " 
						+ result.getHighestSimilarityList().size() + "/" + texts.get(0).getSentences().get(i).getWords().size() +
						 " (Wzorzec: " + secondTextId + ", Zdanie: " + secondTextSentenceId +")\n";
						message += "Badany tekst: " + texts.get(0).getSentenceById(result.getSentenceId())+ "\n";
						message += "Wzorzec: " + texts.get(secondTextId).getSentenceById(secondTextSentenceId)+ "\n";
						message += "Podobne słowa: " + result.getSimilarWordsList(result.getHighestSimilarityId())+ "\n";
						message += "--------------------------------------------------------------"+ "\n";
					}
				}
			}
		}
		return message;
	}
	
	
	public ArrayList<Text> getTexts() {
		return texts;
	}
	public void setTexts(ArrayList<Text> texts) {
		this.texts = texts;
	}
	
}
	

