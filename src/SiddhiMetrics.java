import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import static java.lang.Math.abs;

public class SiddhiMetrics {

    static final String DB_URL = "jdbc:mysql://localhost:3306/performance?autoReconnect=true&useSSL=false";
    static final String USER = "root";
    static final String PASS = "87654321";
    Connection conn=null;


    public void individualSiddhiMetric(ResultSet rs1, ArrayList<ArrayList<Long>> arrayList_exec_paral ){

        try {
            int k=0;
            while (rs1.next() && (k<3317)) {
                k=k+1;
                long iij = rs1.getLong("iijtimestamp");
                long m2 = (long)(rs1.getFloat("m2")*1000);
                long m3 = (long)(rs1.getFloat("m3")*1000);
//                System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW"+m2);
               // System.out.println("!!!!!!!!!!!!!!!!"+rs1.getFloat("m3"));
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
                long m16 = (long)(rs1.getFloat("m16")*1000);

                ArrayList<Long> temp = new ArrayList<Long>();

                temp.add(iij);
                //temp.add(m1);
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
                temp.add(m16);

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
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            fw = new FileWriter(file.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);


            bw.write("Average Time stamp, " +
                    "Standardized X value(s), " +
                    "Standardized X value(min)," +
                    "Throughput in this window (*1000), " +
                    "Entire throughput for the run (*1000), " +
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
                    "Free memory with the Oracle JVM , " +
                    "CPU Usage (*1000)" );

            bw.write("\n");
            bw.flush();


            ArrayList<ArrayList<Long>> arrayList_1_1 = new ArrayList<ArrayList<Long>>();
            String query1 = "SELECT * FROM metricstable where exec=" + exec+ " and parallel=" + para;
            ResultSet rs1 = st.executeQuery(query1);
            individualSiddhiMetric(rs1, arrayList_1_1);

            ArrayList<ArrayList<Long>> arrayListRefined = new ArrayList<ArrayList<Long>>();



            //To consider all the values of siddhi metrics
//            if (tmp_standard_array.size() < arrayList_1_1.size()){


                long len1=arrayList_1_1.size();
                long len2 =tmp_standard_array.size();
                int shift= (int)(len2/len1);

                if (len1 < len2){
                    flag = true;
                    int standardIndex = 0;
                    while(standardIndex < arrayList_1_1.size()){

                        arrayListRefined.add(arrayList_1_1.get(standardIndex));
                        standardIndex += shift;

                    }
                }



              System.out.println("Added to Refined  &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& "+ arrayListRefined.size() +"-original"+ arrayList_1_1.size()+ "-stand"+tmp_standard_array.size());
 //               flag=true;

//            }




            fileWriting(tmp_standard_array,xValue_standard_arrayFile,
                    xValue_standard_arrayFile_s,arrayList_1_1,bw);

//            if (flag){
//                fileWriting(tmp_standard_array,xValue_standard_arrayFile,
//                        xValue_standard_arrayFile_s,arrayListRefined,bw);
//
//            }else{
//                fileWriting(tmp_standard_array,xValue_standard_arrayFile,
//                        xValue_standard_arrayFile_s,arrayList_1_1,bw);
//            }


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

        ArrayList<Long> tmp_standard_array_reversed;
        tmp_standard_array_reversed = tmp_standard_array;

        //Collections.reverse(tmp_standard_array_reversed);

        int size = tmp_standard_array_reversed.size();
        for (int reverseIndex = 0; reverseIndex < reverseIndex / 2; reverseIndex++) {
            final Long number = tmp_standard_array_reversed.get(reverseIndex);
            tmp_standard_array_reversed.set(reverseIndex, tmp_standard_array_reversed.get(size - reverseIndex - 1)); // swap
            tmp_standard_array_reversed.set(size - reverseIndex - 1, number); // swap
        }





        try {

            for (int i = 0; i < tmp_standard_array_reversed.size(); i++) {
                StringBuilder tempStringBuilder = new StringBuilder();
                tempStringBuilder.append(tmp_standard_array_reversed.get(i));
                tempStringBuilder.append(",");
                tempStringBuilder.append(xValue_standard_arrayFile.get(i));
                tempStringBuilder.append(",");
                tempStringBuilder.append(xValue_standard_arrayFile_s.get(i));
                tempStringBuilder.append(",");
                System.out.println("^^^^^^^^"+tmp_standard_array_reversed.get(i));


                for (int j = 1; j < arrayList_1_1.get(i).size(); j++) {


//                    double[] a = new double[arrayList_1_1.size()];
//                    double[] b = new double[arrayList_1_1.size()];
//
//                    for (int k = 0; k < arrayList_1_1.size(); k++) {
//                        a[k]=arrayList_1_1.get(k).get(0);
//                        System.out.println("(((((((((((((((((("+arrayList_1_1.get(k).get(j));
//                        b[k]=arrayList_1_1.get(k).get(j);
//                    }
//
//                    SplineInterpolator interpolator = new SplineInterpolator();
//                    PolynomialSplineFunction estimateFunc = interpolator.interpolate(a,b);

                        long t3= tmp_standard_array_reversed.get(i);
                        long t2=-1 ;
                        long t1 = -1;
                        double temp = -1;
                        boolean equality = false;
                        int indexD1=-1;
                        int indexD2=-1;
                        long d1= -1;
                        long d2= -1;
                    for (int k = 0; k < arrayList_1_1.size(); k++) {
                        if(arrayList_1_1.get(k).get(0)/1000 == t3) {
                            temp = t3;
                            equality = true;
                            break;
                        }
                    }
//                    for(int g=0; g<arrayList_1_1.size();g++)
//                    {
//                        System.out.println("----"+arrayList_1_1.get(g).get(0)/1000);}


                    if(!equality){
                        for (int k = 1; k < arrayList_1_1.size(); k++) {
                            if(arrayList_1_1.get(k).get(0)/1000 > t3) {
                                t2 = arrayList_1_1.get(k).get(0)/1000;
                                t1 = arrayList_1_1.get(k-1).get(0)/1000;
                                System.out.println("t1-----t2"+t1+" = "+t2);
                                indexD1=k-1;
                                indexD2=k;

                                break;

                            }
                        }

                    }



                    if (!equality) {
                        d1=arrayList_1_1.get(indexD1).get(j);
                        d2=arrayList_1_1.get(indexD2).get(j);
                        temp =standarizeValues(t1,t2,t3,d1,d2,j);
                        if(j==1){
                            System.out.println("NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN"+temp);
                        }

                        System.out.println("Builder "+d1+" -"+d2);
                    }





//                    double temp = standarizeValues((arrayList_1_1.get(i).get(0))/1000,(arrayList_1_1.get(i+1).get(0))/1000 ,
//                            tmp_standard_array_reversed.get(i), arrayList_1_1.get(i).get(j),
//                            arrayList_1_1.get(i+1).get(j));
//
//                    System.out.println("First *********** "+(arrayList_1_1.get(i).get(0))/1000);
//                    System.out.println("Second ********** "+(arrayList_1_1.get(i+1).get(0))/1000);
//                    System.out.println("Third *********** "+tmp_standard_array.get(i));



                    tempStringBuilder.append(temp);
                    tempStringBuilder.append(",");





                }

                System.out.println("First *********** "+(arrayList_1_1.get(i).get(0))/1000);
                System.out.println("Second ********** "+(arrayList_1_1.get(i+1).get(0))/1000);
                System.out.println("Third *********** "+tmp_standard_array_reversed.get(i));
                System.out.println("----------------------"+ arrayList_1_1.size()+"  ----- "+tmp_standard_array.size());

                System.out.println("Writing the data");
                System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$"+ tempStringBuilder.toString());

                bw.write(tempStringBuilder.toString());


                bw.write("\n");
                bw.flush();

                tempStringBuilder.setLength(0);
            }
        }catch (IndexOutOfBoundsException e){e.getMessage();}
        catch (IOException e) { }

    }



    public double standarizeValues(long t1,long t2,long t3, double d1,double d2, int j){

        double d3=0;
        System.out.println("Original Value ++++++++ "+t1+" - "+d1);
        System.out.println("Original Value1 +++++++ "+t2+" - "+d2);
        try {
            if(j!=4) {
                d3 = d3 + ((t3 - t2) * 1.0 / (t2 - t1)) * (d2 - d1) + d2;
            } else{
                d3 = d3 + ((t3 - t2) / (t2 - t1)) * (d2 - d1) + d2;
            }
        }
        catch(ArithmeticException e){
            d3 = d1;
            e.printStackTrace();
        }
        System.out.println("Standerdized Value ++++++++ "+t3+" - "+d3);
        return d3;
    }
}



