const nodemailer = require("nodemailer");

const sendMail = async (receiver,code) => {

 

    const transporter = nodemailer.createTransport({
        host: "smtp.gmail.com",
        port: 587,
        auth: {
            user: "firebase683@gmail.com",
            pass: "caps orrv wsjc ppbd",
        },
    });

    try {
        
        console.log("Message sent: va0 ");
        const info = await transporter.sendMail({
            from: 'App coffee',
            to: receiver, 
            subject: "App coffee verification", 
            text: "verification code ", 
            html: `<b>verification code : ${code}</b>`, 
        });

        console.log("Message sent: %s", info);
        return info;
    } catch (error) {
        console.error("Error sending email:", error);
        throw error;
    }
}

module.exports = { sendMail };
