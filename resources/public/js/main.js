
$(function()
{
    window.editors = [];

    function initCodeElement()
    {
        var config = {
            mode: "clojure",
            lineNumbers: true,
            matchBrackets: true
        };

        var editor = CodeMirror.fromTextArea(this, config);

        window.editors.push(editor);
    }

    $('textarea').each(initCodeElement);

    $('.disconnect')
        .click(function() {
            return confirm('Are you sure?');
        });
});

