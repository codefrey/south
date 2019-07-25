/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frey.south.utilities;

/**
 *
 * @author rodrigo
 */
public enum EDataType {

    SALESMAN("001", "ç", 4),
    CLIENT("002", "ç", 4),
    SALE("003", "ç", 4),
    LIST_OF_ITEM("", ",", 0),
    ITEM("", "-", 3);

    private String id;
    private String separator;
    private int length;

    private EDataType(String id, String separator, int length) {
        this.id = id;
        this.separator = separator;
        this.length = length;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

}
