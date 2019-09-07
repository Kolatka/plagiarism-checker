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
@Table(name = "Flexion")
public class Flexion {

	private Integer flexionId;
	private String flexion;
	private Integer hash;
	private Word word;

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "FlexionId")
	public Integer getFlexionId() {
		return flexionId;
	}
	public void setFlexionId(Integer flexionId) {
		this.flexionId = flexionId;
	}
	@Column(name = "Flexion", length = 40, nullable = false)
	public String getFlexion() {
		return flexion;
	}
	public void setFlexion(String flexion) {
		this.flexion = flexion;
	}
	@Column(name = "Hash")
	public Integer getHash() {
		return hash;
	}
	public void setHash(Integer hash) {
		this.hash = hash;
	}
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="wordId", nullable=false)
	public Word getWord() {
		return this.word;
	}
	public void setWord(Word word) {
		this.word = word;
	}
	
	
   
	
	
}
