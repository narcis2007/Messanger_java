/**
 * Composing components
 * - the entire app (UI perspective) can be a component containing other components
 * - e.g. NoteApp contains a NoteForm and NoteList (which can contain other components...)
 */
/////////////////
/// UI components

var MessengerApp = React.createClass({
    displayName: "MessengerApp",

    render: function () {
        return React.createElement(
            "div",
            { style: styles.noteApp },
            React.createElement(
                "h1",
                null,
                "messages"
            ),
            React.createElement(MessageForm, null),
            React.createElement(MessageList, null)
        );
    }
});

var MessageList = React.createClass({
    displayName: "MessageList",

    render: function () {
        return React.createElement(
            "div",
            { style: styles.noteList },
            "MESSAGE LIST"
        );
    }
});

var MessageForm = React.createClass({
    displayName: "MessageForm",

    render: function () {
        return React.createElement(
            "div",
            { style: styles.noteForm },
            "MESSAGE FORM"
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

var req = {
    method: 'GET'
};

req.headers = new Headers({
    'Content-Type': 'application/json',
    Authorization: " Basic " + btoa("admin" + ":" + "admin")
});

var url = "http://localhost:8080/get_all";

fetch(url, req).then(res => {
    if (res.ok) {
        console.log("<<< " + url + ", ok");
        return res.json();
    } else {
        console.log("<<< " + url + ", err: " + res.status);
        throw new Error(res.status);
    }
}).then(json => console.log(json)).catch(err => console.log(err));

ReactDOM.render(React.createElement(MessengerApp, null), document.getElementById('react'));