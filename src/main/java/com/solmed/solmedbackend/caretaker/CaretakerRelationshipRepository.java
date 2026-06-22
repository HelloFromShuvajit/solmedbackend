package com.solmed.solmedbackend.caretaker;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CaretakerRelationshipRepository extends JpaRepository<CaretakerRelationship, Long> {

    List<CaretakerRelationship> findByOwner_Id(Long ownerId);

    List<CaretakerRelationship> findByCaretaker_Id(Long caretakerId);

    Optional<CaretakerRelationship> findByOwner_IdAndCaretaker_Id(Long ownerId, Long caretakerId);

    void deleteByOwner_IdAndCaretaker_Id(Long ownerId, Long caretakerId);
}
