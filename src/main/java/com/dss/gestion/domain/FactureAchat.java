package com.dss.gestion.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A FactureAchat.
 */
@Entity
@Table(name = "facture_achat")
public class FactureAchat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @Column(name = "id_fonc")
    private String idFonc;

    @NotNull
    @Column(name = "designation", nullable = false)
    private String designation;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "montant", nullable = false)
    private Long montant;

    @Column(name = "date")
    private Instant date;

    @Column(name = "payee")
    private Boolean payee;

    @OneToMany(mappedBy = "factureAchat")
    @JsonIgnoreProperties(value = { "produit", "factureAchat", "factureVente" }, allowSetters = true)
    private Set<LotFacture> produits = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "factures" }, allowSetters = true)
    private Fournisseur fournisseur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FactureAchat id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getPhoto() {
        return this.photo;
    }

    public FactureAchat photo(byte[] photo) {
        this.setPhoto(photo);
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return this.photoContentType;
    }

    public FactureAchat photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public String getIdFonc() {
        return this.idFonc;
    }

    public FactureAchat idFonc(String idFonc) {
        this.setIdFonc(idFonc);
        return this;
    }

    public void setIdFonc(String idFonc) {
        this.idFonc = idFonc;
    }

    public String getDesignation() {
        return this.designation;
    }

    public FactureAchat designation(String designation) {
        this.setDesignation(designation);
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return this.description;
    }

    public FactureAchat description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getMontant() {
        return this.montant;
    }

    public FactureAchat montant(Long montant) {
        this.setMontant(montant);
        return this;
    }

    public void setMontant(Long montant) {
        this.montant = montant;
    }

    public Instant getDate() {
        return this.date;
    }

    public FactureAchat date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Boolean getPayee() {
        return this.payee;
    }

    public FactureAchat payee(Boolean payee) {
        this.setPayee(payee);
        return this;
    }

    public void setPayee(Boolean payee) {
        this.payee = payee;
    }

    public Set<LotFacture> getProduits() {
        return this.produits;
    }

    public void setProduits(Set<LotFacture> lotFactures) {
        if (this.produits != null) {
            this.produits.forEach(i -> i.setFactureAchat(null));
        }
        if (lotFactures != null) {
            lotFactures.forEach(i -> i.setFactureAchat(this));
        }
        this.produits = lotFactures;
    }

    public FactureAchat produits(Set<LotFacture> lotFactures) {
        this.setProduits(lotFactures);
        return this;
    }

    public FactureAchat addProduit(LotFacture lotFacture) {
        this.produits.add(lotFacture);
        lotFacture.setFactureAchat(this);
        return this;
    }

    public FactureAchat removeProduit(LotFacture lotFacture) {
        this.produits.remove(lotFacture);
        lotFacture.setFactureAchat(null);
        return this;
    }

    public Fournisseur getFournisseur() {
        return this.fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public FactureAchat fournisseur(Fournisseur fournisseur) {
        this.setFournisseur(fournisseur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FactureAchat)) {
            return false;
        }
        return id != null && id.equals(((FactureAchat) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FactureAchat{" +
            "id=" + getId() +
            ", photo='" + getPhoto() + "'" +
            ", photoContentType='" + getPhotoContentType() + "'" +
            ", idFonc='" + getIdFonc() + "'" +
            ", designation='" + getDesignation() + "'" +
            ", description='" + getDescription() + "'" +
            ", montant=" + getMontant() +
            ", date='" + getDate() + "'" +
            ", payee='" + getPayee() + "'" +
            "}";
    }
}
