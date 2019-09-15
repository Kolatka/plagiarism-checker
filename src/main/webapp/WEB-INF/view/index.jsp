<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://thymeleaf.org">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>Plagiarism Checker</title>
    <link rel="stylesheet" type="text/css" href="resources/css/mystyle.css">
</head>
<body>
    <p><h1>PLAGIARISM CHECKER</h1></p>
    <p>Parameters:</p>
    <p class="unselectable">
        <label><input type="checkbox">Sort words</label>
        <label><input type="checkbox">Sort sentences</label>
        <label><input type="checkbox">Flexions</label>
        <label><input type="checkbox">Synonyms</label>
        <label><input type="checkbox">Part of Speech</label>
    </p>
    <form action="#" th:action="@{/}" th:object="${comparation}" method="post">
        <input rows="8" cols="70" type="textarea" th:field="*{leftText}" />
        <input rows="8" cols="70" type="textarea" th:field="*{rightText}" />
        <p><input type="submit" value="Submit" /> <input type="reset" value="Reset" /></p>
    </form>

    <p style="color: #E81123; font-weight: bold">(this process can take a while)</p>
</body>
</html>