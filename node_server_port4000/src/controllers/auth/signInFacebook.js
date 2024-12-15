const { createToken } = require('../../jwt/createToken');
const User = require("../../models/User")
const e = require("express")


const signInFacebook = async (req, res) => {
    try {
        const { email, fullName } = req.body
        if (email || fullName) {
            const existingUser = await User.findOne({ where: { email } })
            if (!existingUser) {
                const newUser = await User.create({
                    email,
                    fullName,
                    typeAccount: 'facebook'
                })
                const plainUser = newUser.get({ plain: true })

                res.status(200).json({
                    message: "create user sucessfull!",
                    userData: {
                        id: plainUser.id,
                        email: plainUser.email,
                        fullName: plainUser.fullName,
                        accesstoken: await createToken(plainUser.email, plainUser.id),
                    }
                })

            } else {
                if (existingUser.dataValues.typeAccount !== "facebook") {
                    res.status(400).json({
                        message: "email existing with email/password typeAccount!"
                    })
                } else {
                    let currentUser = existingUser.dataValues
                    res.status(200).json({
                        message: 'SigIn sucessfull!',
                        userData: {
                            id: currentUser.id,
                            email: currentUser.email,
                            accesstoken: await createToken(existingUser.email, existingUser.id)
                        }
                    })
                }
            }
        } else {
            console.log("Error : email null")
            res.status(500).json({
                error: `Error message: email null`
            });
        }
    } catch (error) {
        res.status(500).json({
            error: `Error message: ${error.message}`
        });
    }
}



module.exports = { signInFacebook }