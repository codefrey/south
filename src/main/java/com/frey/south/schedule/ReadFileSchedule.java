/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frey.south.schedule;

import com.frey.south.orm.Resume;
import com.frey.south.service.ProcessFileService;
import com.frey.south.utilities.EDataType;
import com.frey.south.utilities.Utilities;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author rodrigo
 */
@Component
public class ReadFileSchedule {

    @Autowired
    private ProcessFileService fileService;

    @Scheduled(fixedRate = (30 * 1000))
    public void ProcessFiles() {
        Logger.getLogger(ReadFileSchedule.class.getName()).log(Level.INFO, "Iniciando leitura da pasta " + Utilities.inDir());
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
                                    fileService.readSalesman(line);
                                } else if (line.startsWith(EDataType.CLIENT.getId())) {
                                    fileService.readClient(line);
                                } else if (line.startsWith(EDataType.SALE.getId())) {
                                    fileService.readSale(line);
                                } else {
                                    Logger.getLogger(ReadFileSchedule.class.getName()).log(Level.INFO, "Linha fora do padrão especificado:  " + line);
                                }
                            }
                            );
                    Resume res = fileService.processResume();
                    fileService.printResults(file.getName(), res);

                } catch (IOException ex) {
                    Logger.getLogger(ReadFileSchedule.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

    }

}
