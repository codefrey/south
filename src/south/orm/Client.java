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
public class Client extends Person {

    private String bussinessArea;

    public Client() {
        super();
    }

    public Client(String bussinessArea, String cnpjCpf, String name) {
        super(cnpjCpf, name);
        this.bussinessArea = bussinessArea;
    }

    public String getBussinessArea() {
        return bussinessArea;
    }

    public void setBussinessArea(String bussinessArea) {
        this.bussinessArea = bussinessArea;
    }

}
