<%@page contentType="text/html;charset=utf-8" language="java" %>

    <%
        String USERNAME = "client";
        String PASSWORD = "client";
    %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" type="text/css" href="styles.css">
    <title>Project 4</title>
</head>
<body>
<header class="header">
    <h1 class="title">Welcome to the Spring 2022 Project 4 Enterprise Database System</h1>
    <h2 class="subtitle">A Servlet/JSP-based Multi-tiered Enterprise Application Using a Tomcat container.</h2>
</header>
<div class="middle-text">
    <h1>
        <span class="middle-text-meta">You are connected to the Project 4 Enterprise System database in a <span class="user-color">client level</span> user.<br/>
        Please enter any valid SQL query or update command in the box below.</span>
    </h1>
</div>
<!--as-->
<div class="text-box">
    <form>
        <textarea class="form-control" id="text-area" rows ="15" cols="60" name = "textsql" autofocus method="post"></textarea><br/>
        <input name="execute" class="execute-button" type = "submit" value = "Execute Command" formaction="/Project4/client" method = "get">
        <input name="reset" class="reset-button" type = "submit" value = "Reset Form" formaction="/Project4/client">
        <input name="clear" class="clear-button" type = "submit" value = "Clear Results" formaction="clientHome.jsp">
    </form>
</div><br/>
<div class="results">
    <h1>All execution results will appear below this line</h1>
    <div class="dbresults">
        <h1>Database Results:</h1>
        <form >
            <table class="dbRes">
            </table>
        </form>
    </div></br></br>
</div>
<div class="dbresults">

</div>

</body>
</html>

<%--Comment--%>