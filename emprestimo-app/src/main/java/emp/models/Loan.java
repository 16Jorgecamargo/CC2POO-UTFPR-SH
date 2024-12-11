package emp.models;

import java.time.LocalDate;

public class Loan {
    private Customer customer;
    private double amount;
    private double interestRate;
    private String interestType; // simples ou composto
    private int term;
    private String termType; // meses ou semanas
    private LocalDate startDate;
    private double totalAmount;
    private String status;

    public Loan(Customer customer, double amount, double interestRate, 
                String interestType, int term, String termType) {
        this.customer = customer;
        this.amount = amount;
        this.interestRate = interestRate;
        this.interestType = interestType;
        this.term = term;
        this.termType = termType;
        this.startDate = LocalDate.now();
        this.status = "ACTIVE";
        calculateTotalAmount();
    }

    private void calculateTotalAmount() {
        if (interestType.equals("SIMPLE")) {
            totalAmount = amount * (1 + (interestRate/100 * term));
        } else {
            totalAmount = amount * Math.pow(1 + (interestRate/100), term);
        }
    }

    // Getters and Setters
    public Customer getCustomer() { return customer; }
    public double getAmount() { return amount; }
    public double getInterestRate() { return interestRate; }
    public String getInterestType() { return interestType; }
    public int getTerm() { return term; }
    public String getTermType() { return termType; }
    public LocalDate getStartDate() { return startDate; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
}
