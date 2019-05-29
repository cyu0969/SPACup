var express = require('express');
var formidable = require('formidable');
var db = require('../db')
var router = express.Router();

//specup/info
router.post('/info', function(req, res, next) {
  if (!req.body.member_seq) {
    return res.sendStatus(400);
  }

  var member_seq = req.body.member_seq;
  var name = req.body.name;
  var type = req.body.tel;
  var homepage = req.body.homepage;
  var date = req.body.date;
  var description = req.body.description;


  var sql_insert = 
    "insert into specup_certification (member_seq, name, type, homepage, date, description) " +
    "values(?, ?, ?, ?, ?, ?); ";

  console.log(sql_insert);

  var params = [member_seq, name, type, homepage, date, description];

  db.get().query(sql_insert, params, function (err, result) {
    console.log(result.insertId);
    res.status(200).send('' + result.insertId);
  });
});

//specup/info/image
router.post('/info/image', function (req, res) {
  var form = new formidable.IncomingForm();

  form.on('fileBegin', function (name, file){    
    file.path = './public/img/' + file.name;
  });

  form.parse(req, function(err, fields, files) {
    var sql_insert = "insert into specup_info_image (info_seq, filename, image_memo) values (?, ?, ?);";

    db.get().query(sql_insert, [fields.info_seq, files.file.name, fields.image_memo], function (err, rows) {
      res.sendStatus(200);
    });
  });
});

module.exports = router;