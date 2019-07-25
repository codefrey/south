/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frey.south.orm;

/**
 *
 * @author rodrigo
 */
public class Salesman extends Person {

    private boolean sold;
    private Double salary;
    private Double totalSold;

    public Salesman() {
        super();
        this.sold = false;
        this.salary = new Double(0);
        this.totalSold = new Double(0);
    }

    public Salesman(String cnpjCpf, String name) {
        super(cnpjCpf, name);
        this.sold = false;
        this.salary = new Double(0);
        this.totalSold = new Double(0);
    }

    public boolean isSold() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
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
