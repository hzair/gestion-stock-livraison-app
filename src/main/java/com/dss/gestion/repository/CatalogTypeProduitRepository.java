package com.dss.gestion.repository;

import com.dss.gestion.domain.CatalogTypeProduit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CatalogTypeProduit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CatalogTypeProduitRepository extends JpaRepository<CatalogTypeProduit, Long> {}
