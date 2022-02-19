package com.dss.gestion.repository;

import com.dss.gestion.domain.Camion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Camion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CamionRepository extends JpaRepository<Camion, Long> {}
