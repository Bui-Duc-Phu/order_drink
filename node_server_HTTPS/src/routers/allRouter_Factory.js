const errhandlerException = require("../middleware/errhandelExcetion");
const authRouter = require("./authRouter");
const cartRouter = require("./cartRouter");
const homeRouter = require("./homeRouter");


const AllRouter  = (app) =>{

    app.use("/auth", authRouter);
    app.use("/home", homeRouter);
    app.use("/cart", cartRouter);
    app.use(errhandlerException);

}


module.exports = AllRouter