/**
 * 
 */
package core;

import java.util.ArrayList;
import java.util.List;
import service.dto.WordDTO;

public class Result {

	private int sentenceId;
	private List<List<WordDTO>> similarWords;
	private int currentSentence;
	
	public Result(int sentenceId){
		this.sentenceId = sentenceId;
		currentSentence = -1;
		similarWords = new ArrayList<List<WordDTO>>();
	}

	public void addToResults(WordDTO wordDTO) {
		similarWords.get(currentSentence).add(wordDTO);
	}
	
	public void nextSentence() {
		currentSentence++;
		similarWords.add(new ArrayList<WordDTO>());
	}
	
	public int getHighestSimilarityId() {
		int maxValue = 0;
		int maxId = -1;
		for(int i = 0; i < similarWords.size(); i++) {
			if(maxValue<similarWords.get(i).size()) {
				maxValue = similarWords.get(i).size();
				maxId = i;
			}	
		}	
		return maxId;
	}
	
	public List<WordDTO> getHighestSimilarityList() {
		if(getHighestSimilarityId()>=0)
			return similarWords.get(getHighestSimilarityId());
		else return null;
	}
	
	
	public String getSimilarWordsList(int sentenceId) {
		String message = "";
		for(int i = 0; i < similarWords.get(sentenceId).size(); i++) {
				message += similarWords.get(sentenceId).get(i).getOriginalWord();
				if(i<similarWords.get(sentenceId).size()-1) {
					message += ", ";
				}else message += ".";
		}
		return message;
	}
	
	public Boolean checkIfAlreadyFound(int wordId){
		for(int i = 0; i < similarWords.get(currentSentence).size(); i++) {
			if(similarWords.get(currentSentence).get(i).getWordId()==wordId) {	
				return true;
			}
		}
		return false;
	}

	public void test() {
		for(int i=0;i<similarWords.size();i++) {
			for(int j=0;j<similarWords.get(i).size();j++) {
				System.out.print(similarWords.get(i).get(j).getOriginalWord() + similarWords.get(i).get(j).getSentenceId()+ " ");
			}
			System.out.println("------------");
			
			System.out.println("------------");
		}
	}
	
	
	public int getSentenceId() {
		return sentenceId;
	}

	public void setSentenceId(int sentenceId) {
		this.sentenceId = sentenceId;
	}
	
	
	
}

