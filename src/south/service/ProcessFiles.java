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
                                    processSalesman(line);
                                } else if (line.startsWith(EDataType.CLIENT.getId())) {
                                    processClient(line);
                                } else if (line.startsWith(EDataType.SALE.getId())) {
                                    processSale(line);
                                } else {
                                    messageError(line);
                                }
                            }
                            );

                    impressResults(file.getName());

                } catch (IOException ex) {
                    Logger.getLogger(ProcessFiles.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

    }

    private void impressResults(String inFileName) throws IOException {

        String outFileName = inFileName.replace(Utilities.EXTENSION_ACCEPTED, "") + ".done.dat";

        Path p = Utilities.createPathFile(outFileName);

        if (p != null) {

            BufferedWriter bw = Files.newBufferedWriter(p, StandardCharsets.UTF_8, StandardOpenOption.WRITE);

            bw.write(cache.getQuantityOfClient());
            bw.newLine();
            bw.write(cache.getQuantityOfSalesman());

            bw.close();

        }
    }

    private void processSalesman(String line) {
        String[] fields = getFields(line, EDataType.SALESMAN);
        if (fields != null) {
            Salesman sal = new Salesman();
            sal.setCnpjCpf(fields[1].trim());
            sal.setName(fields[2].trim());
            sal.setSalary(Utilities.getDouble(fields[3]));
            cache.putSalesman(sal);
        }
    }

    private void processSale(String line) {
        String[] fields = getFields(line, EDataType.SALE);
        if (fields != null) {
            Sale sale = new Sale();
            sale.setId(Long.parseLong(fields[1]));
            sale.setSalesman(cache.getSalesman(fields[3].trim()));

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

                }
            }

        }
    }

    private void processClient(String line) {
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
