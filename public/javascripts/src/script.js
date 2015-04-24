/**
 * Created by julian on 24/04/2015.
 */

var Page = React.createClass({
    render: function () {
        return (
          <div className="page-wrapper">
              <Header />
              <Map />
              <Footer />
          </div>
        )
    }
});
var Header = React.createClass({
    render: function () {
        return (
            <div className="header">
                <img class="header-logo"/>
                <ul class="header-links">
                    <li>About</li>
                    <li>API</li>
                    <li>Contact</li>
                </ul>
            </div>
        )
    }
});


var Search = React.createClass({
    render: function() {
        return (
            <input />
        )
    }
});


var Map = React.createClass({
    getInitialState: function () {
        return {location: this.props.location}
    },
    handleInput: function(){
        //Takes the current input
        //Geocodes it
        //sets state to this location
        //Displays the first result as faded text in addition to what the user has already written
        var input = "london" //get raw input

    },
    handleQuery: function(){
        //Queries server for map using coordintes in state
    },

    render: function () {

        return (
            <div className="map">
                <Search />
                <img src="http://192.168.1.10:9000/maps/0/0/green"/>
            </div>
        )
    }
});

var Footer = React.createClass({
    render: function () {
        return (
            <div className="footer">
            </div>
        )
    }
});

React.render(<Page userLocation={"2323,23424"} queryUrl={"www.fsdfsf.com"}/>, document.getElementById('react-canvas'));

//
//var routes = (
//    <Route handler={App}>
//        <Route name="index" handler={Page} />
//        <Route name="page2" handler={Page2} />
//    </Route>
//);
//
//Router.run(routes, function (Handler) {
//    React.render(<Handler/>, document.getElementById('main'));
//});
