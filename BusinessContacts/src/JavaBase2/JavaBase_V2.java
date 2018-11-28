package JavaBase2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * @author cody barr, c.j. meakim
 */
public class JavaBase_V2 
{
    private ArrayList<Table_V2> tables;
    /*public static void main(String[] args) 
    {
        JavaBase_V2 jb=new JavaBase_V2();
        jb.createTable("Contacts", "First_Name,Last_Name,Company,Phone_Number,Email");
        try
        {
            //This tests the insert and update functions.
            jb.insert("Contacts","First_Name,Last_Name,Company,Phone_Number,Email",
                    "Jane,Doe,ACME,8675309,jDoey@hotmail.com",0);
            jb.insert("Contacts","First_Name,Last_Name,Company,Phone_Number,Email",
                    "John,Smith,ACME,911,JohnSmith@hotmail.com",0);
            jb.insert("Contacts","First_Name,Last_Name,Company,Phone_Number,Email",
                    "Bob,Jones,Taco Bell,1800Tacos,spicy@outlook.com",0);
            System.out.println(jb);//This will show the updated database.
            jb.writeFile("save.bin");
            Table_V2 query=jb.retrieve("Contacts","*","*");
            String[][] multi=jb.getMultiArray(query);
            
            
            //This tests the retrieve function which returns a query table.
            //Table query = jb.retrieve("Dogs", "Breed,Color", "Owner=John AND Color=white");
            //System.out.println(query);
            
            //These test EQUALSIGNORECASE, CONTAINSIGNORECASE, and CONTAINS clauses.
            //Table_V2 queryEqualsIgnore = jb.retrieve("Dogs", "Owner,Breed", "OwnerEQUALSIGNORECASEjoHN");
            //System.out.println(queryEqualsIgnore);
            //Table_V2 queryContainsIgnore = jb.retrieve("Dogs", "Owner,Breed", "OwnerCONTAINSIGNORECASEOh");
            //System.out.println(queryContainsIgnore);
            //Table_V2 queryContains = jb.retrieve("Dogs", "Owner,Breed", "OwnerCONTAINSoh");
            //System.out.println(queryContains);
            
            //These test > and <.
            //Table_V2 queryGreater = jb.retrieve("Cats","*", "Size>5 OR OwnerCONTAINSIGNORECASEiLl");
            //System.out.println(queryGreater);
            //Table_V2 queryLess = jb.retrieve("Cats", "*", "Size<10 OR ColorCONTAINSIGNORECASEyELl");
            //System.out.println(queryLess);
            
            //This test the delete function which can return a table.
            //jb.delete("Dogs", "ColorEQUALSIGNORECASEblue OR OwnerCONTAINSohn");
            //System.out.println(jb);
        }
        catch(Exception e)
        {
           e.printStackTrace();
        }
    }*/
    public JavaBase_V2()
    {
        tables=new ArrayList();
    }
    public String[] getTables()
    {
        String[] tables=new String[this.tables.size()];
        for (int i = 0; i < tables.length; i++) {
            tables[i]=this.tables.get(i).getName();
        }
        return tables;
    }
    public String[][] getMultiArray(Table_V2 table)
    {
        int col=table.getFields().size();
        int row=table.getFields().get(0).getData().size();
        String[][] multi=new String[row][col];
        for (int i = 0; i < col; i++) 
        {
            for (int j = 0; j < row; j++) 
            {
                multi[j][i]=(String)table.getFields().get(i).getData().get(j);
            }
        }
        return multi;
    }
    
   public void insert(String tableName, String fieldNames, String values, Integer row)throws JBInputException_V2
    {
        int loc=-1;
        if (fieldNames.split(",").length != values.split(",").length)
        {
            System.out.println("Must provide values for all fields.");
            return;
        }
        for(int i=0; i<tables.size(); i++)
        {
            if(tables.get(i).getName().equals(tableName))
            {
                loc=i;
                break;
            }
        }
        if(loc>=0)
            tables.get(loc).insert(values, fieldNames,row);
        else
            System.out.println(tableName+" does not exist in this database");
    }
    public Table_V2 createTable(String tableName, String fieldName)
    {
        Table_V2 temp= new Table_V2(tableName, fieldName);
        tables.add(temp);
        return temp;
    }
    public Table_V2 createTable(String tableName, Table_V2 tempTable)
    {
        tempTable.setName(tableName);
        tables.add(tempTable);
        return tempTable;
    }
    public Table_V2 retrieve(String tableName, String fieldNames, String where )throws JBInputException_V2
    {
        Table_V2 query=null;

        for (int i = 0; i < tables.size(); i++) {
            String name = tables.get(i).getName();
            if (name.equals(tableName)) 
            {
                query = tables.get(i).retrieve(fieldNames, where);
                break;
            }
        }
        if(query==null)
            throw new JBInputException_V2(tableName+" was not found in the database");
        return query;
    }

    public void update(String tableName, String fieldNames,String values, String where)
    {
        Table_V2 temp=null;
        for(int i=0; i<tables.size(); i++)
        {
            temp=tables.get(i);
            if(temp.getName().equals(tableName))
                temp.update(fieldNames,values, where);
        }
    }
    /*JavaBase delete method will not call the Table delete method until both
    *the table name and desired row number are verified. If either entered
    *value turns out to be invalid, an error message will print saying so and the
    *method will quit.
    */
    public Table_V2 delete(String tableName, String where)
    {
        Table_V2 tableBoy = null;
        boolean tableExists = false;
        for (int i = 0; i < tables.size(); i++)
        {
            tableBoy = tables.get(i);
            if (tableBoy.getName().equals(tableName))
            {
                tableExists = true;
                tableBoy.delete(where);
                break;
            }
        }
        if (!tableExists)
            System.out.println("Table " + tableName + " does not exist.");
        return tableBoy;
    }
    public Table_V2 delete(String tableName,int row)
    {
        Table_V2 tableBoy = null;
        boolean tableExists = false;
        for (int i = 0; i < tables.size(); i++)
        {
            tableBoy = tables.get(i);
            if (tableBoy.getName().equals(tableName))
            {
                tableExists = true;
                tableBoy.delete(row);
                break;
            }
        }
        if (!tableExists)
            System.out.println("Table " + tableName + " does not exist.");
        return tableBoy;
    }
    public void delete(String tableName)
    {
        for (int i = 0; i < tables.size(); i++) {
            Table_V2 temp=tables.get(i);
            if(temp.getName().equals(tableName))
            {
                tables.remove(i);
                break;
            }
        }
        
    }

    public void readFile(String fileName)
    {
        File file=new File(fileName);
        try
        {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            tables = (ArrayList<Table_V2>)ois.readObject();
            ois.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

        // writeFile() - This method writes all of the data in list to the file CatData.cat.
        // If the file does not exist, it is created.
        // If the file already exists, it is overwritten.
    public void writeFile(String fileName)
    {
        File file=new File(fileName);
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(tables);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public String toString()
    {
        String out="JavaBase \n";
        for(Table_V2 e: tables)
        {
            out+=e.toString()+"\n";
        }
        return out;
    }
}