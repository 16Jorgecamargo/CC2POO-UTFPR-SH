package emp.models;

import java.time.LocalDate;

public class Payment {
    private Customer customer;
    private double amount;
    private LocalDate date;
    private String status;

    public Payment(Customer customer, double amount, LocalDate date) {
        this.customer = customer;
        this.amount = amount;
        this.date = date;
        this.status = LocalDate.now().isAfter(date) ? "Atrasado" : "Em dia";
    }

    // Getters
    public Customer getCustomer() { return customer; }
    public double getAmount() { return amount; }
    public LocalDate getDate() { return date; }
    public String getStatus() { return status; }
}