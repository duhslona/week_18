<html lang="en">
<head>
    <link rel="stylesheet" href="/books.css", type="text/css">
</head>
<body>
<div class="container">
    <h2>BOOKS</h2>
    <ul class="responsive-table">
        <li class="table-header">
            <div class="col col-1">ID</div>
            <div class="col col-2">NAME</div>
            <div class="col col-3">GENRE</div>
        </li>
        <#list books as book>
            <li class="table-row">
                <div class="col col-1">${book.id}</div>
                <div class="col col-2">${book.name}</div>
                    <#if book.genre="Роман">
                        <div class="col col-3 red">${book.genre}</div>
                    <#elseif book.genre="Рассказ">
                        <div class="col col-3 blue">${book.genre}</div>
                    <#elseif book.genre="Комедия">
                        <div class="col col-3 green">${book.genre}</div>
                    </#if>
            </li>
        </#list>
    </ul>
</div>
</body>
</html>