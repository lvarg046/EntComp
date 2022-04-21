/*
* Name: Luis Vargaster
* Course: CNT4714 - Spring 2022 - Project Four
* Assignment Title: A Three-Tier Distributed Web-Based Application
* Date: April 24, 2022
*/

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.*;
import java.util.*;

public class WebApplication extends HttpServlet {
    private Statement statement;
    private Connection connection;

    public void init( ServletConfig config ) throws ServletException{
        Properties properties = new Properties();
        FileInputStream fileIn = null;
        MysqlDataSource ds = null;
        try {
            fileIn = new FileInputStream("/usr/local/apache-tomcat-10.0.20/webapps/Project4/WEB-INF/lib/root.properties");
            properties.load(fileIn);
            ds = new MysqlDataSource();
            ds.setURL(properties.getProperty("MYSQL_DB_URL"));
            ds.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
            ds.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));
            // process "get" requests from clients
            connection = ds.getConnection();
            statement = connection.createStatement();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    private void printTopExecute( PrintWriter out, String sql){
        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"en\">");
        out.println("<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "    <link rel=\"stylesheet\" href=\"styles.css\">\n" +
                "    <title>Project 4</title>\n" +
                "</head>");
        out.println("<body>\n" +
                "<header class=\"header\">\n" +
                "    <h1 class=\"title\">Welcome to the Spring 2022 Project 4 Enterprise Database System</h1>\n" +
                "    <h2 class=\"subtitle\">A Servlet/JSP-based Multi-tiered Enterprise Application Using a Tomcat container.</h2>\n" +
                "</header>\n" +
                "<div class=\"middle-text\">\n" +
                "    <h1>\n" +
                "        <span class=\"middle-text-meta\">You are connected to the Project 4 Enterprise System database in a <span class=\"user-color\">root level</span> user.<br/>\n" +
                "        Please enter any valid SQL query or update command in the box below. </span>\n" +
                "    </h1>\n" +
                "</div>");
        out.println("<div class=\"text-box\">\n" +
                "    <form>\n" +
                "        <textarea class=\"form-control\" id=\"text-area\" rows =\"15\" cols=\"60\" name = \"textsql\" autofocus method=\"post\">"+ sql +"</textarea><br/>\n" +
                "        <input name=\"execute\" class=\"execute-button\" type = \"submit\" value = \"Execute Command\" formaction=\"/Project4/webapp\" method = \"get\">\n" +
                "        <input name=\"reset\" class=\"reset-button\" type = \"submit\" value = \"Reset Form\" formaction=\"rootHome.jsp\">\n" +
                "        <input name=\"clear\" class=\"clear-button\" type = \"submit\" value = \"Clear Results\" formaction=\"rootHome.jsp\">\n" +
                "    </form>\n" +
                "</div><br/>\n" +
                "<div class=\"results\">\n" +
                "    <h1>All execution results will appear below this line</h1>");

    }

    void printTopClear( PrintWriter out, String sql ){
        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"en\">");
        out.println("<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "    <link rel=\"stylesheet\" href=\"styles.css\">\n" +
                "    <title>Project 4</title>\n" +
                "</head>");
        out.println("<body>\n" +
                "<header class=\"header\">\n" +
                "    <h1 class=\"title\">Welcome to the Spring 2022 Project 4 Enterprise Database System</h1>\n" +
                "    <h2 class=\"subtitle\">A Servlet/JSP-based Multi-tiered Enterprise Application Using a Tomcat container.</h2>\n" +
                "</header>\n" +
                "<div class=\"middle-text\">\n" +
                "    <h1>\n" +
                "        <span class=\"middle-text-meta\">You are connected to the Project 4 Enterprise System database in a <span class=\"user-color\">client level</span> user.<br/>\n" +
                "        Please enter any valid SQL query or update command in the box below. </span>\n" +
                "    </h1>\n" +
                "</div>");
        out.println("<div class=\"text-box\">\n" +
                "    <form>\n" +
                "        <textarea class=\"form-control\" id=\"text-area\" rows =\"15\" cols=\"60\" name = \"textsql\" autofocus method=\"post\">"+ sql +"</textarea><br/>\n" +
                "        <input name=\"execute\" class=\"execute-button\" type = \"submit\" value = \"Execute Command\" formaction=\"/Project4/webapp\" method = \"post\">\n" +
                "        <input name=\"reset\" class=\"reset-button\" type = \"submit\" value = \"Reset Form\" formaction=\"/Project4/webapp\">\n"  +
                "        <input name=\"clear\" class=\"clear-button\" type = \"submit\" value = \"Clear Results\" formaction=\"rootHome.jsp\">\n" +
                "    </form>\n" +
                "</div><br/>\n" +
                "<div class=\"results\">\n" +
                "    <h1>All execution results will appear below this line</h1>");
        out.println("<div class=\"dbresults\">\n" +
                "        <h1>Database Results:</h1>\n" +
                "        <form >\n" +
                "            <table class=\"dbRes\">\n" +
                "            </table>\n" +
                "        </form>\n" +
                "    </div></br></br>\n" +
                "</div>\n" +
                "<div class=\"dbresults\">\n" +
                "\n" +
                "</div>\n" +
                "\n" +
                "</body>\n" +
                "</html>");
        out.close();
    }

    void printTopReset( PrintWriter out ){
        out.println("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\">\n" +
                "    <title>Project 4</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<header class=\"header\">\n" +
                "    <h1 class=\"title\">Welcome to the Spring 2022 Project 4 Enterprise Database System</h1>\n" +
                "    <h2 class=\"subtitle\">A Servlet/JSP-based Multi-tiered Enterprise Application Using a Tomcat container.</h2>\n" +
                "</header>\n" +
                "<div class=\"middle-text\">\n" +
                "    <h1>\n" +
                "        <span class=\"middle-text-meta\">You are connected to the Project 4 Enterprise System database in a <span class=\"user-color\">root level</span> user.<br/>\n" +
                "        Please enter any valid SQL query or update command in the box below.</span>s\n" +
                "    </h1>\n" +
                "</div>\n" +
                "<!--as-->\n" +
                "<div class=\"text-box\">\n" +
                "    <form>\n" +
                "        <textarea class=\"form-control\" id=\"text-area\" rows =\"15\" cols=\"60\" name = \"textsql\" autofocus method=\"post\"></textarea><br/>\n" +
                "        <input name=\"execute\" class=\"execute-button\" type = \"submit\" value = \"Execute Command\" formaction=\"/Project4/webapp\" method = \"post\">\n" +
                "        <input name=\"reset\" class=\"reset-button\" type = \"submit\" value = \"Reset Form\" formaction=\"/Project4/webapp\">\n" +
                "        <input name=\"clear\" class=\"clear-button\" type = \"submit\" value = \"Clear Results\" formaction=\"rootHome.jsp\">\n" +
                "    </form>\n" +
                "</div><br/>\n" +
                "<div class=\"results\">\n" +
                "    <h1>All execution results will appear below this line</h1>");
    }
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException  {

        response.setContentType( "text/html" );
        PrintWriter out = response.getWriter();
        String str = request.getParameter( "textsql");
        String urlQ = request.getQueryString();
        urlQ = urlQ.toLowerCase();

        if( urlQ.contains("clear") ){
            printTopClear(out, str);

        } else if ( urlQ.contains("reset") ){
            printTopReset(out);
            try {
                String str2 = str.toLowerCase();
                ResultSet resultsRS;
                int rowCount = 0;
                int res = 0;
                if( str2.contains("update") || str2.contains("insert") ){
                    String businessLogic = "update suppliers set status = status + 5 where snum in (select distinct snum from shipments left join beforeShipments using( snum, pnum, jnum, quantity) where beforeShipments.snum is null and quantity > 100);";
                    if( str2.contains("shipments")) {
                        statement.execute("drop table if exists beforeShipments;");
                        statement.execute("create table beforeShipments like shipments;");
                        statement.execute("insert into beforeShipments select * from shipments;");
                    }
                    res = statement.executeUpdate(str);
                    out.println("<div class=\"message\">\n" +
                            "            <b>The statement executed successfully</b></br>\n" +
                            "            <b>"+res+"row(s) affected.</b></br>");
                    if( str2.contains("shipments")){
                        int rowsAffected = statement.executeUpdate(businessLogic);
                        statement.executeUpdate("drop table beforeShipments;");
                        if( rowsAffected > 0 ){
                            out.println("<b>Business Logic detected! - Updating Supplier Status</b><br/>\n" +
                                    "            <b>Business Logic Updated - "+rowsAffected+" supplier status marks.</b>");
                        } else {
                            out.println("<b> Business Logic Not Triggered!</b><br/>");
                        }
                    }
                    out.println("</div>");
                } else if( str2.contains("delete") ){
                    res = statement.executeUpdate(str);
                    out.println("<div class=\"message\">\n" +
                            "            <b>The statement executed successfully</b></br>\n" +
                            "            <b>"+res+"row(s) affected.</b></br>");
                    out.println("</div>");
                } else {
                    sqlExecution(out, str, rowCount, statement);
                }

                out.println("</body></html>");
                out.close();
            } catch (SQLException e) {
                String error = e.getMessage();
                e.printStackTrace();
                out.println( "<title>Error</title>" );
                out.println( "</head>" );
                out.println( "<body><div class='error-message'><p>Database error occurred. " );
                out.println( error+"</p></div></body></html>" );
                out.close();
            }
        } else if( urlQ.contains("execute")){
            printTopExecute(out, str);
            try {
                String str2 = str.toLowerCase();
                ResultSet resultsRS;
                int rowCount = 0;
                int res;
                if( str2.contains("update") || str2.contains("insert") ){
                    String businessLogic = "update suppliers set status = status + 5 where snum in (select distinct snum from shipments left join beforeShipments using( snum, pnum, jnum, quantity) where beforeShipments.snum is null and quantity > 100);";
                    if( str2.contains("shipments")) {
                        statement.execute("drop table if exists beforeShipments;");
                        statement.execute("create table beforeShipments like shipments;");
                        statement.execute("insert into beforeShipments select * from shipments;");
                    }
                    res = statement.executeUpdate(str);
                    out.println("<div class=\"message\">\n" +
                            "            <b>The statement executed successfully</b></br>\n" +
                            "            <b>"+res+" row(s) affected.</b></br>");
                    if( str2.contains("shipments")){
                        statement.execute(businessLogic);
                        int rowsAffected = statement.getUpdateCount();
                        statement.executeUpdate("drop table beforeShipments;");
                        if( rowsAffected > 0 ){
                            out.println("<b>Business Logic detected! - Updating Supplier Status</b><br/>\n" +
                                    "            <b>Business Logic Updated - "+rowsAffected+" supplier status marks.</b>");
                        } else {
                            out.println("<b> Business Logic Not Triggered!</b><br/>");
                        }
                    }
                    out.println("</div>");
                } else if( str2.contains("delete") ){
                    res = statement.executeUpdate(str);
                    out.println("<div class=\"message\">\n" +
                            "            <b>The statement executed successfully</b></br>\n" +
                            "            <b>"+res+"row(s) affected.</b></br>");
                    out.println("</div>");
                } else {
                    sqlExecution(out, str, rowCount, statement);
                }
                out.println("</body></html>");
                out.close();
            } catch (SQLException e) {
                String error = e.getMessage();
                e.printStackTrace();
                out.println( "<title>Error</title>" );
                out.println( "</head>" );
                out.println( "<body><div class='error-message'><p>Database error occurred. " );
                out.println( error+"</p></div></body></html>" );
                out.close();
            }
        }

        // send HTML5 page to client
    } //end doGet() method

    private static void sqlExecution(PrintWriter out, String str, int rowCount, Statement statement) throws SQLException {
        ResultSet resultsRS;
        resultsRS = statement.executeQuery(str);
        out.println("<div class= 'dbresults'>");
        out.println("<h1>Database Results: </h1>");
        out.println("<form><table class='dbRes'>");
        ResultSetMetaData rsmd = resultsRS.getMetaData();
        int colCount = rsmd.getColumnCount();
        out.println("<thead>");
        out.println("<tr>");

        for( int i = 0; i < colCount; i++) {
            out.println("<th>"+ rsmd.getColumnLabel(i+1) +"</th>");
        }
        out.println("</thead>");
        out.println("</tr>");
        out.println("<tbody>");
        while( resultsRS.next() ){
            rowCount++;
            out.println("<tr>");
            for( int i = 0; i < colCount; i++ ){
                out.println("<td>"+ resultsRS.getString(i+1)+"</td>");
            }
            out.println("</tr>");
        }
        out.println("</tbody></table></form></div></br></br></div>");
        out.println("<div class=\"dbresults\">\n" + "\n" + "</div>");
        out.println();
        out.println("</span>");
        out.println();
        out.println();
        resultsRS.close();
    }
    public void destroy(){
        try{
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

} //end WebApplication Servlet class
// Comment