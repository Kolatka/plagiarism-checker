package domain;

import javax.persistence.*;

@Entity
@Table(name = "Acronym")
public class Acronym {
	private Integer acronymId;
	private String acronym;
	private String fullWord;
	private Integer hash;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "AcronymId")
	public Integer getAcronymId() {
		return acronymId;
	}

	public void setAcronymId(Integer acronymId) {
		this.acronymId = acronymId;
	}

	@Column(name = "Acronym", length = 50, nullable = false)
	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	@Column(name = "FullWord", length = 50, nullable = false)
	public String getFullWord() {
		return fullWord;
	}

	public void setFullWord(String fullWord) {
		this.fullWord = fullWord;
	}

	@Column(name = "Hash")
	public Integer getHash() {
		return hash;
	}

	public void setHash(Integer hash) {
		this.hash = hash;
	}


}

