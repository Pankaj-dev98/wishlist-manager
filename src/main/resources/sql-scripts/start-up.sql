show tables;

insert into customer(first_name, last_name, date_of_birth, email, address_line_1, address_line_2, address_pincode)
values('Dummy', 'Admin', '1998-09-21', 'admin@example.com', 'demo-address line-1', 'demo-address line-2, New Delhi, India', 110049);

insert into user(email, name, password) values('admin@example.com', 'Dummy Admin', '$2a$10$dPEuQh2zNKSuF8rMneLgY.0DAmaWY22xkl2sWaWfHeLGvcSYfrOWK');
insert into roles(role) values('CUSTOMER'), ('ADMIN');
insert into `user_roles`(username, role_id) values('admin@example.com', 1);