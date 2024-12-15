const { sendMail } = require("../../configs/SendMail");
const codeRandom = require("../../utils/codeRandom");



const verification = async (req, res) => {
    const { email } = await req.body
    let code = codeRandom(4)
    console.log('email sdsd --------------------: ', email)

    if (email) {
        sendMail(email, code)
            .then(infor => {
                res.status(200).json({
                    message: 'Verified successfully!',
                    data: { code: code }
                })
                console.log(infor)
            })
            .catch(error => {
                res.status(403).json({
                    message: 'Verification failed, email is not valid!',
                })
            });

    } else {
        res.status(403).json({
            message: 'Email null ,Verification failed!',
        })
    }

}

module.exports = { verification }