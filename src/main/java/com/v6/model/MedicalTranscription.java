package com.v6.model;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
@Service
public class MedicalTranscription {

    String medicalSpecialty;
    String subjective;
    String medications;
    String allergies;
    String vitals;
    String heent;
    String neck;
    String assessment;
    String plan;
    String keywords;

    public String getMedicalSpecialty() {
        return medicalSpecialty;
    }

    public void setMedicalSpecialty(String medicalSpecialty) {
        this.medicalSpecialty = medicalSpecialty;
    }

    public String getSubjective() {
        return subjective;
    }

    public void setSubjective(String subjective) {
        this.subjective = subjective;
    }

    public String getMedications() {
        return medications;
    }

    public void setMedications(String medications) {
        this.medications = medications;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getVitals() {
        return vitals;
    }

    public void setVitals(String vitals) {
        this.vitals = vitals;
    }

    public String getHeent() {
        return heent;
    }

    public void setHeent(String heent) {
        this.heent = heent;
    }

    public String getNeck() {
        return neck;
    }

    public void setNeck(String neck) {
        this.neck = neck;
    }

    public String getAssessment() {
        return assessment;
    }

    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @Override
    public String toString() {
        return "MedicalTranscription{\n" +
                medicalSpecialty + '\n'+
                subjective+ '\n'+
                medications + '\n' +
                allergies + '\n' +
                vitals + '\n' +
                heent + '\n' +
                neck + '\n' +
                assessment + '\n' +
                plan + '\n' +
                keywords + '\n' +
                '}';
    }
}
