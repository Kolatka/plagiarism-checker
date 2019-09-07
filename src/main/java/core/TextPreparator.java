/**
 * 
 */
package core;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import domain.Acronym;
import domain.Word;
import service.DataService;
import service.SessionService;
import service.dto.WordDTO;
import util.Logger;
import util.Statistics;

public class TextPreparator {

	private Text textToPrepare;
	private Logger logger;
	private Statistics statistics;
	private DataService dataService = new DataService();
	private String[] words;
	private Parameters parameters;
	private SessionService ss;
	
	public TextPreparator(Text textToPrepare, Statistics statistics, Parameters parameters){
		logger = new Logger("TextPreparator");
		this.statistics = statistics;
		this.textToPrepare = textToPrepare;
		this.parameters = parameters;
		ss = new SessionService();
	}
	
	
	public Boolean prepareText() {
		Long time = 0L;
		splitText();
		if(parameters.getIsUsingFlexions() || parameters.getIsUsingWordTypes() || parameters.getIsCheckingSynonyms()) {
			identifyWords(true);
			time += statistics.getLastStatLong();
		}else {
			identifyWords(false);
			time += statistics.getLastStatLong();
		}
		if(parameters.getIsSortingSentences()) {
			sortSentences();
			time += statistics.getLastStatLong();
		}
		if(parameters.getIsSortingWords()) {
			sortWords();
			time += statistics.getLastStatLong();
		}
		if(parameters.getIsUsingWordTypes()) {
			sortTypes();
			time += statistics.getLastStatLong();
		}	
		logger.log("Preparing text completed in " + time/1000000 + " ms");
		statistics.addTime("prepareText" + textToPrepare.getTextId(), time);
		return true;
	}
	
	
	
	public Boolean splitText() {
		statistics.startTimer("splitText");
		logger.log("Splitting text");

		String textString =  textToPrepare.getRawText().replaceAll("\\[|\\]|\\)|\\(|\\}|\\{|\\,|\\<|\\>|\\:|\\;|\\-|\\”|\\„", " ");
		textString =  textString.replaceAll("\\.", "\\. ");
		textString =  textString.replaceAll("  ", " ");
		words = textString.split("\\s+");

		statistics.stopTimer();
		logger.log("Splitting text completed in " +  statistics.getLastStat() + " ms");
		logger.log("Found: " + words.length +  " words.");
		return true;
	}
	
	
	public Boolean identifyWords(Boolean isUsingDB) {
		logger.log("Identifying words");
		statistics.startTimer("identifyWords");
		if (isUsingDB) ss.startSession();
		
		int sentenceId=0;
		Sentence sentence = new Sentence(sentenceId);
		String rawSentence ="";
		for(int i=0;i<words.length;i++) {
			if(!words[i].equals(" ") && words[i] !=null && !words[i].equals("") && 
					(words[i].matches("^[\\p{IsAlphabetic}\\d]*$") || words[i].endsWith("."))) {
				rawSentence += words[i] +" ";
				if(words[i].endsWith(".")){
					words[i] = words[i].replaceAll("\\.", "");
					if(isUsingDB && dataService.checkIfAcronym(words[i], ss.getSession())!=null){
						addAcronymToSentence(words[i], sentenceId, i, sentence);
					}else {
						addWordToSentence(words[i], sentenceId, i, sentence, isUsingDB, 
								parameters.getIsUsingFlexions());
						sentence.setRawSentence(rawSentence);
						textToPrepare.getSentences().add(sentence);
						textToPrepare.getSentencesSorted().add(sentence);
						sentenceId++;
						sentence = new Sentence(sentenceId);
						rawSentence = "";
					}
				}else {
					addWordToSentence(words[i], sentenceId, i, sentence, isUsingDB, parameters.getIsUsingFlexions());
				}
			}
		}
		if(sentence.getWords().size()>0) {
			sentence.setRawSentence(rawSentence);
			textToPrepare.getSentences().add(sentence);
		}
		statistics.stopTimer();
		logger.log("Identifying words text completed in " +  statistics.getLastStat() + " ms");
		logger.log("Found: " + (sentenceId) +  " sentences.");
		return true;
	}
	

