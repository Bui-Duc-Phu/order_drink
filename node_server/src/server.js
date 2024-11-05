const config = require('./configs/import');
const socketIO_Server = require('./soket.io/indexIO')
const { AllRouter } = require('./routers');

const { port, hostname, printColoredConsole, conFigViewEngine, express, sequelize,create_default_data } = config;
const app = express();
socketIO_Server()
conFigViewEngine(app);


AllRouter(app)

sequelize.sync()
  .then(() => {
    create_default_data()
    app.listen(port, () => {
      printColoredConsole('violet', 'Server Running ---> listening on port ' + port);
    });
  })
  .catch(err => {
    console.error('Unable to sync database:', err);
  });


