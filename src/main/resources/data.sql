INSERT INTO category (name) VALUES
('수입 명품'),
('패션의류'),
('뷰티'),
('출산/유아동'),
('가전제품'),
('카메라/캠코더'),
('모바일/탬플릿'),
('도서/음반/분류'),
('노트북/PC')
ON DUPLICATE KEY UPDATE name = VALUES(name);


