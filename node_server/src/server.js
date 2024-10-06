const config = require('./configs/import');
const socketIO_Server = require('./soket.io/indexIO')
const { authRouter } = require('./routers/authRouter')

const { port, hostname, printColoredConsole, conFigViewEngine, express, sequelize } = config;
const app = express();

socketIO_Server()


conFigViewEngine(app);


app.use("/auth", authRouter);


sequelize.sync()
  .then(() => {
    app.listen(port, () => {
      printColoredConsole('violet', 'Server Running ---> listening on port ' + port);
    });
  })
  .catch(err => {
    console.error('Unable to sync database:', err);
  });


