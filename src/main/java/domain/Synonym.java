/**
 * 
 */
package domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Synonym")
public class Synonym {
	private Integer synonymId;
	private Integer synonymGroupId;
	private Word word;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SynonymId")
	public Integer getSynonymId() {
		return synonymId;
	}
	public void setSynonymId(Integer synonymId) {
		this.synonymId = synonymId;
	}
	@Column(name = "SynonymGroupId",nullable = false)
	public Integer getSynonymGroupId() {
		return synonymGroupId;
	}
	public void setSynonymGroupId(int synonymGroupId) {
		this.synonymGroupId = synonymGroupId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="wordId", nullable=false)
	public Word getWord() {
		return this.word;
	}
	public void setWord(Word word) {
		this.word = word;
	}
	
	
	@Override
	public String toString() {
		return synonymGroupId +"";
	}
	
	
}
