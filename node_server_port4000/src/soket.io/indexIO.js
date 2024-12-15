const express = require("express");
const config = require('../configs/import');
const { printColoredConsole } = config;
const socketMiddleware = require('./ControllerIO')


const socketIO_Server = () => {


    const port = process.env.PORT_SOKET_IO || 3001;
    const hostname = process.env.HOST_NAME || 'localhost'

    const app2 = express();
    app2.use(express.json());
    app2.use(express.urlencoded({ extended: true }));
    const server2 = app2.listen(port, hostname, () => {
        printColoredConsole('violet', 'Server soket.io Running --->  listening on port ' + port);
    });

    socketMiddleware(server2)


}

module.exports = socketIO_Server


