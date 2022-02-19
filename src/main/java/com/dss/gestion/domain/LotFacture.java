package com.dss.gestion.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A LotFacture.
 */
@Entity
@Table(name = "lot_facture")
public class LotFacture implements Serializable {

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

    @ManyToOne
    @JsonIgnoreProperties(value = { "produits", "fournisseur" }, allowSetters = true)
    private FactureAchat factureAchat;

    @ManyToOne
    @JsonIgnoreProperties(value = { "produits", "client" }, allowSetters = true)
    private FactureVente factureVente;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LotFacture id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantite() {
        return this.quantite;
    }

    public LotFacture quantite(Integer quantite) {
        this.setQuantite(quantite);
        return this;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Instant getDate() {
        return this.date;
    }

    public LotFacture date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Long getMontantTotal() {
        return this.montantTotal;
    }

    public LotFacture montantTotal(Long montantTotal) {
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

    public LotFacture produit(CatalogProduit catalogProduit) {
        this.setProduit(catalogProduit);
        return this;
    }

    public FactureAchat getFactureAchat() {
        return this.factureAchat;
    }

    public void setFactureAchat(FactureAchat factureAchat) {
        this.factureAchat = factureAchat;
    }

    public LotFacture factureAchat(FactureAchat factureAchat) {
        this.setFactureAchat(factureAchat);
        return this;
    }

    public FactureVente getFactureVente() {
        return this.factureVente;
    }

    public void setFactureVente(FactureVente factureVente) {
        this.factureVente = factureVente;
    }

    public LotFacture factureVente(FactureVente factureVente) {
        this.setFactureVente(factureVente);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LotFacture)) {
            return false;
        }
        return id != null && id.equals(((LotFacture) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LotFacture{" +
            "id=" + getId() +
            ", quantite=" + getQuantite() +
            ", date='" + getDate() + "'" +
            ", montantTotal=" + getMontantTotal() +
            "}";
    }
}
