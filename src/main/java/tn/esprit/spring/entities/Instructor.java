package tn.esprit.spring.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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
public class Instructor implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long numInstructor;
	String firstName;
	String lastName;
	LocalDate dateOfHire;
	@OneToMany
	Set<Course> courses;

	public Long getNumInstructor() {
		return numInstructor;
	}

	public void setNumInstructor(Long numInstructor) {
		this.numInstructor = numInstructor;
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

	public LocalDate getDateOfHire() {
		return dateOfHire;
	}

	public void setDateOfHire(LocalDate dateOfHire) {
		this.dateOfHire = dateOfHire;
	}

	public Set<Course> getCourses() {
		return courses;
	}

	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}
}
