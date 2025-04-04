package tn.esprit.spring.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
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
public class Skier implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long numSkier;
	String firstName;
	String lastName;
	LocalDate dateOfBirth;
	String city;

	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	Subscription subscription;

	@JsonIgnore
	@ManyToMany
	@JoinTable(
			name = "excursion",
			joinColumns = @JoinColumn(name = "numSkier"),
			inverseJoinColumns = @JoinColumn(name = "numPiste"))
	private Set<Piste> pistes;


	@OneToMany(mappedBy = "skier")
	Set<Registration> registrations;


	public Long getNumSkier() {
		return numSkier;
	}

	public void setNumSkier(Long numSkier) {
		this.numSkier = numSkier;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Subscription getSubscription() {
		return subscription;
	}

	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}

	public Set<Piste> getPistes() {
		return pistes;
	}

	public void setPistes(Set<Piste> pistes) {
		this.pistes = pistes;
	}

	public Set<Registration> getRegistrations() {
		return registrations;
	}

	public void setRegistrations(Set<Registration> registrations) {
		this.registrations = registrations;
	}
}
