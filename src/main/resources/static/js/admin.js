$('.ui .dropdown')
    .dropdown();
$("tbody>tr>td>button[name='delete']").click(function (e) {
    $.post("/Editor/deleteBlog/" + $(this).attr("blogId"),
        function (data, textStatus, jqXHR) {
        if (data=="OK")location.reload();
        else alert(data);
        }
    );
});
$("tbody>tr>td>button[name='editor']").click(function (e) {
    location="/Editor/editorPage/"+$(this).attr("blogId");
});