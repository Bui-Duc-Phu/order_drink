
var morgan = require('morgan')



const conFigViewEngine = (app) => {
    require('dotenv').config()
    app.use(morgan('combined'))
}

module.exports = conFigViewEngine