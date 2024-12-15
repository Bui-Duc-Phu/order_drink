const e = require("express");
const { resetPass } = require("../../configs/resetPass");
const codeRandom = require("../../utils/codeRandom");
const User = require("../../models/User");
const bcrypt = require('bcrypt');


const resetPasswrod = async (req, res) => {
    const { email } = req.body
    let newPassword = codeRandom(6)

    console.log('resetpass , ', email)

    if (email) {

        const user = await User.findOne({ where: { email } });

        if (user) {

            const salt = await bcrypt.genSalt(10)
            const hashedPassword = await bcrypt.hash(newPassword, salt)

            try {
                await User.update(
                    { password: hashedPassword },
                    { where: { id: user.id } }
                );

            } catch (error) {

                console.error('Error updating password:', error);

            }


            resetPass(email, newPassword)
                .then(infor => {
                    res.status(200).json({
                        message: 'reset password successfully, you can check email!',
                    })
                })
                .catch(error => {
                    res.status(403).json({
                        message: 'reset password failed, email is not valid!',
                    })
                });
        }


    } else {
        res.status(403).json({
            message: 'Email null ,Verification failed!',
        })
    }


}



module.exports = { resetPasswrod }