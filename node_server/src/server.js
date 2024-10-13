const config = require('./configs/import');
const socketIO_Server = require('./soket.io/indexIO')
const { authRouter } = require('./routers/authRouter')
const Product = require('./models/Product'); 

const errhandlerException = require('./middleware/errhandelExcetion')

const { port, hostname, printColoredConsole, conFigViewEngine, express, sequelize } = config;
const app = express();




socketIO_Server()


conFigViewEngine(app);


app.use("/auth", authRouter);
app.use(errhandlerException);





sequelize.sync()
  .then(() => {
    app.listen(port, () => {
      printColoredConsole('violet', 'Server Running ---> listening on port ' + port);
    });
  })
  .catch(err => {
    console.error('Unable to sync database:', err);
  });


