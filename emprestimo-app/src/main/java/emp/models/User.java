package emp.models;

public class User {
    private String username;
    private String userType;
    private String status;

    public User(String username, String userType, String status) {
        this.username = username;
        this.userType = userType;
        this.status = status;
    }

    public String getUsername() { return username; }
    public String getUserType() { return userType; }
    public String getStatus() { return status; }
}
