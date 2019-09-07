/**
 * 
 */
package core;

import java.util.ArrayList;
import java.util.List;

public class Text {
	private int textId;
	private String rawText;
	private String hashedText;
	private List<Sentence> sentences = new ArrayList<Sentence>();
	private List<Sentence> sentencesSorted = new ArrayList<Sentence>();
	private Boolean isPrepared;
	
	Text(int textId, String rawText){
		this.textId = textId;
		this.rawText = rawText;
		isPrepared = false;
	}
	
	public int getTextId() {
		return textId;
	}

	public void setTextId(int textId) {
		this.textId = textId;
	}

	public String getRawText() {
		return rawText;
	}

	public void setRawText(String rawText) {
		this.rawText = rawText;
	}

	public String getHashedText() {
		return hashedText;
	}

	public void setHashedText(String hashedText) {
		this.hashedText = hashedText;
	}

	public List<Sentence> getSentences() {
		return sentences;
	}

	public void setSentences(List<Sentence> sentences) {
		this.sentences = sentences;
	}

	public List<Sentence> getSentencesSorted() {
		return sentencesSorted;
	}

	public void setSentencesSorted(List<Sentence> sentencesSorted) {
		this.sentencesSorted = sentencesSorted;
	}

	public Boolean getIsPrepared() {
		return isPrepared;
	}

	public void setIsPrepared(Boolean isPrepared) {
		this.isPrepared = isPrepared;
	}

	public Sentence getSentenceById(int id) {
		for(int i=0;i<sentences.size();i++) {
			if(sentences.get(i).getSentenceId() == id) return sentences.get(i);
		}
		return null;
	}
	
	
}
