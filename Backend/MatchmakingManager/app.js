/**
 * This is the actual server that parses requests
 * 
 * This file:
 *      -Sets up morgan as logger for easy debugging
 *      -Adds header to all possible requests, such that we can avoid all CORS errors
 *      -Specifies route for /matchmaker, described in ../routes/matchmaker.js
 *      -Handles errors for any requests that do not provide a valid endpoint
 * 
 */
const express = require('express');
const app = express();
const morgan = require('morgan');
const bodyParser = require('body-parser');
const mongoose = require('mongoose');

const matchmakerRoutes = require('./routes/matchmaker');


//used for logging requests made
app.use(morgan('dev'));
app.use(bodyParser.urlencoded({extended: false}));
app.use(bodyParser.json());


//Adding headers to all our responses to avoid CORS errors
//for all possible clients
app.use((req, res, next) => {
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Headers', '*');
    if (req.method === 'OPTIONS') {
        res.header('Access-Control-Allow-Methods', 'PUT, POST, GET, DELETE, PATCH');
        return res.status(200).json({});
    }
    next();   
});


app.use('/matchmaker', matchmakerRoutes);


//No valid entrypoint
app.use((req, res, next) => {
    const error = new Error('Not found');
    error.status = 404;
    next(error);
});


//general error handling code
app.use((error, req, res, next) => {
    res.status(error.status || 500);
    res.json({
        error: {
            message: error.message,
            status: error.status
        }
    });
});

module.exports = app;