import java.io.*;
import java.sql.*;
import java.util.ArrayList;

import static java.lang.Math.abs;

public class SiddhiMetrics {

    static final String DB_URL = "jdbc:mysql://localhost:3306/MetricsDB?autoReconnect=true&useSSL=false";
    static final String USER = "sarangan";
    static final String PASS = "sarangan";
    Connection conn=null;


    public void individualSiddhiMetric(ResultSet rs1, ArrayList<ArrayList<Long>> arrayList_exec_paral ){

        try {
            int k=0;
            while (rs1.next() && (k<3317)) {
                k=k+1;
                long iij = rs1.getLong("iijtimestamp");
                long m2 = rs1.getLong("m2");
                long m3 = rs1.getLong("m3");
                long m4 = rs1.getLong("m4");
                long m5 = rs1.getLong("m5");
                long m6 = rs1.getLong("m6");
                long m7 = rs1.getLong("m7");
                long m8 = rs1.getLong("m8");
                long m9 = rs1.getLong("m9");
                long m10 = rs1.getLong("m10");
                long m11 = rs1.getLong("m11");
                long m12 = rs1.getLong("m12");
                long m13 = rs1.getLong("m13");
                long m14 = rs1.getLong("m14");
                long m15 = rs1.getLong("m15");

                ArrayList<Long> temp = new ArrayList<Long>();

                temp.add(iij);
                temp.add(m2);
                temp.add(m3);
                temp.add(m4);
                temp.add(m5);
                temp.add(m6);
                temp.add(m7);
                temp.add(m8);
                temp.add(m9);
                temp.add(m10);
                temp.add(m11);
                temp.add(m12);
                temp.add(m13);
                temp.add(m14);
                temp.add(m15);

                arrayList_exec_paral.add(temp);
            }
        }catch( SQLException e){
            e.printStackTrace();}
    }




    public void siddhiProcessing( ArrayList<Long>tmp_standard_array,
                                  ArrayList<Long>xValue_standard_arrayFile ,
                                  ArrayList<Long>xValue_standard_arrayFile_s,
                                  File file,int exec,int para) {

        try {
            boolean flag=false;

            //DB configuration
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement st = conn.createStatement();

            //File configuration
            BufferedWriter bw = null;
            FileWriter fw = null;
            if (!file.exists()) {
                file.createNewFile();
            }
            fw = new FileWriter(file.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);


            bw.write("Average Time stamp, " +
                    "Standardized X value(s), " +
                    "Standardized X value(min)," +
                    "Throughput in this window (thousands events/second), " +
                    "Entire throughput for the run (thousands events/second), " +
                    "Total elapsed time(s)," +
                    "Total Events," +
                    "Average latency per event in this window(ms)," +
                    "Entire Average latency per event for the run(ms), " +
                    "AVG latency from start (90), " +
                    "AVG latency from start(95), " +
                    "AVG latency from start (99), " +
                    "AVG latency in this window(90), " +
                    "AVG latency in this window(95), " +
                    "AVG latency in this window(99), " +
                    "Total memory with the Oracle JVM, " +
                    "Free memory with the Oracle JVM " );

            bw.write("\n");
            bw.flush();


            ArrayList<ArrayList<Long>> arrayList_1_1 = new ArrayList<ArrayList<Long>>();
            String query1 = "SELECT * FROM metricstable where exec=" + exec+ " and paralllel=" + para;
            ResultSet rs1 = st.executeQuery(query1);
            individualSiddhiMetric(rs1, arrayList_1_1);

            ArrayList<ArrayList<Long>> arrayListRefined = new ArrayList<ArrayList<Long>>();



            //To consider all the values of siddhi metrics
            if (tmp_standard_array.size() < arrayList_1_1.size()){

                long len1=arrayList_1_1.size();
                long len2 =tmp_standard_array.size();


                int shift= (int)(len1/len2);

                while (shift>1){
                    flag=true;
                    arrayListRefined.add(arrayList_1_1.get((shift-1)));
                    shift=shift+1;

                    if(shift==arrayList_1_1.size()){
                        break;
                    }
                }
            }

            if (flag){
                fileWriting(tmp_standard_array,xValue_standard_arrayFile,
                        xValue_standard_arrayFile_s,arrayListRefined,bw);

            }else{
                fileWriting(tmp_standard_array,xValue_standard_arrayFile,
                        xValue_standard_arrayFile_s,arrayList_1_1,bw);
            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch(IndexOutOfBoundsException e) {
        }
    }


    public void fileWriting( ArrayList<Long>tmp_standard_array,
                             ArrayList<Long>xValue_standard_arrayFile,
                             ArrayList<Long>xValue_standard_arrayFile_s,
                             ArrayList<ArrayList<Long>> arrayList_1_1,
                             BufferedWriter bw){

        try {

            for (int i = 0; i < arrayList_1_1.size(); i++) {
                StringBuilder tempStringBuilder = new StringBuilder();
                tempStringBuilder.append(tmp_standard_array.get(i));
                tempStringBuilder.append(",");
                tempStringBuilder.append(xValue_standard_arrayFile.get(i));
                tempStringBuilder.append(",");
                tempStringBuilder.append(xValue_standard_arrayFile_s.get(i));
                tempStringBuilder.append(",");

                for (int j = 1; j < arrayList_1_1.get(i).size(); j++) {

                    double temp = standarizeValues(arrayList_1_1.get(i).get(0), arrayList_1_1.get(i).get(1),
                            tmp_standard_array.get(i), arrayList_1_1.get(i).get(j),
                            arrayList_1_1.get(i).get(j));
                    tempStringBuilder.append(temp);
                    tempStringBuilder.append(",");


                }

                System.out.println("Writing the data");

                bw.write(tempStringBuilder.toString());
                bw.write("\n");
                bw.flush();

                tempStringBuilder.setLength(0);
            }
        }catch (IndexOutOfBoundsException e){e.getMessage();}
        catch (IOException e) { }

    }



    public double standarizeValues(long t1,long t2,long t3, double d1,double d2){
        double d3=0;
        try {
            d3 = d3 + ((abs(t3 - t2) / abs(t2 - t1)) * abs(d2 - d1)) + d2;
        }
        catch(ArithmeticException e){
            d3 = d1;
        }
        return d3;
    }
}



