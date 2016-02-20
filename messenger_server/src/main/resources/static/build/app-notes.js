/// Service
var noteService = (function () {
    var notesUrl = 'http://localhost:8080/api/note';
    var appJsonHeader = new Headers({
        'Content-Type': 'application/json'
    });
    return {
        getAll: function () {
            return fetch(notesUrl).then(res => {
                if (res.ok) {
                    return res.json();
                } else {
                    throw new Error(res.status);
                }
            });
        },
        create: function (note) {
            return fetch(notesUrl, {
                method: 'POST',
                body: JSON.stringify(note),
                headers: appJsonHeader
            }).then(res => {
                if (res.ok) {
                    return res.json();
                } else {
                    throw new Error(res.status);
                }
            });
        }
    };
})();

/// UI
var Note = React.createClass({
    displayName: 'Note',

    render: function () {
        return React.createElement(
            'li',
            { key: this.props.id },
            React.createElement(
                'a',
                { href: '#', onClick: this.handleClick },
                this.props.children
            )
        );
    },
    handleClick: function (e) {
        e.preventDefault();
        console.log(e);
    }
});

var NoteList = React.createClass({
    displayName: 'MessageList',

    render: function () {
        var noteNodes = this.props.data.map(note => {
            return React.createElement(
                'ul',
                null,
                React.createElement(
                    Note,
                    { id: note.id, key: note.id },
                    note.content
                )
            );
        });
        return React.createElement(
            'div',
            null,
            noteNodes
        );
    }
});

var NoteForm = React.createClass({
    displayName: 'MessageForm',

    getInitialState: function () {
        return { content: '' };
    },
    handleContentChange: function (e) {
        this.setState({ content: e.target.value });
    },
    handleSubmit: function (e) {
        e.preventDefault();
        var content = this.state.content.trim();
        if (!content) {
            return;
        }
        this.props.onNoteSubmit({ content: content });
        this.setState({ content: '' });
    },
    render: function () {
        return React.createElement(
            'form',
            { onSubmit: this.handleSubmit },
            React.createElement('input', { type: 'text', placeholder: 'Your note content',
                value: this.state.content,
                onChange: this.handleContentChange }),
            React.createElement('input', { type: 'submit', value: 'Add' })
        );
    }
});

var NoteBox = React.createClass({
    displayName: 'NoteBox',

    getInitialState: function () {
        return { data: [] };
    },
    getAllThenUpdate: function () {
        noteService.getAll().then(json => this.setState({ data: json })).catch(err => console.error(err));
    },
    handleNoteSubmit: function (note) {
        noteService.create(note).then(json => /*this.setState({data: json})*/this.getAllThenUpdate()).catch(err => console.error(err));
    },
    componentDidMount: function () {
        this.getAllThenUpdate();
        //setInterval(this.getAllThenUpdate, 2000);
    },
    render: function () {
        return React.createElement(
            'div',
            { style: noteBoxStyle },
            React.createElement(
                'h1',
                null,
                'Notes'
            ),
            React.createElement(NoteList, { data: this.state.data }),
            React.createElement(NoteForm, { onNoteSubmit: this.handleNoteSubmit })
        );
    }
});

/// UI Style
var noteBoxStyle = {
    flex: 1
};

ReactDOM.render(React.createElement(NoteBox, null), document.getElementById('app'));