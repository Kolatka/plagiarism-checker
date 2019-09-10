<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
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
    <textarea rows="8" cols="70" style="resize: none;"></textarea>
    <textarea rows="8" cols="70" style="resize: none;"></textarea>
    <p class="unselectable"><button type="button">Compare</button></p>
    <p style="color: #E81123; font-weight: bold">(this process can take a while)</p>
</body>
</html>