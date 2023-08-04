-- tạo cơ sở dữ liệu
CREATE DATABASE IF NOT EXISTS `ImageDataBase` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `ImageDataBase`;

-- tạo bảng với 3 cột id, title và image (id là khóa chính) (title và image không được null) (image là chuỗi base64)
CREATE TABLE IF NOT EXISTS `ImageTable` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` tinytext NOT NULL,
  `url` longtext NOT NULL, -- chuỗi base64
  `copyright` varchar(255),
  `date` date,
  `explanation` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
