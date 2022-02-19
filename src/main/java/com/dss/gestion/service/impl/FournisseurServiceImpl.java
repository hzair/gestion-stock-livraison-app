package com.dss.gestion.service.impl;

import com.dss.gestion.domain.Fournisseur;
import com.dss.gestion.repository.FournisseurRepository;
import com.dss.gestion.service.FournisseurService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Fournisseur}.
 */
@Service
@Transactional
public class FournisseurServiceImpl implements FournisseurService {

    private final Logger log = LoggerFactory.getLogger(FournisseurServiceImpl.class);

    private final FournisseurRepository fournisseurRepository;

    public FournisseurServiceImpl(FournisseurRepository fournisseurRepository) {
        this.fournisseurRepository = fournisseurRepository;
    }

    @Override
    public Fournisseur save(Fournisseur fournisseur) {
        log.debug("Request to save Fournisseur : {}", fournisseur);
        return fournisseurRepository.save(fournisseur);
    }

    @Override
    public Optional<Fournisseur> partialUpdate(Fournisseur fournisseur) {
        log.debug("Request to partially update Fournisseur : {}", fournisseur);

        return fournisseurRepository
            .findById(fournisseur.getId())
            .map(existingFournisseur -> {
                if (fournisseur.getImage() != null) {
                    existingFournisseur.setImage(fournisseur.getImage());
                }
                if (fournisseur.getImageContentType() != null) {
                    existingFournisseur.setImageContentType(fournisseur.getImageContentType());
                }
                if (fournisseur.getMatricule() != null) {
                    existingFournisseur.setMatricule(fournisseur.getMatricule());
                }
                if (fournisseur.getDesignation() != null) {
                    existingFournisseur.setDesignation(fournisseur.getDesignation());
                }
                if (fournisseur.getNom() != null) {
                    existingFournisseur.setNom(fournisseur.getNom());
                }
                if (fournisseur.getPrenom() != null) {
                    existingFournisseur.setPrenom(fournisseur.getPrenom());
                }
                if (fournisseur.getEmail() != null) {
                    existingFournisseur.setEmail(fournisseur.getEmail());
                }
                if (fournisseur.getPhoneNumber() != null) {
                    existingFournisseur.setPhoneNumber(fournisseur.getPhoneNumber());
                }
                if (fournisseur.getAdresse() != null) {
                    existingFournisseur.setAdresse(fournisseur.getAdresse());
                }

                return existingFournisseur;
            })
            .map(fournisseurRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Fournisseur> findAll(Pageable pageable) {
        log.debug("Request to get all Fournisseurs");
        return fournisseurRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Fournisseur> findOne(Long id) {
        log.debug("Request to get Fournisseur : {}", id);
        return fournisseurRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Fournisseur : {}", id);
        fournisseurRepository.deleteById(id);
    }
}
