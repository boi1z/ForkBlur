package JavaBase2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author cody barr, c.j. meakim
 */
public class Table_V2 implements Serializable
{
    
    private ArrayList<Field_V2> fields;
    private String name;

    public ArrayList<Field_V2> getFields()
    {
        System.out.println("");
        return fields;
    }
    /*Won't need to use setFields since array lists have built in add and romove
    *functions.
    */
    public void setFields(ArrayList<Field_V2> fields)
    {
        this.fields = fields;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name.trim();
    }
    public Table_V2(String name)
    {
        this.name=name;
        this.fields=new ArrayList();
    }
    public Table_V2(String name, String fields)
    {
        this.name=name;
        this.fields=new ArrayList();
        String[] fieldsplit=fields.split(",");
        for(int i=0; i<fieldsplit.length; i++)
        {
            String splitName=fieldsplit[i].trim();//had to add trim in case the user types an extra space at the begginning or end
            Field_V2 temp=new Field_V2(splitName);
            this.fields.add(temp);
        }
    }
    public void insert(String values, String fieldNames, Integer row)throws JBInputException_V2
    {
        if (fieldNames.split(",").length != fields.size())
        {
            System.out.println("Must give every field in the table.");
            return;
        }
        String[] fieldSplit=fieldNames.split(",");
        String[] valueSplit=values.split(",");
        for (int i = 0; i < fieldSplit.length; i++)
        {
            for (int j = 0; j < fields.size(); j++)
            {
                Field_V2 temp = fields.get(j);
                if (temp.getName().equals(fieldSplit[i]))
                {
                    temp.insert(valueSplit[i].trim(), row);
                    break;
                }
            }
        }
    }
    public Table_V2 retrieve(String fieldNames, String where ) throws JBInputException_V2
    {
        String[] fieldSplit = fieldNames.split(",");
        int[] location;
        if(!(fieldNames.equals("*")))
            location = new int[fieldSplit.length];//location of the fields in the fields arraylist
        else{
            location=new int[fields.size()];//the location is set to the location of every field if given *
            for (int i = 0; i < location.length; i++) {
                location[i]=i;
            }
        }
        ArrayList<Integer> whereLoc = getWhere(where);
        Table_V2 queryTable=new Table_V2(this.name);
        
        for (int i = 0; i < location.length; i++)//For every field name listed **************changed from fieldSplit.length
        {
            if(fieldNames.equals("*"))
                break;
            int loc = -1;
            for (int j = 0; j < fields.size(); j++)//For field in table 
            {
                if (fields.get(j).getName().equals(fieldSplit[i]))
                {
                    location[i] = j;
                    loc = j;
                    break;
                }
            }
            if (loc == -1)
                throw new JBInputException_V2(fieldSplit[i]+" is not in "+this.name);
        }
        for (int i = 0; i < location.length; i++)//builds the fields and table to output
        {
            Field_V2 temp = fields.get(location[i]);
            Field_V2 queryField=new Field_V2(temp.getName());
            for (int j = 0; j < whereLoc.size(); j++)
                queryField.getData().add(temp.getData().get(whereLoc.get(j)));
            queryTable.fields.add(queryField);
        }
        
        return queryTable;
    }
    public void update(String fieldNames, String values, String where)
    {
        ArrayList whereLoc= this.getWhere(where);
        String[] fieldSplit=fieldNames.split(",");
        String[] valueSplit=values.split(",");
        for(int i=0; i<fieldSplit.length; i++)//for every field that was passed
        {
            for(int j=0; j<fields.size(); j++)//find the correct field in the ar list
            {
                Field_V2 temp=fields.get(j);//changed from i to j
                if(temp.getName().equals(fieldSplit[i].trim()))
                {
                    for(int k=0; k<whereLoc.size(); k++)//set the given value everywhere that is applicable
                        temp.getData().set((int)whereLoc.get(k), valueSplit[i].trim());
                }
            }    
        }
        
    }
    //This is the bad boy that will actually do the deleting
    public void delete(String where)
    {
        ArrayList whereLoc= this.getWhere(where);
        for (int i = 0; i < fields.size(); i++) //for every field in table
        {
            Field_V2 temp=fields.get(i);
            for (int j = 0; j < whereLoc.size(); j++) //for every row in the field
            {
                temp.getData().set((Integer)whereLoc.get(j),"null");
            }
            temp.delete();
        }
    }
    public void delete(int row)
    {
        for (int i = 0; i < fields.size(); i++) //for every field in table
        {
            Field_V2 temp=fields.get(i);
            temp.delete(row);
        }
    }
    public String toString()
    {
        String out="Table: "+name+"\n";
        for(Field_V2 e: fields)
        {
            out+=e.getName()+" ";
        }
        out+="\n";
        for(int i=0; i<fields.get(0).getData().size(); i++)
        {
            for(int j=0; j<fields.size(); j++)
            {
                if(!(fields.get(j).getData().get(i)==null))
                    out+=fields.get(j).getData().get(i).toString()+" ";
                else
                    out+=" ";
            }
            out+="\n";
        }
        return out;
    }
    private ArrayList<Boolean> getBoolList(String where)
    {
        ArrayList<Boolean> whereLoc = new ArrayList<Boolean>();
        if(where.contains("EQUALSIGNORECASE"))
        {
            String field = where.split("EQUALSIGNORECASE")[0].trim();
            String value = where.split("EQUALSIGNORECASE")[1].trim();
            value=value.toLowerCase();
            for (int i = 0; i < fields.size(); i++)
            {
                Field_V2 temp = fields.get(i);
                if (temp.getName().equals(field))
                {
                    for (int j = 0; j < temp.getData().size(); j++)
                    {
                        String current=(String)temp.getData().get(j);
                        current=current.toLowerCase();
                        if (current.equals(value))
                                whereLoc.add(true);
                        else
                            whereLoc.add(false);
                    }
                    break;
                }
            }
        }
        else if(where.contains("CONTAINSIGNORECASE"))
        {
            String field = where.split("CONTAINSIGNORECASE")[0].trim();
            String value = where.split("CONTAINSIGNORECASE")[1].trim();
            value=value.toLowerCase();
            for (int i = 0; i < fields.size(); i++)
            {
                Field_V2 temp = fields.get(i);
                if (temp.getName().equals(field))
                {
                    for (int j = 0; j < temp.getData().size(); j++)
                    {
                        String current=(String)temp.getData().get(j);
                        current=current.toLowerCase();
                        if (current.contains(value))
                                whereLoc.add(true);
                        else
                            whereLoc.add(false);
                    }
                    break;
                }
            }
        }
        else if(where.contains("CONTAINS"))
        {
            String field = where.split("CONTAINS")[0].trim();
            String value = where.split("CONTAINS")[1].trim();

            for (int i = 0; i < fields.size(); i++)
            {
                Field_V2 temp = fields.get(i);
                if (temp.getName().equals(field))
                {
                    for (int j = 0; j < temp.getData().size(); j++)
                    {
                        String current=(String)temp.getData().get(j);
                        if (current.contains(value))
                            whereLoc.add(true);
                        else
                            whereLoc.add(false);
                    }
                    break;
                }
            }
        }
        else if(where.contains(">"))
        {
            String[] clause = where.split(">");
            String field = clause[0].trim();
            String value = clause[1].trim();
            
            boolean isInt = false;
            boolean isDouble = false;
            
            try {
                double valueDouble = Double.parseDouble(value);
                isDouble = true;
            } catch (Exception e) {}
            try {
                int valueInt = Integer.parseInt(value);
                isInt = true;
            } catch (Exception e) {}
            
            if (isInt && isDouble)
                isInt = false;
            
            if (isInt)
            {
                for (int j = 0; j < fields.size(); j++)
                {
                    if(fields.get(j).getName().equals(field))
                    {
                        //System.out.println("Size of data fields is: " + fields.get(j).getData().size());
                        for (int k = 0; k < fields.get(j).getData().size(); k++)
                        {
                            //System.out.println("Int is: " + Integer.parseInt(fields.get(j).getData().get(k).toString()));
                            //System.out.println("Value is: " + Integer.parseInt(value));
                            int valueToBeTested = Integer.parseInt(fields.get(j).getData().get(k).toString().trim());
                            int valueParameter = Integer.parseInt(value);
                            if (valueToBeTested > valueParameter)
                            {
                                //System.out.println("True");
                                whereLoc.add(true);
                            }
                            else
                            {
                                //System.out.println("False");
                                whereLoc.add(false);
                            }
                        }
                    }
                }
            }
            else if (isDouble)
            {
                for (int j = 0; j < fields.size(); j++)
                {
                    if (fields.get(j).getName().equals(field))
                    {
                        for (int k = 0; k < fields.get(j).getData().size(); k++)
                        {
                            double valueToBeTested = Double.parseDouble(fields.get(j).getData().get(k).toString().trim());
                            double valueParameter = Double.parseDouble(value);
                            if (valueToBeTested > valueParameter)
                                whereLoc.add(true);
                            else
                                whereLoc.add(false);
                        }
                    }
                }
            }
            else
                System.out.println("Invalid Input: Needs to be a number.");
        }
        else if(where.contains("<"))
        {
            String[] clause = where.split("<");
            String field = clause[0].trim();
            String value = clause[1].trim();
            
            boolean isInt = false;
            boolean isDouble = false;
            
            try {
                int valueInt = Integer.parseInt(value);
                isInt = true;
            } catch (Exception e) {}
            try {
                double valueDouble = Double.parseDouble(value);
                isDouble = true;
            } catch (Exception e) {};
            
            if (isInt && isDouble)
                isDouble = false;
            
            if (isInt)
            {
                for (int j = 0; j < fields.size(); j++)
                {
                    if(fields.get(j).getName().equals(field))
                    {
                        //System.out.println("Size of data fields is: " + fields.get(j).getData().size());
                        for (int k = 0; k < fields.get(j).getData().size(); k++)
                        {
                            //System.out.println("Int is: " + Integer.parseInt(fields.get(j).getData().get(k).toString()));
                            //System.out.println("Value is: " + Integer.parseInt(value));
                            int valueToBeTested = Integer.parseInt(fields.get(j).getData().get(k).toString().trim());
                            int valueParameter = Integer.parseInt(value);
                            if (valueToBeTested < valueParameter)
                            {
                                //System.out.println("True");
                                whereLoc.add(true);
                            }
                            else
                            {
                                //System.out.println("False");
                                whereLoc.add(false);
                            }
                        }
                    }
                }
            }
            else if (isDouble)
            {
                for (int j = 0; j < fields.size(); j++)
                {
                    if (fields.get(j).getName().equals(field))
                    {
                        for (int k = 0; k < fields.get(j).getData().size(); k++)
                        {
                            double valueToBeTested = Double.parseDouble(fields.get(j).getData().get(k).toString().trim());
                            double valueParameter = Double.parseDouble(value);
                            if (valueToBeTested < valueParameter)
                                whereLoc.add(true);
                            else
                                whereLoc.add(false);
                        }
                    }
                }
            }
            else
                System.out.println("Invalid Input: Needs to be a number.");
        }
        else//not AND, OR, CONTAINS, CONTAINSIGNORECASE, or EQUALSIGNORECASE
        {
            String field = where.split("=")[0].trim();
            String value = where.split("=")[1].trim();

            for (int i = 0; i < fields.size(); i++)
            {
                Field_V2 temp = fields.get(i);
                if (temp.getName().equals(field))
                {
                    for (int j = 0; j < temp.getData().size(); j++)
                    {
                        if (temp.getData().get(j).equals(value))
                            whereLoc.add(true);
                        else
                            whereLoc.add(false);
                    }
                    break;
                }
            }
        }
        
        if(whereLoc.isEmpty())
        {
            for (int i = 0; i < fields.get(0).getData().size(); i++)
                whereLoc.add(false);
        }
        return whereLoc;
    }
    private ArrayList<Integer> getWhere(String where)
    {
        ArrayList<Integer> whereLoc = new ArrayList<Integer>();
        if (!where.equals("*"))
        {
            if (where.contains(" OR "))
            {
                String[] split = where.split(" OR ");
                String side1=split[0];
                String side2=split[1];
                //System.out.println("Side1: " + side1 + " || Side2:" + side2);
                ArrayList<Boolean> bOne=getBoolList(side1);
                ArrayList<Boolean> bTwo=getBoolList(side2);
                //System.out.println("bOne size = " + bOne.size() + " || bTwo size =" + bTwo.size());
                for (int i = 0; i < fields.get(0).getData().size(); i++)
                {
                    if(bOne.get(i)||bTwo.get(i))
                        whereLoc.add(i);
                }
            }
            if (where.contains(" AND "))
            {
                String[] split = where.split(" AND ");
                String side1=split[0];
                String side2=split[1];
                ArrayList<Boolean> bOne=getBoolList(side1);
                ArrayList<Boolean> bTwo=getBoolList(side2);
                //System.out.println("bOne size = " + bOne.size() + " || bTwo size =" + bTwo.size());
                for (int i = 0; i < fields.get(0).getData().size(); i++)
                {
                    if(bOne.get(i)&&bTwo.get(i))
                        whereLoc.add(i);
                }
            }
            else
            {//not AND or OR
                ArrayList<Boolean> bool=getBoolList(where);
                //System.out.println("Bool size is: " + bool.size());
                for (int i = 0; i < fields.get(0).getData().size(); i++)
                {
                    //System.out.println(bool.get(i));
                    if(bool.get(i))
                        whereLoc.add(i);
                }
            }
            
        }//end if where!=*
        else
        {
            for (int i = 0; i < fields.get(0).getData().size(); i++)
                whereLoc.add(i);
        }
        Collections.sort(whereLoc);
        return whereLoc;
    }
}