package emp.models;

import java.time.LocalDate;
import java.util.List;

public class Report {
    private LocalDate startDate;
    private LocalDate endDate;
    private String type;
    private Customer customer;
    private List<String> data;

    public Report(LocalDate startDate, LocalDate endDate, String type, Customer customer) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.customer = customer;
    }

    // Getters and setters
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public String getType() { return type; }
    public Customer getCustomer() { return customer; }
    public List<String> getData() { return data; }
}
