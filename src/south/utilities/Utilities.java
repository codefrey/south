package south.utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author rodrigo
 */
public class Utilities {

    private static final String USER_HOME = System.getProperty("user.home");
    public static final String EXTENSION_ACCEPTED = ".dat";

    public static final File[] getFiles() {

        File inDirFile = new File(inDir());

        return inDirFile.listFiles();
    }

    public static final String inDir() {
        return USER_HOME + "/data/in";
    }

    public static final String outDir() {
        return USER_HOME + "/data/out";
    }

    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public static int getInt(String value) {
        try {
            value = value != null ? value.trim() : "";
            if (value.length() > 0) {
                return Integer.parseInt(value);
            } else {
                return 0;
            }
        } catch (Exception e) {
            Logger.getLogger(Utilities.class.getName()).log(Level.WARNING, "Nao foi possivel converter a String ''{0}'' para Integer.", new Object[]{value,});
            return 0;
        }
    }

    public static long getLong(String value) {
        try {
            value = value != null ? value.trim() : "";
            if (value.length() > 0) {
                return Long.parseLong(value);
            } else {
                return 0;
            }
        } catch (Exception e) {
            Logger.getLogger(Utilities.class.getName()).log(Level.WARNING, "Nao foi possivel converter a String ''{0}'' para Long.", new Object[]{value,});
            return 0;
        }
    }
    public static double getDouble(String value) {
        try {
            value = value != null ? value.trim() : "";
            if (value.length() > 0) {
                return Double.parseDouble(value);
            } else {
                return 0;
            }
        } catch (Exception e) {
            Logger.getLogger(Utilities.class.getName()).log(Level.WARNING, "Nao foi possivel converter a String ''{0}'' para Double.", new Object[]{value,});
            return 0;
        }
    }

    public static Path createPathFile(String outFile) {
        try {
            outFile = outDir() + "/" + outFile;
            Path p = Paths.get(outFile);
            if (Files.notExists(p)) {
                Files.createFile(p);
            }
            return p;
        } catch (IOException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, "Não foi possível criar o arquivo de saida.", ex);
            return null;
        }
    }

}
