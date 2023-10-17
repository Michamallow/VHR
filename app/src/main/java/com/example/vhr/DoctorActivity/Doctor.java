package com.example.vhr.DoctorActivity;

//Class containing the information returned by the API
public class Doctor {
    private int id;
    private String prenom;
    private String nom;
    private String libelleCommune;
    private String identificationNationalePP;
    private String libelleSavoirFaire;
    private String libelleProfession;
    private String numeroVoie;
    private String libelleVoie;
    private String libelleTypeVoie;
    private String bureauCedex;
    private String codePostal;

    public Doctor(int id, String prenom, String nom, String libelleCommune, String identificationNationalePP,
                           String libelleSavoirFaire, String libelleProfession, String numeroVoie, String libelleVoie,
                           String libelleTypeVoie, String bureauCedex, String codePostal) {
        this.id = id;
        this.prenom = prenom;
        this.nom = nom;
        this.libelleCommune = libelleCommune;
        this.identificationNationalePP = identificationNationalePP;
        this.libelleSavoirFaire = libelleSavoirFaire;
        this.libelleProfession = libelleProfession;
        this.numeroVoie = numeroVoie;
        this.libelleVoie = libelleVoie;
        this.libelleTypeVoie = libelleTypeVoie;
        this.bureauCedex = bureauCedex;
        this.codePostal = codePostal;
    }

    public String getIdentificationNationalePP() {
        return identificationNationalePP;
    }

    public String getNumeroVoie() {
        return numeroVoie;
    }

    public String getLibelleVoie() {
        return libelleVoie;
    }

    public String getLibelleTypeVoie() {
        return libelleTypeVoie;
    }

    public String getBureauCedex() {
        return bureauCedex;
    }

    public String getPrenom() {
        return prenom;
    }
    public String getNom() {
        return nom;
    }
    public String getLibelleCommune() {
        return libelleCommune;
    }
    public String getLibelleSavoirFaire() {
        return libelleSavoirFaire;
    }
    public int getId() {
        return id;
    }
    public String getLibelleProfession() {
        return libelleProfession;
    }
    public String getCodePostal() {
        return codePostal;
    }

}
