/**
 * Created by Narcis2007 on 14.02.2016.
 */


var MessengerApp = React.createClass({
    displayName: "MessengerApp",
    getInitialState: function () {
        this.refreshMessages();
        return { messages: [] };
    },
    render: function () {
        return React.createElement(
            "div",
            null,
            React.createElement(
                "h1",
                null,
                "messages"
            ),
            React.createElement(MessageForm, { onMessageSubmit: this.handleMessageSubmit }),
            React.createElement(MessageList, { messages: this.state.messages })
        );
    },
    handleMessageSubmit:function(message){

        messageService.sendMessage(message).then(function(){
            this.refreshMessages();
        }.bind(this));

    },
    refreshMessages: function() {

        messageService.getAll().then(function(data){
            this.setState({messages: data});
            console.log("refreshing...");
        }.bind(this));
    },
});
var MessageList = React.createClass({
    displayName: "MessageList",


    render: function () {

        var messageListItems = this.props.messages.map(message => {
                return React.createElement(Message, { message: message, key: message.id });
        });
        return React.createElement(
            "div",
            null,
            "MESSAGE LIST"
        ),
        React.createElement(
                "ul",
                null,
                messageListItems
            );
    }
});

var Message = React.createClass({
    displayName: "Message",

    render: function () {
        var message = this.props.message;
        return React.createElement(
            "li",
            { id: message.id },
            "status: ",message.status,
            ", sender: ",message.sender,
            ", receiver: ",message.receiver,
            ", title: ",message.title,
            ", content: ",message.content
        );
    }
});
var model = {
    Message: function (id, status,sender,receiver, title,content) {
        // constructor
        this.id = id;
        this.status = status ;
        this.sender = sender;
        this.receiver = receiver;
        this.title = title;
        this.content = content;
    },
    ClientMessage: function ( ) {
        // constructor
        this.receiver = '';
        this.title = '';
        this.content = '';
    },
    emptyFields:function(clientMessage){

        if(clientMessage.receiver!=''&&clientMessage.title!=''&&clientMessage.content!='')
            return true;
        return false;
    }
};

var MessageForm = React.createClass({
    displayName: "MessageForm",

    getInitialState: function () {
        this.message = new model.ClientMessage();
        console.log( this.message);
        //this.message ={};
        return this.message;
    },
    render: function () {
        return React.createElement(
            "div",
            null,
            "message form",
            React.createElement(
                "form",
                { onSubmit: this.handleSubmit },
                React.createElement("input", { type: "title", name:"title", placeholder: "Message title",
                    value: this.state.title,
                    onChange: this.handleTitleChanged }),
                React.createElement("input", { type: "content", name:"content", placeholder: "Message content",
                    value: this.state.content,
                    onChange: this.handleContentChanged }),
                React.createElement("input", { type: "receiver", name:"receiver", placeholder: "Message receiver",
                    value: this.state.receiver,
                    onChange: this.handleReceiverChanged }),
                React.createElement("input", { type: "submit", disabled:!model.emptyFields(this.state), value: "Send Message" })
            )
        );
    },
    handleTitleChanged: function (event) {
        this.message.title= event.target.value;
        this.setState(this.message);
    },
    handleContentChanged: function (event) {
        this.message.content= event.target.value;
        this.setState(this.message);
    },
    handleReceiverChanged: function (event) {
        this.message.receiver= event.target.value;
        this.setState(this.message);
    },
    handleSubmit: function (event) {
        event.preventDefault();
        var message = this.message;

        this.props.onMessageSubmit(message);
        this.setState(this.getInitialState());
        console.log(this.message)
    }
});


var messageService=(function(){

    return {
        getAll:function()
        {
            console.log("get all");
            return $.ajax({
                url: 'http://localhost:8080/get_all',
                dataType: 'json',
                cache: false,
                success: function(data) {
                    return data;
                },
                error: function(xhr, status, err) {
                    console.error(this.props.url, status, err.toString());
                }
            });

        },
        sendMessage:function(message){
            return $.ajax({
                url : 'http://localhost:8080/add',
                datatype:'json',
                type: "post",
                contentType: "application/json",
                data: JSON.stringify(message),
                success:function(data){
                    console.log('post message success');
                }
            });
        }
    }
})();





ReactDOM.render(React.createElement(MessengerApp, []), document.getElementById('react'));