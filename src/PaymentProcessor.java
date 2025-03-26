public interface PaymentProcessor {
    PaymentResult process(PaymentRequest request);
}

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod
) {}
}