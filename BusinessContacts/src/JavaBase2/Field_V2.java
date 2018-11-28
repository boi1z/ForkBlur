package JavaBase2;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author cody barr & cj meakim
 */
public class Field_V2 implements Serializable 
{
    private ArrayList data;
    private String name;

    public ArrayList getData()
    {
        return data;
    }
    public void setData(ArrayList data)
    {
        this.data = data;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public Field_V2(String name)
    {
        this.name=name;
        data=new ArrayList();
        //this.dataType=dataType;
    }
    public void insert(String values, Integer row)
    {
        if(row!=null)
        {
            if(row>data.size()-1)
               row=data.size();
            else if(row<data.size())
                row=0;
            data.add(row,values); 
        }
        else
            data.add(values);
    }
    public void delete()
    {
        while(data.contains("null"))
        {
        for (int i = 0; i < data.size(); i++) {
            if(data.get(i).equals("null"))
            {
                data.remove(i);
            }
        }
        }
    }
    public void delete(int rowNum)
    {
        data.remove(rowNum);
    }
    public String toString()
    {
        String out=name+" "+data.size()+" ";
        for(Object e:data)
        {
            if(e!=null)
                out+=e.toString()+" ";
            else
                out+=" ";
        }
        return out;
    }
}