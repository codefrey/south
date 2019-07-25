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
public abstract class Person {
    protected String cnpjCpf;
    protected String name;

    public Person() {
    }

    
    public Person(String cnpjCpf, String name) {
        this.cnpjCpf = cnpjCpf;
        this.name = name;
    }

    
    public String getCnpjCpf() {
        return cnpjCpf;
    }

    public void setCnpjCpf(String cnpjCpf) {
        this.cnpjCpf = cnpjCpf;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
