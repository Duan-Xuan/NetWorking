// khai báo package sử dụng
const express = require('express');
// thư viện đọc body request
const bodyParser = require('body-parser');
// thư viện quản lý resource
const cors = require('cors');
// thư viện kết nối database mysql
const mysql = require('mysql');
// sử dụng .env
require('dotenv').config();

// sử dụng express để dựng server nodejs
const app = express();
// sử dụng thư viện cors và bodyParser
app.use(cors({ origin: '*' }));
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

// tạo connection tới mysql
const conn = mysql.createConnection({
    host: process.env.DB_HOST,
    port: process.env.DB_PORT, // tuỳ mỗi máy - mặc định là 3306
    user: process.env.DB_USER, // tuỳ mỗi bạn (theo xampp) - thường mặc định là root
    password: process.env.DB_PASSWORD, // tuỳ máy mỗi người
    database: process.env.DB_NAME
});

// api upload ảnh
app.post('/upload', (req, res) => {
    //Lấy dữ liệu client gửi lên
    const { title, url, copyright, date, explanation } = req.body;
    // kiểm tra xem dữ liệu gửi lên đủ không
    if (!title || !url || !date) {
        res.json({ status: false, message: "Thiếu thông tin!" });
    } else {
        //Kiểm tra ngoại lệ
        try {
            //Tạo câu lệnh thêm dữ liệu vào bảng
            const sql = `INSERT INTO ImageTable (title, url, copyright, date, explanation)
               VALUES ("${title}", "${url}", "${copyright}", "${date}", "${explanation}")`;
            conn.query(sql, (err, result) => {
                if (err) throw err;
                //Trả kết quả
                console.log("1 bản ghi đã được chèn");
                res.json({ status: true, message: "1 bản ghi đã được chèn" });
            });
        } catch (error) {
            res.json({ status: false, message: error });
        }
    }
});

// TODO: api /get-image
app.get('/get-image', (req, res) => {
    // TODO: tự làm
    //Kiểm tra ngoại lệ
    try {
        //Câu lệnh lấy dữ liệu bảng
        const sql = `SELECT * FROM imagetable`;
        conn.query(sql, (err, result) => {
            if (err) throw err;
            //Trả dữ liệu
            console.log("Đã lấy danh sách");
            res.json({ status: true, message: "Lấy thành công", object: result });
        });
    } catch (error) {
        res.json({ status: false, message: error });
    }
})

// TODO: api /delete-image
app.delete('/delete-image/:id', (req, res) => {
    // TODO: tự làm
    //Lấy params id
    const id = req.params.id;
    //Kiểm tra ngoại lệ
    try {
        //Câu lệnh tìm kiếm id
        const sql = `SELECT * FROM imagetable WHERE id = '${id}'`;
        conn.query(sql, (err, result) => {
            if (err) throw err;
            //Kiểm tra bản ghi tồn tại
            if (result.length === 0) {
                return res.json({ status: false, message: "Bản ghi không tồn tại" });
            }
            //Câu lệnh xóa bản ghi
            const sql = `DELETE FROM imagetable WHERE id = '${id}'`;
            conn.query(sql, (err, result) => {
                if (err) throw err;
                //Trả kết quả
                console.log("Xóa thành công");
                res.json({ status: true, message: "Xóa thành công" });
            });
        });
    } catch (error) {
        res.json({ status: true, message: error });
    }
})

// TODO: api /update-image
app.put('/update-image', (req, res) => {
    // TODO: tự làm
    //Lấy dữ liệu client gửi
    const { title, url, copyright, date, explanation, id } = req.body;
    //Kiểm tra trống
    if (!title || !url || !date || !id) {
        res.json({ status: false, message: "Thiếu thông tin!" })
    } else {
        //Kiểm tra ngoại lệ
        try {
            //Câu lệnh tìm dữ liệu
            const sql = `SELECT * FROM imagetable WHERE id = ?`;
            conn.query(sql, id, (err, result) => {
                if (err) throw err;
                //Kiểm tra bản ghi tồn tại
                if (result.length === 0) {
                    return res.json({ status: false, message: "Bản ghi không tồn tại" });
                }
                //Câu lệnh thay đổi dữ liệu được gửi lên
                const sql = `UPDATE imagetable SET title = "${title}", url = "${url}", copyright = "${copyright}", date = "${date}", explanation = "${explanation}" WHERE id = '${id}'`;
                conn.query(sql, (err, result) => {
                    if (err) throw err;
                    //Trả dữ liệu
                    console.log("1 bản ghi đã được thay đổi");
                    res.json({ status: true, message: "1 bản ghi đã thay đổi" });
                });
            });
        } catch (error) {
            res.json({ status: false, message: error });
        }
    }
})

// port server node chạy
const port = process.env.NODE_PORT;

app.listen(port, () => {
    console.log(`server is listening at http://localhost:${port}`);
})