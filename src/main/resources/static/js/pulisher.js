$('.ui.form')
    .form({
        fields: {
            tittle: {
                identifier: 'tittle',
                rules: [{
                    type: 'empty',
                    prompt: '标题：请输入博客标题'
                }]
            }
        }
    });
var testEditor;
$(function () {
    testEditor = editormd("test-editormd", {
        width: "100%",
        height: 640,
        syncScrolling: "single",
        path: "/lib/editor/lib/"
    });
});
$('#save').click(function (e) {
    this.form.action='/Editor/saveManuscript'
    this.form.submit()
});
$('#publish').click(function (e) {
    this.form.action='/Editor/publish'
    this.form.submit()
});