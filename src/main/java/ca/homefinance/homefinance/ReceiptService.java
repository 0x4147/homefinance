package ca.homefinance.homefinance;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReceiptService {

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public Optional<Receipt> addReceipt(String id, String fileName){
        Receipt receipt = receiptRepository.insert(new Receipt(fileName));

        mongoTemplate.update(Transaction.class)
                .matching(Criteria.where("id").is(id))
                .apply(new Update().push("receipts").value(receipt))
                .first();

        return Optional.of(receipt);
    }
}
