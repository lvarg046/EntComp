
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.*;
import java.util.*;

public class threeTierApp extends HttpServlet {
    private Connection connection;
    private Statement statement;

    public void init( ServletConfig config ) throws ServletException{
        Properties prop = new Properties();
        FileInputStream fileIn = null;
        MysqlDataSource dataSource = null;
        try{
            fileIn = new FileInputStream("/usr/local/apache-tomcat-10.0.20/webapps/Project4/WEB-INF/lib/root.properties");
            prop.load(fileIn);
            dataSource = new MysqlDataSource();
            dataSource.setURL(prop.getProperty("MYSQL_DB_URL"));
            dataSource.setUser(prop.getProperty("MYSQL_DB_USERNAME"));
            dataSource.setPassword(prop.getProperty("MYSQL_DB_PASSWORD"));
            connection = dataSource.getConnection();
            statement = connection.createStatement();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        // set up response to client
        response.setContentType( "text/html" );
        PrintWriter out = response.getWriter();

        // read current survey response
        int value = Integer.parseInt( request.getParameter( "color" ) );
        String sql;
        // attempt to process a user vote and display current results
        try
        {
            // update total for current survey response
            sql = "UPDATE surveyresults SET votes = votes + 1  WHERE id = " + value;
            statement.execute(sql);

            // get total of all survey responses
            sql = "SELECT sum( votes ) FROM surveyresults";
            ResultSet totalRS = statement.executeQuery( sql );
            totalRS.next(); // position to first record
            int total = totalRS.getInt( 1 );

            // get results
            sql = "SELECT surveyoption, votes, id FROM surveyresults ORDER BY id";
            ResultSet resultsRS = statement.executeQuery( sql );

            // start HTML document
            out.println( "<html>" );
            // begin head section of document
            out.println( "<head>" );
            out.println( "<meta charset=\"utf-8\">" );
            out.println( "<style type='text/css'>");
            out.println( "<!-- ");
            out.println( "body{background-color:black; color:lime; font-family:calibri,sans-serif; font-size:1.3em; }");
            out.println( "h1 {text-align:center;} ");
            out.println( "h2 {font-size:1.1em;}");
            out.println( " .different {color:red; font-family: Calibri, sans-serif;}");
            out.println( " .moredifferent {color:#00BFFF; font-family:Calibri, sans-serif; text-decoration:underline; text-decoration-color:#00BFFF;}");
            out.println( " .finalnum {color:white;}");
            out.println( " -->");
            out.println( "</style>");
            out.println( "<title>Thank you!</title>" );
            out.println( "</head>" );
            // end head section of document

            // begin body section of document
            out.println( "<body>" );
            out.println ("<h1>Thank you for participating in the CNT 4714 <span style='color:cyan'>C</span><span style='color:red'>O</span><span style='color:green'>L</span><span style='color:yellow'>O</span><span style='color:orange'>R</span> Preference Survey!!" );
            out.println ("</b><br></h1>");
            out.println ("<br /><h2 class=\"moredifferent\"> Current Results:</h2>" );
            out.println("<pre><span style='color:lightblue; font-family:Courier;'>");

            // process results
            int votes;

            while ( resultsRS.next() )
            {
                out.print( resultsRS.getString( 1 ) );
                out.print( ": " );
                votes = resultsRS.getInt( 2 );
                out.printf( "%.2f", ( double ) votes / total * 100 );
                out.print( "%\t  responses: " );
                out.println( votes );
            } // end while


            out.println();
            out.println("</span>");
            out.print( "<span class=\"different\"> Total number of responses: <span class=\"finalnum\"> "+ total + "</span></span>" );
            out.println();
            out.println();
            resultsRS.close();


            sql = "select surveyoption, votes  from surveyresults where votes = (select max(votes) from surveyresults)";
            ResultSet favoriteRS = statement.executeQuery(sql);
            favoriteRS.next();
            out.print("<span class=\"different\">Currently the favorite color is ");
            out.print(favoriteRS.getString( 1 ) );
            out.println(", with a total of <span class=\"finalnum\">" + favoriteRS.getInt( 2 ) + "</span> votes.</span>");

            //favoriteRS.close();



            // end HTML document
            out.println("</pre></body></html>");
            out.close();
        } // end try
        // if database exception occurs, return error page
        catch ( SQLException sqlException )
        {
            sqlException.printStackTrace();
            out.println( "<title>Error</title>" );
            out.println( "</head>" );
            out.println( "<body><p>Database error occurred. " );
            out.println( "Try again later.</p></body></html>" );
            out.close();
        } // end catch
    } // end method doPost
    public void destroy(){
        try{
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
