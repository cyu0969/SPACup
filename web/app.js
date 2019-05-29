var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
var db = require('./db');
var bodyParser = require('body-parser');

var app = express();

var member = require('./routes/member');
var favorite = require('./routes/favorite');
var certification = require('./routes/certification');

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

db.connect(function(err) {
	if (err) {
		console.log('Unable to connect to MySQL.')
	    process.exit(1)
	  }
});

app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/member', require('./routes/member'));
app.use('/favorite', require('./routes/favorite'));
app.use('/certification', require('./routes/certification'));


// catch 404 and forward to error handler
app.use(function(req, res, next) {
	var err = new Error('Not Found');
	err.status = 404;
    next(err);
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;
