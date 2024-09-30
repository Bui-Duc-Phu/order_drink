const config = require('./configs/import');
const { port, hostname, printColoredConsole, conFigViewEngine, express,sequelize } = config;
const app = express();
conFigViewEngine(app);

const {authRouter} = require('./routers/authRouter')




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


