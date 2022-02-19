package com.dss.gestion.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.dss.gestion.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LotFactureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LotFacture.class);
        LotFacture lotFacture1 = new LotFacture();
        lotFacture1.setId(1L);
        LotFacture lotFacture2 = new LotFacture();
        lotFacture2.setId(lotFacture1.getId());
        assertThat(lotFacture1).isEqualTo(lotFacture2);
        lotFacture2.setId(2L);
        assertThat(lotFacture1).isNotEqualTo(lotFacture2);
        lotFacture1.setId(null);
        assertThat(lotFacture1).isNotEqualTo(lotFacture2);
    }
}
