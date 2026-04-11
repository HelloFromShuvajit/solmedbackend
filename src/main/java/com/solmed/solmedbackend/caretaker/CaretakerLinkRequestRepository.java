package com.solmed.solmedbackend.caretaker;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CaretakerLinkRequestRepository extends JpaRepository<CaretakerLinkRequest, Long> {

    List<CaretakerLinkRequest> findByOwner_IdAndStatus(Long ownerId, InviteStatus status);

    List<CaretakerLinkRequest> findByCaretaker_IdOrderByCreatedAtDesc(Long caretakerId);

    Optional<CaretakerLinkRequest> findByCaretaker_IdAndOwner_IdAndStatus(
            Long caretakerId, Long ownerId, InviteStatus status);
}
