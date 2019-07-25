/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package south.orm;

/**
 *
 * @author rodrigo
 */
public class Salesman extends Person {

    private Double salary;
    private Double totalSold;

    public Salesman() {
        super();
        this.salary = new Double(0);
        this.totalSold= new Double(0);
    }

    public Salesman(String cnpjCpf, String name) {
        super(cnpjCpf, name);
        this.salary = new Double(0);
        this.totalSold= new Double(0);
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Double getTotalSold() {
        return totalSold;
    }

    public void setTotalSold(Double totalSold) {
        this.totalSold = totalSold;
    }

}
