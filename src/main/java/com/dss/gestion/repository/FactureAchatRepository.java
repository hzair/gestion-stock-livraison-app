package com.dss.gestion.repository;

import com.dss.gestion.domain.FactureAchat;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FactureAchat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FactureAchatRepository extends JpaRepository<FactureAchat, Long> {}
