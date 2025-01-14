package ca.homefinance.homefinance;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptRepository extends MongoRepository<Receipt, ObjectId> {

}
