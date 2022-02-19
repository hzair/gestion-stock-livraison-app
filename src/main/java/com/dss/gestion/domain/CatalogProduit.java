package com.dss.gestion.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A CatalogProduit.
 */
@Entity
@Table(name = "catalog_produit")
public class CatalogProduit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Column(name = "id_fonc")
    private String idFonc;

    @NotNull
    @Column(name = "designation", nullable = false)
    private String designation;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "quantite", nullable = false)
    private Integer quantite;

    @NotNull
    @Column(name = "prix_achat_unit", nullable = false)
    private Long prixAchatUnit;

    @NotNull
    @Column(name = "prix_vente_unit", nullable = false)
    private Long prixVenteUnit;

    @Column(name = "prix_vente_gros")
    private Long prixVenteGros;

    @Column(name = "date")
    private Instant date;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "factures" }, allowSetters = true)
    private Fournisseur fournisseur;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "catalogProduits" }, allowSetters = true)
    private CatalogTypeProduit type;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CatalogProduit id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImage() {
        return this.image;
    }

    public CatalogProduit image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public CatalogProduit imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getIdFonc() {
        return this.idFonc;
    }

    public CatalogProduit idFonc(String idFonc) {
        this.setIdFonc(idFonc);
        return this;
    }

    public void setIdFonc(String idFonc) {
        this.idFonc = idFonc;
    }

    public String getDesignation() {
        return this.designation;
    }

    public CatalogProduit designation(String designation) {
        this.setDesignation(designation);
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return this.description;
    }

    public CatalogProduit description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantite() {
        return this.quantite;
    }

    public CatalogProduit quantite(Integer quantite) {
        this.setQuantite(quantite);
        return this;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Long getPrixAchatUnit() {
        return this.prixAchatUnit;
    }

    public CatalogProduit prixAchatUnit(Long prixAchatUnit) {
        this.setPrixAchatUnit(prixAchatUnit);
        return this;
    }

    public void setPrixAchatUnit(Long prixAchatUnit) {
        this.prixAchatUnit = prixAchatUnit;
    }

    public Long getPrixVenteUnit() {
        return this.prixVenteUnit;
    }

    public CatalogProduit prixVenteUnit(Long prixVenteUnit) {
        this.setPrixVenteUnit(prixVenteUnit);
        return this;
    }

    public void setPrixVenteUnit(Long prixVenteUnit) {
        this.prixVenteUnit = prixVenteUnit;
    }

    public Long getPrixVenteGros() {
        return this.prixVenteGros;
    }

    public CatalogProduit prixVenteGros(Long prixVenteGros) {
        this.setPrixVenteGros(prixVenteGros);
        return this;
    }

    public void setPrixVenteGros(Long prixVenteGros) {
        this.prixVenteGros = prixVenteGros;
    }

    public Instant getDate() {
        return this.date;
    }

    public CatalogProduit date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Fournisseur getFournisseur() {
        return this.fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public CatalogProduit fournisseur(Fournisseur fournisseur) {
        this.setFournisseur(fournisseur);
        return this;
    }

    public CatalogTypeProduit getType() {
        return this.type;
    }

    public void setType(CatalogTypeProduit catalogTypeProduit) {
        this.type = catalogTypeProduit;
    }

    public CatalogProduit type(CatalogTypeProduit catalogTypeProduit) {
        this.setType(catalogTypeProduit);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CatalogProduit)) {
            return false;
        }
        return id != null && id.equals(((CatalogProduit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CatalogProduit{" +
            "id=" + getId() +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", idFonc='" + getIdFonc() + "'" +
            ", designation='" + getDesignation() + "'" +
            ", description='" + getDescription() + "'" +
            ", quantite=" + getQuantite() +
            ", prixAchatUnit=" + getPrixAchatUnit() +
            ", prixVenteUnit=" + getPrixVenteUnit() +
            ", prixVenteGros=" + getPrixVenteGros() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
