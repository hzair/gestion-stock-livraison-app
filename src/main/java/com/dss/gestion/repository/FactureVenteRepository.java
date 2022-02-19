package com.dss.gestion.repository;

import com.dss.gestion.domain.FactureVente;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FactureVente entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FactureVenteRepository extends JpaRepository<FactureVente, Long> {}
