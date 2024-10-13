class LoginResponse {
    constructor(email, id, accessToken) {
      this.email = email;
      this.id = id;
      this.accessToken = accessToken;
    }
  }
  
  module.exports = LoginResponse;
  