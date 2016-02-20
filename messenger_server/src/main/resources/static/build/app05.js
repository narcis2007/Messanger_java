/**
 * Fetching data from server
 *
 */

/////////////////
/// UI components

var NoteApp = React.createClass({
    displayName: "MessengerApp",

    getInitialState: function () {
        return { notes: null };
    },
    componentDidMount: function () {
        this.getAllThenUpdate();
        //setInterval(this.getAllThenUpdate, 2000);
    },
    render: function () {
        if (!this.state.notes) {
            return this.renderLoading();
        }
        return this.renderNotes();
    },
    renderLoading: function () {
        return React.createElement(
            "div",
            { style: styles.noteApp },
            React.createElement(
                "p",
                null,
                "Loading notes..."
            )
        );
    },
    renderNotes: function () {
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
    getAllThenUpdate: function () {
        noteService.getAll().then(json => this.setState({ notes: json })).catch(err => console.error(err));
    },
    handleNoteSubmit: function (note) {
        noteService.create(note).then(json => this.getAllThenUpdate()).catch(err => console.error(err));
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
    //justifyContent: 'left',
    //alignItems: 'center',
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
        ACTIVE: "ACTIVE", DONE: "DONE", ARCHIVED: "ARCHIVED"
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
    var notesUrl = 'http://localhost:8080/api/note';
    var appJsonHeader = new Headers({
        'Content-Type': 'application/json'
    });

    function _fetchPromise(url, req, obj) {
        if (req) {
            req.headers = appJsonHeader;
        }
        if (obj) {
            req.body = JSON.stringify(obj);
        }
        console.log(">>> " + url);
        return fetch(url, req).then(res => {
            if (res.ok) {
                console.log("<<< " + url + ", ok");
                return res.json();
            } else {
                console.log("<<< " + url + ", err: " + res.status);
                throw new Error(res.status);
            }
        });
    }

    return {
        getAll: function () {
            return _fetchPromise(notesUrl, { method: 'GET' });
        },
        create: function (note) {
            return _fetchPromise(notesUrl, { method: 'POST' }, note);
        }
    };
})();

///////
/// App

ReactDOM.render(React.createElement(NoteApp, null), document.getElementById('app'));