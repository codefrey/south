/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package south.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import south.cache.SalesCache;
import south.orm.Client;
import south.orm.Item;
import south.orm.Resume;
import south.orm.Sale;
import south.orm.Salesman;
import south.utilities.EDataType;
import south.utilities.Utilities;

/**
 *
 * @author rodrigo
 */
public class ProcessFiles {

    private SalesCache cache = new SalesCache();

    public ProcessFiles() {

        File[] listFiles = Utilities.getFiles();

        for (File file : listFiles) {
            if (file.getName().endsWith(Utilities.EXTENSION_ACCEPTED)) {
                try {

                    Path path = Paths.get(Utilities.inDir(), file.getName());

                    Stream<String> lines = Files.lines(path);

                    lines
                            .filter(l -> !Utilities.isEmpty(l))
                            .forEach(line -> {
                                if (line.startsWith(EDataType.SALESMAN.getId())) {
                                    readSalesman(line);
                                } else if (line.startsWith(EDataType.CLIENT.getId())) {
                                    readClient(line);
                                } else if (line.startsWith(EDataType.SALE.getId())) {
                                    readSale(line);
                                } else {
                                    messageError(line);
                                }
                            }
                            );
                    Resume res = processResume();
                    printResults(file.getName(), res);

                } catch (IOException ex) {
                    Logger.getLogger(ProcessFiles.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

    }

    private Resume processResume() {
        Resume res = new Resume();
        res.setQttClient(cache.getClients().size());
        res.setQttSalesman(cache.getSellers().size());

        cache.getSales().forEach((k, v) -> {
            if (res.getExpensiveSale() == null || res.getExpensiveSale().getTotalPrice() < v.getTotalPrice()) {
                res.setExpensiveSale(v);
            }
        });

        cache.getSellers().forEach((k, v) -> {
            if (res.getWorstSeller()== null || res.getWorstSeller().getTotalSold()< v.getTotalSold()) {
                res.setWorstSeller(v);
            }
        });

        return res;
    }

    private void printResults(String inFileName, Resume resume) throws IOException {

        String outFileName = inFileName.replace(Utilities.EXTENSION_ACCEPTED, "") + ".done.dat";

        Path p = Utilities.createPathFile(outFileName);

        if (p != null) {

            BufferedWriter bw = Files.newBufferedWriter(p, StandardCharsets.UTF_8, StandardOpenOption.WRITE);

            bw.write("Quantidade de clientes: " + resume.getQttClient());
            bw.newLine();
            bw.write("Quantidade de vendedores: " + resume.getQttSalesman());
            bw.newLine();
            bw.write("Venda mais cara: {id = " + resume.getExpensiveSale().getId() +", valor = "+resume.getExpensiveSale().getTotalPrice()+"}");
            bw.newLine();
            bw.write("Pior vendedor : {Nome = " + resume.getWorstSeller().getName()+ ", valor = " + resume.getWorstSeller().getTotalSold()+ "}");

            bw.close();

        }
    }

    private void readSalesman(String line) {
        String[] fields = getFields(line, EDataType.SALESMAN);
        if (fields != null) {
            Salesman sal = new Salesman();
            sal.setCnpjCpf(fields[1].trim());
            sal.setName(fields[2].trim());
            sal.setSalary(Utilities.getDouble(fields[3]));
            cache.putSalesman(sal);
        }
    }

    private void readSale(String line) {
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
                    if (sale.getSalesman() ==null) {
                        System.out.println("salesma null");
                    }
                    if (sale.getSalesman().getTotalSold()==null) {
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

    private void readClient(String line) {
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
            messageError(line);
            return null;
        }

        return fields;
    }

    private void messageError(String line) {
        Logger.getLogger(ProcessFiles.class.getName()).log(Level.INFO, "Linha fora do padrão especificado:  " + line);
    }

}
