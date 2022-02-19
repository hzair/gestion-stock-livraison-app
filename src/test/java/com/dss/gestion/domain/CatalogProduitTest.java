package com.dss.gestion.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.dss.gestion.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CatalogProduitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CatalogProduit.class);
        CatalogProduit catalogProduit1 = new CatalogProduit();
        catalogProduit1.setId(1L);
        CatalogProduit catalogProduit2 = new CatalogProduit();
        catalogProduit2.setId(catalogProduit1.getId());
        assertThat(catalogProduit1).isEqualTo(catalogProduit2);
        catalogProduit2.setId(2L);
        assertThat(catalogProduit1).isNotEqualTo(catalogProduit2);
        catalogProduit1.setId(null);
        assertThat(catalogProduit1).isNotEqualTo(catalogProduit2);
    }
}
