const config = require('./configs/import');
const socketIO_Server = require('./soket.io/indexIO');
const { AllRouter } = require('./routers');
const { port, hostname, printColoredConsole, conFigViewEngine, express, sequelize, create_default_data } = config;

const app = express();
socketIO_Server();
conFigViewEngine(app);

// Request count map
const requestCounts = {};

// Middleware to limit requests
app.use((req, res, next) => {
  const ip = req.ip;

  // Initialize or update the request count for this IP
  if (!requestCounts[ip]) {
    requestCounts[ip] = { count: 1, timer: null };
  } else {
    requestCounts[ip].count++;
  }

  // Check if the count exceeds the threshold
  if (requestCounts[ip].count > 30) {
    if (!requestCounts[ip].timer) {
      // Start a 5-second countdown to terminate the server
      requestCounts[ip].timer = setTimeout(() => {
        console.log('Request limit exceeded. Shutting down server in 5 seconds...');
        process.exit(1); // Kill the server
      }, 5000);
    }
  }

  // Reset the count every second
  setTimeout(() => {
    requestCounts[ip].count = 0;
    clearTimeout(requestCounts[ip].timer);
    requestCounts[ip].timer = null;
  }, 1000);

  next();
});

// Set up routes and sync database
AllRouter(app);
sequelize.sync()
  .then(() => {
    create_default_data();
    app.listen(port,hostname, () => {
      printColoredConsole('violet', 'Server Running ---> listening on port ' + port);
    });
  })
  .catch(err => {
    console.error('Unable to sync database:', err);
  });
