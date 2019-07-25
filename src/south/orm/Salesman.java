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

    public Salesman() {
        super();
    }

    public Salesman(Double salary, String cnpjCpf, String name) {
        super(cnpjCpf, name);
        this.salary = salary;
    }

    
    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

}
