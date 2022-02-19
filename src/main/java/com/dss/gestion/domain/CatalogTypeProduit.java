package com.dss.gestion.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A CatalogTypeProduit.
 */
@Entity
@Table(name = "catalog_type_produit")
public class CatalogTypeProduit implements Serializable {

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

    @OneToMany(mappedBy = "type")
    @JsonIgnoreProperties(value = { "fournisseur", "type" }, allowSetters = true)
    private Set<CatalogProduit> catalogProduits = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CatalogTypeProduit id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImage() {
        return this.image;
    }

    public CatalogTypeProduit image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public CatalogTypeProduit imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getIdFonc() {
        return this.idFonc;
    }

    public CatalogTypeProduit idFonc(String idFonc) {
        this.setIdFonc(idFonc);
        return this;
    }

    public void setIdFonc(String idFonc) {
        this.idFonc = idFonc;
    }

    public String getDesignation() {
        return this.designation;
    }

    public CatalogTypeProduit designation(String designation) {
        this.setDesignation(designation);
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return this.description;
    }

    public CatalogTypeProduit description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<CatalogProduit> getCatalogProduits() {
        return this.catalogProduits;
    }

    public void setCatalogProduits(Set<CatalogProduit> catalogProduits) {
        if (this.catalogProduits != null) {
            this.catalogProduits.forEach(i -> i.setType(null));
        }
        if (catalogProduits != null) {
            catalogProduits.forEach(i -> i.setType(this));
        }
        this.catalogProduits = catalogProduits;
    }

    public CatalogTypeProduit catalogProduits(Set<CatalogProduit> catalogProduits) {
        this.setCatalogProduits(catalogProduits);
        return this;
    }

    public CatalogTypeProduit addCatalogProduit(CatalogProduit catalogProduit) {
        this.catalogProduits.add(catalogProduit);
        catalogProduit.setType(this);
        return this;
    }

    public CatalogTypeProduit removeCatalogProduit(CatalogProduit catalogProduit) {
        this.catalogProduits.remove(catalogProduit);
        catalogProduit.setType(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CatalogTypeProduit)) {
            return false;
        }
        return id != null && id.equals(((CatalogTypeProduit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CatalogTypeProduit{" +
            "id=" + getId() +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", idFonc='" + getIdFonc() + "'" +
            ", designation='" + getDesignation() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
