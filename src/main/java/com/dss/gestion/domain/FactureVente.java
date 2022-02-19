package com.dss.gestion.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A FactureVente.
 */
@Entity
@Table(name = "facture_vente")
public class FactureVente implements Serializable {

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

    @OneToMany(mappedBy = "factureVente")
    @JsonIgnoreProperties(value = { "produit", "factureAchat", "factureVente" }, allowSetters = true)
    private Set<LotFacture> produits = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "factures" }, allowSetters = true)
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FactureVente id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getPhoto() {
        return this.photo;
    }

    public FactureVente photo(byte[] photo) {
        this.setPhoto(photo);
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return this.photoContentType;
    }

    public FactureVente photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public String getIdFonc() {
        return this.idFonc;
    }

    public FactureVente idFonc(String idFonc) {
        this.setIdFonc(idFonc);
        return this;
    }

    public void setIdFonc(String idFonc) {
        this.idFonc = idFonc;
    }

    public String getDesignation() {
        return this.designation;
    }

    public FactureVente designation(String designation) {
        this.setDesignation(designation);
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return this.description;
    }

    public FactureVente description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getMontant() {
        return this.montant;
    }

    public FactureVente montant(Long montant) {
        this.setMontant(montant);
        return this;
    }

    public void setMontant(Long montant) {
        this.montant = montant;
    }

    public Instant getDate() {
        return this.date;
    }

    public FactureVente date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Boolean getPayee() {
        return this.payee;
    }

    public FactureVente payee(Boolean payee) {
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
            this.produits.forEach(i -> i.setFactureVente(null));
        }
        if (lotFactures != null) {
            lotFactures.forEach(i -> i.setFactureVente(this));
        }
        this.produits = lotFactures;
    }

    public FactureVente produits(Set<LotFacture> lotFactures) {
        this.setProduits(lotFactures);
        return this;
    }

    public FactureVente addProduit(LotFacture lotFacture) {
        this.produits.add(lotFacture);
        lotFacture.setFactureVente(this);
        return this;
    }

    public FactureVente removeProduit(LotFacture lotFacture) {
        this.produits.remove(lotFacture);
        lotFacture.setFactureVente(null);
        return this;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public FactureVente client(Client client) {
        this.setClient(client);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FactureVente)) {
            return false;
        }
        return id != null && id.equals(((FactureVente) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FactureVente{" +
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
