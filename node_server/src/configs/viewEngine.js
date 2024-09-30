
const express = require('express')
var morgan = require('morgan')



const conFigViewEngiine = (app , ) => {  
    require('dotenv').config()
    app.use(morgan('combined'))



}

module.exports = conFigViewEngiine