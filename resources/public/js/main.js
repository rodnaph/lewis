
$(function()
{
    window.editors = [];

    function removeInsertRow(row)
    {
        row.remove();
    }

    function initInsertRow()
    {
        var row = $(this);
        var destroy = _.bind(removeInsertRow, {}, row);

        $('<a></a>')
            .html('delete')
            .addClass('btn btn-destroy')
            .click(destroy)
            .appendTo(row);
    }

    function makeInsertRow(name)
    {
        var input = $('<input type="text"/>')
            .attr({name: name});

        return $('<div></div>')
            .addClass(name)
            .append(input);
    }

    function addInsertRow(fields)
    {
        var row = $('<div></div>')
            .addClass('field')
            .append(makeInsertRow('name'))
            .append(makeInsertRow('value'))
            .appendTo(fields);

        initInsertRow.apply(row);
    }

    /**
     * Initialise the data insert form to allow adding/deleting fields
     *
     */
    function initDataInsert()
    {
        var fields = $('.fields', this);
        var addRow = _.bind(addInsertRow, {}, fields);

        $('<a></a>')
            .addClass('add btn btn-info')
            .html('Add row')
            .click(addRow)
            .insertAfter(fields);
    }

    /**
     * Initialise a code element into a CodeMirror editor
     *
     */
    function initCodeElement()
    {
        var config = { mode: "clojure",
                       lineNumbers: true,
                       matchBrackets: true };
        var editor = CodeMirror.fromTextArea(this, config);

        window.editors.push(editor);
    }

    $('textarea').each(initCodeElement);

    $('.form-insert').each(initDataInsert);

    $('.disconnect')
        .click(function() {
            return confirm('Are you sure?');
        });
});

