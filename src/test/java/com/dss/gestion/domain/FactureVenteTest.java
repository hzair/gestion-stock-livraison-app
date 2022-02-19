package com.dss.gestion.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.dss.gestion.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FactureVenteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FactureVente.class);
        FactureVente factureVente1 = new FactureVente();
        factureVente1.setId(1L);
        FactureVente factureVente2 = new FactureVente();
        factureVente2.setId(factureVente1.getId());
        assertThat(factureVente1).isEqualTo(factureVente2);
        factureVente2.setId(2L);
        assertThat(factureVente1).isNotEqualTo(factureVente2);
        factureVente1.setId(null);
        assertThat(factureVente1).isNotEqualTo(factureVente2);
    }
}
