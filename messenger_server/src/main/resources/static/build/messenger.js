<<<<<<< HEAD
/**
 * Created by Narcis2007 on 14.02.2016.
 */

//should have separated them into different modules


var MessengerApp = React.createClass({
    displayName: "MessengerApp",
    pageInfo:{pageSize:2,pageNr:0},
    loadingMessages:true,
    getInitialState: function () {
        this.refreshMessages(null);
        this.refreshUsers();
        return { messages: [],users:[],messageCount:0,numberOfPages:0 };
    },
    render: function () {
        var formElement=React.createElement(MessageForm, { users:this.state.users,onMessageSubmit: this.handleMessageSubmit });
        var messageListElement=React.createElement(MessageList, { messages: this.state.messages,nextPage:this.nextPage,prevPage:this.prevPage,pageInfo:this.pageInfo,numberOfPages:this.state.numberOfPages });

        //if(this.state.users.length==0)
        //    formElement=this.renderLoading();
        console.log("render");
        console.log("this.loadingMessages="+this.loadingMessages);
        if(this.loadingMessages===true){
            messageListElement=this.renderLoading();
            console.log("should have displayed loading...");
        }
        return React.createElement(
            "div",
            null,
            React.createElement(
                "h1",
                null,
                "messages"
            ),
            formElement,
            messageListElement
        );
    },
    renderLoading: function () {
        return React.createElement(
            "div",
            null,
            "Loading Data from Server",
            React.createElement(
                Button,{name:"Cancel Loading",onClick:this.cancelLoading,disabled:false}
            )
        );
    },
    cancelLoading:function(){
        this.loadingMessages=false;
        this.forceUpdate();
        console.log("loading cancelled");
    },
    handleMessageSubmit:function(message){

        messangerService.sendMessage(message).then(function(){
            this.refreshMessages(null);
        }.bind(this));

    },
    refreshMessages: function(onCancel) {
        this.loadingMessages=true;
        this.forceUpdate();
        console.log("trying to refresh messages");
        messangerService.getPage(this.pageInfo).then(function(data) {
            console.log("about to refresh");
            if (this.loadingMessages === true) {

                this.setState({messages: data});
                console.log("refreshing messages...");

                this.refreshMessageCount();
                this.loadingMessages=false;
            }
            else {
                if(onCancel!=null)
                     onCancel();
                this.forceUpdate();
                console.log("this.loadingMessages= "+this.loadingMessages);
            }


        }.bind(this));


    },
    refreshUsers: function() {

        messangerService.getUsers().then(function(data){
            this.setState({users: data});
            console.log("refreshing users...");
        }.bind(this));
    },
    nextPage:function(){
        console.log("getting next page");
        this.pageInfo.pageNr++;
        var f=function(){this.pageInfo.pageNr--;}.bind(this);
        this.refreshMessages(f);
    },
    prevPage:function(){
        console.log("getting prev page");
        this.pageInfo.pageNr--;
        var f=function(){this.pageInfo.pageNr++;}.bind(this);
        this.refreshMessages(f);
    },
    refreshMessageCount:function(){
        messangerService.getMessageCount().then(function(data){
            var state=this.state;
            state.messageCount=data;
            state.numberOfPages=Math.ceil(state.messageCount/this.pageInfo.pageSize);
            this.setState(state);
            console.log("refreshing message count: "+data);
        }.bind(this));
    }
});
var PageCountIndicator=React.createClass({
    displayName: "PageCountIndicator",
    render: function () {
        return React.createElement(
            "p",
            null,
            "Page:"+(this.props.pageNr+1)+"/"+this.props.numberOfPages
        )
    }
});
var UserList = React.createClass({
    displayName: "UserList",
    render: function () {

        var userListItems = this.props.users.map(user => {
                return React.createElement(User, { user: user, key: user.username,onClick:this.props.onClick });
    });
        return React.createElement(
            "div",
            null,
            "USER LIST:",
            React.createElement(
                "ul",
                {id:"userList"},
                userListItems
            )
        )
    }

});
var MessageList = React.createClass({
    displayName: "MessageList",

    //nextPage: function() {
    //console.log("next button pushed");
    //},

    render: function () {

        var messageListItems = this.props.messages.map(message => {
                return React.createElement(Message, { message: message, key: message.id });
        });
        return React.createElement(
            "div",
            null,
            "MESSAGE LIST:",
            React.createElement(
                "ul",
                {id:"messageList"},
                messageListItems
            ),
            React.createElement(
                Button,{name:"Previous Page",onClick:this.props.prevPage,disabled:this.props.pageInfo.pageNr===0}
            ),
            React.createElement(
                Button,{name:"Next Page",onClick:this.props.nextPage,disabled:this.props.pageInfo.pageNr+1>=this.props.numberOfPages}
            ),
            React.createElement(PageCountIndicator, { pageNr:this.props.pageInfo.pageNr,numberOfPages:this.props.numberOfPages})
        )
    }

});

