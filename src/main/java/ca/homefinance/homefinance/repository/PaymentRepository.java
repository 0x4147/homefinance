package ca.homefinance.homefinance.repository;

import ca.homefinance.homefinance.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findByFromPersonIdAndToPersonId(Integer fromPersonId, Integer toPersonId);
}
