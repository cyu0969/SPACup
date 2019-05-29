var express = require('express');
var db = require('../db')
var router = express.Router();

//favorite/list
router.get('/list', function(req, res, next) {
  var member_seq = req.query.member_seq;
  
  console.log(member_seq);

  if (!member_seq) {
      return res.sendStatus(400);
  }

  var sql = 
    "select a.seq as favorite_seq, a.member_seq as favorite_member_seq, a.reg_date as favorite_date, " + 
    "  b.*, " + 
    "  (( 6371 * acos( cos( radians(?) ) * cos( radians( latitude ) ) * cos( radians( longitude ) - radians(?) ) " + 
    "  + sin( radians(?) ) * sin( radians( latitude ) ) ) ) * 1000) AS user_distance_meter, " + 
    "  'true' as is_favorite, " + 
    "  (select filename from specup_info_image where info_seq = a.info_seq) as image_filename " + 
    "from specup_favorite as a left join specup_certification as b " + 
    " on (a.info_seq = b.seq) " + 
    "where a.member_seq = ? " + 
    "order by a.reg_date desc ";
  console.log("sql : " + sql);
    
  db.get().query(sql, [member_seq], function (err, rows) {
      if (err) return res.sendStatus(400);
      res.status(200).json(rows);
  }); 
});

//favorite/:member_seq/:info_seq
router.post('/:member_seq/:info_seq', function(req, res, next) {
    var member_seq = req.params.member_seq;
    var info_seq = req.params.info_seq;
    
    console.log(member_seq);
    console.log(info_seq);

    if (!member_seq || !info_seq) {
        return res.sendStatus(400);
    }

    var sql_select = "select count(*) as cnt from specup_favorite where member_seq = ? and info_seq = ?;";
    var sql_insert = "insert into specup_favorite (member_seq, info_seq) values(?, ?);";
    var sql_update = "update specup_certification set favorite_cnt = favorite_cnt+1 where seq = ? ";    

    db.get().query(sql_select, [member_seq, info_seq], function (err, rows) {
        if (rows[0].cnt > 0) {
            return res.sendStatus(400);
        }

        db.get().query(sql_insert, [member_seq, info_seq], function (err, rows) {
            db.get().query(sql_update, info_seq, function (err, rows) {
                if (err) return res.sendStatus(400);
                res.sendStatus(200); 
            });  
        });        
    }); 
});

//favorite/:member_seq/:info_seq
router.delete('/:member_seq/:info_seq', function(req, res, next) {
    var member_seq = req.params.member_seq;
    var info_seq = req.params.info_seq;
    
    console.log(member_seq);
    console.log(info_seq);

    if (!member_seq || !info_seq) {
        return res.sendStatus(400);
    }

    var sql_delete = "delete from specup_favorite where member_seq = ? and info_seq = ? ";
    var sql_update = "update specup_certification set favorite_cnt = favorite_cnt-1 where seq = ? ";

    db.get().query(sql_delete, [member_seq, info_seq], function (err, rows) {
        db.get().query(sql_update, info_seq, function (err, rows) {
            if (err) return res.sendStatus(400);
            res.sendStatus(200);
        });  
    });
});

module.exports = router;