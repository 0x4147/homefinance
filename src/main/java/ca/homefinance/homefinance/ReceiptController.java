package ca.homefinance.homefinance;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")

public class ReceiptController {

    @Autowired
    ReceiptService receiptService;

    @PostMapping("/receipts")
    public ResponseEntity<Optional<Receipt>> addReceipt(@RequestBody Map<String, String> payload){
        try{
            return new ResponseEntity<Optional<Receipt>>(receiptService.addReceipt(payload.get("id"), payload.get("fileName")), HttpStatus.CREATED);
        }
        catch(Exception e){
            return new ResponseEntity<>(Optional.empty(), HttpStatus.BAD_REQUEST);
        }

    }
}
