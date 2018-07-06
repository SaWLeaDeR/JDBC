package de.vogella.mysql.first;

import com.sun.rowset.internal.InsertRow;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.HttpURLConnection;
import java.net.URL;

public class InsertSQL {



    public static void InsertRow(int id,Connection conn,String order) throws SQLException {
        List<String> myList = new ArrayList<String>();
        String[] columns = {"MYUSER","EMAIL","WEBPAGE","SUMMARY","COMMENTS"};
        int a=0;
        String item ;



        Scanner user_input = new Scanner( System.in );

        String name;
        System.out.print("Enter your first USERNAME: ");
        name = user_input.nextLine( );

        String email=null;
        System.out.print("Enter your first EMAIL: ");
        email = user_input.nextLine( );

        String eamail = email;
        if (isValid(eamail)){
            System.out.println("E-mail Valid");
        }
        else {
            System.out.println("Invalid Email Address");
            email="";
        }



        String webpage;
        System.out.print("Enter your webpage: ");
        webpage = user_input.nextLine( );

        webpage="http://"+webpage+"/";

        checkIfURLExists(webpage);


        String summary;
        System.out.print("Enter your SUMMARY: ");
        summary = user_input.nextLine( );

        String comments;
        System.out.print("Enter your COMMENTS: ");
        comments = user_input.nextLine( );

        myList.add(name);
        myList.add(email);
        myList.add(webpage);
        myList.add(summary);
        myList.add(comments);
        System.out.println(viewValue(conn,id,"MYUSER"));
        checkSize(myList);
        if (order.equals("insert")) {
            Insert(id, myList, conn);
        }
        else if (order.equals("update")) {
            for (String element : myList) {

                if (element.isEmpty()) {
                    myList.set(myList.indexOf(element), viewValue(conn,id,columns[a]));
                    System.out.println(element);

                }a++;
            }
            System.out.println(columns);
            Update(id, myList, conn);

        }

    }

    public static void Insert(int id,List<String> element, Connection conn) {

        try
        {




            // create a sql date object so we can use it in our INSERT statement
            Calendar calendar = Calendar.getInstance();
            java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

            // the mysql insert statement
            String query = " insert into comments (id, MYUSER, EMAIL, WEBPAGE, DATUM, SUMMARY,COMMENTS)"
                    + " values (?, ?, ?, ?, ?, ?, ?)";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, id);
            preparedStmt.setString(2, element.get(0));
            preparedStmt.setString(3, element.get(1));
            preparedStmt.setString(4, element.get(2));
            preparedStmt.setDate(5, startDate);
            preparedStmt.setString(6, element.get(3));
            preparedStmt.setString(7, element.get(4));

            // execute the preparedstatement
            preparedStmt.execute();

            conn.close();

        }
        catch(
                Exception e)

        {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }

    }
    public static void DeleteRow(String id,Connection conn)
    {
        try
        {

            PreparedStatement st = conn.prepareStatement("DELETE FROM comments WHERE id = " + id + ";");
            st.executeUpdate();
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());
        }
    }





    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static void checkSize(List<String> element){
        int i =0;
        int[] maxLength = {30, 50, 50, 40,400};


        for (String asd: element) {
            if (asd.length() > maxLength[i]) {
                asd = asd.substring(0, maxLength[i]);
                i++;
            }
        }
    }



    public static boolean checkIfURLExists(String targetUrl) {

        HttpURLConnection httpUrlConn;


        try {

            httpUrlConn = (HttpURLConnection) new URL(targetUrl).openConnection();
            /*
            A HEAD request is just like a GET request, except that it asks
            the server to return the response headers only, and not the
            actual resource (i.e. no message body).
            This is useful to check characteristics of a resource without
            actually downloading it,thus saving bandwidth. Use HEAD when
            you don't actually need a file's contents.
            */
            httpUrlConn.setRequestMethod("HEAD");

            // Set timeouts in milliseconds
            httpUrlConn.setConnectTimeout(30000);
            httpUrlConn.setReadTimeout(30000);

            // Print HTTP status code/message for your information.
            System.out.println("Response Code: "+ httpUrlConn.getResponseCode());
            System.out.println("Response Message: "+ httpUrlConn.getResponseMessage());

            return (httpUrlConn.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());


            return false;
        }

    }
    public static void Update(int id ,List<String> element,Connection conn)
    {
        String a = "set";
        String b = "String";
        try
        {

            Calendar calendar = Calendar.getInstance();
            java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());


            PreparedStatement st = conn.prepareStatement("UPDATE comments SET id=?,MYUSER = ?, EMAIL = ?,WEBPAGE = ?,DATUM=?,SUMMARY=?,COMMENTS=? WHERE id = "+id+"");

            st.setInt(1, id);
            st.setString(2, element.get(0));
            st.setString(3, element.get(1));
            st.setString(4, element.get(2));
            st.setDate(5, startDate);
            st.setString(6, element.get(3));
            st.setString(7, element.get(4));

            st.executeUpdate();
            conn.close();
        }
        catch(Exception e)
        {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
    }public static String viewValue(Connection con,int id,String column) throws SQLException
    {
        String value = null;
        Statement stmt = null;



        String command = "SELECT "+column+" FROM comments WHERE id ="+id+";";

        try
        {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(command);

            while (rs.next())
                value = rs.getString(1);
        }

        catch (SQLException e )
        {
            e.printStackTrace();
        }

        finally
        {
            if (stmt != null) { stmt.close(); }
        }

        return value;

    }public void Deneme(){
        List<String> lclmnname = new ArrayList<>();
        List<String> lclmnname2 = new ArrayList<>();
        try {

            String myDriver = "com.mysql.cj.jdbc.Driver";
            String myUrl = "jdbc:mysql://127.0.0.1:3306/feedback?"+"&useSSL=true";

            Class.forName(myDriver);

            Connection conn = DriverManager.getConnection(myUrl, "root", "Ailem_79***");
            PreparedStatement rs =conn.prepareStatement("select * from feedback.comments");
            ResultSetMetaData rsmd = rs.getMetaData();





            int columnSize = rs.getMetaData().getColumnCount();


            for (int i = 1; i <= columnSize; i++ ) {

                String columname = rsmd.getColumnName(i);
                lclmnname.add(columname);

            }




            for (String element:lclmnname
                    ) {
                lclmnname2.add(getColDbType(conn,"comments",element,myUrl));

            }
            System.out.println(lclmnname2+"hggggggggggggggggggggggggggggggggghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");






        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }

    }private static String getColDbType(Connection connection, String table, String col, String url) throws Exception {
        ArrayList a = new ArrayList();

        DatabaseMetaData metadata = connection.getMetaData();
        int i = url.lastIndexOf("/") + 1;
        String schema = url.substring(i);
        ResultSet resultSet = metadata.getColumns(connection.getCatalog(), schema.trim(), table, col);
        while (resultSet.next()) {
            String name = resultSet.getString("COLUMN_NAME");
            String type = resultSet.getString("TYPE_NAME");
            int size = resultSet.getInt("COLUMN_SIZE");


            a.add(name);
            a.add(type);
            a.add(size);
            if (type.equalsIgnoreCase("varchar")) {


            }


        }
        return String.valueOf(a);
    }

}