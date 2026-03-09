import java.util.*;

public class UsernameSystem {
    private Map<String, Integer> registry = new HashMap<>();
    private Map<String, Integer> attempts = new HashMap<>();

    public UsernameSystem() {
        registry.put("john_doe", 101);
        registry.put("admin", 102);
    }

    public boolean checkAvailability(String username) {
        attempts.put(username, attempts.getOrDefault(username, 0) + 1);
        return !registry.containsKey(username);
    }

    public List<String> suggestAlternatives(String username) {
        List<String> suggestions = new ArrayList<>();
        int i = 1;
        while (suggestions.size() < 3) {
            String candidate = username + i;
            if (!registry.containsKey(candidate)) {
                suggestions.add(candidate);
            }
            i++;
        }
        return suggestions;
    }

    public String getMostAttempted() {
        return Collections.max(attempts.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    public static void main(String[] args) {
        UsernameSystem sys = new UsernameSystem();
        System.out.println(sys.checkAvailability("john_doe")); 
        System.out.println(sys.suggestAlternatives("john_doe"));
        sys.checkAvailability("admin");
        sys.checkAvailability("admin");
        System.out.println(sys.getMostAttempted());
    }
}