package com.kolatka.textscomparator.domain;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Word")
public class Word {
	private Integer wordId;
	private String word;
	private String type;
	private Integer hash;
	private Set<Flexion> flexions;
	private Set<Synonym> synonyms;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "WordId")
	public Integer getWordId() {
		return wordId;
	}

	public void setWordId(Integer wordId) {
		this.wordId = wordId;
	}

	@Column(name = "Word", length = 50, nullable = false)
	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	@Column(name = "Type", length = 50, nullable = false)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "Hash")
	public Integer getHash() {
		return hash;
	}

	public void setHash(Integer hash) {
		this.hash = hash;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "word")
	public Set<Flexion> getFlexions() {
		return flexions;
	}

	public void setFlexions(Set<Flexion> flexions) {
		this.flexions = flexions;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "word")
	public Set<Synonym> getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(Set<Synonym> synonyms) {
		this.synonyms = synonyms;
	}


}
