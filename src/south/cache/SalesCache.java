/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package south.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import south.orm.Client;
import south.orm.Sale;
import south.orm.Salesman;

/**
 *
 * @author rodrigo
 */
public class SalesCache {

    private Map<String, Salesman> sellers = new HashMap<>();
    private Map<String, Client> clients = new HashMap<>();
    private Map<Long, Sale> sales = new HashMap<>();

    public Salesman getSalesman(String name) {

        if (sellers.containsKey(name)) {
            return sellers.get(name);
        } else {
            Salesman s = new Salesman(0.00, "", name);
            sellers.put(name, s);
            return s;
        }
    }

    public void putSalesman(Salesman salesman) {
        sellers.put(salesman.getName(), salesman);
    }

    public void putClient(Client client) {
        clients.put(client.getName(), client);
    }

    public void putSale(Sale sale) {
        sales.put(sale.getId(), sale);
    }

    public String getQuantityOfClient() {
        return "Quantidade de clientes: "+String.valueOf(clients.size());
    }

    public String getQuantityOfSalesman() {
        return "Quantidade de vendedores: "+String.valueOf(sellers.size());
    }
}
