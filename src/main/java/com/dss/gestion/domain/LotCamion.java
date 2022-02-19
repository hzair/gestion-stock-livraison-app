package com.dss.gestion.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A LotCamion.
 */
@Entity
@Table(name = "lot_camion")
public class LotCamion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "quantite")
    private Integer quantite;

    @Column(name = "date")
    private Instant date;

    @Column(name = "montant_total")
    private Long montantTotal;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "fournisseur", "type" }, allowSetters = true)
    private CatalogProduit produit;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "vendeur", "produits" }, allowSetters = true)
    private Camion camion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LotCamion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantite() {
        return this.quantite;
    }

    public LotCamion quantite(Integer quantite) {
        this.setQuantite(quantite);
        return this;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Instant getDate() {
        return this.date;
    }

    public LotCamion date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Long getMontantTotal() {
        return this.montantTotal;
    }

    public LotCamion montantTotal(Long montantTotal) {
        this.setMontantTotal(montantTotal);
        return this;
    }

    public void setMontantTotal(Long montantTotal) {
        this.montantTotal = montantTotal;
    }

    public CatalogProduit getProduit() {
        return this.produit;
    }

    public void setProduit(CatalogProduit catalogProduit) {
        this.produit = catalogProduit;
    }

    public LotCamion produit(CatalogProduit catalogProduit) {
        this.setProduit(catalogProduit);
        return this;
    }

    public Camion getCamion() {
        return this.camion;
    }

    public void setCamion(Camion camion) {
        this.camion = camion;
    }

    public LotCamion camion(Camion camion) {
        this.setCamion(camion);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LotCamion)) {
            return false;
        }
        return id != null && id.equals(((LotCamion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LotCamion{" +
            "id=" + getId() +
            ", quantite=" + getQuantite() +
            ", date='" + getDate() + "'" +
            ", montantTotal=" + getMontantTotal() +
            "}";
    }
}
