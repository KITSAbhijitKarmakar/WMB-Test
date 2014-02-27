/*
 * Field.java
 *
 * Created on 02 November 2004, 16:49
 */

package com.bq.mb.utils.fileconversions;

/**
 *
 * @author  stantr01
 */
public class Field implements Comparable
{
    public String name;
    public int length;
    public String type;
    public String value;
    public int startPosition;
    
    /** Creates a new instance of Field */
    public Field() {
    }
    
    public Field(String name, int startPosition, int length, String type)
    {
        this.name = name;
        this.length = length;
        this.type = type;
        this.startPosition = startPosition;
    }
    
    public void setValue(String value)
    {
        this.value = value;
    }
    
    public String getValue(String value)
    {
        return value;
    }
    public void setStartPosition(int startPosition)
    {
        this.startPosition = startPosition;
    }
    
    public int getStartPosition()
    {
        return startPosition;
    }
    //Malay TCS: Added for IBMigration project
    public int compareTo(Object O){
    	Field compareField = (Field)O;
    	int compareStartPos = compareField.getStartPosition();
    	return this.startPosition - compareStartPos;
    }
}
