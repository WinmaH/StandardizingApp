import java.io.*;
import java.util.ArrayList;

public class FileRead {

    public void sfileRead(File source_file,ArrayList<Double>array,ArrayList<Long>tmp_standard_array) throws IOException{

        int j=0;

        try (BufferedReader reader = new BufferedReader(new FileReader(source_file))) {
            while (true) {
                String line = reader.readLine();
                //System.out.println("This is the line" + line);


                try {
                    if (line.contains("Start") || line.contains("gke")) {
                        continue;
                    }
                }
                catch(NullPointerException e){}

                if (line == null) {
                    break; }


                try {
                    long  avgtimestamp=Integer.parseInt(line.substring(25, 35));
                    //System.out.println("Line" + avgtimestamp);
                    tmp_standard_array.add(avgtimestamp);
                    Double  value=Double.parseDouble(line.substring(37,line.length()-2));
                    array.add(value);

                }
                catch (StringIndexOutOfBoundsException e){}
                catch(NullPointerException e){}
                catch(NumberFormatException e){}



            }
        }

        int i=0;
    }




    public void fileRead(File source_file,ArrayList<Double>array) throws IOException{

        int j=0;

        try (BufferedReader reader = new BufferedReader(new FileReader(source_file))) {
            while (true) {
                String line = reader.readLine();

                try {
                    if (line.contains("compute.googleapis.com")) {
                        continue;
                    }
                }
                catch(NullPointerException e){}

                if (line == null) {
                    j=j+1; }

                if(j==2){
                    break; }



                try {

                    Double  value=Double.parseDouble(line.substring(37,line.length()-2));
                    array.add(value);
                }



                catch (StringIndexOutOfBoundsException e){}
                catch(NullPointerException e){}
                catch(NumberFormatException e){}



            }






        }catch(FileNotFoundException e){}
    }









    public void fileRead_cpuutil(File source_file,ArrayList<Double>array,ArrayList<Double>array2) throws IOException{

        int j=0;

        try (BufferedReader reader = new BufferedReader(new FileReader(source_file))) {
            while (true) {
                String line = reader.readLine();

                try {
                    if (line.contains("compute.googleapis.com")) {
                        continue;
                    }
                }
                catch(NullPointerException e){}

                if (line == null) {
                    j=j+1; }

                if(j==2){
                    break; }



                try {

                    Double  value=Double.parseDouble(line.substring(37,line.length()-2));
                   // System.out.println("CPU uti"+ value);
                    //System.out.println(line);
                    array2.add(value);
                    if(array2.size()>1){
                        value=(value+array2.get(0))/2;
                        array.add(value);
                        array2.remove(0);

                    }

                }



                catch (StringIndexOutOfBoundsException e){}
                catch(NullPointerException e){}
                catch(NumberFormatException e){}



            }
        }
    }


    public void fileRead_cpu_usage_cont(File source_file,ArrayList<Double>array,ArrayList<Double>array2,ArrayList<Double>array3) throws IOException{

        int j=0;

        try (BufferedReader reader = new BufferedReader(new FileReader(source_file))) {
            while (true) {
                String line = reader.readLine();

                try {
                    if (line.contains("compute.googleapis.com")) {
                        continue;
                    }
                }
                catch(NullPointerException e){}

                if (line == null) {
                    j=j+1; }

                if(j==2){
                    break; }



                try {

                    Double  value=Double.parseDouble(line.substring(37,line.length()-2));
                    array2.add(value);
                    if(array2.size()>1){
                        value=(value+array2.get(0))/2;
                        array2.add(1,value);
                        double value1=(array2.get(0)+array2.get(1))/2;
                        double value2=(array2.get(1)+array2.get(2))/2;

                        array.add(value1);
                        array.add(value2);
                        array2.remove(0);
                        array2.remove(0);

                    }

                }



                catch (StringIndexOutOfBoundsException e){}
                catch(NullPointerException e){}
                catch(NumberFormatException e){}



            }
        }

        for( int i=0;i<array.size()-1;i++){
            try {
                array3.add(array.get(i) - array.get(i+1));
            }
            catch(IndexOutOfBoundsException e){}
        }


    }




    public void fileRead_memory_usage(File source_file,ArrayList<Double>array,ArrayList<Double>array2) throws IOException{
        int j=0;

        try (BufferedReader reader = new BufferedReader(new FileReader(source_file))) {
            while (true) {
                String line = reader.readLine();
                try {
                    if (line.contains("compute.googleapis.com")) {
                        continue;
                    }
                } catch(NullPointerException e){}

                if (line == null) {
                    j=j+1;
                }

                if(j==2) {
                    break;
                }

                try {

                    Double  value=Double.parseDouble(line.substring(37,line.length()-2));
                    array2.add(value);
                    if(array2.size()>1){
                        value=(value+array2.get(0))/2;
                        array2.add(1,value);
                        double value1=(array2.get(0)+array2.get(1))/2;
                        double value2=(array2.get(1)+array2.get(2))/2;
                        array.add(value1);
                        array.add(value2);
                        array2.remove(0);
                        array2.remove(0);

                    }

                }catch (StringIndexOutOfBoundsException e){}
                catch(NullPointerException e){ }
                catch(NumberFormatException e){}
            }
        }
    }
}
