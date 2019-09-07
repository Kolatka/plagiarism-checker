package core;

public class Parameters {
	private Boolean isSortingWords;
	private Boolean isSortingSentences;
	private Boolean isUsingFlexions;
	private Boolean isUsingWordTypes;
	private Boolean isCheckingSynonyms;
	
	public Parameters(Boolean isSortingWords, Boolean isSortingSentences, Boolean isUsingFlexions,
			Boolean isUsingWordTypes, Boolean isCheckingSynonyms) {
		this.isSortingWords = isSortingWords;
		this.isSortingSentences = isSortingSentences;
		this.isUsingFlexions = isUsingFlexions;
		this.isUsingWordTypes = isUsingWordTypes;
		this.isCheckingSynonyms = isCheckingSynonyms;
	}
	
	public Boolean getIsSortingWords() {
		return isSortingWords;
	}
	public void setIsSortingWords(Boolean isSortingWords) {
		this.isSortingWords = isSortingWords;
	}
	public Boolean getIsSortingSentences() {
		return isSortingSentences;
	}
	public void setIsSortingSentences(Boolean isSortingSentences) {
		this.isSortingSentences = isSortingSentences;
	}
	public Boolean getIsUsingFlexions() {
		return isUsingFlexions;
	}
	public void setIsUsingFlexions(Boolean isUsingFlexions) {
		this.isUsingFlexions = isUsingFlexions;
	}
	public Boolean getIsUsingWordTypes() {
		return isUsingWordTypes;
	}
	public void setIsUsingWordTypes(Boolean isUsingWordTypes) {
		this.isUsingWordTypes = isUsingWordTypes;
	}
	public Boolean getIsCheckingSynonyms() {
		return isCheckingSynonyms;
	}
	public void setIsCheckingSynonyms(Boolean isCheckingSynonyms) {
		this.isCheckingSynonyms = isCheckingSynonyms;
	}
	
}
