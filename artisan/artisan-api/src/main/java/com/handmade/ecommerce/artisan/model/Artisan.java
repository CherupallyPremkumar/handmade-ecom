package com.handmade.ecommerce.artisan.model;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "hm_artisan")
public class Artisan extends AbstractJpaStateEntity
{

	@Column(nullable = false, length = 100)
	private String artisan_name;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(length = 20)
	private String phone;

	private String bio;

	@ElementCollection
	@CollectionTable(name = "artisan_skills", joinColumns = @JoinColumn(name = "artisan_id"))
	@Column(name = "skill")
	private Set<String> skills;

	@Column(length = 1000)
	private String description;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime registrationDate;

	@Column
	private LocalDateTime verificationDate;

	@Column
	private String verifiedBy;

	@Column
	private LocalDateTime activationDate;

	@Column
	private String activatedBy;

	@Column
	private LocalDateTime suspensionDate;

	@Column
	private String suspendedBy;

	@Column(length = 500)
	private String suspensionReason;

	@Column
	private LocalDateTime deactivationDate;

	@Column
	private String deactivatedBy;

	@Column(length = 500)
	private String deactivationReason;

	@Column(length = 1000)
	private String verificationDocuments;

	@Column(length = 1000)
	private String verificationNotes;

	@Column(length = 1000)
	private String activationNotes;

	@Column(length = 1000)
	private String suspensionNotes;

	@Column(length = 1000)
	private String deactivationNotes;


	public String getArtisan_name() {
		return artisan_name;
	}

	public void setArtisan_name(String artisan_name) {
		this.artisan_name = artisan_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public Set<String> getSkills() {
		return skills;
	}

	public void setSkills(Set<String> skills) {
		this.skills = skills;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDateTime registrationDate) {
		this.registrationDate = registrationDate;
	}

	public LocalDateTime getVerificationDate() {
		return verificationDate;
	}

	public void setVerificationDate(LocalDateTime verificationDate) {
		this.verificationDate = verificationDate;
	}

	public String getVerifiedBy() {
		return verifiedBy;
	}

	public void setVerifiedBy(String verifiedBy) {
		this.verifiedBy = verifiedBy;
	}

	public LocalDateTime getActivationDate() {
		return activationDate;
	}

	public void setActivationDate(LocalDateTime activationDate) {
		this.activationDate = activationDate;
	}

	public String getActivatedBy() {
		return activatedBy;
	}

	public void setActivatedBy(String activatedBy) {
		this.activatedBy = activatedBy;
	}

	public LocalDateTime getSuspensionDate() {
		return suspensionDate;
	}

	public void setSuspensionDate(LocalDateTime suspensionDate) {
		this.suspensionDate = suspensionDate;
	}

	public String getSuspendedBy() {
		return suspendedBy;
	}

	public void setSuspendedBy(String suspendedBy) {
		this.suspendedBy = suspendedBy;
	}

	public String getSuspensionReason() {
		return suspensionReason;
	}

	public void setSuspensionReason(String suspensionReason) {
		this.suspensionReason = suspensionReason;
	}

	public LocalDateTime getDeactivationDate() {
		return deactivationDate;
	}

	public void setDeactivationDate(LocalDateTime deactivationDate) {
		this.deactivationDate = deactivationDate;
	}

	public String getDeactivatedBy() {
		return deactivatedBy;
	}

	public void setDeactivatedBy(String deactivatedBy) {
		this.deactivatedBy = deactivatedBy;
	}

	public String getDeactivationReason() {
		return deactivationReason;
	}

	public void setDeactivationReason(String deactivationReason) {
		this.deactivationReason = deactivationReason;
	}

	public String getVerificationDocuments() {
		return verificationDocuments;
	}

	public void setVerificationDocuments(String verificationDocuments) {
		this.verificationDocuments = verificationDocuments;
	}

	public String getVerificationNotes() {
		return verificationNotes;
	}

	public void setVerificationNotes(String verificationNotes) {
		this.verificationNotes = verificationNotes;
	}

	public String getActivationNotes() {
		return activationNotes;
	}

	public void setActivationNotes(String activationNotes) {
		this.activationNotes = activationNotes;
	}

	public String getSuspensionNotes() {
		return suspensionNotes;
	}

	public void setSuspensionNotes(String suspensionNotes) {
		this.suspensionNotes = suspensionNotes;
	}

	public String getDeactivationNotes() {
		return deactivationNotes;
	}

	public void setDeactivationNotes(String deactivationNotes) {
		this.deactivationNotes = deactivationNotes;
	}

}
