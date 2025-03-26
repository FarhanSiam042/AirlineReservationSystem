import java.util.List;

/**
 * Refactored RolesAndPermissions with:
 * 1. Strategy pattern for auth
 * 2. Extracted value objects
 * 3. Simplified conditionals
 * 4. Proper encapsulation
 */
public final class RolesAndPermissions {
    // 1.3.4 Replace Magic Literal with constants
    private static final String DEFAULT_ADMIN_USERNAME = "root";
    private static final String DEFAULT_ADMIN_PASSWORD = "root";
    private static final int MAX_ADMINS = 10;

    // 1.2.3 Extract Class - Auth strategies
    private final List<AuthStrategy> authStrategies;

    // 1.6.1 Pull Up Constructor Body
    public RolesAndPermissions() {
        this.authStrategies = List.of(
                new AdminAuthStrategy(),
                new CustomerAuthStrategy()
        );
    }

    // 1.5.2 Introduce Parameter Object
    public AuthResult authenticate(AuthRequest request) {
        return authStrategies.stream()
                .map(strategy -> strategy.authenticate(request))
                .filter(AuthResult::isSuccess)
                .findFirst()
                .orElse(AuthResult.failed("Invalid credentials"));
    }

    // 1.4.4 Replace Conditional with Polymorphism
    private interface AuthStrategy {
        AuthResult authenticate(AuthRequest request);
    }

    // 1.2.3 Extract Class - Admin auth
    private static final class AdminAuthStrategy implements AuthStrategy {
        private final String[][] adminCredentials;

        public AdminAuthStrategy() {
            this.adminCredentials = new String[MAX_ADMINS][2];
            adminCredentials[0][0] = DEFAULT_ADMIN_USERNAME;
            adminCredentials[0][1] = DEFAULT_ADMIN_PASSWORD;
        }

        @Override
        public AuthResult authenticate(AuthRequest request) {
            for (String[] creds : adminCredentials) {
                if (creds[0] != null &&
                        creds[0].equals(request.username()) &&
                        creds[1].equals(request.password())) {

                    return AuthResult.success(
                            new AdminPrincipal(creds[0])
                    );
                }
            }
            return AuthResult.failed("Admin not found");
        }
    }

    // 1.2.3 Extract Class - Customer auth
    private static final class CustomerAuthStrategy implements AuthStrategy {
        @Override
        public AuthResult authenticate(AuthRequest request) {
            return CustomerRepository.getInstance()
                    .findByEmail(request.username())
                    .filter(c -> c.getPassword().equals(request.password()))
                    .map(c -> AuthResult.success(
                            new CustomerPrincipal(c.getId(), c.getEmail()))
                    )
                    .orElse(AuthResult.failed("Customer not found"));
        }
    }

    // 1.3.1 Replace Data Value with Object
    public record AuthRequest(String username, String password) {
        public AuthRequest {
            if (username == null || username.isBlank()) {
                throw new IllegalArgumentException("Username required");
            }
            if (password == null || password.isBlank()) {
                throw new IllegalArgumentException("Password required");
            }
        }
    }

    // 1.3.1 Replace Data Value with Object
    public record AuthResult(boolean success, UserPrincipal principal, String message) {
        public static AuthResult success(UserPrincipal principal) {
            return new AuthResult(true, principal, "Authentication successful");
        }

        public static AuthResult failed(String message) {
            return new AuthResult(false, null, message);
        }

        public boolean isSuccess() {
            return success;
        }
    }

    // 1.4.4 Replace Conditional with Polymorphism
    public sealed interface UserPrincipal permits AdminPrincipal, CustomerPrincipal {
        String getUsername();
        List<String> getPermissions();
    }

    public record AdminPrincipal(String username) implements UserPrincipal {
        @Override
        public List<String> getPermissions() {
            return List.of("CREATE_FLIGHT", "EDIT_CUSTOMER", "VIEW_REPORTS");
        }
    }

    public record CustomerPrincipal(CustomerId customerId, String email) implements UserPrincipal {
        @Override
        public String getUsername() {
            return email;
        }

        @Override
        public List<String> getPermissions() {
            return List.of("BOOK_FLIGHT", "VIEW_BOOKINGS", "EDIT_PROFILE");
        }
    }

    // 1.5.1 Rename Method - More descriptive name
    public boolean hasPermission(UserPrincipal principal, String permission) {
        return principal.getPermissions().contains(permission);
    }

    // 1.1.1 Extract Method - Admin creation
    public void createAdminAccount(String username, String password) {
        validateAdminCredentials(username, password);

        // Original admin creation logic
        synchronized (adminCredentials) {
            // Find empty slot
            for (int i = 1; i < adminCredentials.length; i++) {
                if (adminCredentials[i][0] == null) {
                    adminCredentials[i][0] = username;
                    adminCredentials[i][1] = password;
                    return;
                }
            }
        }
        throw new IllegalStateException("Max admin accounts reached");
    }

    // 1.1.1 Extract Method - Validation logic
    private void validateAdminCredentials(String username, String password) {
        if (username == null || username.length() < 4) {
            throw new IllegalArgumentException("Username too short");
        }
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password too weak");
        }
    }
}