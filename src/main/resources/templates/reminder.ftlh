<#ftl encoding="UTF-8">
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>The event on which you reserve tickets will be held tomorrow.</title>
    <style>
        #body {
            display: flex;
            flex-direction: column;
            font-family: "Segoe UI", serif;
            align-items: center;
            background-color: #607FF2;
            font-size: 20px;
            height: 100%;
        }

        #div {
            display: block;
            flex-direction: column;
            margin: 10%;
            font-family: "Segoe UI", serif;
            background-color: white;
            padding: 3%;
            border-radius: 20px;
            color: black;
        }

        #a {
            text-decoration: none;
        }
    </style>
</head>
<body>
<div id="body">
    <div id="div">
        <h1 style="text-align: center">Reminder</h1>
        <p>Hello, ${name}!</p>
        <p>This is friendly reminder that you have an event on tomorrow.</p>
        <div>
            <hr>
            <p>The Event: ${eventName}</p>
            <#if description??>
                <p>Description: ${description}</p>
            </#if>
            <#if date??>
                <p>Date: ${date}</p>
            </#if>
            <hr>
        </div>
        <p>You can get more information about this event in the app.</p>
        <p>With best regards, <br> Artem Alchibaev</p>
    </div>
</div>
</body>
</html>