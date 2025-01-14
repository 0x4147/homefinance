package ca.homefinance.homefinance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

@Document(collection = "transactions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    private ObjectId id;
    private String type;
    private String name;
    private BigDecimal amount;
    private ArrayList<String> categories;
    private String date;
    private String entity;
    private ArrayList<String> receipts;
}
