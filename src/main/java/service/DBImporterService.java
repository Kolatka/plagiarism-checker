package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.hibernate.Session;

import domain.Acronym;
import domain.Flexion;
import domain.Synonym;
import domain.Word;
import util.Logger;

public class DBImporterService {
	Logger logger;
	Word word;
	Flexion flexion;
	Acronym acronym;
	Synonym synonym;
	DataService dataService;
	SessionService sessionService;
	Session session;
	int counter;
	float progress;
	
	public DBImporterService(){
		dataService = new DataService();
		sessionService = new SessionService();
		logger = new Logger("DBImporterService");
		counter = 0;
		progress = 0;
	}
	
	public void createWordsDatabase(String location) throws IOException{
		String wordString, flexionString, typeString;
		word = new Word();
		
		File file = new File(location);
		Scanner scanner = new Scanner(file,"UTF-8");

		sessionService.startSession();
		session = sessionService.getSession();
		while (scanner.hasNextLine()) {
			flexionString = scanner.next();
			flexionString = flexionString.split(":")[0];
			wordString = scanner.next();
			wordString = wordString.split(":")[0];
			typeString = scanner.next();
			if(!typeString.contains("brev")) {
				if(!wordString.equals(word.getWord())){
					word = dataService.createWord(wordString, typeString);
					session.save(word);
				}
				flexion = dataService.createFlexion(flexionString, word);
				session.save(flexion);	
			}else {
				acronym = dataService.createAcronym(flexionString, wordString);
				session.save(acronym);
			}
			scanner.nextLine();
			counter(7530000);
		}
		sessionService.stopSession();
		scanner.close();
		logger.log("Words, Flexion and Acronym tables created.");
	}
	
	private void counter(int num) {
		if(counter++%(num/100)==0){
			sessionService.commit();
			progress += 1;
			logger.log("Completed: " + progress + "%");
		}
	}

	public void createSynonymDatabase(String location) throws IOException {
		File file = new File(location);
		Scanner scanner = new Scanner(file,"UTF-8");
		int groupId=0;

		sessionService.startSession();
		session = sessionService.getSession();
		while (scanner.hasNextLine()) {
			String[] words = scanner.nextLine().split(";");
			for(int i=0;i<words.length;i++) {
				if(!words[i].contains(" ")) {
					word = dataService.findWord(words[i], session);
					synonym = dataService.createSynonym(groupId, word);
					if(word.getWordId()!=null) session.save(synonym);
				}
			}
			counter(13200);
			groupId++;
		}
		logger.log("Synonym table created.");
		sessionService.stopSession();
		scanner.close();
		
	}
	
	
}
