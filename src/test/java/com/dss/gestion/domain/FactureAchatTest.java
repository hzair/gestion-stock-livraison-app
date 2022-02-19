package com.dss.gestion.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.dss.gestion.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FactureAchatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FactureAchat.class);
        FactureAchat factureAchat1 = new FactureAchat();
        factureAchat1.setId(1L);
        FactureAchat factureAchat2 = new FactureAchat();
        factureAchat2.setId(factureAchat1.getId());
        assertThat(factureAchat1).isEqualTo(factureAchat2);
        factureAchat2.setId(2L);
        assertThat(factureAchat1).isNotEqualTo(factureAchat2);
        factureAchat1.setId(null);
        assertThat(factureAchat1).isNotEqualTo(factureAchat2);
    }
}
