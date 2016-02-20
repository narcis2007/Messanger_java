
/////////////////
/// UI components

var NoteBox = React.createClass({
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
            React.createElement(NoteList, null)
        );
    }
});

var NoteList = React.createClass({
    displayName: "MessageList",

    render: function () {
        return React.createElement(
            "div",
            { style: styles.noteList },
            "Note list"
        );
    }
});

var NoteForm = React.createClass({
    displayName: "MessageForm",

    render: function () {
        return React.createElement(
            "div",
            { style: styles.noteForm },
            "Note form"
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

///////
/// App

ReactDOM.render(React.createElement(NoteBox, null), document.getElementById('app'));