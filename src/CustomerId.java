
public record CustomerId(String value) {
    public CustomerId {
        if (value == null || value.length() != 6) {
            throw new IllegalArgumentException("Invalid customer ID format");
        }
    }
}

