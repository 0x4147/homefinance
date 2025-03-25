package ca.homefinance.homefinance.repository;

import ca.homefinance.homefinance.entity.Receipt;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptRepository extends MongoRepository<Receipt, ObjectId> {

}
