const axios = require('axios');

// Địa chỉ endpoint và dữ liệu yêu cầu
const url = 'http://192.168.0.109:3000/cart/getSize';
const data = { productName: 'Espresso' };

// Hàm gửi yêu cầu POST
const sendRequest = async (threadId) => {
  try {
    const response = await axios.post(url, data, {
      headers: {
        'Content-Type': 'application/json',
        'User-Agent': 'okhttp/4.11.0',
        'Connection': 'keep-alive',
        'Accept-Encoding': 'gzip, deflate, br',
      }
    });
    console.log(`Thread ${threadId} - Response:`, response.data);
  } catch (error) {
    console.error(`Thread ${threadId} - Request failed:`, error.message);
  }
};


const startFlooding = (threadId) => {
  setInterval(() => {
    sendRequest(threadId);
  }, 10); // Gửi yêu cầu mỗi 10ms (100 yêu cầu mỗi giây)
};

// Tạo 3 "luồng" gửi yêu cầu cùng lúc
for (let i = 1; i <= 4; i++) {
  startFlooding(i); 
}
