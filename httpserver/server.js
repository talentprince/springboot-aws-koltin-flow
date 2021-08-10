const express = require('express');
const app = express();

app.get('/user/1', function(req, res) {
    res.json({
        name: 'James',
    });
});

app.listen(3000, function(req, res) {
    console.log("Server is running at port 3000");
});