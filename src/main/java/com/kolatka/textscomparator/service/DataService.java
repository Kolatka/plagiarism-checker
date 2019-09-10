package com.kolatka.textscomparator.service;

import com.kolatka.textscomparator.domain.Word;
import com.kolatka.textscomparator.domain.Acronym;
import com.kolatka.textscomparator.domain.Flexion;
import com.kolatka.textscomparator.domain.Synonym;
import org.hibernate.Query;
import org.hibernate.Session;
import com.kolatka.textscomparator.util.Hasher;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class DataService {

	private Session session;
	private String wordString;

	public Word findWord(String wordString, Session session) {
		this.session = session;
		this.wordString = wordString;
		if (wordString != null) {
			if (!isNumeric(wordString)) {
				Word word = getWordFromDB();
				if (word != null) {
					return word;
				} else {
					return createNewWord(wordString, "other");
				}
			} else {
				return createNewWord(wordString, "numeral");
			}
		}
		return null;
	}

	public Word findFlexion(String wordString, Session session) {
		this.session = session;
		this.wordString = wordString;
		Word word;
		if (!isNumeric(wordString)) {
			word = getWordFromDB();
			if (word == null) {
				word = getFlexionFromDB();
				if (word != null) {
					return word;
				} else {
					return createNewWord(wordString, "other");
				}
			} else return word;
		} else {
			return createNewWord(wordString, "numeral");
		}
	}

	private Word getWordFromDB() {
		Query query = session.createQuery("from Word where word = :word ");
		query.setParameter("word", wordString);
		List<Word> list = query.list();
		if (list.size() > 0)
			return list.get(0);
		else return null;
	}

	private Word getFlexionFromDB() {
		Query query = session.createQuery("from Flexion where flexion = :word ");
		query.setParameter("word", wordString);
		List<Flexion> list = query.list();
		if (list.size() > 0)
			return list.get(0).getWord();
		else return null;
	}

	public Word createNewWord(String wordString, String wordType) {
		Word newWord = new Word();
		newWord.setHash(Hasher.hash(wordString));
		newWord.setWord(wordString);
		newWord.setType(wordType);
		return newWord;
	}

	public Acronym checkIfAcronym(String wordString, Session session) {
		Query query = session.createQuery("from Acronym where acronym = :word ");
		query.setParameter("word", wordString);
		List<Acronym> list = query.list();
		if (list.size() > 0)
			return list.get(0);
		else return null;
	}

	public Word getRandomWordFromDB(Session session) {
		this.session = session;
		Random rand = new Random();
		int wordId = rand.nextInt(7500000);

		Query query = session.createQuery("from Flexion where flexionId = :id ");
		query.setParameter("id", wordId);
		List<Flexion> list = query.list();
		if (list.size() > 0)
			return list.get(0).getWord();
		else return null;
	}

	private Boolean isNumeric(String word) {
		try {
			Double.parseDouble(word);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public Word createWord(String wordString, String typeString, Set<Flexion> flexions) {
		Word word = new Word();
		word.setWord(wordString);
		word.setType(identifyWordType(typeString));
		word.setFlexions(flexions);
		word.setHash(Hasher.hash(wordString));
		return word;
	}

	public Word createWord(String wordString, String typeString) {
		Word word = new Word();
		word.setWord(wordString);
		word.setType(identifyWordType(typeString));
		word.setHash(Hasher.hash(wordString));
		return word;
	}

	public Flexion createFlexion(String flexionString, Word word) {
		Flexion flexion = new Flexion();
		flexion.setFlexion(flexionString);
		flexion.setWord(word);
		flexion.setHash(Hasher.hash(flexionString));
		return flexion;
	}

	public Synonym createSynonym(int synonymGroupId, Word word) {
		Synonym synonym = new Synonym();
		synonym.setSynonymGroupId(synonymGroupId);
		synonym.setWord(word);
		return synonym;
	}

	public Acronym createAcronym(String acronymString, String fullWordString) {
		Acronym acronym = new Acronym();
		acronym.setAcronym(acronymString);
		acronym.setFullWord(fullWordString);
		acronym.setHash(Hasher.hash(fullWordString));
		return acronym;
	}


	private String identifyWordType(String typeString) {
		String type;
		if (typeString.toLowerCase().contains("subst") || typeString.toLowerCase().contains("depr")) {
			type = "noun";
		} else if (typeString.toLowerCase().contains("num")) {
			type = "numeral";
		} else if (typeString.toLowerCase().contains("adj")) {
			type = "adjective";
		} else if (typeString.toLowerCase().contains("adv")) {
			type = "adverb";
		} else if (typeString.toLowerCase().contains("inf") || typeString.toLowerCase().contains("impt") ||
				typeString.toLowerCase().contains("fin") || typeString.toLowerCase().contains("bedzie") ||
				typeString.toLowerCase().contains("aglt") || typeString.toLowerCase().contains("imps") ||
				typeString.toLowerCase().contains("pcon") || typeString.toLowerCase().contains("pant") ||
				typeString.toLowerCase().contains("praet") || typeString.toLowerCase().contains("ppas") ||
				typeString.toLowerCase().contains("pact") || typeString.toLowerCase().contains("ger")) {
			type = "verb";
		} else if (typeString.toLowerCase().contains("brev")) {
			type = "acronym";
		} else {
			type = "other";
		}
		return type;
	}


}
