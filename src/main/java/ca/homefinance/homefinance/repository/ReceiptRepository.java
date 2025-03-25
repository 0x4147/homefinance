package ca.homefinance.homefinance.repository;

import ca.homefinance.homefinance.entity.Receipt;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Integer> {
    Receipt findByFilename(String filename);
}