	private void addWordToSentence(String wordString, int sentenceId, int wordId, 
			Sentence sentence, Boolean isUsingDB, Boolean isUsingFlexions) {
		Word newWord;
		if(isUsingDB) {
			if(isUsingFlexions) newWord = dataService.findFlexion(wordString, ss.getSession());
			else newWord = dataService.findWord(wordString, ss.getSession());
		} else {
			newWord = dataService.createNewWord(wordString, "other");
		}
		WordDTO word = new WordDTO(wordId, sentenceId, textToPrepare.getTextId(), wordString, 
				newWord.getWord(), newWord.getType(), newWord.getHash(), newWord.getSynonyms());
		sentence.getWords().add(word);
		sentence.getWordsSorted().add(word);
	}
	
	private void addAcronymToSentence(String wordString, int sentenceId, int wordId, Sentence sentence) {
		Acronym acronym = dataService.checkIfAcronym(wordString, ss.getSession());
		WordDTO word = new WordDTO(wordId, sentenceId, textToPrepare.getTextId(), acronym.getAcronym(), acronym.getAcronym(), "acronym", acronym.getHash(), null);
		sentence.getWords().add(word);
	}
	
	public Boolean sortWords() {
		logger.log("Sorting words");
		statistics.startTimer("sortWords");
		for(int i=0;i<textToPrepare.getSentences().size();i++) {
			Collections.sort(textToPrepare.getSentences().get(i).getWords(),
					Collections.reverseOrder());
		}
		statistics.stopTimer();
		logger.log("Identifying words completed in " +  statistics.getLastStat() + " ms");
		return true;
	}
	
	public Boolean sortSentences() {
		logger.log("Sorting sentences");
		statistics.startTimer("sortSentences");
		Collections.sort(textToPrepare.getSentences(), Collections.reverseOrder());
		statistics.stopTimer();
		logger.log("Sorting sentences completed in " +  statistics.getLastStat() + " ms");
		return true;
	}
	
	public Boolean reverseSortWords() {
		for(int i=0;i<textToPrepare.getSentences().size();i++) {
			Sentence sentence = textToPrepare.getSentences().get(i);
			List<WordDTO> newList = new ArrayList<WordDTO>(Collections.nCopies(sentence.getWords().size()+1, null));
			for(int j=0;j<sentence.getWords().size();j++) {
				WordDTO word = sentence.getWords().get(j);
				newList.set(word.getWordId(), word);
			}
			sentence.setWords(newList);
		}
		return true;
	}
	
	public Boolean reverseSortSentences() {
		List<Sentence> newList = new ArrayList<Sentence>(Collections.nCopies(textToPrepare.getSentences().size()+1, null));
		for(int i=0;i<textToPrepare.getSentences().size();i++) {
			Sentence sentence = textToPrepare.getSentences().get(i);
			newList.set(sentence.getSentenceId(), sentence);
		}
		textToPrepare.setSentences(newList);
		return true;
	}
	
	public Boolean sortTypes() {
		logger.log("Sorting word types");
		statistics.startTimer("sortTypes");
		for(int i=0;i<textToPrepare.getSentences().size();i++) {
			Sentence sentence = textToPrepare.getSentences().get(i);
			if(sentence!=null) {
				for(int j=0;j<textToPrepare.getSentences().get(i).getWords().size();j++) {
					WordDTO word = sentence.getWords().get(j);
					String type = word.getType();
					if(type.equals("verb")) {
						sentence.getVerbs().add(word);
					}else if(type.equals("noun")) {
						sentence.getNouns().add(word);
					}else if(type.equals("adjective")) {
						sentence.getAdjectives().add(word);
					}else if(type.equals("adverb")) {
						sentence.getAdverbs().add(word);
					}else if(type.equals("numeral")) {
						sentence.getNumerals().add(word);
					}else{
						sentence.getOtherWords().add(word);
					}
				}
			}
		}
		statistics.stopTimer();
		logger.log("Sorting types completed in " +  statistics.getLastStat() + " ms");
		return true;
	}
	

	
	
	
	public Text getText() {
		return textToPrepare;
	}
	
	
	
	
}
