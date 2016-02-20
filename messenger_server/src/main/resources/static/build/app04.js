/**
 * Events and component state
 * 
 */

/////////////////
/// UI components

var NoteApp = React.createClass({
    displayName: "MessengerApp",

    getInitialState: function () {
        return { notes: noteService.getAll() };
    },
    render: function () {
        return React.createElement(
            "div",
            { style: styles.noteApp },
            React.createElement(
                "h1",
                null,
                "Notes"
            ),
            React.createElement(NoteForm, { onNoteSubmit: this.handleNoteSubmit }),
            React.createElement(NoteList, { notes: this.state.notes })
        );
    },
    handleNoteSubmit: function (note) {
        noteService.create(note);
        console.log('changing state');
        this.setState({ notes: noteService.getAll() });
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

    getInitialState: function () {
        this.note = new model.Note(null, "");
        return this.note;
    },
    render: function () {
        return React.createElement(
            "div",
            { style: styles.noteForm },
            React.createElement(
                "form",
                { onSubmit: this.handleSubmit },
                React.createElement("input", { type: "text", placeholder: "Note text",
                    value: this.state.text,
                    onChange: this.handleTextChanged }),
                React.createElement("input", { type: "submit", value: "+" })
            )
        );
    },
    handleTextChanged: function (event) {
        this.note.text = event.target.value;
        this.setState(this.note);
    },
    handleSubmit: function (event) {
        event.preventDefault();
        var note = this.note;
        if (!note.text.trim()) {
            return;
        }
        this.props.onNoteSubmit(note);
        this.setState(this.getInitialState());
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
        },
        create: function (note) {
            note.id = notes.length + 1;
            notes.push(note);
        }
    };
})();

///////
/// App

ReactDOM.render(React.createElement(NoteApp, null), document.getElementById('app'));