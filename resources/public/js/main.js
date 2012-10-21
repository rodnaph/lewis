
$(function()
{
    function initCodeElement()
    {
        var config = {
            mode: "clojure",
            lineNumbers: true,
            matchBrackets: true
        };

        CodeMirror.fromTextArea(this, config);
    }

    $('textarea').each(initCodeElement);
});