var Button = React.createClass({
    render: function () {
        return (
            React.createElement("input", { type: "submit",disabled:this.props.disabled, value: this.props.name, onClick:this.props.onClick })
        );
    }
});
var SelectList = React.createClass({
    displayName: "SelectList",

    render: function () {
        var options=[];
        for (var i = 0; i < this.props.users.length; i++) {
            options.push(React.createElement(Option,{onClick:this.props.onClick,value:this.props.users[i].username,key:this.props.users[i].username}));
        }
        return React.createElement(
            "select",
            {id:this.props.id,onChange:this.props.onClick},
            options
        );
    }
});
var Option = React.createClass({
    displayName: "Option",
    render: function () {

        return React.createElement(
            "option",
            {value:this.props.value},
            this.props.value
        );
    }
});



//$(document).ready ( function () {
//    $(document).on ("click", "#messageList li", function () {
//
//        console.log('jquery clicked'+ this.id);
//        $('#'+this.id).css({
//        'background-color':'red'
//        });
//    });
//});


var User = React.createClass({
    displayName: "User",
    clicked:function(){
        console.log("user clicked");
        this.props.onClick(this.props.user);
    },
    render: function () {
        var user = this.props.user;
        return React.createElement(
            "li",
            { id: user.username,onClick:this.clicked },
            "username: ",user.username
        );
    }
});
var Message = React.createClass({
    displayName: "Message",
    clicked:function(){
        console.log("message clicked");
    },
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
    },
    User: function (username) {
        // constructor
        this.username = username ;
    },
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
                React.createElement(SelectList, { id:"form_user_select",users: this.props.users,onClick:this.handleUserSelect }), // pun ca select options in form
                React.createElement("input", { type: "submit", disabled:!model.emptyFields(this.state), value: "Send Message" })
            )
        );
    },
    handleUserSelect: function () { //da object clicked
        var select = document.getElementById( "form_user_select" );
        var option=select.options[ select.selectedIndex ].value;

        console.log(option +" selected");
        this.message.receiver=option;
        this.setState(this.message);
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


var messangerService=(function(){

    return {
        getUsers:function()
        {
            console.log("get users");
            return $.ajax({
                url: 'http://localhost:8080/get_users',
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
        },
        getPage:function(pageInfo){
            return $.ajax({
                url : 'http://localhost:8080/get_page',
                datatype:'json',
                type: "post",
                contentType: "application/json",
                data: JSON.stringify(pageInfo),
                success:function(pageData){
                    console.log('get page success');
                    return pageData;
                }
            });
        },
        getMessageCount:function(){
            return $.ajax({
                url : 'http://localhost:8080/getMessageCountByReceiver',
                datatype:'json',
                type: "post",
                contentType: "application/json",
                data: JSON.stringify({}),
                success:function(messageCount){
                    console.log('geting message count success');
                    return messageCount;
                }
            });
        }
    }
})();


=======
/**
 * Created by Narcis2007 on 14.02.2016.
 */

//should have separated them into different modules


var MessengerApp = React.createClass({
    displayName: "MessengerApp",
    pageInfo:{pageSize:2,pageNr:0},
    loadingMessages:true,
    getInitialState: function () {
        this.refreshMessages(null);
        this.refreshUsers();
        return { messages: [],users:[],messageCount:0,numberOfPages:0 };
    },
    render: function () {
        var formElement=React.createElement(MessageForm, { users:this.state.users,onMessageSubmit: this.handleMessageSubmit });
        var messageListElement=React.createElement(MessageList, { messages: this.state.messages,nextPage:this.nextPage,prevPage:this.prevPage,pageInfo:this.pageInfo,numberOfPages:this.state.numberOfPages });

        //if(this.state.users.length==0)
        //    formElement=this.renderLoading();
        console.log("render");
        console.log("this.loadingMessages="+this.loadingMessages);
        if(this.loadingMessages===true){
            messageListElement=this.renderLoading();
            console.log("should have displayed loading...");
        }
        return React.createElement(
            "div",
            null,
            React.createElement(
                "h1",
                null,
                "messages"
            ),
            formElement,
            messageListElement
        );
    },
    renderLoading: function () {
        return React.createElement(
            "div",
            null,
            "Loading Data from Server",
            React.createElement(
                Button,{name:"Cancel Loading",onClick:this.cancelLoading,disabled:false}
            )
        );
    },
    cancelLoading:function(){
        this.loadingMessages=false;
        this.forceUpdate();
        console.log("loading cancelled");
    },
    handleMessageSubmit:function(message){

        messangerService.sendMessage(message).then(function(){
            this.refreshMessages(null);
        }.bind(this));

    },
    refreshMessages: function(onCancel) {
        this.loadingMessages=true;
        this.forceUpdate();
        console.log("trying to refresh messages");
        messangerService.getPage(this.pageInfo).then(function(data) {
            console.log("about to refresh");
            if (this.loadingMessages === true) {

                this.setState({messages: data});
                console.log("refreshing messages...");

                this.refreshMessageCount();
                this.loadingMessages=false;
            }
            else {
                if(onCancel!=null)
                     onCancel();
                this.forceUpdate();
                console.log("this.loadingMessages= "+this.loadingMessages);
            }


        }.bind(this));


    },
    refreshUsers: function() {

        messangerService.getUsers().then(function(data){
            this.setState({users: data});
            console.log("refreshing users...");
        }.bind(this));
    },
    nextPage:function(){
        console.log("getting next page");
        this.pageInfo.pageNr++;
        var f=function(){this.pageInfo.pageNr--;}.bind(this);
        this.refreshMessages(f);
    },
    prevPage:function(){
        console.log("getting prev page");
        this.pageInfo.pageNr--;
        var f=function(){this.pageInfo.pageNr++;}.bind(this);
        this.refreshMessages(f);
    },
    refreshMessageCount:function(){
        messangerService.getMessageCount().then(function(data){
            var state=this.state;
            state.messageCount=data;
            state.numberOfPages=Math.ceil(state.messageCount/this.pageInfo.pageSize);
            this.setState(state);
            console.log("refreshing message count: "+data);
        }.bind(this));
    }
});
var PageCountIndicator=React.createClass({
    displayName: "PageCountIndicator",
    render: function () {
        return React.createElement(
            "p",
            null,
            "Page:"+(this.props.pageNr+1)+"/"+this.props.numberOfPages
        )
    }
});
var UserList = React.createClass({
    displayName: "UserList",
    render: function () {

        var userListItems = this.props.users.map(user => {
                return React.createElement(User, { user: user, key: user.username,onClick:this.props.onClick });
    });
        return React.createElement(
            "div",
            null,
            "USER LIST:",
            React.createElement(
                "ul",
                {id:"userList"},
                userListItems
            )
        )
    }

});
var MessageList = React.createClass({
    displayName: "MessageList",

    //nextPage: function() {
    //console.log("next button pushed");
    //},

    render: function () {

        var messageListItems = this.props.messages.map(message => {
                return React.createElement(Message, { message: message, key: message.id });
        });
        return React.createElement(
            "div",
            null,
            "MESSAGE LIST:",
            React.createElement(
                "ul",
                {id:"messageList"},
                messageListItems
            ),
            React.createElement(
                Button,{name:"Previous Page",onClick:this.props.prevPage,disabled:this.props.pageInfo.pageNr===0}
            ),
            React.createElement(
                Button,{name:"Next Page",onClick:this.props.nextPage,disabled:this.props.pageInfo.pageNr+1>=this.props.numberOfPages}
            ),
            React.createElement(PageCountIndicator, { pageNr:this.props.pageInfo.pageNr,numberOfPages:this.props.numberOfPages})
        )
    }

});

var Button = React.createClass({
    render: function () {
        return (
            React.createElement("input", { type: "submit",disabled:this.props.disabled, value: this.props.name, onClick:this.props.onClick })
        );
    }
});


//$(document).ready ( function () {
//    $(document).on ("click", "#messageList li", function () {
//
//        console.log('jquery clicked'+ this.id);
//        $('#'+this.id).css({
//        'background-color':'red'
//        });
//    });
//});


var User = React.createClass({
    displayName: "User",
    clicked:function(){
        console.log("user clicked");
        this.props.onClick(this.props.user);
    },
    render: function () {
        var user = this.props.user;
        return React.createElement(
            "li",
            { id: user.username,onClick:this.clicked },
            "username: ",user.username
        );
    }
});
var Message = React.createClass({
    displayName: "Message",
    clicked:function(){
        console.log("message clicked");
    },
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
    },
    User: function (username) {
        // constructor
        this.username = username ;
    },
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
                React.createElement("input", { type: "receiver", name:"receiver", placeholder: "Select Message receiver",
                    value: this.state.receiver,
                    onChange: this.handleReceiverChanged }),
                React.createElement("input", { type: "submit", disabled:!model.emptyFields(this.state), value: "Send Message" })
            ),
            React.createElement(UserList, { users: this.props.users,onClick:this.handleUserSelect })
        );
    },
    handleUserSelect: function (user) {
        console.log(user.username+ ' clicked');
        this.message.receiver=user.username;
        this.setState(this.message);
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


var messangerService=(function(){

    return {
        getUsers:function()
        {
            console.log("get users");
            return $.ajax({
                url: 'http://localhost:8080/get_users',
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
        },
        getPage:function(pageInfo){
            return $.ajax({
                url : 'http://localhost:8080/get_page',
                datatype:'json',
                type: "post",
                contentType: "application/json",
                data: JSON.stringify(pageInfo),
                success:function(pageData){
                    console.log('get page success');
                    return pageData;
                }
            });
        },
        getMessageCount:function(){
            return $.ajax({
                url : 'http://localhost:8080/getMessageCountByReceiver',
                datatype:'json',
                type: "post",
                contentType: "application/json",
                data: JSON.stringify({}),
                success:function(messageCount){
                    console.log('geting message count success');
                    return messageCount;
                }
            });
        }
    }
})();


>>>>>>> origin/master
ReactDOM.render(React.createElement(MessengerApp, []), document.getElementById('react'));