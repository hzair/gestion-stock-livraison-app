package com.dss.gestion.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.dss.gestion.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LotCamionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LotCamion.class);
        LotCamion lotCamion1 = new LotCamion();
        lotCamion1.setId(1L);
        LotCamion lotCamion2 = new LotCamion();
        lotCamion2.setId(lotCamion1.getId());
        assertThat(lotCamion1).isEqualTo(lotCamion2);
        lotCamion2.setId(2L);
        assertThat(lotCamion1).isNotEqualTo(lotCamion2);
        lotCamion1.setId(null);
        assertThat(lotCamion1).isNotEqualTo(lotCamion2);
    }
}
