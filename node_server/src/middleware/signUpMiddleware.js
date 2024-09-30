

const checkData = (req,res,next) =>{
    const {email,phoneNumber,fullName,password} = req.body;

    if (typeof phoneNumber === 'undefined') {
        req.body.phoneNumber = '';
    }
    next()
}

module.exports = {checkData}