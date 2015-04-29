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
                <div className="header-wrapper">
                    <img className="header-logo" src="http://192.168.1.10:9000/assets/images/logo.png"/>
                    <ul className="header-links">
                        <li><a>about</a></li>
                        <li><a>API</a></li>
                        <li><a>contact</a></li>
                    </ul>
                </div>
            </div>
        )
    }
});

var Search = React.createClass({
    getInitialState: function(){
        return {processing: false, suggestion: "", coordinates: [0,0]}
    },
    componentDidMount: function(){
        var self = this;

        React.findDOMNode(this.refs.searchInput).focus();
        React.findDOMNode(this.refs.searchInput).spellcheck = false; //Stop annoying red underline due to all lowercase chars forced by css

        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(showPosition);
        }
        function showPosition(position) {
            self.setState({processing: false, suggestion: "Your current location", coordinates: [position.coords.latitude, position.coords.longitude]})
            self.props.coordCallback(self.state.coordinates[0], self.state.coordinates[1]); //Pass the long and lat of location to parent
        }
    },
    handleClick: function(){
        if(React.findDOMNode(this.refs.searchInput).value !== "" && this.state.suggestion !== null) {
            this.geocode(this.state.suggestion); //Geocode the users input
            //Toggle arrow button
            this.state.processing === true ? this.setState({processing: false, suggestion: "", coordinates: this.state.coordinates}) : this.setState({processing: true, suggestion: "", coordinates: this.state.coordinates});
        }
    },
    handleKeyPress: function(e){
        e.which === 13 ? this.handleClick() : function(){}
    },
    handleInput: function(){
        var input = React.findDOMNode(this.refs.searchInput).value;
        this.setState({processing: false, suggestion: input, coordinates: this.state.coordinates})
    },
    geocode: function(location) {
        var self = this;
        var input = React.findDOMNode(self.refs.searchInput).value;

        $.ajax({
            url : 'http://api.opencagedata.com/geocode/v1/json?q='+location+'&key=8aef2700defca0c5f5cd7918916818e2&limit=1',
            type : 'GET',
            dataType:'json',
            success : function(data) {
                self.setState({processing: false, suggestion: input+"     -"+data.results[0].formatted, coordinates: [data.results[0].geometry.lng, data.results[0].geometry.lat]});
                self.props.coordCallback(self.state.coordinates[0], self.state.coordinates[1]); //Pass the long and lat of location to parent
            },
            error : function(request,error)
            {
                alert("There was an error processing your request "+JSON.stringify(request));
            }
        });
    },
    render: function() {
        var css = "";
        this.state.processing === true ? css = "menu btn10 wait" : css = "menu btn10 open";
        var suggestion = this.state.suggestion;

        return (
            <div className="search">
                <input className="search-completions" value={suggestion} readOnly/>
                <input className="searchInput" onChange={this.handleInput} onKeyPress={this.handleKeyPress} ref="searchInput" type="text" autocomplete="off" autocorrect="off" autocapitalize="off" spellcheck="false"/>
                <div onClick={this.handleClick} onKeyPress={this.handleKeyPress} className="search-arrow">
                    <div className={css} data-menu="10">
                        <div className="icon"></div>
                    </div>
                </div>
            </div>
        )
    }
});


var Map = React.createClass({
    getInitialState: function () {
        return {lat: 0, lon: 0}
    },
    handleQuery: function(lat, lon){
        var self = this;
        self.setState({lat: lat, lon: lon});
    },
    render: function () {
        var image = "http://192.168.1.10:9000/maps/"+this.state.lat+"/"+this.state.lon+"/orange"

        return (
            <div className="map">
                <Search coordCallback={this.handleQuery}/>
                <img src={image}/>
            </div>
        )
    }
});

var ItMakesSense = React.createClass({
    render: function(){
        return(
            <div class="it-makes-sense">
                <h2>It Makes Sense</h2>
                <p></p>
            </div>
        )
    }
})

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
