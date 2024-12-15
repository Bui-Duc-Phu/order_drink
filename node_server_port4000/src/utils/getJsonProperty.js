const getJsonProperty = async (jsonString, fieldName) => {
    try {
      // Chuyển chuỗi JSON thành đối tượng
      const jsonObj = JSON.parse(jsonString);
  
      // Hàm đệ quy tìm kiếm, sử dụng arrow function
      const searchValue = (obj) => {
        if (typeof obj === 'object' && obj !== null) {
          for (const key in obj) {
            if (key === fieldName) {
              return obj[key];
            }
            // Đệ quy nếu là đối tượng hoặc mảng
            const result = searchValue(obj[key]);
            if (result !== undefined) {
              return result;
            }
          }
        }
      };
  
      // Bắt đầu tìm kiếm từ gốc JSON
      return searchValue(jsonObj);
    } catch (error) {
      console.error('Lỗi khi phân tích chuỗi JSON:', error);
      return null;
    }
  };


module.exports = getJsonProperty