package com.dss.gestion.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Camion.
 */
@Entity
@Table(name = "camion")
public class Camion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "designation")
    private String designation;

    @Column(name = "date")
    private Instant date;

    @Column(name = "montant_total")
    private Long montantTotal;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Vendeur vendeur;

    @OneToMany(mappedBy = "camion")
    @JsonIgnoreProperties(value = { "produit", "camion" }, allowSetters = true)
    private Set<LotCamion> produits = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Camion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignation() {
        return this.designation;
    }

    public Camion designation(String designation) {
        this.setDesignation(designation);
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Instant getDate() {
        return this.date;
    }

    public Camion date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Long getMontantTotal() {
        return this.montantTotal;
    }

    public Camion montantTotal(Long montantTotal) {
        this.setMontantTotal(montantTotal);
        return this;
    }

    public void setMontantTotal(Long montantTotal) {
        this.montantTotal = montantTotal;
    }

    public Vendeur getVendeur() {
        return this.vendeur;
    }

    public void setVendeur(Vendeur vendeur) {
        this.vendeur = vendeur;
    }

    public Camion vendeur(Vendeur vendeur) {
        this.setVendeur(vendeur);
        return this;
    }

    public Set<LotCamion> getProduits() {
        return this.produits;
    }

    public void setProduits(Set<LotCamion> lotCamions) {
        if (this.produits != null) {
            this.produits.forEach(i -> i.setCamion(null));
        }
        if (lotCamions != null) {
            lotCamions.forEach(i -> i.setCamion(this));
        }
        this.produits = lotCamions;
    }

    public Camion produits(Set<LotCamion> lotCamions) {
        this.setProduits(lotCamions);
        return this;
    }

    public Camion addProduits(LotCamion lotCamion) {
        this.produits.add(lotCamion);
        lotCamion.setCamion(this);
        return this;
    }

    public Camion removeProduits(LotCamion lotCamion) {
        this.produits.remove(lotCamion);
        lotCamion.setCamion(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Camion)) {
            return false;
        }
        return id != null && id.equals(((Camion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Camion{" +
            "id=" + getId() +
            ", designation='" + getDesignation() + "'" +
            ", date='" + getDate() + "'" +
            ", montantTotal=" + getMontantTotal() +
            "}";
    }
}
