package com.dss.gestion.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.dss.gestion.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CatalogTypeProduitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CatalogTypeProduit.class);
        CatalogTypeProduit catalogTypeProduit1 = new CatalogTypeProduit();
        catalogTypeProduit1.setId(1L);
        CatalogTypeProduit catalogTypeProduit2 = new CatalogTypeProduit();
        catalogTypeProduit2.setId(catalogTypeProduit1.getId());
        assertThat(catalogTypeProduit1).isEqualTo(catalogTypeProduit2);
        catalogTypeProduit2.setId(2L);
        assertThat(catalogTypeProduit1).isNotEqualTo(catalogTypeProduit2);
        catalogTypeProduit1.setId(null);
        assertThat(catalogTypeProduit1).isNotEqualTo(catalogTypeProduit2);
    }
}
