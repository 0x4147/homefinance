package ca.homefinance.homefinance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "receipts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Receipt {
    @Id
    ObjectId id;
    String nameOfFile;

    public Receipt(String nameOfFile) {
        this.nameOfFile = nameOfFile;
    }
}
