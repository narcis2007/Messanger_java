/**
 * Generating dynamically components
 * - a component can render dynamically content based on the model/data
 * - if an array of components is generated, the items must have unique keys
 * - e.g. NoteList generates dynamically a list of Note components having their ids as keys
 */

/////////////////
/// UI components

var NoteApp = React.createClass({
    displayName: "MessengerApp",

    render: function () {
        return React.createElement(
            "div",
            { style: styles.noteApp },
            React.createElement(
                "h1",
                null,
                "Notes"
            ),
            React.createElement(NoteForm, null),
            React.createElement(NoteList, { notes: this.props.notes })
        );
    }
});

var NoteList = React.createClass({
    displayName: "MessageList",

    render: function () {
        var noteListItems = this.props.notes.map(note => {
            return React.createElement(Note, { note: note, key: note.id });
        });
        return React.createElement(
            "ul",
            { style: styles.noteList },
            noteListItems
        );
    }
});

var NoteForm = React.createClass({
    displayName: "MessageForm",

    render: function () {
        return React.createElement(
            "div",
            { style: styles.noteForm },
            "NOTE FORM"
        );
    }
});

var Note = React.createClass({
    displayName: "Note",

    render: function () {
        var note = this.props.note;
        return React.createElement(
            "li",
            { id: note.id },
            note.text
        );
    }
});

/////////////
/// UI styles

var styles = {
    noteApp: {
        display: "flex",
        flexDirection: "column"
    },
    noteList: {
        listStyleType: "none",
        margin: 0,
        padding: 0
    },
    noteForm: {}
};

////////
/// Model

var model = {
    NoteStatus: {
        ACTIVE: "active", DONE: "done", ARCHIVED: "archived"
    },
    Note: function (id, text, status) {
        // constructor
        this.id = id;
        this.text = text;
        this.status = status || model.NoteStatus.ACTIVE;
    }
};

///////////
/// Service
var noteService = (function () {
    var notes = [new model.Note(1, "JS"), new model.Note(2, "React")];
    return {
        getAll: function () {
            return notes;
        }
    };
})();

///////
/// App

ReactDOM.render(React.createElement(NoteApp, { notes: noteService.getAll() }), document.getElementById('app'));