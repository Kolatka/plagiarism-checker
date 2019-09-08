package service;

import core.Parameters;
import core.TextComparator;

class SolverService {

	private Parameters parameters;
	private TextComparator textComparator;

	public SolverService() {
		parameters = new Parameters(false, false, false, false, false);
		textComparator = new TextComparator(parameters);
	}

	private void resetService() {
		textComparator = new TextComparator(parameters);
	}

	public void loadParameters(Boolean isSortingWords, Boolean isSortingSentences, Boolean isUsingFlexions,
							   Boolean isUsingWordTypes, Boolean isCheckingSynonyms) {
		parameters = new Parameters(isSortingWords, isSortingSentences, isUsingFlexions, isUsingWordTypes, isCheckingSynonyms);
		resetService();
	}

	public void loadFirstText(String firstText) {
		textComparator.loadMainTextFromString(firstText);
	}

	public void loadSecondText(String secondText) {
		textComparator.loadTextFromString(secondText);
	}

	public Boolean prepareText(int textId) {
		return textComparator.prepareText(textId);
	}

	public Boolean compareTexts() {
		Boolean result = textComparator.solve();
		textComparator.printResults(0.5, 1.0);
		return result;
	}

	public String getComparingStatistics() {
		String message = "";
		message += textComparator.getStatistics();
		message += textComparator.getSentencesStats(0.0, 1.0);
		message += textComparator.getSentencesStats(0.5, 1.0);
		message += textComparator.getSentencesStats(0.3, 0.5);
		message += textComparator.getSentencesStats(0.5, 0.7);
		message += textComparator.getSentencesStats(0.7, 1.0);
		return message;
	}

	public String getSimilarSentences() {
		return textComparator.getSimilarSentences(0.5, 1.0);
	}


}
