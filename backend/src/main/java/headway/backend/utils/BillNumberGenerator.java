package headway.backend.utils;

import org.springframework.stereotype.Component;

@Component
public class BillNumberGenerator {
    public String generateBillNumber() {
        return "KS-" + System.currentTimeMillis();
    }
}
