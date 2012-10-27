
function controlFor(container, name)
{
    return $('*[name=' +name+ ']', container);
}

function valueFrom(name)
{
    return controlFor(this, name)
        .val();
}

/**
 * Schema filtering
 */

$(function()
{
    function toggle(value, index, element)
    {
        var row = $(element);
        var toggler = row.html().indexOf(value) != -1
            ? 'show'
            : 'hide';

        row[toggler]();
    }

    function update()
    {
        var value = $(this).val();
        var filter = _.bind(toggle, {}, value);

        $('.schema-table tbody tr')
            .each(filter);
    }

    function init()
    {
        var input = $('<input type="text"/>')
            .attr({ placeholder: 'Filter schema' })
            .appendTo(this);

        input.keyup(update);
    }

    $('.schema-filter')
        .each(init);
});

/**
 * Schema update controls
 */

$(function()
{
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

/**
 * Data inserting
 */

$(function()
{
    function remove(row)
    {
        row.remove();
    }

    function initRow()
    {
        var row = $(this);
        var destroy = _.bind(remove, {}, row);

        $('<a></a>')
            .html('delete')
            .addClass('delete')
            .click(destroy)
            .appendTo(row);
    }

    function toNameValue(field)
    {
        var valueOf = _.bind(valueFrom, field);

        return { name: valueOf('name'),
                 value: valueOf('value') };
    }

    function toEdn(acc, e)
    {
        return acc+ '\n   ' +e.name+ ' ' +e.value;
    }

    function update()
    {
        var editor = $('textarea', this).data('editor');
        var fields = $('.field', this);
        var edn = _.chain(fields)
                   .map(toNameValue)
                   .reduce(toEdn, '')
                   .value();

        editor.setValue('[{ ' +edn.substring(4)+ ' }]');
    }

    function make(name)
    {
        var input = $('<input type="text"/>')
            .attr({name: name});

        return $('<div></div>')
            .addClass(name)
            .append(input);
    }

    function toResult(datum)
    {
        var id = ':' +datum['db/ident'];

        return { id:id, text:id };
    }

    function results(data)
    {
        return {results: _.map(data, toResult)};
    }

    function query(term)
    {
        return {term: term};
    }

    function select2Changed(name, onChange, evt)
    {
        controlFor(name, 'name')
            .val(evt.val);

        onChange();
    }

    function add(container)
    {
        var fields = $('.fields', container);
        var updater = _.bind(update, container);
        var onChange = _.debounce(updater, 500);
        var config = { placeholder: 'Select an atrribute...',
                       ajax: { url: '/session/schema.json',
                               data: query,
                               results: results }};
        var name = make('name');
        var value = make('value');
        var row = $('<div></div>')
            .addClass('field')
            .append(name)
            .append(value)
            .appendTo(fields);

        name.select2(config)
            .on('change', _.bind(select2Changed, {}, name, onChange));
        value.keyup(onChange);

        initRow.apply(row);
    }

    function init()
    {
        var container = $(this);
        var fields = $('.fields', container);
        var addRow = _.bind(add, {}, container);

        $('<a></a>')
            .addClass('add btn btn-info')
            .html('Add Attribute')
            .click(addRow)
            .insertAfter(fields);
    }

    $('.data-insert')
        .each(init);
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

