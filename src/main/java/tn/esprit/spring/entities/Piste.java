package tn.esprit.spring.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE)
@Entity
public class Piste implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long numPiste;
	String namePiste;
	@Enumerated(EnumType.STRING)
	Color color;
	int length;
	int slope;

	@ManyToMany(mappedBy= "pistes")
	Set<Skier> skiers;

	public Long getNumPiste() {
		return numPiste;
	}

	public void setNumPiste(Long numPiste) {
		this.numPiste = numPiste;
	}

	public String getNamePiste() {
		return namePiste;
	}

	public void setNamePiste(String namePiste) {
		this.namePiste = namePiste;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getSlope() {
		return slope;
	}

	public void setSlope(int slope) {
		this.slope = slope;
	}

	public Set<Skier> getSkiers() {
		return skiers;
	}

	public void setSkiers(Set<Skier> skiers) {
		this.skiers = skiers;
	}
}
