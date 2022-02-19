package com.dss.gestion.repository;

import com.dss.gestion.domain.LotCamion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LotCamion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LotCamionRepository extends JpaRepository<LotCamion, Long> {}
