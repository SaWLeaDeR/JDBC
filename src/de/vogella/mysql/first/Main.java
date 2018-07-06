package de.vogella.mysql.first;

import de.vogella.mysql.first.MySQLAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {


        public static void main(String[] args) throws Exception {
            MySQLAccess dao = new MySQLAccess();
            InsertSQL sql = new InsertSQL();
            //dao.readDataBase();
            sql.Deneme();

            try {
                String myDriver = "com.mysql.cj.jdbc.Driver";
                String myUrl = "jdbc:mysql://127.0.0.1:3306/feedback?"+"&useSSL=true";
                Class.forName(myDriver);
                Connection conn = DriverManager.getConnection(myUrl, "root", "Ailem_79***");


                Scanner Input = new Scanner( System.in );


                String operation;
                System.out.println("Choose One Of the Listed Text Which are Below: ");
                System.out.println("Insert , Delete , Update  Operations");
                System.out.print("Write Here : ");
                operation = Input.nextLine();

                String[] arrayOfString =operation.split("\\s+");
                System.out.println("  Executing ");
                if(operation.isEmpty()){
                    System.out.println("Please Try Again");

                }else{
                    for (String order : arrayOfString) {

                        order = order.toLowerCase();
                        if (order.equals("insert")) {
                            String id;
                            System.out.println("Pls write the id of row which is inserting : ");
                            id = Input.next();
                            int row;
                            row = Integer.parseInt(id);
                            sql.InsertRow(row,conn,order);

                        } else if (order.equals("delete")) {
                            String id;
                            System.out.println("Pls write the id of row which is deleted : ");
                            id = Input.next();
                            sql.DeleteRow(id,conn);
                        } else if (order.equals("update")) {
                            String id;

                            System.out.println("Pls write the id of row which is update : ");
                            id = Input.next();
                            int row;
                            row = Integer.parseInt(id);
                            sql.InsertRow(row,conn,order);
                            break;
                        } else {

                        }

                    }

                }
            }catch (Exception e){
                System.out.println("Error: " + e);
                System.err.println(e.getMessage());
            }
        }
}
