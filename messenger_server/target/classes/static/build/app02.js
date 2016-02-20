/**
 * Component properties
 * - a component has a props object holding the data shown by the component
 * - e.g. NoteApp and NoteList have a notes property; when NoteApp is created,
 *   the notes are initialized via a note service which uses an in-memory array of notes;
 *   when NoteList is created, the NoteApp passes its notes property to the NoteList component
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
        return React.createElement(
            "div",
            { style: styles.noteList },
            JSON.stringify(this.props.notes)
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

/////////////
/// UI styles

var styles = {
    noteApp: {
        display: "flex",
        flexDirection: "column"
    },
    noteList: {},
    noteForm: {}
};

////////
/// Model
const NoteStatus = {
    ACTIVE: "active", DONE: "done", ARCHIVED: "archived"
};

function Note(id, text, status) {
    // constructor
    this.id = id;
    this.text = text;
    this.status = status || NoteStatus.ACTIVE;
}

///////////
/// Service
var noteService = (function () {
    var notes = [new Note(1, "JS"), new Note(1, "React")];
    return {
        getAll: function () {
            return notes;
        }
    };
})();

///////
/// App

ReactDOM.render(React.createElement(NoteApp, { notes: noteService.getAll() }), document.getElementById('app'));