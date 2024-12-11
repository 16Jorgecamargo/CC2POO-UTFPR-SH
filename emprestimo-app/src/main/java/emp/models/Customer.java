package emp.models;

public class Customer {
    private String name;
    private String cpf;
    private String address;
    private String creditNote;

    public Customer(String name, String cpf, String address, String creditNote) {
        this.name = name;
        this.cpf = cpf;
        this.address = address;
        this.creditNote = creditNote;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCreditNote() { return creditNote; }
    public void setCreditNote(String creditNote) { this.creditNote = creditNote; }
}