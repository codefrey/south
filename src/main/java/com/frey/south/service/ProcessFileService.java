/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frey.south.service;

import com.frey.south.cache.SalesCache;
import com.frey.south.orm.Client;
import com.frey.south.orm.Item;
import com.frey.south.orm.Resume;
import com.frey.south.orm.Sale;
import com.frey.south.orm.Salesman;
import com.frey.south.schedule.ReadFileSchedule;
import com.frey.south.utilities.EDataType;
import com.frey.south.utilities.Utilities;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author rodrigo
 */
@Component
public class ProcessFileService {

    @Autowired
    private SalesCache cache;

    public Resume processResume() {
        Resume res = new Resume();
        res.setQttClient(cache.getClients().size());
        res.setQttSalesman(cache.getSellers().size());

        cache.getSales().forEach((k, v) -> {
            if (res.getExpensiveSale() == null || res.getExpensiveSale().getTotalPrice() < v.getTotalPrice()) {
                res.setExpensiveSale(v);
            }
        });

        cache.getSellers().forEach((k, v) -> {
            if (res.getWorstSeller() == null || res.getWorstSeller().getTotalSold() < v.getTotalSold()) {
                res.setWorstSeller(v);
            }
        });

        return res;
    }

    public void printResults(String inFileName, Resume resume) throws IOException {

        String outFileName = inFileName.replace(Utilities.EXTENSION_ACCEPTED, "") + ".done.dat";

        Path p = Utilities.createPathFile(outFileName);

        if (p != null) {

            BufferedWriter bw = Files.newBufferedWriter(p, StandardCharsets.UTF_8, StandardOpenOption.WRITE);

            bw.write("Quantidade de clientes: " + resume.getQttClient());
            bw.newLine();
            bw.write("Quantidade de vendedores: " + resume.getQttSalesman());
            bw.newLine();
            bw.write("Venda mais cara: {id = " + resume.getExpensiveSale().getId() + ", valor = " + resume.getExpensiveSale().getTotalPrice() + "}");
            bw.newLine();
            bw.write("Pior vendedor : {Nome = " + resume.getWorstSeller().getName() + ", valor = " + resume.getWorstSeller().getTotalSold() + "}");

            bw.close();

        }
    }

    public void readSalesman(String line) {
        String[] fields = getFields(line, EDataType.SALESMAN);
        if (fields != null) {
            Salesman sal = new Salesman();
            sal.setCnpjCpf(fields[1].trim());
            sal.setName(fields[2].trim());
            sal.setSalary(Utilities.getDouble(fields[3]));
            cache.putSalesman(sal);
        }
    }

    public void readSale(String line) {
        String[] fields = getFields(line, EDataType.SALE);
        if (fields != null) {
            Sale sale = new Sale();
            sale.setId(Long.parseLong(fields[1]));
            sale.setSalesman(cache.getSalesman(fields[3]));

            String listOfItemsStr = fields[2].replace("[", "").replace("]", "");

            String[] listOfItems = getFields(listOfItemsStr, EDataType.LIST_OF_ITEM);
            if (listOfItems != null) {
                for (int i = 0; i < listOfItems.length; i++) {

                    String[] itemFields = getFields(listOfItems[i], EDataType.ITEM);

                    Item item = new Item();
                    item.setId(Utilities.getLong(itemFields[0]));
                    item.setQuantity(Utilities.getInt(itemFields[1]));
                    item.setPrice(Utilities.getDouble(itemFields[2]));
                    sale.getItems().add(item);

                    sale.setTotalPrice(sale.getTotalPrice() + item.getPrice());
                    if (sale.getSalesman() == null) {
                        System.out.println("salesma null");
                    }
                    if (sale.getSalesman().getTotalSold() == null) {
                        System.out.println(sale.getSalesman().getCnpjCpf());
                        System.out.println(sale.getSalesman().getName());
                        System.out.println(sale.getSalesman().getSalary());
                        System.out.println(sale.getSalesman().getTotalSold());
                        System.out.println("sold null");
                    }
                    sale.getSalesman().setTotalSold(sale.getSalesman().getTotalSold() + item.getPrice());

                    cache.putSalesman(sale.getSalesman());

                }
            }
            cache.putSale(sale);
        }
    }

    public void readClient(String line) {
        String[] fields = getFields(line, EDataType.CLIENT);
        if (fields != null) {
            if (fields != null) {
                Client cli = new Client();
                cli.setCnpjCpf(fields[1].trim());
                cli.setName(fields[2].trim());
                cli.setBussinessArea(fields[3].trim());
                cache.putClient(cli);
            }
        }

    }

    private String[] getFields(String line, EDataType type) {
        String[] fields = line.split(type.getSeparator());

        if (type.getLength() > 0 && type.getLength() != fields.length) {
            Logger.getLogger(ProcessFileService.class.getName()).log(Level.INFO, "Linha fora do padrão especificado:  " + line);
            return null;
        }

        return fields;
    }

}
