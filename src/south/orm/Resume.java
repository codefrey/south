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
public class Resume {
    private int qttClient;
    private int qttSalesman;
    private Sale expensiveSale;
    private Salesman worstSeller;

    public int getQttClient() {
        return qttClient;
    }

    public void setQttClient(int qttClient) {
        this.qttClient = qttClient;
    }

    public int getQttSalesman() {
        return qttSalesman;
    }

    public void setQttSalesman(int qttSalesman) {
        this.qttSalesman = qttSalesman;
    }

    public Sale getExpensiveSale() {
        return expensiveSale;
    }

    public void setExpensiveSale(Sale expensiveSale) {
        this.expensiveSale = expensiveSale;
    }


    public Salesman getWorstSeller() {
        return worstSeller;
    }

    public void setWorstSeller(Salesman worstSeller) {
        this.worstSeller = worstSeller;
    }
    
    
}
