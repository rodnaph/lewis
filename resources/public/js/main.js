
$(function()
{
    function controlFor(container, name)
    {
        return $('*[name=' +name+ ']', container);
    }

    function valueFrom(name)
    {
        return controlFor(this, name)
            .val();
    }

    function booleanFrom(name)
    {
        return controlFor(this, name)
            .is(':checked');
    }

    function uniqueVal(value)
    {
        return value
            ? '   :db/unique :db.unique/' +value+ '\n'
            : '';
    }

    function update()
    {
        var container = $(this);
        var valueOf = _.bind(valueFrom, container);
        var booleanOf = _.bind(booleanFrom, container);
        var editor = $('textarea', container).data('editor')
        var edn = '[{ :db/id #db/id[:db.part/db]\n' +
                  '   :db/ident ' +valueOf('ident')+ '\n' +
                  '   :db/valueType :db.type/' +valueOf('valueType')+ '\n' +
                  '   :db/cardinality :db.cardinality/' +valueOf('cardinality')+ '\n' +
                  '   :db/doc "' +valueOf('doc').replace('"', '\\"')+ '"\n' +
                  uniqueVal(valueOf('unique')) +
                  '   :db/fulltext ' +booleanOf('fulltext')+ '\n' +
                  '   :db/noHistory ' +booleanOf('noHistory')+ '\n' +
                  '   :db.install/_attribute :db.part/db }]';

        editor.setValue(edn);
        editor.markText(
            {line:0, ch:0},
            {line:100, ch:100},
            {readOnly: true}
        );
    }

    function init()
    {
        var container = $(this);
        var updateEdn = _.bind(update, container);
        var onChange = _.debounce(updateEdn, 500);
        var selector = '.control-group input, .control-group select';

        $('.control-group select')
            .change(onChange);

        $('.control-group input[type=text]')
            .keyup(onChange);

        $('.control-group input[type=checkbox]')
            .click(onChange);

        setTimeout(onChange, 100);
    }

    $('.schema-update')
        .each(init);
});

$(function()
{
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

    $('.form-insert')
        .each(initDataInsert);
});

$(function()
{
    /**
     * Initialise a code element into a CodeMirror editor
     *
     */
    function initCodeElement()
    {
        var config = { mode: "clojure",
                       lineNumbers: true,
                       matchBrackets: true };

        $(this).data('editor', CodeMirror.fromTextArea(this, config));
    }

    $('textarea')
        .each(initCodeElement);
});

$(function()
{
    $('.disconnect')
        .click(function() {
            return confirm('Are you sure?');
        });
});

