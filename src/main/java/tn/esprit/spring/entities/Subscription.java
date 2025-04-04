package tn.esprit.spring.entities;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE)
@Entity
public class Subscription implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long numSub;
	LocalDate startDate;
	LocalDate endDate;
	Float price;
//	@Enumerated(EnumType.STRING)
	TypeSubscription typeSub;


	public Long getNumSub() {
		return numSub;
	}

	public void setNumSub(Long numSub) {
		this.numSub = numSub;
	}

	public TypeSubscription getTypeSub() {
		return typeSub;
	}

	public void setTypeSub(TypeSubscription typeSub) {
		this.typeSub = typeSub;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
}
