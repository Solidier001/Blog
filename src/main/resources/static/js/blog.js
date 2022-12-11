var date = new Date();
var yearstr = date.getFullYear().toString();
var monthstr = (date.getMonth() + 1).toString();
var daystr = date.getDate().toString();
var datestr = yearstr + '-' + monthstr + '-' + daystr;
$("#date").text(datestr);
var blogFileIdLcation = $("#bolgcontent").attr("blogFileId");
function parseMd(){
    editormd.markdownToHTML("bolgcontent", {
        htmlDecode: "style,script,iframe",
        emoji: true,
        taskList: true,
        tex: true,  // 默认不解析
        flowChart: true,  // 默认不解析
        sequenceDiagram: true  // 默认不解析
    });
}
$.get(blogFileIdLcation,
    function (data, textStatus, jqXHR) {
        $("#bolgcontent>textarea").val(data);
        parseMd();
    }
);